package corpora.modid.command;

import corpora.modid.Corpora;
import corpora.modid.block.ServerBlock;
import corpora.modid.init.ModCardinalComponents;
import corpora.modid.networking.custom.ShellScreenS2CPacket;
import corpora.modid.util.EntityRegistryState;
import corpora.modid.util.ShellDataComponent;
import corpora.modid.util.config.ConfigManager;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.BlockState;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;

public class ShellScreenCommand {

    private static int execute(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();

        if (player == null) {
            return 0;
        }

        ArrayList<ShellDataComponent> allShells = new ArrayList<>();

        // Only get shells from the player's current dimension
        EntityRegistryState state = EntityRegistryState.get(player.getServerWorld());
        allShells.addAll(state.getAll());

        // Only keep shells owned by this player
        allShells.removeIf(shell ->
                !shell.owner().equals(player.getUuid())
        );


        BlockPos serverPos =
                ModCardinalComponents.SERVERCOMP.get(player).getPos();

        if (serverPos == null) {
            player.sendMessage(Text.literal("Server position is not set."));
            Corpora.LOGGER.info("SERVERPOSCOMP is null");
            return 0;
        }

        RegistryKey<World> storedDimension =
                ModCardinalComponents.SERVERCOMP.get(player).getDimension();

        if (storedDimension == null) {
            player.sendMessage(Text.literal("Server dimension is not set."));
            Corpora.LOGGER.info("SERVERDIMCOMP is null");
            return 0;
        }

        // Server block must be in the same dimension as the player
        if (!storedDimension.equals(player.getWorld().getRegistryKey())) {
            player.sendMessage(Text.literal(
                    "Your server block is located in another dimension."
            ));
            Corpora.LOGGER.info(
                    "Player attempted to access server block in different dimension. Stored: {}, Current: {}",
                    storedDimension.getValue(),
                    player.getWorld().getRegistryKey().getValue()
            );
            return 0;
        }

        // Check the server block in the player's current world
        BlockState serverBlock =
                player.getServerWorld().getBlockState(serverPos);

        if (!(serverBlock.getBlock() instanceof ServerBlock)) {
            player.sendMessage(Text.literal(
                    "Server block was destroyed or does not exist."
            ));
            Corpora.LOGGER.info(
                    "No server block found at {}",
                    serverPos
            );
            return 0;
        }

        if (!serverBlock.get(ServerBlock.POWERED)) {
            player.sendMessage(Text.literal(
                    "Server block is not powered."
            ));
            Corpora.LOGGER.info(
                    "Server block is not powered at {}",
                    serverPos
            );
            return 0;
        }

        //distance check for player
        if (!serverPos.isWithinDistance(player.getPos(), ConfigManager.getConfig().serverBlockMaxRange)) {
            player.sendMessage(Text.literal(
                    "Active Server is out of range"
            ));
            Corpora.LOGGER.info(
                    "Server block is out of range, distance: {}, max distance: {}",
                    serverPos.isWithinDistance(player.getPos(), ConfigManager.getConfig().serverBlockMaxRange),
                    ConfigManager.getConfig().serverBlockMaxRange
            );
            return 0;
        }

        allShells.removeIf(shell -> !shell.pos().isWithinDistance(serverPos, ConfigManager.getConfig().serverBlockMaxRange));


        //xp check
        if (player.experienceLevel < ConfigManager.getConfig().tpExperienceLevelCost) {
            player.sendMessage(Text.literal(
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
                                CommandManager.literal("list_shells")
                                        .requires(source -> source.hasPermissionLevel(2))
                                        .executes(ctx -> execute(ctx.getSource()))
                        )
        );
    }
}