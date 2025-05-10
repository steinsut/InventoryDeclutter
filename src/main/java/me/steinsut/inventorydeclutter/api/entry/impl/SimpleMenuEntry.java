package me.steinsut.inventorydeclutter.api.entry.impl;

import me.steinsut.inventorydeclutter.api.entry.AbstractMenuEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

import org.apache.commons.lang3.function.TriFunction;

public class SimpleMenuEntry extends AbstractMenuEntry {
    private final String modId;
    private final TriFunction<Integer, Inventory, Player, AbstractContainerMenu> menuSupplier;
    private final Class<?> guiClass;
    private final String translationKey;
    private final ResourceLocation icon;

    public SimpleMenuEntry(TriFunction<Integer, Inventory, Player, AbstractContainerMenu> menuSupplier, String modId, Class<?> guiClass, String translationKey, ResourceLocation icon) {
        this.menuSupplier = menuSupplier;
        this.modId = modId;
        this.guiClass = guiClass;
        this.translationKey = translationKey;
        this.icon = icon;
    }

    @Override
    public AbstractContainerMenu createMenu(int menuId, Inventory inventory, Player player) {
        return menuSupplier.apply(menuId, inventory, player);
    }

    @Override
    public String getModId() {
        return modId;
    }

    @Override
    public Class<?> getScreenClass() {
        return guiClass;
    }

    @Override
    public String getTranslationKey() {
        return translationKey;
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    @Override
    public boolean isEntryVisible() {
        return true;
    }
}
