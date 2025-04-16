package me.rarehyperion.smoothui.utility;

import net.minecraft.client.MinecraftClient;

public final class ClientUtility {

    public static boolean isNotInGame() {
        final MinecraftClient client = MinecraftClient.getInstance();
        return client.world == null && client.player == null;
    }

}
