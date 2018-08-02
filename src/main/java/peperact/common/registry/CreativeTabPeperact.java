package peperact.common.registry;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

public class CreativeTabPeperact extends CreativeTabs {

    public CreativeTabPeperact() {
        super("peperact");
    }

    @Override
    public ItemStack createIcon() {
        return new ItemStack(RegistryItems.peperact);
    }
}
