package corpora.modid.init;

import corpora.modid.Corpora;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class ModItemGroups {

    public static final CreativeModeTab CORPORA_ITEMS = Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, "corpora"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.SERVER_BLOCK))
                    .title(Component.translatable("itemgroup.corpora.corpora"))
                    .displayItems((displayContext, entries) -> {
                        entries.accept(ModBlocks.SERVER_BLOCK);
                        entries.accept(ModItems.SHELL_ITEM);
                        entries.accept(ModItems.SHELL_ITEM_BROKEN);

                    })
                    .build());


    public static void registerItemGroups() {
        Corpora.LOGGER.info("Registering Mod Item Groups for " + Corpora.MOD_ID);
    }
}

