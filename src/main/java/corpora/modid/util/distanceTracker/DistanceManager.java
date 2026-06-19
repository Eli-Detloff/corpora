package corpora.modid.util.distanceTracker;

import corpora.modid.init.ModCardinalComponents;
import corpora.modid.util.config.ConfigManager;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

public class DistanceManager {

    private static final DistanceDisplay DISPLAY =
            new DistanceDisplay(ConfigManager.getConfig().serverBlockMaxRange);


    public static void initialize() {

        ServerTickEvents.END_SERVER_TICK.register(server -> {

            if (server.getTicks() % 20 != 0) {
                return;
            }

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {

                BlockPos target = ModCardinalComponents.SERVERCOMP.get(player).getPos();

                if (target == null) {
                    continue;
                }

                double distance = Math.sqrt(
                        target.getSquaredDistance(player.getPos())
                );

                DISPLAY.update(player, distance);
            }
        });
    }

    private static void updatePlayer(ServerPlayerEntity player, BlockPos target) {

        double distance = Math.sqrt(
                target.getSquaredDistance(player.getPos())
        );

        player.sendMessage(
                Text.literal("Distance: " + (int) distance),
                true
        );
    }
}