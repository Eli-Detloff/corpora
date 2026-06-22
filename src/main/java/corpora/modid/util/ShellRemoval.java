package corpora.modid.util;


import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;

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

            ServerLevel world = server.getLevel(op.dimension);
            if (world == null) continue;

            Entity e = world.getEntity(op.shellUuid);

            if (e != null && e instanceof ShellEntity) {
                ((ShellEntity) e).loadPlayerInventory(op.player);
                e.discard();

                long elapsed = server.getTickCount() - op.startTick;
                Corpora.LOGGER.info("Deleted entity after {} ticks", elapsed);

                it.remove();
                op.player.getInventory().setChanged();
                op.player.containerMenu.broadcastChanges();
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

    public static void add(ServerLevel world, UUID uuid, int delayTicks, ServerPlayer player) {
        ShellRemovalData op = new ShellRemovalData();
        op.shellUuid = uuid;
        op.dimension = world.dimension();
        op.ticksRemaining = delayTicks;
        op.startTick = world.getServer().getTickCount();
        op.player = player;
        PENDING.add(op);
    }
}
