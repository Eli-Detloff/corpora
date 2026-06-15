package corpora.modid.init;

import corpora.modid.Corpora;
import corpora.modid.block.ServerBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static Block SERVER_BLOCK = registerBlock("server_block", new ServerBlock(AbstractBlock.Settings.create()
            .strength(1).luminance(state -> state.get(ServerBlock.POWERED) ? 10 : 0)));

    private static void registerBlockItem(String name, Block block) {
        Registry.register(Registries.ITEM, Identifier.of(Corpora.MOD_ID, name),
                new BlockItem(block, new Item.Settings()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, Identifier.of(Corpora.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        Corpora.LOGGER.info("Registering Modded Blocks for " + Corpora.MOD_ID);
    }
}
