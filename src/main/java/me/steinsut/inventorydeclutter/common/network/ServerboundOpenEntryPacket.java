package me.steinsut.inventorydeclutter.common.network;

import com.mojang.logging.LogUtils;
import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import me.steinsut.inventorydeclutter.api.entry.AbstractMenuEntry;
import me.steinsut.inventorydeclutter.api.entry.IDeclutterEntry;
import me.steinsut.inventorydeclutter.api.registry.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

public record ServerboundOpenEntryPacket(ResourceLocation entryLocation, ItemStack carried) implements CustomPacketPayload {
    private final static Logger LOGGER = LogUtils.getLogger();

    public static final CustomPacketPayload.Type<ServerboundOpenEntryPacket> TYPE =
            new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "s_open_entry"));

    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundOpenEntryPacket> STREAM_CODEC = StreamCodec.composite(
            ResourceLocation.STREAM_CODEC,
            ServerboundOpenEntryPacket::entryLocation,
            ItemStack.STREAM_CODEC,
            ServerboundOpenEntryPacket::carried,
            ServerboundOpenEntryPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handleOnServer(final ServerboundOpenEntryPacket packet, final IPayloadContext context) {
        IDeclutterEntry entry = Registries.DECLUTTER_ENTRIES.getValue(packet.entryLocation);
        if(entry == null) {
            LOGGER.error("no entry found for location {}", packet.entryLocation);
            return;
        }

        ServerPlayer player = (ServerPlayer) context.player();

        switch(entry.getType()) {
            case MENU -> {
                ItemStack carried = player.isCreative() ? packet.carried : player.containerMenu.getCarried();

                AbstractMenuEntry menuEntry = (AbstractMenuEntry) entry;
                player.containerMenu.setCarried(ItemStack.EMPTY);
                player.openMenu(new SimpleMenuProvider(menuEntry::createMenu, Component.translatable(entry.getTranslationKey())));

                if(!carried.isEmpty()) {
                    player.containerMenu.setCarried(carried);
                    PacketDistributor.sendToPlayer(player, new ClientboundCarryItemPacket(carried));
                }
            }
            case SCREEN -> {
                player.doCloseContainer();
                PacketDistributor.sendToPlayer(player, new ClientboundOpenScreenPacket(packet.entryLocation));
            }
            default -> LOGGER.info("You should not be here. What shenanigans did you pull?");
        }
    }
}
