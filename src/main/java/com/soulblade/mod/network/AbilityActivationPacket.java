package com.soulblade.mod.network;

import com.soulblade.mod.item.SoulBladeSword;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class AbilityActivationPacket {

    public AbilityActivationPacket() {}

    public AbilityActivationPacket(FriendlyByteBuf buf) {}

    public void toBytes(FriendlyByteBuf buf) {}

    public boolean handle(Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                SoulBladeSword.tryActivateAbility(player);
            }
        });
        context.setPacketHandled(true);
        return true;
    }
}
