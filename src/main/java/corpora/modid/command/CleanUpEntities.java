package corpora.modid.command;

import corpora.modid.util.EntityRegistryState;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class CleanUpEntities {
    private static int execute(ServerCommandSource source) {

        ServerWorld world = source.getWorld();

        EntityRegistryState state = EntityRegistryState.get(world);

        int before = state.size(); // you'll add this method below

        state.cleanupNullEntries(world);

        int after = state.size();

        int removed = before - after;

        source.sendFeedback(
                () -> Text.literal("Cleaned registry. Removed " + removed + " dead entries."),
                true
        );

        return 1;
    }


    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                CommandManager.literal("cleanupentities")
                        .requires(source -> source.hasPermissionLevel(2)) // OP only
                        .executes(ctx -> execute(ctx.getSource()))
        ));
    }


}
