package me.steinsut.inventorydeclutter.api.entry;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;

public abstract non-sealed class AbstractScreenEntry implements IDeclutterEntry {
    @Override
    public final EntryType getType() {
        return EntryType.SCREEN;
    }

    public abstract Screen createScreen(Player player);
}
