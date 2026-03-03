package io.github.andriyko69.softwindbears.registry;

import io.github.andriyko69.softwindbears.entity.AbstractBearEntity;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

public final class ModAttributes {
    private ModAttributes() {
    }

    public static void onEntityAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.GRIZZLY_BEAR.get(), AbstractBearEntity.createAttributes().build());
        event.put(ModEntities.BEAR_PANDA.get(), AbstractBearEntity.createAttributes().build());
    }
}