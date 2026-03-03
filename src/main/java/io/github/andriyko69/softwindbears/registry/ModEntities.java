package io.github.andriyko69.softwindbears.registry;

import io.github.andriyko69.softwindbears.SoftWindBears;
import io.github.andriyko69.softwindbears.entity.GrizzlyBearEntity;
import io.github.andriyko69.softwindbears.entity.PandaBearEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, SoftWindBears.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<GrizzlyBearEntity>> GRIZZLY_BEAR =
            ENTITIES.register("grizzly_bear", () ->
                    EntityType.Builder.of(GrizzlyBearEntity::new, MobCategory.CREATURE)
                            .sized(1.4f, 1.4f)
                            .build("grizzly_bear"));

    public static final DeferredHolder<EntityType<?>, EntityType<PandaBearEntity>> BEAR_PANDA =
            ENTITIES.register("bear_panda", () ->
                    EntityType.Builder.of(PandaBearEntity::new, MobCategory.CREATURE)
                            .sized(1.4f, 1.4f)
                            .build("bear_panda"));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}