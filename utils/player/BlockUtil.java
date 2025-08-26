package ru.luminar.utils.player;

import net.minecraft.block.Block;
import net.minecraft.block.AbstractBlock.AbstractBlockState;
import net.minecraft.util.math.BlockPos;
import ru.luminar.utils.client.IMinecraft;

public class BlockUtil {
   public Block getBlock(BlockPos pos) {
      return this.getState(pos).func_177230_c();
   }

   public static Block getBlock(double x, double y, double z) {
      return IMinecraft.mc.field_71441_e.func_180495_p(new BlockPos(x, y, z)).func_177230_c();
   }

   public AbstractBlockState getState(BlockPos pos) {
      return IMinecraft.mc.field_71441_e.func_180495_p(pos);
   }
}
