package me.steinsut.inventorydeclutter.common.network;

import com.mojang.logging.LogUtils;
import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public record ClientboundCarryItemPacket(ItemStack carried) implements CustomPacketPayload {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static final CustomPacketPayload.Type<ClientboundCarryItemPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "c_carry_item"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCarryItemPacket> STREAM_CODEC = StreamCodec.composite(
            ItemStack.OPTIONAL_STREAM_CODEC,
            ClientboundCarryItemPacket::carried,
            ClientboundCarryItemPacket::new
    );

    @Override
    @MethodsReturnNonnullByDefault
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(final ClientboundCarryItemPacket packet, final IPayloadContext context) {
        LocalPlayer player = Minecraft.getInstance().player;
        if(player == null) {
            LOGGER.error("player is null");
            return;
        }

        player.containerMenu.setCarried(packet.carried);
    }
}
