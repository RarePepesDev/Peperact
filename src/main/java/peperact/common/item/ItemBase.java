package peperact.common.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import peperact.Peperact;
import peperact.common.helper.StringHelper;

public class ItemBase extends Item {
    private final ResourceLocation resourceLocation;

    public ItemBase() {
        resourceLocation = getResourceLocation();
        this.setTranslationKey(StringHelper.toKey(resourceLocation));
        this.setRegistryName(resourceLocation);
        this.setCreativeTab(Peperact.CREATIVE_TAB);
    }

    /**
     * Automatically generates resource location from class name by converting CamelCase to snake_case.
     * Make sure your sub class starts with "Block".
     * This is called in the constructor.
     *
     * @return Auto generated ResourceLocation
     */
    public ResourceLocation getResourceLocation() {
        if(resourceLocation == null) {
            String name = StringHelper.camelCaseToSnakeCase(StringHelper.removePrefix(this.getClass().getSimpleName(), "Item"));
            return new ResourceLocation(Peperact.MODID, name);
        }
        return resourceLocation;
    }
}
