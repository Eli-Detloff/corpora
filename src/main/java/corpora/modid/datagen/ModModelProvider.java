package corpora.modid.datagen;

import corpora.modid.block.ServerBlock;
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
        Identifier offModel = Models.CUBE.upload(
                ModBlocks.SERVER_BLOCK,
                "_off",
                new TextureMap()
                        .put(TextureKey.NORTH, Identifier.of("corpora", "block/server_lights_off"))
                        .put(TextureKey.SOUTH, Identifier.of("corpora", "block/server_lights_off"))
                        .put(TextureKey.EAST, Identifier.of("corpora", "block/server_side"))
                        .put(TextureKey.WEST, Identifier.of("corpora", "block/server_side"))
                        .put(TextureKey.UP, Identifier.of("corpora", "block/server_top"))
                        .put(TextureKey.DOWN, Identifier.of("corpora", "block/server_bottom"))
                        .put(TextureKey.PARTICLE, Identifier.of("corpora", "block/server_side")),
                blockStateModelGenerator.modelCollector
        );

        Identifier onModel = Models.CUBE.upload(
                ModBlocks.SERVER_BLOCK,
                "_on",
                new TextureMap()
                        .put(TextureKey.NORTH, Identifier.of("corpora", "block/server_lights_on"))
                        .put(TextureKey.SOUTH, Identifier.of("corpora", "block/server_lights_on"))
                        .put(TextureKey.EAST, Identifier.of("corpora", "block/server_side"))
                        .put(TextureKey.WEST, Identifier.of("corpora", "block/server_side"))
                        .put(TextureKey.UP, Identifier.of("corpora", "block/server_top"))
                        .put(TextureKey.DOWN, Identifier.of("corpora", "block/server_bottom"))
                        .put(TextureKey.PARTICLE, Identifier.of("corpora", "block/server_side")),
                blockStateModelGenerator.modelCollector
        );

        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(ModBlocks.SERVER_BLOCK)
                        .coordinate(
                                BlockStateVariantMap.create(ServerBlock.POWERED)
                                        .register(false,
                                                BlockStateVariant.create()
                                                        .put(VariantSettings.MODEL, offModel))
                                        .register(true,
                                                BlockStateVariant.create()
                                                        .put(VariantSettings.MODEL, onModel))
                        )
        );

        blockStateModelGenerator.registerParentedItemModel(
                ModBlocks.SERVER_BLOCK,
                offModel
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        itemModelGenerator.register(ModItems.SHELL_ITEM, Models.GENERATED);
        itemModelGenerator.register(ModItems.SHELL_ITEM_BROKEN, Models.GENERATED);

    }


}
