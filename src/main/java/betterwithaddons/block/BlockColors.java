package betterwithaddons.block;

import betterwithaddons.block.BetterRedstone.BlockWirePCB;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by Christian on 05.08.2016.
 */
public class BlockColors {
    public static final IBlockColor GRASS_COLORING = new IBlockColor()
    {
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex)
        {
            return world != null && pos != null ? BiomeColorHelper.getGrassColorAtPos(world, pos) : ColorizerGrass.getGrassColor(0.5D, 1.0D);
        }
    };

    public static final IBlockColor REDSTONE_COLORING = new IBlockColor()
    {
        @Override
        @SideOnly(Side.CLIENT)
        public int colorMultiplier(IBlockState state, IBlockAccess world, BlockPos pos, int tintIndex)
        {
            if(state != null && state.getBlock() instanceof BlockWirePCB)
            {
                return BlockWirePCB.colorMultiplier(state.getValue(BlockRedstoneWire.POWER));
            }

            return 0;
        }
    };

    public static final IItemColor BLOCK_ITEM_COLORING = new IItemColor()
    {
        @Override
        public int getColorFromItemstack(ItemStack stack, int tintIndex)
        {
            IBlockState state = ((ItemBlock)stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
            IBlockColor blockColor = ((IColorableBlock)state.getBlock()).getBlockColor();
            return blockColor == null ? 0xFFFFFF : blockColor.colorMultiplier(state, null, null, tintIndex);
        }
    };
}