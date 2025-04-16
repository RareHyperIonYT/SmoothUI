package me.rarehyperion.smoothui.utility;

public final class EasingFunctions {

    public static final double SCROLLBAR_DRAG = 0.025D;
    private static final double PUSHBACK_STRENGTH = 1.0D;

    public static float easeInQuint(final float time) {
        return 1 * time * time * time * time * time;
    }

    public static float easeLinearDamped(final float current, final float target, final float deltaTime, final float speed) {
        float difference = target - current;
        return current + difference * deltaTime * speed;
    }

    public static int dampenSquish(final double squish, final int height) {
        double proportion = Math.min(1, squish / 100);
        return (int) (Math.min(0.85, proportion) * height);
    }

    public static double pushBackStrength(final double distance, final float deltaTime) {
        return ((distance + 4D) * deltaTime / 0.3D) / (3.2D / PUSHBACK_STRENGTH);
    }

}
