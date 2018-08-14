package peperact;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;
import peperact.common.CommonProxy;
import peperact.common.registry.CreativeTabPeperact;

@Mod(modid = Peperact.MODID, name = Peperact.NAME, version = Peperact.VERSION, dependencies = Peperact.DEPENDENCIES)
public class Peperact {
    public static final String MODID = "peperact";
    public static final String NAME = "Peperact";
    public static final String VERSION = "999.999.999";
    public static final String DEPENDENCIES = "";
    public static final CreativeTabPeperact CREATIVE_TAB = new CreativeTabPeperact();
    public static Logger log;
    @SidedProxy(clientSide = "peperact.client.ClientProxy", serverSide = "peperact.common.CommonProxy")
    public static CommonProxy proxy;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        log = event.getModLog();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {

    }
}
