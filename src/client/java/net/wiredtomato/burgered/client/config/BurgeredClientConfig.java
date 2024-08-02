package net.wiredtomato.burgered.client.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class BurgeredClientConfig extends MidnightConfig {
    @Entry(category = "rendering") public static int maxRenderedBurgerIngredients = 256;
    @Entry(category = "rendering", isSlider = true, min = 0, max = 360) public static float maxSloppinessRotationX = 10f;
    @Entry(category = "rendering", isSlider = true, min = 0, max = 360) public static float maxSloppinessRotationY = 90f;
    @Entry(category = "rendering", isSlider = true, min = 0, max = 360) public static float maxSloppinessRotationZ = 10f;
    @Entry(category = "rendering") public static boolean renderNoTransform = true;
}
