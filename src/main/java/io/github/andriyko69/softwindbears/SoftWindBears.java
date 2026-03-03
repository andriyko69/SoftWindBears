package io.github.andriyko69.softwindbears;

import io.github.andriyko69.softwindbears.registry.ModAttributes;
import io.github.andriyko69.softwindbears.registry.ModEntities;
import io.github.andriyko69.softwindbears.registry.ModItems;
import io.github.andriyko69.softwindbears.registry.ModSounds;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(SoftWindBears.MOD_ID)
public class SoftWindBears {
    public static final String MOD_ID = "softwindbears";

    public SoftWindBears(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);

        ModEntities.register(modEventBus);
        ModSounds.register(modEventBus);
        ModItems.register(modEventBus);

        modEventBus.addListener(this::registerSpawnPlacements);
        modEventBus.addListener(this::addCreative);

        modEventBus.addListener(ModAttributes::onEntityAttributes);
    }

    private void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(
                ModEntities.GRIZZLY_BEAR.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );

        event.register(
                ModEntities.BEAR_PANDA.get(),
                SpawnPlacementTypes.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.accept(ModItems.GRIZZLY_SPAWN_EGG.get());
            event.accept(ModItems.PANDA_SPAWN_EGG.get());
        }

        if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(ModItems.BEAR_SKIN.get());
        }
    }
}
