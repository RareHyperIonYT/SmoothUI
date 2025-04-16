package me.rarehyperion.smoothui.mixin;

import me.rarehyperion.smoothui.utility.ClientUtility;
import me.rarehyperion.smoothui.utility.GuiAnimations;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public class HandledScreenMixin {

    @Inject(method = "renderBackground", at = @At("HEAD"))
    private void onRenderBackground(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0, -GuiAnimations.getOffsetY(), 0.0);
    }

    @Inject(method = "renderBackground", at = @At(value="INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;renderInGameBackground(Lnet/minecraft/client/gui/DrawContext;)V", shift = At.Shift.AFTER))
    private void afterRenderInGameBackground(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0, GuiAnimations.getOffsetY(), 0.0);
    }

    @Inject(method = "renderBackground", at = @At("TAIL"))
    private void onRenderBackgroundEnd(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;
        context.getMatrices().translate(0.0, GuiAnimations.getOffsetY(), 0.0);
    }

}
