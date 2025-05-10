package me.steinsut.inventorydeclutter.api.registry;

import me.steinsut.inventorydeclutter.api.entry.EntryType;
import me.steinsut.inventorydeclutter.env.BuildConfig;
import me.steinsut.inventorydeclutter.env.IBuildConfig;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import me.steinsut.inventorydeclutter.api.entry.IDeclutterEntry;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.neoforge.registries.RegistryBuilder;
import net.neoforged.neoforge.registries.callback.AddCallback;
import net.neoforged.neoforgespi.Environment;

public class Registries {
    private static void addCallback(Registry<IDeclutterEntry> registry,
                                    int id,
                                    ResourceKey<IDeclutterEntry> key,
                                    IDeclutterEntry value) {
        if(Environment.get().getDist() == Dist.DEDICATED_SERVER ||
                BuildConfig.instance.getTargetDist() == IBuildConfig.TargetDistType.FULL) return;

        if(value.getType() != EntryType.CUSTOM) {
            throw new IllegalStateException("Invalid entry type: " + value.getType().name() + " (not allowed in client build)");
        }
    }

    private static final ResourceLocation DECLUTTER_ENTRY_REGISTRY_LOCATION =
            ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "declutter_entry");
    private static final ResourceKey<net.minecraft.core.Registry<IDeclutterEntry>> DECLUTTER_ENTRY_REGISTRY_KEY =
            ResourceKey.createRegistryKey(DECLUTTER_ENTRY_REGISTRY_LOCATION);
    public static final Registry<IDeclutterEntry> DECLUTTER_ENTRIES = new RegistryBuilder<>(DECLUTTER_ENTRY_REGISTRY_KEY)
            .create();

    static {
        DECLUTTER_ENTRIES.addCallback((AddCallback<IDeclutterEntry>) Registries::addCallback);
    }
}
