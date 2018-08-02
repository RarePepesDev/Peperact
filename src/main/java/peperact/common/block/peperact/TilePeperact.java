package peperact.common.block.peperact;

import com.google.common.base.Predicates;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.items.IItemHandler;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TilePeperact extends TileEntity implements IItemHandler, IFluidHandler, IEnergyStorage {
    @CapabilityInject(IItemHandler.class)
    public static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    @CapabilityInject(IFluidHandler.class)
    public static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = null;

    @CapabilityInject(IEnergyStorage.class)
    public static Capability<IEnergyStorage> ENERGY_STORAGE_CAPABILITY = null;

    private static IFluidTankProperties fluidTankProperties = new EmptyTankProperties();

    private static Set<TilePeperact> allTiles = new HashSet<TilePeperact>();

    private boolean inserting = false;

    @Override
    public void invalidate() {
        super.invalidate();
        if(this.getWorld().isRemote)
            return;
        allTiles.remove(this);
    }

    @Override
    public void validate() {
        super.validate();
        if(this.getWorld().isRemote)
            return;
        allTiles.add(this);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ITEM_HANDLER_CAPABILITY ||
                capability == FLUID_HANDLER_CAPABILITY ||
                capability == ENERGY_STORAGE_CAPABILITY ||
                super.hasCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
        if(capability == ITEM_HANDLER_CAPABILITY) return (T) this;
        if(capability == FLUID_HANDLER_CAPABILITY) return (T) this;
        if(capability == ENERGY_STORAGE_CAPABILITY) return (T) this;
        return super.getCapability(capability, facing);
    }

    @SuppressWarnings("unchecked")
    private <T> Stream<T> getTargetCapabilities(Capability<T> capability) {
        return allTiles.stream()
                .filter(p -> !p.inserting && !p.isInvalid())
                .flatMap(p -> Arrays.stream(EnumFacing.VALUES)
                        .map(side -> Pair.of(side.getOpposite(), p.getPos().offset(side)))
                        .filter(sidepos -> p.getWorld().isBlockLoaded(sidepos.getRight()))
                        .map(sidepos -> Pair.of(sidepos.getLeft(), p.getWorld().getTileEntity(sidepos.getRight())))
                        .filter(sidetile -> sidetile.getRight() != null)
                        .map(sidetile -> sidetile.getRight().getCapability(capability, sidetile.getLeft()))
                        .filter(Predicates.notNull())
                );
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if(this.getWorld().isRemote || inserting || maxReceive <= 0) return 0;
        this.inserting = true;

        List<IEnergyStorage> caps = getTargetCapabilities(ENERGY_STORAGE_CAPABILITY)
                .filter(IEnergyStorage::canReceive)
                .collect(Collectors.toList());

        int remaining = maxReceive;
        for(IEnergyStorage cap : caps) {
            remaining -= cap.receiveEnergy(remaining, simulate);
            if(remaining <= 0) break;
        }

        this.inserting = false;
        return maxReceive - remaining;
    }

    @Override
    public int fill(FluidStack resource, boolean doFill) {
        if(this.getWorld().isRemote || inserting || resource == null || resource.amount <= 0) return 0;
        this.inserting = true;

        List<IFluidHandler> caps = getTargetCapabilities(FLUID_HANDLER_CAPABILITY)
                .filter(cap -> {
                    for(IFluidTankProperties props : cap.getTankProperties())
                        if(props.canFillFluidType(resource))
                            return true;
                    return false;
                })
                .collect(Collectors.toList());

        FluidStack remaining = resource.copy();
        for(IFluidHandler cap : caps) {
            remaining.amount -= cap.fill(remaining, doFill);
            if(remaining.amount <= 0) break;
        }

        this.inserting = false;
        return resource.amount - remaining.amount;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slotAlwaysZero, @Nonnull ItemStack stack, boolean simulate) {
        if(this.getWorld().isRemote || inserting || stack.isEmpty() || stack.getCount() <= 0) return stack;
        this.inserting = true;

        List<IItemHandler> caps = getTargetCapabilities(ITEM_HANDLER_CAPABILITY)
                .filter(cap -> cap.getSlots() > 0) //TODO: Is there a better way to filter out more?
                .collect(Collectors.toList());

        ItemStack remaining = stack.copy();
        for(IItemHandler cap : caps) {
            int numSlots = cap.getSlots();
            for(int slot = 0; slot < numSlots; slot++) {
                remaining = cap.insertItem(slot, remaining, simulate);
                if(remaining.getCount() <= 0) break;
            }
        }

        this.inserting = false;
        return remaining.isEmpty() ? ItemStack.EMPTY : remaining;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        return 0;
    }

    @Override
    public int getEnergyStored() {
        return 0;
    }

    @Override
    public int getMaxEnergyStored() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean canExtract() {
        return false;
    }

    @Override
    public boolean canReceive() {
        return true;
    }

    @Override
    public IFluidTankProperties[] getTankProperties() {
        return new IFluidTankProperties[]{fluidTankProperties};
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain) {
        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Nonnull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    private static class EmptyTankProperties implements IFluidTankProperties {

        @Nullable
        @Override
        public FluidStack getContents() {
            return null;
        }

        @Override
        public int getCapacity() {
            return Integer.MAX_VALUE;
        }

        @Override
        public boolean canFill() {
            return true;
        }

        @Override
        public boolean canDrain() {
            return false;
        }

        @Override
        public boolean canFillFluidType(FluidStack fluidStack) {
            return true;
        }

        @Override
        public boolean canDrainFluidType(FluidStack fluidStack) {
            return false;
        }
    }
}
