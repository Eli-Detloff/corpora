package corpora.modid.entity.client;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class ShellModel<T extends ShellEntity> extends HierarchicalModel<T> {

    public static final ModelLayerLocation SHELL =
            new ModelLayerLocation(
                    ResourceLocation.fromNamespaceAndPath(Corpora.MOD_ID, "shell"),
                    "main"
            );

    private final ModelPart root;

    private final ModelPart waist;
    private final ModelPart head;
    private final ModelPart body;
    private final ModelPart rightArm;
    private final ModelPart leftArm;
    private final ModelPart rightLeg;
    private final ModelPart leftLeg;

    public ShellModel(ModelPart root) {

        this.root = root.getChild("root");

        this.waist = this.root.getChild("waist");

        this.head = this.waist.getChild("head");
        this.body = this.waist.getChild("body");

        this.rightArm = this.waist.getChild("right_arm");
        this.leftArm = this.waist.getChild("left_arm");

        this.rightLeg = this.root.getChild("right_leg");
        this.leftLeg = this.root.getChild("left_leg");
    }

    public static LayerDefinition getTexturedModelData() {

        MeshDefinition modelData = new MeshDefinition();
        PartDefinition rootData = modelData.getRoot();

        // Root
        PartDefinition root = rootData.addOrReplaceChild(
                "root",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F)
        );

        // Waist
        PartDefinition waist = root.addOrReplaceChild(
                "waist",
                CubeListBuilder.create(),
                PartPose.offset(0.0F, -12.0F, 0.0F)
        );

        // Head
        PartDefinition head = waist.addOrReplaceChild(
                "head",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(
                                -4.0F, -8.0F, -4.0F,
                                8.0F, 8.0F, 8.0F,
                                new CubeDeformation(0.0F)
                        )
                        .texOffs(32, 0)
                        .addBox(
                                -4.0F, -8.0F, -4.0F,
                                8.0F, 8.0F, 8.0F,
                                new CubeDeformation(0.5F)
                        ),
                PartPose.offset(0.0F, -12.0F, 0.0F)
        );

        // Body
        PartDefinition body = waist.addOrReplaceChild(
                "body",
                CubeListBuilder.create()
                        .texOffs(16, 16)
                        .addBox(
                                -4.0F, 0.0F, -2.0F,
                                8.0F, 12.0F, 4.0F
                        )
                        .texOffs(16, 32)
                        .addBox(
                                -4.0F, 0.0F, -2.0F,
                                8.0F, 12.0F, 4.0F,
                                new CubeDeformation(0.25F)
                        ),
                PartPose.offset(0.0F, -12.0F, 0.0F)
        );

        // Right Arm
        PartDefinition rightArm = waist.addOrReplaceChild(
                "right_arm",
                CubeListBuilder.create()
                        .texOffs(40, 16)
                        .addBox(
                                -2.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F
                        )
                        .texOffs(40, 32)
                        .addBox(
                                -2.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F,
                                new CubeDeformation(0.25F)
                        ),
                PartPose.offset(-5.0F, -10.0F, 0.0F)
        );

        // Left Arm
        PartDefinition leftArm = waist.addOrReplaceChild(
                "left_arm",
                CubeListBuilder.create()
                        .texOffs(32, 48)
                        .addBox(
                                -1.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F
                        )
                        .texOffs(48, 48)
                        .addBox(
                                -1.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F,
                                new CubeDeformation(0.25F)
                        ),
                PartPose.offset(5.0F, -10.0F, 0.0F)
        );

        // Right Leg
        PartDefinition rightLeg = root.addOrReplaceChild(
                "right_leg",
                CubeListBuilder.create()
                        .texOffs(0, 16)
                        .addBox(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F
                        )
                        .texOffs(0, 32)
                        .addBox(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F,
                                new CubeDeformation(0.25F)
                        ),
                PartPose.offset(-1.9F, -12.0F, 0.0F)
        );

        // Left Leg
        PartDefinition leftLeg = root.addOrReplaceChild(
                "left_leg",
                CubeListBuilder.create()
                        .texOffs(16, 48)
                        .addBox(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F
                        )
                        .texOffs(0, 48)
                        .addBox(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F,
                                new CubeDeformation(0.25F)
                        ),
                PartPose.offset(1.9F, -12.0F, 0.0F)
        );

        return LayerDefinition.create(modelData, 64, 64);
    }


    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {

        this.root().getAllParts().forEach(ModelPart::resetPose);

        // Head rotation
        this.head.yRot = i * 0.017453292F;
        this.head.xRot = j * 0.017453292F;

        // Walking animation
        this.rightLeg.xRot =
                Mth.cos(f * 0.6662F)
                        * 1.4F
                        * g;

        this.leftLeg.xRot =
                Mth.cos(f * 0.6662F + (float) Math.PI)
                        * 1.4F
                        * g;

        this.rightArm.xRot =
                Mth.cos(f * 0.6662F + (float) Math.PI)
                        * 1.4F
                        * g;

        this.leftArm.xRot =
                Mth.cos(f * 0.6662F)
                        * 1.4F
                        * g;
    }

    @Override
    public void renderToBuffer(
            PoseStack matrices,
            VertexConsumer vertices,
            int light,
            int overlay,
            int color
    ) {

        root.render(matrices, vertices, light, overlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }


}