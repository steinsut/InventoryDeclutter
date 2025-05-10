package me.steinsut.inventorydeclutter.common.event;

import net.neoforged.bus.api.IEventBus;

public interface IEventHandler {
    void registerModBusHandlers(IEventBus eventBus);
    void registerFMLBusHandlers(IEventBus eventBus);
}
