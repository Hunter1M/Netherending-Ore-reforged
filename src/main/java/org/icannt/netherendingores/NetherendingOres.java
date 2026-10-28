package org.icannt.netherendingores;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;

@Mod(NetherendingOres.MOD_ID)
public class NetherendingOres {

    public static final String MOD_ID = "explodingnetherores";
    public static final String MOD_NAME = "Exploding Nether Ores Reforged";

    public NetherendingOres(IEventBus modEventBus) {
        Registration.init(modEventBus);
    }

}
