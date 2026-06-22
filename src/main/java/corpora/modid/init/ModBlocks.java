package corpora.modid.init;

import corpora.modid.Corpora;
import corpora.modid.block.ServerBlock;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class ModBlocks {
    public static Block SERVER_BLOCK = registerBlock("server_block", new ServerBlock(BlockBehaviour.Properties.of()
            .strength(1).lightLevel(state -> state.getValue(ServerBlock.POWERED) ? 10 : 0)));

    private static void registerBlockItem(String name, Block block) {
        Registry.register(BuiltInRegistries.ITEM, ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, name),
                new BlockItem(block, new Item.Properties()));
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(BuiltInRegistries.BLOCK, ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, name), block);
    }

    public static void registerModBlocks() {
        Corpora.LOGGER.info("Registering Modded Blocks for " + Corpora.MOD_ID);
    }
}
