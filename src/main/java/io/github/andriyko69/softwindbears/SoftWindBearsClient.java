package io.github.andriyko69.softwindbears;

import io.github.andriyko69.softwindbears.client.model.BearEntityModel;
import io.github.andriyko69.softwindbears.client.renderer.BearEntityRenderer;
import io.github.andriyko69.softwindbears.registry.ModEntities;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;

@Mod(value = SoftWindBears.MOD_ID, dist = Dist.CLIENT)
public class SoftWindBearsClient {
    public static final ModelLayerLocation BEAR_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "bear"), "main");

    public SoftWindBearsClient(IEventBus modEventBus) {
        modEventBus.addListener(this::registerRenderers);
        modEventBus.addListener(this::registerLayerDefinitions);
    }

    private void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.GRIZZLY_BEAR.get(), BearEntityRenderer::new);
        event.registerEntityRenderer(ModEntities.BEAR_PANDA.get(), BearEntityRenderer::new);
    }

    private void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(
                BEAR_LAYER,
                BearEntityModel::createBodyLayer
        );
    }
}
