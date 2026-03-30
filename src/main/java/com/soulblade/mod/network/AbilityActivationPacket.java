package com.soulblade.mod.network;

import com.soulblade.mod.item.SoulBladeSword;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.SimpleChannel;

public class AbilityActivationPacket {

    public AbilityActivationPacket() {}

    public AbilityActivationPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public void handle(SimpleChannel.MessageBuilder.Context context) {
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SoulBladeSword.tryActivateAbility(player);
            }
        });
        context.setPacketHandled(true);
    }
}
