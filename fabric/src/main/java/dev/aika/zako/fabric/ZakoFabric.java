package dev.aika.zako.fabric;

import dev.aika.zako.Zako;
import net.fabricmc.api.ModInitializer;

public final class ZakoFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Zako.init();
    }
}
