package peperact.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import peperact.Peperact;
import peperact.common.helper.StringHelper;

public class BlockBase extends Block {
    private final ResourceLocation resourceLocation;
    private final ItemBlock itemBlock;

    /**
     * For registration purposes, constructor of sub classes must not have any params.
     *
     * @param material
     * @param color
     */
    public BlockBase(Material material, MapColor color) {
        super(material, color);
        resourceLocation = getResourceLocation();
        this.setTranslationKey(resourceLocation.getNamespace());
        this.setRegistryName(resourceLocation);
        itemBlock = getItemBlock();
        itemBlock.setTranslationKey(resourceLocation.getNamespace());
        itemBlock.setRegistryName(resourceLocation);
    }

    /**
     * Automatically generates resource location from class name by converting CamelCase to snake_case.
     * Make sure your sub class starts with "Block".
     * This is called in the constructor.
     *
     * @return Auto generated ResourceLocation
     */
    public ResourceLocation getResourceLocation() {
        if (resourceLocation == null) {
            String name = StringHelper.camelCaseToSnakeCase(StringHelper.removePrefix(this.getClass().getSimpleName(), "Block"));
            return new ResourceLocation(Peperact.MODID, name);
        }
        return resourceLocation;
    }

    /**
     * Automatically generates an ItemBlock for this block.
     * Override to provide a different ItemBlock.
     * This is called in the constructor.
     *
     * @return ItemBlock for this block.
     */
    public ItemBlock getItemBlock() {
        if (itemBlock == null) {
            ItemBlock itemBlock = new ItemBlock(this);
            return itemBlock;
        }
        return itemBlock;
    }
}
