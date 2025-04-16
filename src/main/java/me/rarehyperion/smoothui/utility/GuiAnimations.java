package me.rarehyperion.smoothui.utility;

import net.minecraft.client.MinecraftClient;

public final class GuiAnimations {

    public static long lastGuiOpenedTime = 0;

    private static final float FADE_DURATION = 220.0F;
    private static final float MAX_OFFSET = 9.0F;

    public static float getOffsetY() {
        final MinecraftClient client = MinecraftClient.getInstance();

        final float screenScale = (float) client.getWindow().getHeight() / 1080;

        final float timeElapsed = System.currentTimeMillis() - GuiAnimations.lastGuiOpenedTime;
        final float progress = 1.0F - Math.min(timeElapsed / FADE_DURATION, 1.0F);
        final float eased = EasingFunctions.easeInQuint(progress);

        return eased * MAX_OFFSET * screenScale;
    }

}
