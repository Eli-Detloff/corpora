package corpora.modid.block;

import corpora.modid.Corpora;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Corpora.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }





    public static void registerModBlocks(){
        Corpora.LOGGER.info("Registering Modded Blocks for " + Corpora.MOD_ID);
    }
}
