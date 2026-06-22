package corpora.modid.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class ShellRenderer extends MobRenderer<ShellEntity, ShellModel<ShellEntity>> {
    public ShellRenderer(EntityRendererProvider.Context context) {
        super(context, new ShellModel<>(context.bakeLayer(ShellModel.SHELL)), 0.75f);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(ShellEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, "textures/entity/shell/robot.png");
    }

    @Override
    public void render(ShellEntity livingEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i) {
        if (livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }


        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
