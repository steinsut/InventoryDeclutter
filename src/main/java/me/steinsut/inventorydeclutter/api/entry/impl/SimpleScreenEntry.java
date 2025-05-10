package me.steinsut.inventorydeclutter.api.entry.impl;

import me.steinsut.inventorydeclutter.api.entry.AbstractScreenEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

import java.util.function.Function;

public class SimpleScreenEntry extends AbstractScreenEntry {
    private final String modId;
    private final Function<Player, Screen> screenSupplier;
    private final Class<?> guiClass;
    private final String translationKey;
    private final ResourceLocation icon;

    public SimpleScreenEntry(Function<Player, Screen> screenSupplier, Class<?> guiClass, String modId, String translationKey, ResourceLocation icon) {
        this.screenSupplier = screenSupplier;
        this.modId = modId;
        this.guiClass = guiClass;
        this.translationKey = translationKey;
        this.icon = icon;
    }

    @Override
    public Screen createScreen(Player player) {
        return screenSupplier.apply(player);
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