package corpora.modid.util;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.world.World;

import java.util.UUID;

public class ShellRemovalData {
    public UUID shellUuid;
    public RegistryKey<World> dimension;
    public int ticksRemaining;
    public int startTick;
    public PlayerEntity player;
}
