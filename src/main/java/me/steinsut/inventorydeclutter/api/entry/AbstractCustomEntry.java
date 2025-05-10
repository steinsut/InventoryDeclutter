package me.steinsut.inventorydeclutter.api.entry;

public abstract non-sealed class AbstractCustomEntry implements IDeclutterEntry {
    @Override
    public final EntryType getType() {
        return EntryType.CUSTOM;
    }

    public abstract void onClick();
}
