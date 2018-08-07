package peperact;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import peperact.common.block.BlockBase;
import peperact.common.item.ItemBase;
import peperact.common.registry.RegistryBlocks;
import peperact.common.registry.RegistryItems;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber(modid = Peperact.MODID)
public class EventHandler {

    @SubscribeEvent
    public static void onRegisterBlock(RegistryEvent.Register<Block> event) throws IllegalAccessException, InstantiationException {
        for(Field f : RegistryBlocks.class.getDeclaredFields()) {
            if(BlockBase.class.isAssignableFrom(f.getType())) {
                event.getRegistry().register((BlockBase) f.getType().newInstance());
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterItem(RegistryEvent.Register<Item> event) throws IllegalAccessException, InstantiationException {
        for(Field f : RegistryBlocks.class.getDeclaredFields()) {
            if(BlockBase.class.isAssignableFrom(f.getType())) {
                ItemBlock itemBlock = ((BlockBase) f.get(null)).getItemBlock();
                event.getRegistry().register(itemBlock);
            }
        }
        for(Field f : RegistryItems.class.getDeclaredFields()) {
            if(ItemBase.class.isAssignableFrom(f.getType())) {
                ItemBase itemBase = (ItemBase) f.getType().newInstance();
                event.getRegistry().register(itemBase);
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterModels(ModelRegistryEvent event) throws InstantiationException, IllegalAccessException {
        Peperact.proxy.registerModels();
    }
}
