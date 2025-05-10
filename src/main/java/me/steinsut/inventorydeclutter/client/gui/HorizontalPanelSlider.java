package me.steinsut.inventorydeclutter.client.gui;

import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.metadata.gui.GuiSpriteScaling;
import net.minecraft.util.Mth;

public class HorizontalPanelSlider extends AbstractPanelSlider {
    public HorizontalPanelSlider(int x, int y, int width, int height, int containerSize, int contentSize, Runnable action) {
        super(x, y, width, height, containerSize, contentSize, action);
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
    protected void setMinorAxisSize(int size) { this.setHeight(size); }

    @Override
    protected void setMajorAxisSize(int size) { this.setWidth(size); }

    @Override
    protected void setMinorAxisCoord(int coord) { this.setY(coord); }

    @Override
    protected void setMajorAxisCoord(int coord) { this.setX(coord); }

    @Override
    protected double getMajorAxisMouse(double mouseX, double mouseY) { return mouseX; }

    @Override
    protected void renderScroll(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();

        TextureAtlasSprite sprite = mc.getGuiSprites().getSprite(HORIZONTAL_SCROLL);
        GuiSpriteScaling scaling = mc.getGuiSprites().getSpriteScaling(sprite);
        if(!(scaling instanceof GuiSpriteScaling.NineSlice scaling$nine)) {
            LogUtils.getLogger().info("Scroll texture is not nine sliced");
            return;
        }

        float scaledY = (float) (this.getY() * scaling$nine.height()) / this.height;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(1, (float) this.height / scaling$nine.height(), 1);
        guiGraphics.pose().translate(0, scaledY  - Mth.ceil(scaledY), 301);
        guiGraphics.blitSprite(
                RenderType::guiTextured,
                HORIZONTAL_SCROLL,
                this.getX() + (int) (this.value * (1 - this.getScrollFactor()) * (double) (this.width)),
                Mth.ceil((float) this.getY() * scaling$nine.height() / this.height),
                this.getScrollbarSize(),
                scaling$nine.height());
        guiGraphics.pose().popPose();
    }
}
