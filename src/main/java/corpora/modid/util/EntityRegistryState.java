package corpora.modid.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;

import java.util.*;

public class EntityRegistryState extends SavedData {

    private final Map<UUID, ShellDataComponent> byUuid = new HashMap<>();

    public EntityRegistryState() {
    }

    public static EntityRegistryState get(ServerLevel world) {
        DimensionDataStorage manager = world.getDataStorage();

        return manager.computeIfAbsent(
                new Factory<>(
                        EntityRegistryState::new,
                        (nbt, registryLookup) -> {
                            EntityRegistryState state = new EntityRegistryState();
                            state.readNbt(nbt);
                            return state;
                        },
                        null
                ),
                "my_entity_registry"
        );
    }

    @Override
    public CompoundTag save(CompoundTag nbt, HolderLookup.Provider registryLookup) {
        ListTag list = new ListTag();

        for (ShellDataComponent data : byUuid.values()) {
            CompoundTag entry = new CompoundTag();

            entry.putUUID("uuid", data.uuid());
            entry.putUUID("master_uuid", data.owner());

            entry.putInt("x", data.pos().getX());
            entry.putInt("y", data.pos().getY());
            entry.putInt("z", data.pos().getZ());

            entry.putFloat("yaw", data.yaw());
            entry.putFloat("pitch", data.pitch());
            entry.putFloat("head_yaw", data.headYaw());
            entry.putFloat("body_yaw", data.bodyYaw());

            entry.putString(
                    "dimension",
                    data.dimension().location().toString()
            );

            entry.putString("name", data.name());
            entry.putFloat("health", data.health());

            list.add(entry);
        }

        nbt.put("entities", list);
        return nbt;
    }

    public void readNbt(CompoundTag nbt) {
        byUuid.clear();

        ListTag list = nbt.getList("entities", Tag.TAG_COMPOUND);

        for (Tag element : list) {
            CompoundTag entry = (CompoundTag) element;

            ShellDataComponent data = new ShellDataComponent(
                    entry.getUUID("uuid"),

                    entry.hasUUID("master_uuid")
                            ? entry.getUUID("master_uuid")
                            : new UUID(0, 0),

                    new BlockPos(
                            entry.getInt("x"),
                            entry.getInt("y"),
                            entry.getInt("z")
                    ),

                    entry.contains("yaw") ? entry.getFloat("yaw") : 0F,
                    entry.contains("pitch") ? entry.getFloat("pitch") : 0F,
                    entry.contains("head_yaw") ? entry.getFloat("head_yaw") : 0F,
                    entry.contains("body_yaw") ? entry.getFloat("body_yaw") : 0F,

                    ResourceKey.create(
                            Registries.DIMENSION,
                            ResourceLocation.parse(entry.getString("dimension"))
                    ),

                    entry.getString("name"),
                    entry.getFloat("health")
            );

            byUuid.put(data.uuid(), data);
        }
    }

    public void put(ShellDataComponent data) {
        if (data.uuid() == null) {
            return;
        }

        byUuid.put(data.uuid(), data);
        setDirty();
    }

    public void remove(UUID uuid) {
        if (byUuid.remove(uuid) != null) {
            setDirty();
        }
    }

    public ShellDataComponent get(UUID uuid) {
        return byUuid.get(uuid);
    }

    public int cleanupNullEntries(ServerLevel world) {
        int removed = 0;

        Iterator<Map.Entry<UUID, ShellDataComponent>> iterator = byUuid.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<UUID, ShellDataComponent> entry = iterator.next();

            UUID uuid = entry.getKey();
            Entity entity = world.getEntity(uuid);

            if (entity == null || entity.isRemoved()) {
                iterator.remove();
                removed++;
            }
        }

        if (removed > 0) {
            setDirty();
        }

        return removed;
    }

    public int size() {
        return byUuid.size();
    }

    public Collection<ShellDataComponent> getAll() {
        return Collections.unmodifiableCollection(byUuid.values());
    }
}