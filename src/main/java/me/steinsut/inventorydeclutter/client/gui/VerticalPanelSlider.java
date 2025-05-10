package me.steinsut.inventorydeclutter.client.gui;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.util.Mth;


public class VerticalPanelSlider extends AbstractPanelSlider {
    public VerticalPanelSlider(int x, int y, int width, int height, int containerSize, int contentSize, Runnable action) {
        super(x, y, width, height, containerSize, contentSize, action);
    }

    @Override
    protected int getMinorAxisSize() { return this.getWidth(); }

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
    protected void setMinorAxisSize(int size) { this.setWidth(size); }

    @Override
    protected void setMajorAxisSize(int size) { this.setHeight(size); }

    @Override
    protected void setMinorAxisCoord(int coord) { this.setX(coord); }

    @Override
    protected void setMajorAxisCoord(int coord) { this.setY(coord); }

    @Override
    protected double getMajorAxisMouse(double mouseX, double mouseY) { return mouseY; }

    @Override
    protected void renderScroll(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        TextureAtlasSprite sprite = mc.getGuiSprites().getSprite(VERTICAL_SCROLL);
        GuiSpriteScaling scaling = mc.getGuiSprites().getSpriteScaling(sprite);
        if(!(scaling instanceof GuiSpriteScaling.NineSlice scaling$nine)) {
            LogUtils.getLogger().info("Scroll texture is not nine sliced");
            return;
        }

        float scaledX = (float) (this.getX() * scaling$nine.width()) / this.width;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale((float) this.width / scaling$nine.width(), 1, 1);
        guiGraphics.pose().translate(scaledX  - Mth.ceil(scaledX), 0, 301);
        guiGraphics.blitSprite(
                RenderType::guiTextured,
                VERTICAL_SCROLL,
                Mth.ceil(scaledX),
                this.getY() + (int) (this.value * (1 - this.getScrollFactor()) * (double) (this.height)),
                scaling$nine.width(),
                this.getScrollbarSize());
        guiGraphics.pose().popPose();
    }
}
