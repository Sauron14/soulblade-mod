package com.soulblade.mod;

import com.soulblade.mod.init.ModItems;
import com.soulblade.mod.event.SoulBladeEvents;
import com.soulblade.mod.network.ModNetwork;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SoulBladeMod.MOD_ID)
public class SoulBladeMod {

    public static final String MOD_ID = "soulblade";

    public SoulBladeMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ModItems.ITEMS.register(modEventBus);

        ModNetwork.register();
        MinecraftForge.EVENT_BUS.register(new SoulBladeEvents());
    }
}
