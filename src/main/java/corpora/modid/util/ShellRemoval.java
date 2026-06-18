package corpora.modid.util;


import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class ShellRemoval {

    public static final List<ShellRemovalData> PENDING = new ArrayList<>();


    public static void register() {
        ServerTickEvents.END_SERVER_TICK.register(ShellRemoval::tick);
    }

    private static void tick(MinecraftServer server) {
        Iterator<ShellRemovalData> it = PENDING.iterator();


        while (it.hasNext()) {
            ShellRemovalData op = it.next();

            ServerWorld world = server.getWorld(op.dimension);
            if (world == null) continue;

            Entity e = world.getEntity(op.shellUuid);

            if (e != null && e instanceof ShellEntity) {
                ((ShellEntity) e).loadPlayerInventory(op.player);
                e.discard();

                long elapsed = server.getTicks() - op.startTick;
                Corpora.LOGGER.info("Deleted entity after {} ticks", elapsed);

                it.remove();
                op.player.getInventory().markDirty();
                op.player.currentScreenHandler.sendContentUpdates();
                continue;
            }

            // fallback timeout removal
            op.ticksRemaining--;

            if (op.ticksRemaining <= 0) {
                Corpora.LOGGER.info("Entity not discarded");
                it.remove();
            }
        }
    }

    public static void add(ServerWorld world, UUID uuid, int delayTicks, ServerPlayerEntity player) {
        ShellRemovalData op = new ShellRemovalData();
        op.shellUuid = uuid;
        op.dimension = world.getRegistryKey();
        op.ticksRemaining = delayTicks;
        op.startTick = world.getServer().getTicks();
        op.player = player;
        PENDING.add(op);
    }
}
