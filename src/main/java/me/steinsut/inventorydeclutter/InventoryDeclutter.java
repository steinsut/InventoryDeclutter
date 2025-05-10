package me.steinsut.inventorydeclutter;

import me.steinsut.inventorydeclutter.common.event.CommonEventHandler;
import me.steinsut.inventorydeclutter.env.BuildConfig;
import me.steinsut.inventorydeclutter.env.IBuildConfig;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforgespi.Environment;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(InventoryDeclutterApi.MOD_ID)
public class InventoryDeclutter {
    private static final Logger LOGGER = LogUtils.getLogger();

    private final CommonEventHandler commonEventHandler;

    public InventoryDeclutter(IEventBus modEventBus, ModContainer modContainer) {
        LOGGER.info("Hello from InventoryDeclutter.");

        if(BuildConfig.instance.getTargetDist() == IBuildConfig.TargetDistType.CLIENT_ONLY &&
                Environment.get().getDist() == Dist.DEDICATED_SERVER) {
            throw new IllegalStateException("Trying to run client only build on the server!");
        }

        commonEventHandler = new CommonEventHandler();

        commonEventHandler.registerModBusHandlers(modEventBus);
        commonEventHandler.registerFMLBusHandlers(NeoForge.EVENT_BUS);
    }
}

