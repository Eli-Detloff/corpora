package corpora.modid.item;

import corpora.modid.Corpora;
import corpora.modid.block.ModBlocks;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.block.IceBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModItemGroups {

    public static final ItemGroup CORPORA_ITEMS = Registry.register(Registries.ITEM_GROUP, Identifier.of(Corpora.MOD_ID, "corpora"),
            FabricItemGroup.builder().icon(() -> new ItemStack(ModBlocks.SERVER_BLOCK))
                    .displayName(Text.translatable("itemgroup.corpora.corpora"))
                    .entries((displayContext, entries) -> {
                        entries.add(ModBlocks.SERVER_BLOCK);

                    })
                    .build());


    public static void registerItemGroups() {
        Corpora.LOGGER.info("Registering Mod Item Groups for " + Corpora.MOD_ID);
    }
}

