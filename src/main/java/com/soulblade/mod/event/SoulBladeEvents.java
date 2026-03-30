package com.soulblade.mod.event;

import com.soulblade.mod.SoulBladeMod;
import com.soulblade.mod.init.ModItems;
import com.soulblade.mod.item.SoulBladeSword;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SoulBladeMod.MOD_ID)
public class SoulBladeEvents {

    @SubscribeEvent
    public static void onMobDeath(LivingDeathEvent event) {
        if (!(event.getSource().getEntity() instanceof Player player)) return;

        ItemStack mainHand = player.getMainHandItem();
        ItemStack offHand = player.getOffhandItem();
        boolean holdingSoulBlade = mainHand.is(ModItems.SOUL_BLADE.get())
                || offHand.is(ModItems.SOUL_BLADE.get());

        if (!holdingSoulBlade) return;
        if (event.getEntity() instanceof Player) return;

        SoulBladeSword.onKillMob(player);
        event.getEntity().getPersistentData().remove("soulblade_fire");
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        Player player = event.player;
        if (player.level().isClientSide()) return;

        SoulBladeSword.tickAbility(player);
    }

    @SubscribeEvent
    public static void onLivingTick(net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        if (entity.getPersistentData().getBoolean("soulblade_fire")) {
            entity.igniteForSeconds(10);

            if (entity.level() instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(
                        ParticleTypes.SOUL_FIRE_FLAME,
                        entity.getX(), entity.getY() + 0.5, entity.getZ(),
                        3, 0.3, 0.4, 0.3, 0.01
                );
            }
        }
    }
}
