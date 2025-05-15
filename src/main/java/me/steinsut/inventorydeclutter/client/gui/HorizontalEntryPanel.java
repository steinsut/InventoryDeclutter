package me.steinsut.inventorydeclutter.client.gui;

import me.steinsut.inventorydeclutter.client.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;

public class HorizontalEntryPanel extends AbstractEntryPanel {
    public HorizontalEntryPanel(int x, int y) {
        super(
            x,
            y,
            Config.panelMinorAxis + Config.panelPadding,
            Config.panelMajorAxis,
            Config.panelElementSize
        );
    }

    @Override
    protected int getMinorAxisSize() {
        return this.getHeight();
    }

    @Override
    protected int getMajorAxisSize() {
        return this.getWidth();
    }

    @Override
    protected int getMinorAxisCoord() {
        return this.getY();
    }

    @Override
    protected int getMajorAxisCoord() {
        return this.getX();
    }

    @Override
    protected void setMinorAxisSize(int minorAxis) {
        this.setHeight(minorAxis);
    }

    @Override
    protected void setMajorAxisSize(int majorAxis) {
        this.setWidth(majorAxis);
    }

    @Override
    protected void setMinorAxisCoords(int minorAxis) {
        this.setY(minorAxis);
    }

    @Override
    protected void setMajorAxisCoords(int majorAxis) {
        this.setX(majorAxis);
    }

    @Override
    protected AbstractPanelSlider makeSlider() {
        int scrollWidth = Config.panelScrollMaxMajorAxis < 0
                ? this.getMaxMajorAxis()
                : Config.panelScrollMaxMajorAxis;

        return new HorizontalPanelSlider(
                this.getX() + Config.panelPadding + (int) Math.ceil((this.getMaxMajorAxis() - scrollWidth) / 2.0),
                this.getY() + Config.panelPadding + this.getElementSize() + Config.panelScrollMargin,
                scrollWidth,
                Config.panelScrollMinorAxis,
                this.getMaxMajorAxis(),
                this.getContentSize(),
                this::onScroll);
    }

    @Override
    protected void setEntryOffset(EntryWidget widget, int offset) {
        widget.setPosition(this.getX() + Config.panelPadding - offset, widget.getY());
    }

    @Override
    protected ScreenPosition calculateNewEntryOffset() {
        return new ScreenPosition(this.getContentSize() + Config.panelElementPadding, 0);
    }

    @Override
    protected ScreenPosition getRecenterOffset(ScreenPosition moveAmount) {
        return new ScreenPosition((int) Math.ceil(moveAmount.x() / 2.0), 0);
    }

    @Override
    protected void enableContentScissor(GuiGraphics graphics) {
        graphics.enableScissor(this.getX() + Config.panelPadding + Config.panelElementPadding,
                this.getY() + Config.panelPadding,
                this.getX() + this.width - Config.panelPadding - Config.panelElementPadding,
                this.getY() + Config.panelPadding + Config.panelElementSize);
    }
}
