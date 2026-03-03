package io.github.andriyko69.softwindbears.registry;

import io.github.andriyko69.softwindbears.SoftWindBears;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS =
            DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, SoftWindBears.MOD_ID);

    public static DeferredHolder<SoundEvent, SoundEvent> BEAR_AMBIENT =
            SOUNDS.register("entity.bear.ambient",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "entity.bear.ambient")));
    public static DeferredHolder<SoundEvent, SoundEvent> BEAR_AMBIENT_BABY =
            SOUNDS.register("entity.bear.ambient_baby",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "entity.bear.ambient_baby")));
    public static DeferredHolder<SoundEvent, SoundEvent> BEAR_DEATH =
            SOUNDS.register("entity.bear.death",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "entity.bear.death")));
    public static DeferredHolder<SoundEvent, SoundEvent> BEAR_HURT =
            SOUNDS.register("entity.bear.hurt",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "entity.bear.hurt")));
    public static DeferredHolder<SoundEvent, SoundEvent> BEAR_STEP =
            SOUNDS.register("entity.bear.step",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "entity.bear.step")));
    public static DeferredHolder<SoundEvent, SoundEvent> BEAR_WARNING =
            SOUNDS.register("entity.bear.warning",
                    () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(SoftWindBears.MOD_ID, "entity.bear.warning")));


     public static void register(IEventBus eventBus) {
         SOUNDS.register(eventBus);
     }
}
