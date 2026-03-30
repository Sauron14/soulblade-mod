package com.soulblade.mod.client;

import com.soulblade.mod.SoulBladeMod;
import com.soulblade.mod.init.ModItems;
import com.soulblade.mod.item.SoulBladeSword;
import com.soulblade.mod.network.ModNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = SoulBladeMod.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        Player player = mc.player;
        if (player == null) return;

        // Check if holding Soul Blade
        boolean holdingSoulBlade = player.getMainHandItem().is(ModItems.SOUL_BLADE.get())
                || player.getOffhandItem().is(ModItems.SOUL_BLADE.get());

        if (!holdingSoulBlade) return;

        // Handle ability key press
        while (ModKeyBindings.ABILITY_KEY.consumeClick()) {
            ModNetwork.sendAbilityActivation();
        }
    }
}
