package me.steinsut.inventorydeclutter.api.entry;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public abstract non-sealed class AbstractMenuEntry implements IDeclutterEntry {
    @Override
    public final EntryType getType() {
        return EntryType.MENU;
    }

    public abstract AbstractContainerMenu createMenu(int menuId, Inventory inventory, Player player);
}
