package peperact.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import peperact.client.render.tile.RenderPeperact;
import peperact.common.CommonProxy;
import peperact.common.block.peperact.TilePeperact;
import peperact.common.registry.RegistryBlocks;
import peperact.common.registry.RegistryItems;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerModels() {
        super.registerModels();
        // Tiles
        ClientRegistry.bindTileEntitySpecialRenderer(TilePeperact.class, new RenderPeperact());
        // Items
        ModelLoader.setCustomModelResourceLocation(RegistryItems.peperact, 0, new ModelResourceLocation(RegistryBlocks.peperact.getResourceLocation(), "inventory"));
    }
}