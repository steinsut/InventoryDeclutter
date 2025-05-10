package me.steinsut.inventorydeclutter.api.entry;

import net.minecraft.resources.ResourceLocation;

public sealed interface IDeclutterEntry permits AbstractCustomEntry, AbstractMenuEntry, AbstractScreenEntry {
    String getModId();
    EntryType getType();
    Class<?> getScreenClass();
    String getTranslationKey();
    ResourceLocation getIcon();
    boolean isEntryVisible();
}
