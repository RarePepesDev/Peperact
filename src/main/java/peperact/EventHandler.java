package peperact;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import peperact.common.block.peperact.BlockPeperact;
import peperact.common.registry.RegistryBlocks;
import peperact.common.registry.RegistryItems;

@Mod.EventBusSubscriber(modid = Peperact.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void onRegisterBlock(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(RegistryBlocks.peperact = new BlockPeperact());
    }

    @SubscribeEvent
    public static void onRegisterItem(RegistryEvent.Register<Item> event) {
        RegistryItems.peperact = new ItemBlock(RegistryBlocks.peperact);
        RegistryItems.peperact.setTranslationKey("peperact");
        RegistryItems.peperact.setRegistryName(Peperact.MODID, "peperact");
        event.getRegistry().register(RegistryItems.peperact);
        Peperact.proxy.registerItemModels();
    }
}
