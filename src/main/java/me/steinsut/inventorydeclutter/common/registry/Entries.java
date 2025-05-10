package me.steinsut.inventorydeclutter.common.registry;

import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import me.steinsut.inventorydeclutter.api.entry.IDeclutterEntry;
import me.steinsut.inventorydeclutter.api.entry.impl.SimpleCustomEntry;
import me.steinsut.inventorydeclutter.api.registry.Registries;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class Entries {
    public static final DeferredRegister<IDeclutterEntry> DEFER_ENTRIES = DeferredRegister.create(
            Registries.DECLUTTER_ENTRIES,
            InventoryDeclutterApi.MOD_ID
    );

    public static final Supplier<IDeclutterEntry> PLAYER_INVENTORY_ENTRY = DEFER_ENTRIES.register(
            "player_inventory",
            () -> new SimpleCustomEntry(() -> {
                Minecraft mc = Minecraft.getInstance();
                if(mc.level == null || mc.player == null || mc.gameMode == null) return;

                if (mc.gameMode.isServerControlledInventory()) {
                    mc.player.sendOpenInventory();
                } else {
                    mc.getTutorial().onOpenInventory();
                    mc.setScreen(new InventoryScreen(mc.player));
                }
            }, "minecraft",
            InventoryScreen.class,
"inventorydeclutter.screen.inventory",
            ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "inventory_button"))
    );
}
