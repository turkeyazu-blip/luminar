package ru.luminar.utils.math;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

public class ProjectionUtil {
   private static final Minecraft mc = Minecraft.func_71410_x();

   public static Vector2f project(double x, double y, double z) {
      Vector3d camera_pos = mc.func_175598_ae().field_217783_c.func_216785_c();
      Quaternion cameraRotation = mc.func_175598_ae().func_229098_b_().func_227068_g_();
      cameraRotation.func_195892_e();
      Vector3f result3f = new Vector3f((float)(camera_pos.field_72450_a - x), (float)(camera_pos.field_72448_b - y), (float)(camera_pos.field_72449_c - z));
      result3f.func_214905_a(cameraRotation);
      if (mc.field_71474_y.field_74336_f) {
         Entity renderViewEntity = mc.func_175606_aa();
         if (renderViewEntity instanceof PlayerEntity) {
            PlayerEntity playerentity = (PlayerEntity)renderViewEntity;
            calculateViewBobbing((ClientPlayerEntity)playerentity, result3f);
         }
      }

      double fov = mc.field_71474_y.field_74334_X > 100.0D ? 100.0D : mc.field_71474_y.field_74334_X;
      return calculateScreenPosition(result3f, fov);
   }

   private static void calculateViewBobbing(ClientPlayerEntity player, Vector3f result3f) {
      float walked = player.field_70140_Q;
      float prevWalked = player.field_70141_P;
      float partialTicks = mc.func_184121_ak();
      float f = walked - prevWalked;
      float f1 = -(walked + f * partialTicks);
      float f2 = MathHelper.func_219799_g(partialTicks, player.field_71107_bF, player.field_71109_bG);
      Quaternion quaternion = new Quaternion(Vector3f.field_229179_b_, Math.abs(MathHelper.func_76134_b(f1 * 3.1415927F - 0.2F) * f2 * 5.0F), true);
      quaternion.func_195892_e();
      result3f.func_214905_a(quaternion);
      Quaternion quaternion1 = new Quaternion(Vector3f.field_229183_f_, MathHelper.func_76126_a(f1 * 3.1415927F) * f2 * 3.0F, true);
      quaternion1.func_195892_e();
      result3f.func_214905_a(quaternion1);
      Vector3f bobTranslation = new Vector3f(MathHelper.func_76126_a(f1 * 3.1415927F) * f2 * 0.5F, -Math.abs(MathHelper.func_76134_b(f1 * 3.1415927F) * f2), 0.0F);
      bobTranslation.setY(-bobTranslation.func_195900_b());
      result3f.func_229189_a_(bobTranslation);
   }

   private static Vector2f calculateScreenPosition(Vector3f result3f, double fov) {
      float halfHeight = (float)mc.func_228018_at_().func_198087_p() / 2.0F;
      float scaleFactor = halfHeight / (result3f.func_195902_c() * (float)Math.tan(Math.toRadians(fov / 2.0D)));
      return result3f.func_195902_c() < 0.0F ? new Vector2f(-result3f.func_195899_a() * scaleFactor + (float)mc.func_228018_at_().func_198107_o() / 2.0F, (float)mc.func_228018_at_().func_198087_p() / 2.0F - result3f.func_195900_b() * scaleFactor) : new Vector2f(Float.MAX_VALUE, Float.MAX_VALUE);
   }
}
