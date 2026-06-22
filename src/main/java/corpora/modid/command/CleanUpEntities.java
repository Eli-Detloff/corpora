package corpora.modid.command;

import corpora.modid.util.EntityRegistryState;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;

public class CleanUpEntities {
    private static int execute(CommandSourceStack source) {

        ServerLevel world = source.getLevel();

        EntityRegistryState state = EntityRegistryState.get(world);

        int before = state.size(); // you'll add this method below

        state.cleanupNullEntries(world);

        int after = state.size();

        int removed = before - after;

        source.sendSuccess(
                () -> Component.literal("Cleaned registry. Removed " + removed + " dead entries."),
                true
        );

        return 1;
    }


    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(
                Commands.literal("cleanupentities")
                        .requires(source -> source.hasPermission(2)) // OP only
                        .executes(ctx -> execute(ctx.getSource()))
        ));
    }


}
