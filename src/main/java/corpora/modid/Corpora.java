package corpora.modid;

import corpora.modid.entity.ModEntities;
import corpora.modid.entity.custom.ShellEntity;
import corpora.modid.init.ModBlocks;
import corpora.modid.init.ModItemGroups;
import corpora.modid.init.ModItems;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Corpora implements ModInitializer {
    public static final String MOD_ID = "corpora";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        //registration
        ModEntities.registerModEntities();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModItemGroups.registerItemGroups();

        FabricDefaultAttributeRegistry.register(ModEntities.SHELL, ShellEntity.createAttributes());

    }
}