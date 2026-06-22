package corpora.modid.command;

import corpora.modid.Corpora;
import corpora.modid.block.ServerBlock;
import corpora.modid.init.ModCardinalComponents;
import corpora.modid.networking.custom.ShellScreenS2CPacket;
import corpora.modid.util.EntityRegistryState;
import corpora.modid.util.ShellDataComponent;
import corpora.modid.util.config.ConfigManager;
import io.github.apace100.origins.component.OriginComponent;
import io.github.apace100.origins.origin.Origin;
import io.github.apace100.origins.registry.ModComponents;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;

public class ShellScreenCommand {

    private static int execute(CommandSourceStack source) {
        ServerPlayer player = source.getPlayer();

        if (player == null) {
            return 0;
        }

        //return if origin is not ui
        OriginComponent component = ModComponents.ORIGIN.get(player);

        for (Origin origin : component.getOrigins().values()) {
            Corpora.LOGGER.info("Origin found: {}", origin.getId());
        }

        ResourceLocation myOriginId = ResourceLocation.fromNamespaceAndPath("uiorigin", "ui_origin");

        boolean result = component.getOrigins()
                .values()
                .stream()
                .anyMatch(origin -> origin.getId().equals(myOriginId));

        if (!result) {
            return 0;
        }


        ArrayList<ShellDataComponent> allShells = new ArrayList<>();

        // Only get shells from the player's current dimension
        EntityRegistryState state = EntityRegistryState.get(player.serverLevel());
        allShells.addAll(state.getAll());

        // Only keep shells owned by this player
        allShells.removeIf(shell ->
                !shell.owner().equals(player.getUUID())
        );


        BlockPos serverPos =
                ModCardinalComponents.SERVERCOMP.get(player).getPos();

        if (serverPos == null) {
            player.sendSystemMessage(Component.literal("Server position is not set."));
            Corpora.LOGGER.info("SERVERPOSCOMP is null");
            return 0;
        }

        ResourceKey<Level> storedDimension =
                ModCardinalComponents.SERVERCOMP.get(player).getDimension();

        if (storedDimension == null) {
            player.sendSystemMessage(Component.literal("Server dimension is not set."));
            Corpora.LOGGER.info("SERVERDIMCOMP is null");
            return 0;
        }

        // Server block must be in the same dimension as the player
        if (!storedDimension.equals(player.level().dimension())) {
            player.sendSystemMessage(Component.literal(
                    "Your server block is located in another dimension."
            ));
            Corpora.LOGGER.info(
                    "Player attempted to access server block in different dimension. Stored: {}, Current: {}",
                    storedDimension.location(),
                    player.level().dimension().location()
            );
            return 0;
        }

        // Check the server block in the player's current world
        BlockState serverBlock =
                player.serverLevel().getBlockState(serverPos);

        if (!(serverBlock.getBlock() instanceof ServerBlock)) {
            player.sendSystemMessage(Component.literal(
                    "Server block was destroyed or does not exist."
            ));
            Corpora.LOGGER.info(
                    "No server block found at {}",
                    serverPos
            );
            return 0;
        }

        if (!serverBlock.getValue(ServerBlock.POWERED)) {
            player.sendSystemMessage(Component.literal(
                    "Server block is not powered."
            ));
            Corpora.LOGGER.info(
                    "Server block is not powered at {}",
                    serverPos
            );
            return 0;
        }

        //distance check for player
        if (!serverPos.closerToCenterThan(player.position(), ConfigManager.getConfig().serverBlockMaxRange)) {
            player.sendSystemMessage(Component.literal(
                    "Active Server is out of range"
            ));
            Corpora.LOGGER.info(
                    "Server block is out of range, distance: {}, max distance: {}",
                    serverPos.closerToCenterThan(player.position(), ConfigManager.getConfig().serverBlockMaxRange),
                    ConfigManager.getConfig().serverBlockMaxRange
            );
            return 0;
        }

        allShells.removeIf(shell -> !shell.pos().closerThan(serverPos, ConfigManager.getConfig().serverBlockMaxRange));


        //xp check
        if (player.experienceLevel < ConfigManager.getConfig().tpExperienceLevelCost) {
            player.sendSystemMessage(Component.literal(
                    "Minimum cost of " + ConfigManager.getConfig().tpExperienceLevelCost + " levels to swap"
            ));
            Corpora.LOGGER.info(
                    "Player does not have enough xp to teleport. Has: {}, Needs: {}",
                    player.experienceLevel,
                    ConfigManager.getConfig().tpExperienceLevelCost
            );
            return 0;
        }

        ShellScreenS2CPacket packet =
                new ShellScreenS2CPacket(allShells);

        ServerPlayNetworking.send(player, packet);

        return 1;
    }

    public static void register() {
        CommandRegistrationCallback.EVENT.register(
                (dispatcher, registryAccess, environment) ->
                        dispatcher.register(
                                Commands.literal("list_shells")
                                        .requires(source -> source.hasPermission(2))
                                        .executes(ctx -> execute(ctx.getSource()))
                        )
        );
    }
}