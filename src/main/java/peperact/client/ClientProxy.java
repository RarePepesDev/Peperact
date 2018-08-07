package peperact.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import peperact.client.render.tile.RenderPeperact;
import peperact.common.CommonProxy;
import peperact.common.block.BlockBase;
import peperact.common.block.peperact.TilePeperact;
import peperact.common.item.ItemBase;
import peperact.common.registry.RegistryBlocks;
import peperact.common.registry.RegistryItems;

import java.lang.reflect.Field;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModels() throws IllegalAccessException, InstantiationException {
        super.registerModels();
        // Tiles
        ClientRegistry.bindTileEntitySpecialRenderer(TilePeperact.class, new RenderPeperact());
        // ItemBlocks
        for(Field f : RegistryBlocks.class.getDeclaredFields()) {
            if(BlockBase.class.isAssignableFrom(f.getType())) {
                BlockBase blockBase = (BlockBase) f.get(null);
                ItemBlock itemBlock = blockBase.getItemBlock();
                ModelLoader.setCustomModelResourceLocation(itemBlock, 0, new ModelResourceLocation(blockBase.getResourceLocation(), "inventory"));
            }
        }
        // Items
        for(Field f : RegistryItems.class.getDeclaredFields()) {
            if(ItemBase.class.isAssignableFrom(f.getType())) {
                ItemBase itemBase = (ItemBase) f.get(null);
                ModelLoader.setCustomModelResourceLocation(itemBase, 0, new ModelResourceLocation(itemBase.getResourceLocation(), "inventory"));
            }
        }
    }
}