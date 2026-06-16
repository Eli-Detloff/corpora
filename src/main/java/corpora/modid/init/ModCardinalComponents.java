package corpora.modid.init;


import corpora.modid.Corpora;
import corpora.modid.component.cardinal.components.MyServerPosComponent;
import corpora.modid.component.cardinal.components.MyShellOwnerComponent;
import corpora.modid.component.cardinal.interfaces.ServerPosComponent;
import corpora.modid.component.cardinal.interfaces.ShellOwnerComponent;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentFactoryRegistry;
import org.ladysnake.cca.api.v3.entity.EntityComponentInitializer;
import org.ladysnake.cca.api.v3.entity.RespawnCopyStrategy;

public class ModCardinalComponents implements EntityComponentInitializer {


    public static final ComponentKey<ServerPosComponent> SERVERPOSCOMP = ComponentRegistry
            .getOrCreate(Identifier.of(Corpora.MOD_ID, "server_pos_component"), ServerPosComponent.class);

    public static final ComponentKey<ShellOwnerComponent> SHELL_OWNER_COMPONENT = ComponentRegistry
            .getOrCreate(Identifier.of(Corpora.MOD_ID, "shell_owner_component"), ShellOwnerComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        // Correct order: Register for the base class first


        registry.registerFor(Entity.class, SERVERPOSCOMP, entity -> new MyServerPosComponent());
        registry.registerFor(Entity.class, SHELL_OWNER_COMPONENT, entity -> new MyShellOwnerComponent());

        // Correct signature for 1.21.1 players:
        registry.registerForPlayers(SERVERPOSCOMP, player -> new MyServerPosComponent(), RespawnCopyStrategy.ALWAYS_COPY);
    }

    public static void register() {
        Corpora.LOGGER.info("Registering ModCardinalComponents");
    }
}
