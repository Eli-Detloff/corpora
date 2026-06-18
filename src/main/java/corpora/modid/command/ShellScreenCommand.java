package corpora.modid.command;


import corpora.modid.networking.custom.ShellScreenS2CPacket;
import corpora.modid.util.EntityRegistryState;
import corpora.modid.util.ShellDataComponent;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;

public class ShellScreenCommand {


    private static int execute(ServerCommandSource source) {
        ServerPlayerEntity player = source.getPlayer();
        MinecraftServer server = source.getServer();

        var allShells = new java.util.ArrayList<ShellDataComponent>();

        // Iterate through every loaded dimension/world
        for (var world : server.getWorlds()) {

            EntityRegistryState state = EntityRegistryState.get(world);

            allShells.addAll(state.getAll());
        }


        assert player != null;
        ShellScreenS2CPacket packet = new ShellScreenS2CPacket(allShells);

        ServerPlayNetworking.send(player, packet);

        return 1;
    }


    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                CommandManager.literal("list_shells")
                        .requires(source -> source.hasPermissionLevel(2)) // OP only
                        .executes(ctx -> execute(ctx.getSource()))
        ));
    }

}