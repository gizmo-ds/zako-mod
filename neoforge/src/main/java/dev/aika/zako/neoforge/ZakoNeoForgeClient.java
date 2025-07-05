package dev.aika.zako.neoforge;

import dev.aika.zako.Zako;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(value = Zako.ID,dist = Dist.CLIENT)
public final class ZakoNeoForgeClient {
    public ZakoNeoForgeClient(IEventBus eventBus, ModContainer container) {
    }
}
