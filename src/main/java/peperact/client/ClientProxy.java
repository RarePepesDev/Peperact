package peperact.client;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import peperact.Peperact;
import peperact.common.CommonProxy;
import peperact.common.registry.RegistryBlocks;
import peperact.common.registry.RegistryItems;

public class ClientProxy extends CommonProxy {

    @Override
    public void registerItemModels() {
        super.registerItemModels();
        ModelLoader.setCustomModelResourceLocation(RegistryItems.peperact, 0, new ModelResourceLocation(RegistryBlocks.peperact.getResourceLocation(), "inventory"));
    }
}