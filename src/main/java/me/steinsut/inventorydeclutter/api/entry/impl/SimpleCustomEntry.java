package me.steinsut.inventorydeclutter.api.entry.impl;

import me.steinsut.inventorydeclutter.api.entry.AbstractCustomEntry;
import net.minecraft.resources.ResourceLocation;

public class SimpleCustomEntry extends AbstractCustomEntry {
    private final String modId;
    private final Runnable runnable;
    private final Class<?> guiClass;
    private final String translationKey;
    private final ResourceLocation icon;

    public SimpleCustomEntry(Runnable runnable, String modId, Class<?> guiClass, String translationKey, ResourceLocation icon) {
        this.runnable = runnable;
        this.modId = modId;
        this.guiClass = guiClass;
        this.translationKey = translationKey;
        this.icon = icon;
    }

    public void onClick() {
        runnable.run();
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
