package com.soulblade.mod.event;

import com.soulblade.mod.SoulBladeMod;
import com.soulblade.mod.init.ModItems;
import com.soulblade.mod.item.SoulBladeSword;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SoulBladeMod.MOD_ID)
public class SoulBladeEvents {

    /**
     * Track mob kills with the Soul Blade
     */
    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        // Check if player is holding the Soul Blade
        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean holdingSoulBlade = mainHand.is(ModItems.SOUL_BLADE.get())
                || offHand.is(ModItems.SOUL_BLADE.get());

        if (!holdingSoulBlade) return;

        // Don't count other players
        if (event.getEntity() instanceof Player) return;

        SoulBladeSword.onKillMob(player);

        // Remove soul fire tag when mob dies
        event.getEntity().getPersistentData().remove("soulblade_fire");
    }

    /**
     * Tick: expire ability + keep soul fire burning on marked entities
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide()) return;

        // Tick ability timer
        SoulBladeSword.tickAbility(player);

        // Display charge progress every 5 kills (optional feedback)
        // (handled in onKillMob above)
    }

    /**
     * Keep entities with soul fire tag burning — refresh their fire every tick
     */
    @SubscribeEvent
    public static void onLivingTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        if (entity.getPersistentData().getBoolean("soulblade_fire")) {
            // Keep the fire going — reset fire ticks every tick so it never goes out
            entity.setSecondsOnFire(10);

            // Spawn soul fire particles around burning entity
            if (entity.level() instanceof net.minecraft.server.level.ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        net.minecraft.core.particles.ParticleTypes.SOUL_FIRE_FLAME,
                        entity.getX(), entity.getY() + 0.5, entity.getZ(),
                        3, 0.3, 0.4, 0.3, 0.01
                );
            }
        }
    }
}
