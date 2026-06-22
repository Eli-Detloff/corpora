package corpora.modid.util;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class ShellRemovalData {
    public UUID shellUuid;
    public ResourceKey<Level> dimension;
    public int ticksRemaining;
    public int startTick;
    public Player player;
}
