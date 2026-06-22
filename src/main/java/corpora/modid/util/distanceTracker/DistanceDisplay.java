package corpora.modid.util.distanceTracker;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

public class DistanceDisplay {

    private final double maxDistance;

    public DistanceDisplay(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void update(ServerPlayer player, double distance) {
        ChatFormatting color = getColor(distance);

        Component message = Component.literal(
                String.format("Distance: %d", (int) Math.round(distance))
        ).withStyle(color);

        player.displayClientMessage(message, true); // Action bar
    }

    private ChatFormatting getColor(double distance) {
        double percent = distance / maxDistance;

        if (percent >= 1.0) {
            return ChatFormatting.DARK_RED;
        } else if (percent >= 0.9) {
            return ChatFormatting.RED;
        } else if (percent >= 0.75) {
            return ChatFormatting.GOLD;
        } else if (percent >= 0.5) {
            return ChatFormatting.YELLOW;
        }

        return ChatFormatting.GREEN;
    }


}