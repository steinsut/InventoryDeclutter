package me.steinsut.inventorydeclutter.common.event;

import com.mojang.logging.LogUtils;
import me.steinsut.inventorydeclutter.api.registry.Registries;
import me.steinsut.inventorydeclutter.common.network.ClientboundCarryItemPacket;
import me.steinsut.inventorydeclutter.common.network.ClientboundOpenScreenPacket;
import me.steinsut.inventorydeclutter.common.network.ServerboundOpenEntryPacket;
import me.steinsut.inventorydeclutter.common.registry.Entries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import org.slf4j.Logger;

public class CommonEventHandler implements IEventHandler {
    private final static Logger LOGGER = LogUtils.getLogger();
    private static final String REGISTRAR_VERSION = "1";

    @Override
    public void registerModBusHandlers(IEventBus eventBus) {
        eventBus.addListener(this::registerPackets);
        eventBus.addListener(this::registerRegistries);
        Entries.DEFER_ENTRIES.register(eventBus);
    }

    @Override
    public void registerFMLBusHandlers(IEventBus eventBus) {

    }

    private void registerRegistries(final NewRegistryEvent event) {
        LOGGER.info("Registering declutter registry...");
        event.register(Registries.DECLUTTER_ENTRIES);
    }

    private void registerPackets(final RegisterPayloadHandlersEvent event) {
        LOGGER.info("Registering mod packets...");
        PayloadRegistrar registrar = event.registrar(REGISTRAR_VERSION);
        registrar = registrar.executesOn(HandlerThread.MAIN);
        registrar.playToClient(
                ClientboundOpenScreenPacket.TYPE,
                ClientboundOpenScreenPacket.STREAM_CODEC,
                ClientboundOpenScreenPacket::handleOnClient
        );
        registrar.playToClient(
                ClientboundCarryItemPacket.TYPE,
                ClientboundCarryItemPacket.STREAM_CODEC,
                ClientboundCarryItemPacket::handleOnClient
        );
        registrar.playToServer(
                ServerboundOpenEntryPacket.TYPE,
                ServerboundOpenEntryPacket.STREAM_CODEC,
                ServerboundOpenEntryPacket::handleOnServer
        );
    }
}
