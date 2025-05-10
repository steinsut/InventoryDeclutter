package me.steinsut.inventorydeclutter.common.config;

import java.awt.*;

import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import me.steinsut.inventorydeclutter.client.gui.EntryPanelOrientation;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
@EventBusSubscriber(modid = InventoryDeclutterApi.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.EnumValue<EntryPanelOrientation> PANEL_ORIENTATION = BUILDER
            .comment("Panel orientation")
            .defineEnum("orientation", EntryPanelOrientation.HORIZONTAL);

    private static final ModConfigSpec.IntValue PANEL_X = BUILDER
            .comment("X coordinate of the entry panel on screen (-1 means center)")
            .defineInRange("panelX", -1, -1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_Y = BUILDER
            .comment("Y coordinate of the entry panel on screen (-1 means center)")
            .defineInRange("panelY", 0, -1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_MAX_MAJOR_AXIS = BUILDER
            .comment("Max length of the major axis of the entry panel on screen")
            .defineInRange("panelMajorAxis", 200, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_MINOR_AXIS = BUILDER
            .comment("Length of the minor axis of the entry panel on screen (excluding the scrollbar)")
            .defineInRange("panelMinorAxis", 51, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_ELEMENT_SIZE = BUILDER
            .comment("Size (as one side of a square) of one element that is on the entry panel")
            .defineInRange("panelElementSize", 26, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_PADDING = BUILDER
            .comment("Amount of padding between the sides of the panel and the actual contents")
            .defineInRange("panelPadding", 6, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_ELEMENT_PADDING = BUILDER
            .comment("Amount of space between the elements and the scrollbar")
            .defineInRange("panelElementPadding", 2, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_SCROLL_MARGIN = BUILDER
            .comment("Amount of padding between each element of the panel")
            .defineInRange("panelScrollMargin", 1, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_SCROLL_MAX_MAJOR_AXIS = BUILDER
            .comment("Max length of the major axis of the scrollbar (-1 means fill)")
            .defineInRange("panelScrollMajorAxis", -1, -1, Integer.MAX_VALUE);

    private static final ModConfigSpec.IntValue PANEL_SCROLL_MINOR_AXIS = BUILDER
            .comment("Length of the minor axis of the scrollbar")
            .defineInRange("panelScrollMinorAxis", 10, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static EntryPanelOrientation panelOrientation;
    public static Point panelCoords;
    public static int panelMajorAxis;
    public static int panelMinorAxis;
    public static int panelElementSize;
    public static int panelPadding;
    public static int panelElementPadding;
    public static int panelScrollMargin;
    public static int panelScrollMaxMajorAxis;
    public static int panelScrollMinorAxis;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        panelOrientation = PANEL_ORIENTATION.get();
        panelCoords = new Point(PANEL_X.getAsInt(), PANEL_Y.getAsInt());
        panelMajorAxis = PANEL_MAX_MAJOR_AXIS.getAsInt();
        panelMinorAxis = PANEL_MINOR_AXIS.getAsInt();
        panelElementSize = PANEL_ELEMENT_SIZE.getAsInt();
        panelPadding = PANEL_PADDING.getAsInt();
        panelElementPadding = PANEL_ELEMENT_PADDING.getAsInt();
        panelScrollMargin = PANEL_SCROLL_MARGIN.getAsInt();
        panelScrollMaxMajorAxis = PANEL_SCROLL_MAX_MAJOR_AXIS.getAsInt();
        panelScrollMinorAxis = PANEL_SCROLL_MINOR_AXIS.getAsInt();
    }
}
