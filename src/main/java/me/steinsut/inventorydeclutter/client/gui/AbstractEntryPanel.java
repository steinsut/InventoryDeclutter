package me.steinsut.inventorydeclutter.client.gui;

import me.steinsut.inventorydeclutter.api.entry.IDeclutterEntry;
import me.steinsut.inventorydeclutter.client.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.navigation.ScreenPosition;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ARGB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;


public abstract class AbstractEntryPanel extends AbstractWidget implements ContainerEventHandler {
    private final ScreenPosition centerPosition;
    private final int minorAxis;
    private final int maxMajorAxis;
    private final int elementSize;

    private final List<EntryWidget> entryWidgets;
    private final List<GuiEventListener> children;
    private List<IDeclutterEntry> entries;
    private AbstractWidget focused;

    private IDeclutterEntry currentEntry;

    private boolean dragging;

    private AbstractPanelSlider slider;

    public AbstractEntryPanel(int x, int y, int minorAxis, int maxMajorAxis, int elementSize) {
        super(x, y, 0, 0, Component.empty());

        this.centerPosition = new ScreenPosition(x, y);
        this.minorAxis = minorAxis;
        this.maxMajorAxis = maxMajorAxis;
        this.elementSize = elementSize;

        this.entries = new ArrayList<>();
        this.children = new ArrayList<>();
        this.entryWidgets = new ArrayList<>();

        this.setMinorAxisSize(minorAxis);
        this.setMajorAxisSize(2 * Config.panelPadding);
        this.setMajorAxisCoords(this.getMajorAxisCoord() - Config.panelPadding / 2);

        this.refresh();
    }

    public void add(IDeclutterEntry entry) {
        this.entries.add(entry);

        if(!entry.isEntryVisible()) return;

        int oldHeight = this.height;
        int oldWidth = this.width ;

        ScreenPosition entryOffset = this.calculateNewEntryOffset();
        EntryWidget w = new EntryWidget(entry, this.getX() + Config.panelPadding + entryOffset.x(),
                this.getY() + Config.panelPadding + entryOffset.y(), this.elementSize);
        w.setCurrent(entry == this.currentEntry);
        this.entryWidgets.add(w);
        this.children.add(this.entryWidgets.getLast());

        if(getContentSize() > this.maxMajorAxis - 2 * Config.panelPadding) {
            if(this.slider == null) {
                this.slider = this.makeSlider();
                this.children.add(this.slider);
                this.setMinorAxisSize(this.getMinorAxisSize() + this.slider.getMinorAxisSize() + Config.panelScrollMargin);
            }
            this.setMajorAxisSize(2 * Config.panelPadding + this.maxMajorAxis);
            this.slider.calculateScrollbar(this.maxMajorAxis, this.getContentSize());
        }
        else {
            if(this.slider != null) {
                this.children.remove(slider);
                this.slider = null;
            }
            this.setMajorAxisSize(2 * Config.panelPadding + this.getContentSize());
        }
        ScreenPosition centerOffset = this.getRecenterOffset(
                new ScreenPosition(this.width - oldWidth, this.height - oldHeight)
        );

        if(centerOffset.x() != 0 || centerOffset.y() != 0) {
            this.setPosition(this.getX() - centerOffset.x(), this.getY() - centerOffset.y());
            for(EntryWidget widget : this.entryWidgets) {
                widget.setPosition(widget.getX() - centerOffset.x(), widget.getY() - centerOffset.y());
            }
            if(this.slider != null) {
                this.slider.setPosition(this.slider.getX() - centerOffset.x(), this.slider.getY() - centerOffset.y());
            }
        }
    }

    public void refresh() {
        this.setX(centerPosition.x());
        this.setY(centerPosition.y());

        this.setMinorAxisSize(minorAxis);
        this.setMajorAxisSize(2 * Config.panelPadding);
        this.setMajorAxisCoords(this.getMajorAxisCoord() - Config.panelPadding / 2);

        this.slider = null;

        this.entryWidgets.clear();
        this.children.clear();

        List<IDeclutterEntry> temp = this.entries;
        this.entries = new ArrayList<>();

        for(IDeclutterEntry entry : temp) {
            this.add(entry);
        }
    }

    protected void onScroll() {
        int offset = this.slider.getFactoredValue();
        for (EntryWidget entry : this.entryWidgets) {
            offset -= Config.panelElementPadding;
            this.setEntryOffset(entry, offset);
            offset -= Config.panelElementPadding + this.elementSize;
        }
    }

    public void setCurrent(IDeclutterEntry entry) {
        this.currentEntry = entry;
        for(EntryWidget widget : this.entryWidgets) {
            widget.setCurrent(widget.getEntry() == entry);
        }
    }

    public int getContentSize() {
        return this.entryWidgets.isEmpty()
                ? 0
                : this.elementSize * entryWidgets.size() + Config.panelElementPadding * (entryWidgets.size() * 2);
    }

    public int getMaxMajorAxis() {
        return this.maxMajorAxis;
    }

    public int getMinorAxis() {
        return this.minorAxis;
    }

    public int getElementSize() {
        return this.elementSize;
    }

    protected abstract int getMinorAxisSize();
    protected abstract int getMajorAxisSize();
    protected abstract int getMinorAxisCoord();
    protected abstract int getMajorAxisCoord();

    protected abstract void setMinorAxisSize(int minorAxis);
    protected abstract void setMajorAxisSize(int majorAxis);
    protected abstract void setMinorAxisCoords(int minorAxis);
    protected abstract void setMajorAxisCoords(int majorAxis);

    protected abstract AbstractPanelSlider makeSlider();
    protected abstract void setEntryOffset(EntryWidget widget, int dragAmount);
    protected abstract ScreenPosition calculateNewEntryOffset();
    protected abstract ScreenPosition getRecenterOffset(ScreenPosition moveAmount);

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.focused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener guiEventListener) {
        this.focused = (AbstractWidget) guiEventListener;
    }

    @Override
    @NotNull
    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double dragX, double dragY) {
        return ContainerEventHandler.super.mouseScrolled(mouseX, mouseY, dragX, dragY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return ContainerEventHandler.super.mouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return ContainerEventHandler.super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        ContainerEventHandler.super.mouseMoved(mouseX, mouseY);
    }

    protected abstract void enableContentScissor(GuiGraphics graphics);

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 299, ARGB.color(120, 0));

        this.enableContentScissor(guiGraphics);

        guiGraphics.fill(this.getX(),
                this.getY(),
                this.getX() + this.width,
                this.getY() + this.height,
                300, ARGB.white(0.1f));

        for(EntryWidget widget : this.entryWidgets) {
            widget.render(guiGraphics, i, i1, v);
        }

        guiGraphics.disableScissor();

        if(this.slider != null) {
            this.slider.render(guiGraphics, i, i1, v);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.USAGE, Component.translatable("invenorydeclutter.narration.panel_narration"));
    }
}
