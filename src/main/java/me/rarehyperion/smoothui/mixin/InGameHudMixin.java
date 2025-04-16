package me.rarehyperion.smoothui.mixin;

import me.rarehyperion.smoothui.utility.EasingFunctions;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Unique private float currentX = -1;
    @Unique private long lastTickTime = 0;

    @Inject(method = "renderHotbar", at = @At("HEAD"))
    private void onRenderHotbar(final DrawContext context, final RenderTickCounter counter, final CallbackInfo callback) {
        final PlayerEntity player = this.getCameraPlayer();

        if (player != null) {
            final float targetX = player.getInventory().selectedSlot * 20;
            if(this.currentX == -1) this.currentX = targetX;

            final long currentTime = System.currentTimeMillis();
            final float deltaTime = (currentTime - this.lastTickTime) / 1000.0F;
            this.lastTickTime = currentTime;

            if (Math.abs(targetX - this.currentX) > 0.1F) {
                this.currentX = EasingFunctions.easeLinearDamped(this.currentX, targetX, deltaTime, 25.0F);
            }
        }
    }

    @ModifyArgs(method = "renderHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Lnet/minecraft/util/Identifier;IIII)V", ordinal = 1))
    private void mod(Args args) {
        int baseX = (MinecraftClient.getInstance().getWindow().getScaledWidth() / 2) - 92;
        args.set(1, Math.round(baseX + this.currentX));
    }

    @Shadow @Nullable protected abstract PlayerEntity getCameraPlayer();

}
