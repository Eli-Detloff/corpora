package corpora.modid.entity;

import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class ModEntities {
    public static final EntityType<ShellEntity> SHELL = Registry.register(BuiltInRegistries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, "shell"),
            EntityType.Builder.of(ShellEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.8f).build());

    public static void registerModEntities() {
        Corpora.LOGGER.info("Registering Entities for " + Corpora.MOD_ID);
    }
}
