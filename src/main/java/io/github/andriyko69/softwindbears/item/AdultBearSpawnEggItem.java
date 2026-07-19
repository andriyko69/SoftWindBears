package io.github.andriyko69.softwindbears.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.event.EventHooks;

import java.util.Optional;
import java.util.function.Supplier;

public class AdultBearSpawnEggItem extends DeferredSpawnEggItem {
    public AdultBearSpawnEggItem(Supplier<? extends EntityType<? extends Mob>> type,
                                 int backgroundColor,
                                 int highlightColor,
                                 Properties properties) {
        super(type, backgroundColor, highlightColor, properties);
    }

    @Override
    public Optional<Mob> spawnOffspringFromSpawnEgg(Player player,
                                                    Mob parent,
                                                    EntityType<? extends Mob> entityType,
                                                    ServerLevel level,
                                                    Vec3 position,
                                                    ItemStack stack) {
        if (!this.spawnsEntity(stack, entityType)) {
            return Optional.empty();
        }

        Mob bear = entityType.create(level);
        if (bear == null) {
            return Optional.empty();
        }

        bear.moveTo(position.x(), position.y(), position.z(), 0.0F, 0.0F);
        EventHooks.finalizeMobSpawn(
                bear,
                level,
                level.getCurrentDifficultyAt(bear.blockPosition()),
                MobSpawnType.SPAWN_EGG,
                null
        );
        bear.setBaby(false);
        bear.setCustomName(stack.get(DataComponents.CUSTOM_NAME));
        level.addFreshEntityWithPassengers(bear);
        stack.consume(1, player);
        return Optional.of(bear);
    }
}
