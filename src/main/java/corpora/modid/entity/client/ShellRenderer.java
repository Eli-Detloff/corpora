package corpora.modid.entity.client;


import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class ShellRenderer extends MobEntityRenderer<ShellEntity, ShellModel<ShellEntity>> {
    public ShellRenderer(EntityRendererFactory.Context context) {
        super(context, new ShellModel<>(context.getPart(ShellModel.SHELL)), 0.75f);
    }

    @Override
    public Identifier getTexture(ShellEntity entity) {
        return Identifier.of(Corpora.MOD_ID, "textures/entity/shell/robot.png");
    }

    @Override
    public void render(ShellEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        if (livingEntity.isBaby()) {
            matrixStack.scale(0.5f, 0.5f, 0.5f);
        } else {
            matrixStack.scale(1f, 1f, 1f);
        }


        super.render(livingEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
