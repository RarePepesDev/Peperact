package peperact.common.block.peperact;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import peperact.Peperact;
import peperact.common.block.BlockBase;

import javax.annotation.Nullable;
import java.util.Random;

public class BlockPeperact extends BlockBase implements ITileEntityProvider {

    public BlockPeperact() {
        super(Material.IRON, MapColor.GREEN);
        this.setHardness(5.0F);
        this.setSoundType(SoundType.METAL);
        this.setCreativeTab(Peperact.CREATIVE_TAB);
        // Don't know if I should be registering it here... XD
        GameRegistry.registerTileEntity(TilePeperact.class, this.getResourceLocation());
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(World worldIn, int meta) {
        return new TilePeperact();
    }

    @Override
    public boolean doesSideBlockRendering(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing face) {
        return false;
    }

    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void randomDisplayTick(IBlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        //TODO: Particles
    }
}
