package corpora.modid.entity;

import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModEntities {
    public static final EntityType<ShellEntity> SHELL = Registry.register(Registries.ENTITY_TYPE,
            Identifier.of(Corpora.MOD_ID, "shell"),
            EntityType.Builder.create(ShellEntity::new, SpawnGroup.CREATURE)
                    .dimensions(0.6f, 1.8f).build());

    public static void registerModEntities() {
        Corpora.LOGGER.info("Registering Entities for " + Corpora.MOD_ID);
    }
}
