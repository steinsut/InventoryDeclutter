package me.steinsut.inventorydeclutter.client.gui;

import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.neoforged.neoforge.client.gui.widget.ExtendedSlider;


public abstract class AbstractPanelSlider extends ExtendedSlider {
    public static final ResourceLocation HORIZONTAL_SCROLL = ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "panel_scroll_horizontal");
    public static final ResourceLocation VERTICAL_SCROLL = ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "panel_scroll_vertical");

    private static final double MOUSE_SCROLL_FACTOR = 0.15;
    private final Runnable action;
    private int containerSize;
    private int contentSize;
    private double factor;
    private int scrollSize;

    private double valueOnClick;
    private double clickPosition;

    public AbstractPanelSlider(int x, int y, int width, int height, int containerSize, int contentSize, Runnable onValueChanged) {
        super(x, y, width, height, Component.empty(), Component.empty(), 0, 1, 0, 0, 2, false);
        this.calculateScrollbar(containerSize, contentSize);
        this.action = onValueChanged;
    }

    public void calculateScrollbar(int containerSize, int contentSize) {
        this.containerSize = containerSize;
        this.contentSize = contentSize;
        this.factor = this.getMajorAxisSize() / (double) contentSize;
        this.scrollSize = (int) Math.ceil(Math.max(0, this.getMajorAxisSize() - (contentSize - this.getMajorAxisSize()) * this.factor));
    }

    public int getScrollbarMajorAxisCoord() {
        return this.getMajorAxisCoord() + (int) (this.value * (1 - this.factor) * (double) (this.getMajorAxisSize()));
    }

    public int getFactoredValue() {
        return (int) Math.ceil(Math.max(0,
                (this.contentSize - this.containerSize) * (this.value) // a
        ));
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        clickPosition = this.getMajorAxisMouse(mouseX, mouseY);
        if(clickPosition < this.getScrollbarMajorAxisCoord()) {
            this.setValue((clickPosition - this.getMajorAxisCoord()) / this.getMajorAxisSize() / (1 -  factor));
        }
        else if (clickPosition >= this.getScrollbarMajorAxisCoord() + this.getScrollbarSize()) {
            this.setValue((clickPosition - (this.getMajorAxisCoord() + this.getScrollbarSize())) / this.getMajorAxisSize() / (1 -  factor));
        }
        this.valueOnClick = this.value;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double dragX, double dragY) {
        this.setValue(this.valueOnClick + (this.getMajorAxisMouse(mouseX, mouseY) - clickPosition) / this.getMajorAxisSize() / (1 -  factor));
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        this.setValue(this.value + scrollY * MOUSE_SCROLL_FACTOR);
        return true;
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {

    }

    @Override
    public void setValue(double value) {
        double oldValue = this.value;
        super.setValue(value);
        if(!Mth.equal(this.value, oldValue)) {
            this.action.run();
        }
    }

    protected abstract int getMinorAxisSize();
    protected abstract int getMajorAxisSize();
    protected abstract int getMinorAxisCoord();
    protected abstract int getMajorAxisCoord();

    protected abstract void setMinorAxisSize(int size);
    protected abstract void setMajorAxisSize(int size);
    protected abstract void setMinorAxisCoord(int coord);
    protected abstract void setMajorAxisCoord(int coord);

    protected abstract double getMajorAxisMouse(double mouseX, double mouseY);

    public int getScrollbarSize() { return this.scrollSize; }

    public double getScrollFactor() { return this.factor; }

    protected abstract void renderScroll(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick);

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        //guiGraphics.blitSprite(RenderType::guiTextured, this.getSprite(), this.getX(), this.getY(), this.width, this.height, ARGB.color(255, 0xFF0000));
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 301, ARGB.white(0.3f));
        this.renderScroll(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable("inventorydeclutter.narration.scrollbar"));
    }
}
