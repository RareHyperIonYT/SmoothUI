package me.rarehyperion.smoothui.mixin;

import me.rarehyperion.smoothui.utility.ClientUtility;
import me.rarehyperion.smoothui.utility.GuiAnimations;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TexturedButtonWidget.class)
public class TexturedButtonMixin {

    @Final @Shadow protected ButtonTextures textures;

    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void onRender(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;

        if (this.textures == RecipeBookWidget.BUTTON_TEXTURES) {
            context.getMatrices().translate(0.0, -GuiAnimations.getOffsetY(), 0.0);
        }
    }

    @Inject(method = "renderWidget", at = @At("TAIL"))
    private void onRenderEnd(final DrawContext context, final int mouseX, final int mouseY, final float delta, final CallbackInfo callback) {
        if (ClientUtility.isNotInGame()) return;

        if (this.textures == RecipeBookWidget.BUTTON_TEXTURES) {
            context.getMatrices().translate(0.0, GuiAnimations.getOffsetY(), 0.0);
        }
    }

}
