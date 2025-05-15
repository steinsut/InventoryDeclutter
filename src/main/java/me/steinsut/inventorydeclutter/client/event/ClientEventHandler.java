package me.steinsut.inventorydeclutter.client.event;

import com.mojang.realmsclient.gui.screens.RealmsNotificationsScreen;
import me.steinsut.inventorydeclutter.api.event.DeclutterEntriesChanged;
import me.steinsut.inventorydeclutter.api.registry.Registries;
import me.steinsut.inventorydeclutter.client.gui.AbstractEntryPanel;
import me.steinsut.inventorydeclutter.client.gui.EntryPanelOrientation;
import me.steinsut.inventorydeclutter.client.gui.HorizontalEntryPanel;
import me.steinsut.inventorydeclutter.client.gui.VerticalEntryPanel;
import me.steinsut.inventorydeclutter.client.config.Config;
import me.steinsut.inventorydeclutter.common.event.IEventHandler;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.client.gui.screens.GenericMessageScreen;
import net.minecraft.client.gui.screens.GenericWaitingScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.client.event.ScreenEvent;

public class ClientEventHandler implements IEventHandler {
    private AbstractEntryPanel entryPanel;

    @Override
    public void registerModBusHandlers(IEventBus eventBus) {

    }

    @Override
    public void registerFMLBusHandlers(IEventBus eventBus) {
        eventBus.addListener(this::onPreScreenInit);
        eventBus.addListener(this::onPreMouseDragged);
        eventBus.addListener(this::onPreMouseScrolled);
        eventBus.addListener(this::onDeclutterEntriesChanged);
    }

    private void onPreScreenInit(ScreenEvent.Init.Post event) {
        entryPanel = null;
        Screen screen = event.getScreen();
        Class<?> screenClass = screen.getClass();
        if(screenClass == TitleScreen.class ||
            screenClass == RealmsNotificationsScreen.class ||
            screenClass == GenericMessageScreen.class ||
            screenClass == GenericWaitingScreen.class) return;

        if(Registries.DECLUTTER_ENTRIES.size() == 0) return;

        Registries.DECLUTTER_ENTRIES.stream()
                .filter((entry) -> entry.getScreenClass() == screenClass)
                .findFirst()
                .ifPresent(entry -> {
                    ScreenPosition pos = new ScreenPosition(
                            Config.panelCoords.x < 0 ? event.getScreen().getRectangle().getCenterInAxis(ScreenAxis.HORIZONTAL): Config.panelCoords.x,
                            Config.panelCoords.y < 0 ? event.getScreen().getRectangle().getCenterInAxis(ScreenAxis.VERTICAL): Config.panelCoords.y
                    );
                    this.entryPanel = Config.panelOrientation == EntryPanelOrientation.HORIZONTAL
                            ? new HorizontalEntryPanel(pos.x(),
                                                        pos.y())
                            : new VerticalEntryPanel(pos.x(),
                                                        pos.y());

                    this.entryPanel.setCurrent(entry);
                    Registries.DECLUTTER_ENTRIES.forEach((e) -> this.entryPanel.add(e));
                    event.addListener(this.entryPanel);
        });
    }

    private void onPreMouseDragged(ScreenEvent.MouseDragged.Pre event) {
        if(this.entryPanel != null) {
            if(this.entryPanel.isDragging()) {
                event.setCanceled(this.entryPanel.mouseDragged(
                    event.getMouseX(),
                    event.getMouseY(),
                    event.getMouseButton(),
                    event.getDragX(),
                    event.getDragY()
                ));
            }
        }
    }

    private void onPreMouseScrolled(ScreenEvent.MouseScrolled.Pre event) {
        if(this.entryPanel != null) {
            event.setCanceled(this.entryPanel.mouseScrolled(
                    event.getMouseX(),
                    event.getMouseY(),
                    event.getScrollDeltaX(),
                    event.getScrollDeltaY()
                ));
        }
    }

    private void onDeclutterEntriesChanged(DeclutterEntriesChanged event) {
        if(this.entryPanel != null) {
            this.entryPanel.refresh();
        }
    }
}
