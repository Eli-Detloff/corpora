package corpora.modid.datagen;

import corpora.modid.init.ModBlocks;
import corpora.modid.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        Identifier modelId = Models.CUBE.upload(
                ModBlocks.SERVER_BLOCK,
                new TextureMap()
                        .put(TextureKey.NORTH, Identifier.of("corpora", "block/server_lights"))
                        .put(TextureKey.SOUTH, Identifier.of("corpora", "block/server_lights"))
                        .put(TextureKey.EAST, Identifier.of("corpora", "block/server_side"))
                        .put(TextureKey.WEST, Identifier.of("corpora", "block/server_side"))
                        .put(TextureKey.UP, Identifier.of("corpora", "block/server_top"))
                        .put(TextureKey.DOWN, Identifier.of("corpora", "block/server_bottom"))
                        .put(TextureKey.PARTICLE, Identifier.of("corpora", "block/server_side")),
                blockStateModelGenerator.modelCollector
        );

        blockStateModelGenerator.blockStateCollector.accept(
                BlockStateModelGenerator.createSingletonBlockState(
                        ModBlocks.SERVER_BLOCK,
                        modelId
                )
        );


    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SHELL_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.SHELL_ITEM_BROKEN, Models.GENERATED);
    }


}
