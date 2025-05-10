package me.steinsut.inventorydeclutter.client.gui;

import com.mojang.logging.LogUtils;
import me.steinsut.inventorydeclutter.api.InventoryDeclutterApi;
import me.steinsut.inventorydeclutter.api.entry.AbstractCustomEntry;
import me.steinsut.inventorydeclutter.api.entry.EntryType;
import me.steinsut.inventorydeclutter.api.entry.IDeclutterEntry;
import me.steinsut.inventorydeclutter.api.registry.Registries;
import me.steinsut.inventorydeclutter.common.network.ServerboundOpenEntryPacket;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModList;

import net.neoforged.neoforge.network.PacketDistributor;
import org.slf4j.Logger;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.Optional;


public class EntryWidget extends AbstractWidget {
    private static final ResourceLocation BUTTON = ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "panel_button");
    private static final ResourceLocation HOVERED_BUTTON = ResourceLocation.fromNamespaceAndPath(InventoryDeclutterApi.MOD_ID, "panel_button_hover");

    private static final Logger LOGGER = LogUtils.getLogger();

    private boolean isCurrent;

    private final IDeclutterEntry entry;

    public EntryWidget(IDeclutterEntry entry, int x, int y, int size) {
        super(x, y, size, size, Component.empty());
        this.isCurrent = false;
        this.entry = entry;

        String modName = "UNKNOWN";
        Optional<? extends ModContainer> modContainer = ModList.get().getModContainerById(entry.getModId());
        if(modContainer.isPresent()) {
           modName = modContainer.get().getModInfo().getDisplayName();
        }

        MutableComponent tooltipComponent = Component
                .translatable(entry.getTranslationKey())
                .append(Component.literal("\n"))
                .append(Component.literal(modName).setStyle(
                        Style.EMPTY.withColor(0x5454FC)
                                .withShadowColor(0x15153E)
                                .withItalic(true)
                )
        );

        if(Minecraft.getInstance().options.advancedItemTooltips) {
            tooltipComponent
                    .append(Component.literal("\n"))
                    .append(Component.literal(Objects.requireNonNull(Registries.DECLUTTER_ENTRIES.getKey(entry)).toString()).withStyle(ChatFormatting.DARK_GRAY)
            );
        }

        this.setTooltip(
            Tooltip.create(tooltipComponent, Component.translatable(entry.getTranslationKey()))
        );
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int i, int i1, float v) {
        guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 301, 0xFFFFFFFF);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0, 0, 301);

        guiGraphics.blitSprite(
                RenderType::guiTextured,
                !this.isCurrent && this.isHoveredOrFocused() ? HOVERED_BUTTON : BUTTON,
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight());
        guiGraphics.blitSprite(
                RenderType::guiTextured,
                entry.getIcon(),
                this.getX(),
                this.getY(),
                this.getWidth(),
                this.getHeight());
        guiGraphics.pose().popPose();
        if(this.isCurrent) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.getWidth(), this.getY() + this.getHeight(), 301, ARGB.color(120, 0x000000));
        }
    }

    public IDeclutterEntry getEntry() {
        return this.entry;
    }

    public void setCurrent(boolean isCurrent) {
        this.isCurrent = isCurrent;
    }

    @Override
    public void onClick(double mouseX, double mouseY, int button) {
        if(this.isCurrent) return;

        Minecraft mc = Minecraft.getInstance();

        if(mc.player == null) {
            LOGGER.info("player is null (are you in a level?)");
            return;
        }

        if (entry.getType() == EntryType.CUSTOM) {
            ((AbstractCustomEntry) entry).onClick();
        } else {
            ItemStack carried = mc.player.containerMenu.getCarried();
            mc.player.containerMenu.setCarried(ItemStack.EMPTY);
            PacketDistributor.sendToServer(new ServerboundOpenEntryPacket(Registries.DECLUTTER_ENTRIES.getKey(entry), carried));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void playDownSound(SoundManager handler) {
        if(!this.isCurrent) {
            super.playDownSound(handler);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(NarratedElementType.TITLE, Component.translatable(entry.getTranslationKey()));
    }
}
