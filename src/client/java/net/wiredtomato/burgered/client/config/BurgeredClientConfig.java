package net.wiredtomato.burgered.client.config;

import eu.midnightdust.lib.config.MidnightConfig;

public class BurgeredClientConfig extends MidnightConfig {
    @Entry(category = "Rendering") public static int maxRenderedBurgerIngredients = 256;
}
