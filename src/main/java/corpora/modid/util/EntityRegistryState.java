package corpora.modid.util;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;

import java.util.*;

public class EntityRegistryState extends PersistentState {

    private final Map<UUID, ShellDataComponent> byUuid = new HashMap<>();

    public EntityRegistryState() {
    }

    public static EntityRegistryState get(ServerWorld world) {
        PersistentStateManager manager = world.getPersistentStateManager();

        return manager.getOrCreate(
                new Type<>(
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
    public NbtCompound writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        NbtList list = new NbtList();

        for (ShellDataComponent data : byUuid.values()) {
            NbtCompound entry = new NbtCompound();

            entry.putUuid("uuid", data.uuid());
            entry.putUuid("master_uuid", data.owner());

            entry.putInt("x", data.pos().getX());
            entry.putInt("y", data.pos().getY());
            entry.putInt("z", data.pos().getZ());

            entry.putFloat("yaw", data.yaw());
            entry.putFloat("pitch", data.pitch());
            entry.putFloat("head_yaw", data.headYaw());
            entry.putFloat("body_yaw", data.bodyYaw());

            entry.putString(
                    "dimension",
                    data.dimension().getValue().toString()
            );

            entry.putString("name", data.name());
            entry.putFloat("health", data.health());

            list.add(entry);
        }

        nbt.put("entities", list);
        return nbt;
    }

    public void readNbt(NbtCompound nbt) {
        byUuid.clear();

        NbtList list = nbt.getList("entities", NbtElement.COMPOUND_TYPE);

        for (NbtElement element : list) {
            NbtCompound entry = (NbtCompound) element;

            ShellDataComponent data = new ShellDataComponent(
                    entry.getUuid("uuid"),

                    entry.containsUuid("master_uuid")
                            ? entry.getUuid("master_uuid")
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

                    RegistryKey.of(
                            RegistryKeys.WORLD,
                            Identifier.of(entry.getString("dimension"))
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
        markDirty();
    }

    public void remove(UUID uuid) {
        if (byUuid.remove(uuid) != null) {
            markDirty();
        }
    }

    public ShellDataComponent get(UUID uuid) {
        return byUuid.get(uuid);
    }

    public int cleanupNullEntries(ServerWorld world) {
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
            markDirty();
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