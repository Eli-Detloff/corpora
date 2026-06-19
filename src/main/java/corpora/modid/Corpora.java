package corpora.modid;

import corpora.modid.command.CleanUpEntities;
import corpora.modid.command.ShellScreenCommand;
import corpora.modid.entity.ModEntities;
import corpora.modid.entity.custom.ShellEntity;
import corpora.modid.init.*;
import corpora.modid.networking.ModNetworking;
import corpora.modid.networking.custom.SelectShellC2SPayload;
import corpora.modid.networking.custom.ShellScreenS2CPacket;
import corpora.modid.util.ShellDropEvent;
import corpora.modid.util.ShellRemoval;
import corpora.modid.util.ShellTeleportHandler;
import corpora.modid.util.config.ConfigManager;
import corpora.modid.util.distanceTracker.DistanceManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.server.network.ServerPlayerEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Corpora implements ModInitializer {
    public static final String MOD_ID = "corpora";

    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        //config
        ConfigManager.load();

        //registration
        ModEntities.registerModEntities();
        ModBlocks.registerModBlocks();
        ModItems.registerModItems();
        ModItemGroups.registerItemGroups();
        ModCardinalComponents.register();
        ModNetworking.register();
        ShellRemoval.register();
        DistanceManager.initialize();
        ModSounds.registerSounds();
        ShellDropEvent.init();

        FabricDefaultAttributeRegistry.register(ModEntities.SHELL, ShellEntity.createAttributes());

        PayloadTypeRegistry.playS2C().register(ShellScreenS2CPacket.ID, ShellScreenS2CPacket.CODEC);
        PayloadTypeRegistry.playC2S().register(SelectShellC2SPayload.ID, SelectShellC2SPayload.CODEC);

        ShellScreenCommand.register();
        CleanUpEntities.register();


        //packets
        ServerPlayNetworking.registerGlobalReceiver(
                SelectShellC2SPayload.ID,
                (payload, context) -> {

                    ServerPlayerEntity player = context.player();

                    context.server().execute(() -> {

                        UUID uuid = payload.shellId();

                        ShellTeleportHandler teleportHandler = new ShellTeleportHandler();
                        teleportHandler.ShellTeleport(player, uuid);
                    });
                }
        );

    }
}