package corpora.modid.init;


import corpora.modid.Corpora;
import corpora.modid.component.cardinal.components.MyCurrentShellComponent;
import corpora.modid.component.cardinal.components.MyServerComponent;
import corpora.modid.component.cardinal.components.MyShellOwnerComponent;
import corpora.modid.component.cardinal.interfaces.CurrentShellComponent;
import corpora.modid.component.cardinal.interfaces.ServerComponent;
import corpora.modid.component.cardinal.interfaces.ShellOwnerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModCardinalComponents implements EntityComponentInitializer {


    public static final ComponentKey<ServerComponent> SERVERCOMP = ComponentRegistry
            .getOrCreate(Identifier.of(Corpora.MOD_ID, "server_comp"), ServerComponent.class);

    public static final ComponentKey<ShellOwnerComponent> SHELL_OWNER_COMPONENT = ComponentRegistry
            .getOrCreate(Identifier.of(Corpora.MOD_ID, "shell_owner_component"), ShellOwnerComponent.class);
    public static final ComponentKey<CurrentShellComponent> CURRENT_SHELL_COMPONENT = ComponentRegistry
            .getOrCreate(Identifier.of(Corpora.MOD_ID, "current_shell_component"), CurrentShellComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Correct order: Register for the base class first


        registry.registerFor(Entity.class, SERVERCOMP, entity -> new MyServerComponent());
        registry.registerFor(Entity.class, SHELL_OWNER_COMPONENT, entity -> new MyShellOwnerComponent());
        registry.registerFor(Entity.class, CURRENT_SHELL_COMPONENT, entity -> new MyCurrentShellComponent());

        // Correct signature for 1.21.1 players:
        registry.registerForPlayers(SERVERCOMP, player -> new MyServerComponent(), RespawnCopyStrategy.ALWAYS_COPY);
        registry.registerForPlayers(CURRENT_SHELL_COMPONENT, player -> new MyCurrentShellComponent(), RespawnCopyStrategy.NEVER_COPY);
    }

    public static void register() {
        Corpora.LOGGER.info("Registering ModCardinalComponents");
    }
}
