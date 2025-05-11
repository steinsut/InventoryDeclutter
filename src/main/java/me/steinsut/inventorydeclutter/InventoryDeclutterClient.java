package me.steinsut.inventorydeclutter;

import com.mojang.logging.LogUtils;
import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import me.steinsut.inventorydeclutter.client.event.ClientEventHandler;
import me.steinsut.inventorydeclutter.common.config.Config;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;

@Mod(value = InventoryDeclutterApi.MOD_ID, dist = Dist.CLIENT)
public class InventoryDeclutterClient {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final ClientEventHandler clientEventHandler;

    public InventoryDeclutterClient(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Hello from InventoryDeclutter Client.");

        clientEventHandler = new ClientEventHandler();

        clientEventHandler.registerModBusHandlers(modEventBus);
        clientEventHandler.registerFMLBusHandlers(NeoForge.EVENT_BUS);

        modContainer.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
