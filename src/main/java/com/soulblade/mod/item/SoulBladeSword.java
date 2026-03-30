package com.soulblade.mod.item;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;
import java.util.HashMap;
import java.util.Map;

public class SoulBladeSword extends SwordItem {

    // Per-player tracking (UUID -> data)
    public static final Map<UUID, Integer> killCount = new HashMap<>();
    public static final Map<UUID, Boolean> abilityActive = new HashMap<>();
    public static final Map<UUID, Long> abilityEndTime = new HashMap<>();

    public static final int KILLS_REQUIRED = 20;
    public static final int ABILITY_DURATION_TICKS = 20 * 60; // 1 minute
    public static final int ABILITY_BONUS_DAMAGE = 3; // 10 + 3 = 13

    public SoulBladeSword(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, attackDamageModifier, attackSpeedModifier, properties);
    }

    /**
     * Called when a mob is killed by this sword.
     * Increments kill counter for the player.
     */
    public static void onKillMob(Player player) {
        UUID uid = player.getUUID();
        if (abilityActive.getOrDefault(uid, false)) return; // Already active, no counting needed

        int kills = killCount.getOrDefault(uid, 0) + 1;
        killCount.put(uid, kills);

        if (kills >= KILLS_REQUIRED) {
            // Notify player ability is charged
            player.sendSystemMessage(Component.literal("§9[Soul Blade] §bAbility charged! Press the ability key to activate."));
        }
    }

    /**
     * Called when player activates the ability via keybind.
     */
    public static boolean tryActivateAbility(Player player) {
        UUID uid = player.getUUID();

        if (abilityActive.getOrDefault(uid, false)) {
            player.sendSystemMessage(Component.literal("§9[Soul Blade] §cAbility already active!"));
            return false;
        }

        int kills = killCount.getOrDefault(uid, 0);
        if (kills < KILLS_REQUIRED) {
            int remaining = KILLS_REQUIRED - kills;
            player.sendSystemMessage(Component.literal("§9[Soul Blade] §cNot charged! Kill §b" + remaining + " §cmore mobs."));
            return false;
        }

        // Activate ability
        abilityActive.put(uid, true);
        abilityEndTime.put(uid, player.level().getGameTime() + ABILITY_DURATION_TICKS);
        killCount.put(uid, 0);
        player.sendSystemMessage(Component.literal("§9[Soul Blade] §bAbility ACTIVATED! §360 seconds of Soul Power!"));
        return true;
    }

    /**
     * Check and expire ability if time is up. Call each tick.
     */
    public static void tickAbility(Player player) {
        UUID uid = player.getUUID();
        if (!abilityActive.getOrDefault(uid, false)) return;

        long endTime = abilityEndTime.getOrDefault(uid, 0L);
        if (player.level().getGameTime() >= endTime) {
            abilityActive.put(uid, false);
            killCount.put(uid, 0);
            player.sendSystemMessage(Component.literal("§9[Soul Blade] §cAbility expired. Kill §b" + KILLS_REQUIRED + " §cmobs to recharge."));
        }
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker instanceof Player player) {
            UUID uid = player.getUUID();
            boolean active = abilityActive.getOrDefault(uid, false);

            if (active) {
                // Apply extra damage
                target.hurt(player.damageSources().playerAttack(player), ABILITY_BONUS_DAMAGE);

                // Apply infinite blue fire — we use soul fire (blue fire in MC)
                // Soul fire tag: we set fire and mark with a custom tag
                target.setSecondsOnFire(Integer.MAX_VALUE / 20); // effectively infinite
                target.getPersistentData().putBoolean("soulblade_fire", true);

                // Spawn blue particles on server
                Level level = player.level();
                if (level instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.SOUL_FIRE_FLAME,
                            target.getX(), target.getY() + 1.0, target.getZ(),
                            15, 0.4, 0.5, 0.4, 0.05
                    );
                    serverLevel.sendParticles(
                            ParticleTypes.SOUL,
                            target.getX(), target.getY() + 0.5, target.getZ(),
                            10, 0.3, 0.3, 0.3, 0.02
                    );
                }
            }
        }
        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("§9Damage: §f10 §7(§b13 §7when active)"));
        tooltip.add(Component.literal("§9Ability: §bSoul Inferno"));
        tooltip.add(Component.literal("§7Kill §b20 mobs §7to charge."));
        tooltip.add(Component.literal("§7Press §b[Ability Key] §7to activate for §b60s§7."));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
