package me.rarehyperion.smoothui.mixin;

import me.rarehyperion.smoothui.utility.EasingFunctions;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntryListWidget.class)
public abstract class ScrollableWidgetMixin {

    @Unique private static final double SCROLL_SPEED = 0.5D;
    @Unique private static final double DURATION = 1.0D;

    @Unique private double animationTimer = 0;
    @Unique private double scrollStartVelocity = 0;
    @Unique private boolean renderSmooth = false;

    @Shadow public abstract int getMaxScroll();
    @Shadow private double scrollAmount;

    @Inject(method = "renderWidget", at = @At("HEAD"))
    private void manipulateScrollAmount(final DrawContext context, final int mouseX, final int mouseY, final float deltaTime, final CallbackInfo callback) {
        this.renderSmooth = true;

        this.correctScrollBounds(deltaTime);
        if (Math.abs(this.computeVelocity(this.animationTimer, this.scrollStartVelocity)) < 1.0D) return;
        this.updateScrollAmount(deltaTime);
    }

    @Redirect(method = "mouseScrolled", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/EntryListWidget;setScrollAmount(D)V"))
    private void setVelocity(EntryListWidget<?> instance, double amount) {
        if(!this.renderSmooth) {
            instance.setScrollAmount(amount);
            return;
        }

        double difference = amount - this.scrollAmount;
        difference = Math.signum(difference) * Math.min(Math.abs(difference), 10);
        difference *= SCROLL_SPEED;

        if(Math.signum(difference) != Math.signum(this.scrollStartVelocity)) difference *= 2.5D;

        this.animationTimer *= 0.5;
        this.scrollStartVelocity = this.computeVelocity(this.animationTimer, this.scrollStartVelocity) + difference;
        this.animationTimer = 0;
    }

    @Redirect(method = "renderWidget", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private void modifyScrollbar(final DrawContext context, final Identifier sprite, int x, int y, int width, int height) {
        if(this.scrollAmount < 0) {
            height -= EasingFunctions.dampenSquish(Math.abs(this.scrollAmount), height);
        }

        int bottom = ((EntryListWidget<?>) (Object) this).getBottom();
        if(y + height > bottom) y = bottom - height;

        if(this.scrollAmount > getMaxScroll()) {
            int overshootSquish = EasingFunctions.dampenSquish(this.scrollAmount - getMaxScroll(), height);
            y += overshootSquish;
            height -= overshootSquish;
        }

        context.drawGuiTexture(sprite, x, y, width, height);
    }

    @Unique
    private double computeVelocity(final double timeElapsed, final double speed) {
        return Math.pow(1 - EasingFunctions.SCROLLBAR_DRAG, timeElapsed) * speed;
    }

    @Unique
    private void updateScrollAmount(float delta) {
        this.scrollAmount += this.computeVelocity(this.animationTimer, this.scrollStartVelocity) * delta;
        this.animationTimer += delta * 10 / DURATION;
    }

    @Unique
    private void correctScrollBounds(float delta) {
        if(this.scrollAmount < 0) {
            this.scrollAmount += EasingFunctions.pushBackStrength(Math.abs(this.scrollAmount), delta);
            if(this.scrollAmount > -0.2D) this.scrollAmount = 0;
        }else if(this.scrollAmount > getMaxScroll()) {
            this.scrollAmount -= EasingFunctions.pushBackStrength(this.scrollAmount - getMaxScroll(), delta);
            if(this.scrollAmount < getMaxScroll() + 0.2D) this.scrollAmount = getMaxScroll();
        }
    }

}
