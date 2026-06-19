package corpora.modid.init;


import corpora.modid.Corpora;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent SERVER_BLOCK_RUN = registerSoundEvent("server_block_run");

    public static final SoundEvent SHELL_POWER_DOWN = registerSoundEvent("shell_power_down");
    public static final SoundEvent SHELL_POWER_UP = registerSoundEvent("shell_power_up");


    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(Corpora.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void registerSounds() {
        Corpora.LOGGER.info("Registering Mod Sounds for " + Corpora.MOD_ID);

    }
}
