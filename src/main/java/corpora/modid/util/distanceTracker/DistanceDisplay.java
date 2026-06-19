package corpora.modid.util.distanceTracker;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class DistanceDisplay {

    private final double maxDistance;

    public DistanceDisplay(double maxDistance) {
        this.maxDistance = maxDistance;
    }

    public void update(ServerPlayerEntity player, double distance) {
        Formatting color = getColor(distance);

        Text message = Text.literal(
                String.format("Distance: %d", (int) Math.round(distance))
        ).formatted(color);

        player.sendMessage(message, true); // Action bar
    }

    private Formatting getColor(double distance) {
        double percent = distance / maxDistance;

        if (percent >= 1.0) {
            return Formatting.DARK_RED;
        } else if (percent >= 0.9) {
            return Formatting.RED;
        } else if (percent >= 0.75) {
            return Formatting.GOLD;
        } else if (percent >= 0.5) {
            return Formatting.YELLOW;
        }

        return Formatting.GREEN;
    }


}