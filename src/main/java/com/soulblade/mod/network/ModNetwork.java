package com.soulblade.mod.network;

import com.soulblade.mod.SoulBladeMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.SimpleChannel;

public class ModNetwork {

    public static SimpleChannel CHANNEL;

    public static void register() {
        CHANNEL = ChannelBuilder
                .named(ResourceLocation.fromNamespaceAndPath(SoulBladeMod.MOD_ID, "main"))
                .networkProtocolVersion(1)
                .simpleChannel();

        CHANNEL.messageBuilder(AbilityActivationPacket.class)
                .encoder(AbilityActivationPacket::toBytes)
                .decoder(AbilityActivationPacket::new)
                .consumerMainThread(AbilityActivationPacket::handle)
                .add();
    }

    public static void sendAbilityActivation() {
        CHANNEL.sendToServer(new AbilityActivationPacket());
    }
}
