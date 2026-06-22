package corpora.modid.init;


import corpora.modid.Corpora;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class ModSounds {

    public static final SoundEvent SERVER_BLOCK_RUN = registerSoundEvent("server_block_run");

    public static final SoundEvent SHELL_POWER_DOWN = registerSoundEvent("shell_power_down");
    public static final SoundEvent SHELL_POWER_UP = registerSoundEvent("shell_power_up");


    private static SoundEvent registerSoundEvent(String name) {
        ResourceLocation id = ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, id, SoundEvent.createVariableRangeEvent(id));
    }

    public static void registerSounds() {
        Corpora.LOGGER.info("Registering Mod Sounds for " + Corpora.MOD_ID);

    }
}
