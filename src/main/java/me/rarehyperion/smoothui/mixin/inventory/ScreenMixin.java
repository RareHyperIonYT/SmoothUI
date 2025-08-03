package me.rarehyperion.smoothui.mixin.inventory;

import me.rarehyperion.smoothui.utility.ClientUtility;
import me.rarehyperion.smoothui.utility.GuiAnimations;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public class ScreenMixin {

    @Inject(method = "render", at = @At("HEAD"))
    private void onRender(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0F, GuiAnimations.getOffsetY());
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void onRenderEnd(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0F, -GuiAnimations.getOffsetY());
    }

    @Inject(method = "renderBackground", at = @At("HEAD"))
    private void onRenderBackground(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0F, -GuiAnimations.getOffsetY());
    }

    @Inject(method = "renderBackground", at = @At("TAIL"))
    private void onRenderBackgroundEnd(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0F, GuiAnimations.getOffsetY());
    }

    @Inject(method = "renderWithTooltip", at = @At("HEAD"))
    private void onRenderWithTooltip(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0F, GuiAnimations.getOffsetY());
    }

    @Inject(method = "renderWithTooltip", at = @At("TAIL"))
    private void onRenderWithTooltipEnd(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0F, -GuiAnimations.getOffsetY());
    }

    @Inject(method = "onDisplayed", at = @At("HEAD"))
    private void onDisplayed(final CallbackInfo callback) {
        GuiAnimations.lastGuiOpenedTime = System.currentTimeMillis();
    }

}