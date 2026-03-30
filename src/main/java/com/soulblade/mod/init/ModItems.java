package com.soulblade.mod.init;

import com.soulblade.mod.SoulBladeMod;
import com.soulblade.mod.item.SoulBladeSword;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = SoulBladeMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModItems {

    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, SoulBladeMod.MOD_ID);

    public static final RegistryObject<SoulBladeSword> SOUL_BLADE =
            ITEMS.register("soul_blade", () -> new SoulBladeSword(
                    Tiers.NETHERITE,
                    9,    // attackDamageModifier -> 9 + 1 (sword base) = 10 damage
                    -2.4f, // attackSpeedModifier
                    new Item.Properties()
            ));

    @SubscribeEvent
    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == net.minecraft.world.item.CreativeModeTabs.COMBAT) {
            event.accept(SOUL_BLADE);
        }
    }
}
