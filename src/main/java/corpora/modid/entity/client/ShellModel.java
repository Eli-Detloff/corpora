package corpora.modid.entity.client;


import corpora.modid.Corpora;
import corpora.modid.entity.custom.ShellEntity;
import net.minecraft.client.model.*;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.SinglePartEntityModel;
import net.minecraft.client.util.math.MatrixStack;

import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public class ShellModel<T extends ShellEntity> extends SinglePartEntityModel<T> {

    public static final EntityModelLayer SHELL =
            new EntityModelLayer(
                    Identifier.of(Corpora.MOD_ID, "shell"),
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

    public static TexturedModelData getTexturedModelData() {

        ModelData modelData = new ModelData();
        ModelPartData rootData = modelData.getRoot();

        // Root
        ModelPartData root = rootData.addChild(
                "root",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, 24.0F, 0.0F)
        );

        // Waist
        ModelPartData waist = root.addChild(
                "waist",
                ModelPartBuilder.create(),
                ModelTransform.pivot(0.0F, -12.0F, 0.0F)
        );

        // Head
        ModelPartData head = waist.addChild(
                "head",
                ModelPartBuilder.create()
                        .uv(0, 0)
                        .cuboid(
                                -4.0F, -8.0F, -4.0F,
                                8.0F, 8.0F, 8.0F,
                                new Dilation(0.0F)
                        )
                        .uv(32, 0)
                        .cuboid(
                                -4.0F, -8.0F, -4.0F,
                                8.0F, 8.0F, 8.0F,
                                new Dilation(0.5F)
                        ),
                ModelTransform.pivot(0.0F, -12.0F, 0.0F)
        );

        // Body
        ModelPartData body = waist.addChild(
                "body",
                ModelPartBuilder.create()
                        .uv(16, 16)
                        .cuboid(
                                -4.0F, 0.0F, -2.0F,
                                8.0F, 12.0F, 4.0F
                        )
                        .uv(16, 32)
                        .cuboid(
                                -4.0F, 0.0F, -2.0F,
                                8.0F, 12.0F, 4.0F,
                                new Dilation(0.25F)
                        ),
                ModelTransform.pivot(0.0F, -12.0F, 0.0F)
        );

        // Right Arm
        ModelPartData rightArm = waist.addChild(
                "right_arm",
                ModelPartBuilder.create()
                        .uv(40, 16)
                        .cuboid(
                                -2.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F
                        )
                        .uv(40, 32)
                        .cuboid(
                                -2.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F,
                                new Dilation(0.25F)
                        ),
                ModelTransform.pivot(-5.0F, -10.0F, 0.0F)
        );

        // Left Arm
        ModelPartData leftArm = waist.addChild(
                "left_arm",
                ModelPartBuilder.create()
                        .uv(32, 48)
                        .cuboid(
                                -1.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F
                        )
                        .uv(48, 48)
                        .cuboid(
                                -1.0F, -2.0F, -2.0F,
                                3.0F, 12.0F, 4.0F,
                                new Dilation(0.25F)
                        ),
                ModelTransform.pivot(5.0F, -10.0F, 0.0F)
        );

        // Right Leg
        ModelPartData rightLeg = root.addChild(
                "right_leg",
                ModelPartBuilder.create()
                        .uv(0, 16)
                        .cuboid(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F
                        )
                        .uv(0, 32)
                        .cuboid(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F,
                                new Dilation(0.25F)
                        ),
                ModelTransform.pivot(-1.9F, -12.0F, 0.0F)
        );

        // Left Leg
        ModelPartData leftLeg = root.addChild(
                "left_leg",
                ModelPartBuilder.create()
                        .uv(16, 48)
                        .cuboid(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F
                        )
                        .uv(0, 48)
                        .cuboid(
                                -2.0F, 0.0F, -2.0F,
                                4.0F, 12.0F, 4.0F,
                                new Dilation(0.25F)
                        ),
                ModelTransform.pivot(1.9F, -12.0F, 0.0F)
        );

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    public void setAngles(
            T entity,
            float limbSwing,
            float limbSwingAmount,
            float ageInTicks,
            float netHeadYaw,
            float headPitch
    ) {

        this.getPart().traverse().forEach(ModelPart::resetTransform);

        // Head rotation
        this.head.yaw = netHeadYaw * 0.017453292F;
        this.head.pitch = headPitch * 0.017453292F;

        // Walking animation
        this.rightLeg.pitch =
                MathHelper.cos(limbSwing * 0.6662F)
                        * 1.4F
                        * limbSwingAmount;

        this.leftLeg.pitch =
                MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)
                        * 1.4F
                        * limbSwingAmount;

        this.rightArm.pitch =
                MathHelper.cos(limbSwing * 0.6662F + (float) Math.PI)
                        * 1.4F
                        * limbSwingAmount;

        this.leftArm.pitch =
                MathHelper.cos(limbSwing * 0.6662F)
                        * 1.4F
                        * limbSwingAmount;
    }

    @Override
    public void render(
            MatrixStack matrices,
            VertexConsumer vertices,
            int light,
            int overlay,
            int color
    ) {

        root.render(matrices, vertices, light, overlay, color);
    }

    @Override
    public ModelPart getPart() {
        return root;
    }
}