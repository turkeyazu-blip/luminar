package ru.luminar.utils.player;

import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import ru.luminar.utils.client.IMinecraft;

public class PlayerUtils {
   public static boolean isBlockSolid(double x, double y, double z) {
      return block(new BlockPos(x, y, z)).func_176223_P().func_185904_a().func_76220_a();
   }

   public static Block block(BlockPos pos) {
      return IMinecraft.mc.field_71441_e.func_180495_p(pos).func_177230_c();
   }

   public static boolean connectedTo(String ip) {
      return IMinecraft.mc.func_147104_D() != null && IMinecraft.mc.func_147104_D().field_78845_b != null && IMinecraft.mc.func_147104_D().field_78845_b.contains(ip);
   }
}
