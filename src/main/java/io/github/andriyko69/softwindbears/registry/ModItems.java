package io.github.andriyko69.softwindbears.registry;

import io.github.andriyko69.softwindbears.SoftWindBears;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModItems {
    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(SoftWindBears.MOD_ID);

    public static final Supplier<Item> GRIZZLY_SPAWN_EGG = ITEMS.register("grizzly_spawn_egg", () ->
            new DeferredSpawnEggItem(
                    ModEntities.GRIZZLY_BEAR,
                    0x8B4513, // Brown color
                    0xFFFFFF,
                    new Item.Properties()
            ));
    public static final Supplier<Item> GRIZZLY_LEATHER = ITEMS.registerSimpleItem("grizzly_leather");

    public static final Supplier<Item> PANDA_SPAWN_EGG = ITEMS.register("panda_spawn_egg", () ->
            new DeferredSpawnEggItem(
                    ModEntities.BEAR_PANDA,
                    0xFFFFFF,
                    0x000000,
                    new Item.Properties()
            ));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
