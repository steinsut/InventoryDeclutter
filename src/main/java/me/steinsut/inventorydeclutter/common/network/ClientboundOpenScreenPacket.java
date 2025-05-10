package me.steinsut.inventorydeclutter.common.network;

import com.mojang.logging.LogUtils;
import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import me.steinsut.inventorydeclutter.api.entry.AbstractScreenEntry;
import me.steinsut.inventorydeclutter.api.entry.EntryType;
import me.steinsut.inventorydeclutter.api.entry.IDeclutterEntry;
import me.steinsut.inventorydeclutter.api.registry.Registries;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.slf4j.Logger;

public record ClientboundOpenScreenPacket(ResourceLocation entryLocation) implements CustomPacketPayload {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static final CustomPacketPayload.Type<ClientboundOpenScreenPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "c_open_screen"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundOpenScreenPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ClientboundOpenScreenPacket::entryLocation,
            ClientboundOpenScreenPacket::new
    );

    @Override
    @MethodsReturnNonnullByDefault
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnClient(final ClientboundOpenScreenPacket packet, final IPayloadContext context) {
        Minecraft mc = Minecraft.getInstance();

        if (mc.level == null) {
            LOGGER.error("level is null");
            return;
        }

        if (!mc.level.isClientSide) return;

        Player player = context.player();

        IDeclutterEntry entry = Registries.DECLUTTER_ENTRIES.getValue(packet.entryLocation);
        if (entry == null) {
            LOGGER.error("no entry found for location {}", packet.entryLocation);
            return;
        }

        if (entry.getType() != EntryType.SCREEN) {
            LOGGER.error("entry {} is not a screen", packet.entryLocation);
            return;
        }

        mc.setScreen(((AbstractScreenEntry) entry).createScreen(player));
    }
}
