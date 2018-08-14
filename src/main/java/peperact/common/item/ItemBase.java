package peperact.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import peperact.Peperact;
import peperact.common.helper.StringHelper;

public class ItemBase extends Item {
    /**
     * For registration purposes, constructor of sub classes must not have any params.
     * Automatically generates resource location from class name by converting CamelCase to snake_case.
     * Make sure your sub class starts with "Block".
     */
    public ItemBase() {
        this.setRegistryName(new ResourceLocation(
                Peperact.MODID,
                StringHelper.camelCaseToSnakeCase(StringHelper.removePrefix(this.getClass().getSimpleName(), "Item"))
        ));
        this.setTranslationKey(StringHelper.toKey(this.getRegistryName()));
        this.setCreativeTab(Peperact.CREATIVE_TAB);
    }
}
