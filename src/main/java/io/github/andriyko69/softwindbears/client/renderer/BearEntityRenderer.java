package io.github.andriyko69.softwindbears.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.andriyko69.softwindbears.SoftWindBears;
import io.github.andriyko69.softwindbears.SoftWindBearsClient;
import io.github.andriyko69.softwindbears.client.model.BearEntityModel;
import io.github.andriyko69.softwindbears.entity.AbstractBearEntity;
import io.github.andriyko69.softwindbears.registry.ModEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class BearEntityRenderer extends MobRenderer<AbstractBearEntity, BearEntityModel<AbstractBearEntity>> {

    private static final ResourceLocation GRIZZLY_TEX =
            ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "textures/entity/grizzly_bear.png");
    private static final ResourceLocation PANDA_TEX =
            ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "textures/entity/bear_panda.png");

    public BearEntityRenderer(EntityRendererProvider.Context ctx) {
        super(ctx, new BearEntityModel<>(ctx.bakeLayer(SoftWindBearsClient.BEAR_LAYER)), 0.9F);
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(AbstractBearEntity entity) {
        return entity.getType() == ModEntities.BEAR_PANDA.get() ? PANDA_TEX : GRIZZLY_TEX;
    }

    protected void scale(@NotNull AbstractBearEntity bearEntity, PoseStack matrixStack, float f) {
        matrixStack.scale(1.2F, 1.2F, 1.2F);
        super.scale(bearEntity, matrixStack, f);
    }
}
