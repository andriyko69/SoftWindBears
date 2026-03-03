package io.github.andriyko69.softwindbears;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = SoftWindBears.MOD_ID)
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ARE_FRIENDS;
    private static final ModConfigSpec.IntValue BEAR_HEALTH;
    private static final ModConfigSpec.IntValue BEAR_FOLLOW_RANGE;
    private static final ModConfigSpec.IntValue BEAR_ATTACK_DAMAGE;
    private static final ModConfigSpec.DoubleValue BEAR_SPEED;
    private static final ModConfigSpec.IntValue BEAR_ANGER_MIN_DURATION;
    private static final ModConfigSpec.IntValue BEAR_ANGER_MAX_DURATION;

    static {
        BUILDER.push("bear_settings");
        ARE_FRIENDS = BUILDER
                .comment("Whether the bears should be friendly towards players.")
                .define("areFriends", false);
        BEAR_HEALTH = BUILDER
                .comment("The health of the bears.")
                .defineInRange("bearHealth", 30, 1, Integer.MAX_VALUE);
        BEAR_FOLLOW_RANGE = BUILDER
                .comment("The follow range of the bears.")
                .defineInRange("bearFollowRange", 16, 1, Integer.MAX_VALUE);
        BEAR_ATTACK_DAMAGE = BUILDER
                .comment("The attack damage of the bears.")
                .defineInRange("bearAttackDamage", 6, 1, Integer.MAX_VALUE);
        BEAR_SPEED = BUILDER
                .comment("The movement speed of the bears.")
                .defineInRange("bearSpeed", 0.25, 0.0, Double.MAX_VALUE);
        BEAR_ANGER_MIN_DURATION = BUILDER
                .comment("The minimum duration (in ticks) that a bear will remain angry.")
                .defineInRange("bearAngerMinDuration", 20, 1, Integer.MAX_VALUE);
        BEAR_ANGER_MAX_DURATION = BUILDER
                .comment("The maximum duration (in ticks) that a bear will remain angry.")
                .defineInRange("bearAngerMaxDuration", 40, 1, Integer.MAX_VALUE);
        BUILDER.pop();
    }

    static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean areFriends;
    public static int bearHealth;
    public static int bearFollowRange;
    public static int bearAttackDamage;
    public static double bearSpeed;
    public static int bearAngerMinDuration;
    public static int bearAngerMaxDuration;

    @SubscribeEvent
    static void onConfigLoading(final ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() != SPEC) return;
        bake();
    }

    @SubscribeEvent
    static void onConfigReloading(final ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() != SPEC) return;
        bake();
    }

    private static void bake() {
        areFriends = ARE_FRIENDS.get();
        bearHealth = BEAR_HEALTH.get();
        bearFollowRange = BEAR_FOLLOW_RANGE.get();
        bearAttackDamage = BEAR_ATTACK_DAMAGE.get();
        bearSpeed = BEAR_SPEED.get();
        bearAngerMinDuration = BEAR_ANGER_MIN_DURATION.get();
        bearAngerMaxDuration = BEAR_ANGER_MAX_DURATION.get();
    }
}
