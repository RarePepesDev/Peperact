package peperact.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import peperact.Peperact;
import peperact.common.helper.StringHelper;

public class BlockBase extends Block {
    private final ItemBlock itemBlock;

    /**
     * For registration purposes, constructor of sub classes must not have any params.
     * Automatically generates resource location from class name by converting CamelCase to snake_case.
     * Make sure your sub class starts with "Block".
     * @param material of the block
     * @param color to display on a map
     */
    public BlockBase(Material material, MapColor color) {
        super(material, color);
        ResourceLocation location = new ResourceLocation(
                Peperact.MODID,
                StringHelper.camelCaseToSnakeCase(StringHelper.removePrefix(this.getClass().getSimpleName(), "Block"))
        );
        this.setRegistryName(location);
        this.setTranslationKey(StringHelper.toKey(location));
        itemBlock = getItemBlock();
        itemBlock.setRegistryName(location);
        this.setCreativeTab(Peperact.CREATIVE_TAB);
    }

    /**
     * Automatically generates an ItemBlock for this block.
     * Override to provide a different ItemBlock.
     * This is called in the constructor.
     *
     * @return ItemBlock for this block.
     */
    public ItemBlock getItemBlock() {
        if(itemBlock == null) {
            return new ItemBlock(this);
        }
        return itemBlock;
    }
}
