package corpora.modid.util.distanceTracker;

import corpora.modid.init.ModCardinalComponents;
import corpora.modid.util.config.ConfigManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DistanceManager {

    private static final DistanceDisplay DISPLAY =
            new DistanceDisplay(ConfigManager.getConfig().serverBlockMaxRange);


    public static void initialize() {

        ServerTickEvents.END_SERVER_TICK.register(server -> {

            if (server.getTickCount() % 20 != 0) {
                return;
            }

            for (ServerPlayer player : server.getPlayerList().getPlayers()) {

                BlockPos target = ModCardinalComponents.SERVERCOMP.get(player).getPos();

                if (target == null) {
                    continue;
                }

                double distance = Math.sqrt(
                        target.distToCenterSqr(player.position())
                );

                DISPLAY.update(player, distance);
            }
        });
    }

    private static void updatePlayer(ServerPlayer player, BlockPos target) {

        double distance = Math.sqrt(
                target.distToCenterSqr(player.position())
        );

        player.displayClientMessage(
                Component.literal("Distance: " + (int) distance),
                true
        );
    }
}