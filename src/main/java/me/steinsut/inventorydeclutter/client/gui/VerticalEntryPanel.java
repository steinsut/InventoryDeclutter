package me.steinsut.inventorydeclutter.client.gui;

import me.steinsut.inventorydeclutter.client.config.Config;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.ScreenPosition;


public class VerticalEntryPanel extends AbstractEntryPanel {
    public VerticalEntryPanel(int x, int y) {
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
        return this.getWidth();
    }

    @Override
    protected int getMajorAxisSize() {
        return this.getHeight();
    }

    @Override
    protected int getMinorAxisCoord() {
        return this.getX();
    }

    @Override
    protected int getMajorAxisCoord() {
        return this.getY();
    }

    @Override
    protected void setMinorAxisSize(int minorAxis) {
        this.setWidth(minorAxis);
    }

    @Override
    protected void setMajorAxisSize(int majorAxis) {
        this.setHeight(majorAxis);
    }

    @Override
    protected void setMinorAxisCoords(int minorAxis) {
        this.setX(minorAxis);
    }

    @Override
    protected void setMajorAxisCoords(int majorAxis) {
        this.setY(majorAxis);
    }

    @Override
    protected AbstractPanelSlider makeSlider() {
        int scrollHeight = Config.panelScrollMaxMajorAxis < 0
                ? this.getMaxMajorAxis()
                : Config.panelScrollMaxMajorAxis;

        return new VerticalPanelSlider(
                this.getX() + Config.panelPadding + this.getElementSize() + Config.panelScrollMargin,
                this.getY() + Config.panelPadding + (int) Math.ceil((this.getMaxMajorAxis() - scrollHeight) / 2.0),
                Config.panelScrollMinorAxis,
                scrollHeight,
                this.getMajorAxisSize(),
                this.getContentSize(),
                this::onScroll);
    }

    @Override
    protected void setEntryOffset(EntryWidget widget, int offset) {
        widget.setPosition(widget.getX(), this.getY() + Config.panelPadding - offset);
    }

    @Override
    protected ScreenPosition calculateNewEntryOffset() {
        return new ScreenPosition(0, this.getContentSize() + Config.panelElementPadding);
    }

    @Override
    protected ScreenPosition getRecenterOffset(ScreenPosition sizeChange) {
        return new ScreenPosition(0, (int) Math.ceil(sizeChange.y() / 2.0));
    }

    @Override
    protected void enableContentScissor(GuiGraphics graphics) {
        graphics.enableScissor(this.getX() + Config.panelPadding,
                this.getY() + Config.panelPadding + Config.panelElementPadding,
                this.getX() + Config.panelPadding + Config.panelElementSize,
                this.getY() + this.height - Config.panelPadding - Config.panelElementPadding);
    }
}
