package corpora.modid.datagen;

import corpora.modid.block.ServerBlock;
import corpora.modid.init.ModBlocks;
import corpora.modid.init.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.models.BlockModelGenerators;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.data.models.blockstates.PropertyDispatch;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.data.models.blockstates.VariantProperties;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockModelGenerators blockStateModelGenerator) {
        ResourceLocation offModel = ModelTemplates.CUBE.createWithSuffix(
                ModBlocks.SERVER_BLOCK,
                "_off",
                new TextureMapping()
                        .put(TextureSlot.NORTH, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_lights_off"))
                        .put(TextureSlot.SOUTH, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_lights_off"))
                        .put(TextureSlot.EAST, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_side"))
                        .put(TextureSlot.WEST, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_side"))
                        .put(TextureSlot.UP, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_top"))
                        .put(TextureSlot.DOWN, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_bottom"))
                        .put(TextureSlot.PARTICLE, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_side")),
                blockStateModelGenerator.modelOutput
        );

        ResourceLocation onModel = ModelTemplates.CUBE.createWithSuffix(
                ModBlocks.SERVER_BLOCK,
                "_on",
                new TextureMapping()
                        .put(TextureSlot.NORTH, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_lights_on"))
                        .put(TextureSlot.SOUTH, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_lights_on"))
                        .put(TextureSlot.EAST, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_side"))
                        .put(TextureSlot.WEST, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_side"))
                        .put(TextureSlot.UP, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_top"))
                        .put(TextureSlot.DOWN, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_bottom"))
                        .put(TextureSlot.PARTICLE, ResourceLocation.fromNamespaceAndPath("corpora", "block/server_side")),
                blockStateModelGenerator.modelOutput
        );

        blockStateModelGenerator.blockStateOutput.accept(
                MultiVariantGenerator.multiVariant(ModBlocks.SERVER_BLOCK)
                        .with(
                                PropertyDispatch.property(ServerBlock.POWERED)
                                        .select(false,
                                                Variant.variant()
                                                        .with(VariantProperties.MODEL, offModel))
                                        .select(true,
                                                Variant.variant()
                                                        .with(VariantProperties.MODEL, onModel))
                        )
        );

        blockStateModelGenerator.delegateItemModel(
                ModBlocks.SERVER_BLOCK,
                offModel
        );
    }

    @Override
    public void generateItemModels(ItemModelGenerators itemModelGenerator) {
        itemModelGenerator.generateFlatItem(ModItems.SHELL_ITEM, ModelTemplates.FLAT_ITEM);
        itemModelGenerator.generateFlatItem(ModItems.SHELL_ITEM_BROKEN, ModelTemplates.FLAT_ITEM);

    }


}
