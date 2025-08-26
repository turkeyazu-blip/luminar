package ru.luminar.utils.math;

import com.mojang.blaze3d.systems.RenderSystem;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import ru.luminar.mixins.accessors.MinecraftAccessor;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.math.vectors.Vec2i;
import ru.luminar.utils.math.vectors.Vector2d;

public class MathUtil {
   private final int SCALE = 2;

   public static double interpolate(double current, double old, double scale) {
      return old + (current - old) * scale;
   }

   public static boolean isHovered(float mouseX, float mouseY, float x, float y, float width, float height) {
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
   }

   private static void validateRange(double min, double max) {
      if (max < min) {
         throw new IllegalArgumentException("max не может быть меньше min.");
      }
   }

   public static double step(double value, double steps) {
      double roundedValue = (double)Math.round(value / steps) * steps;
      return (double)Math.round(roundedValue * 100.0D) / 100.0D;
   }

   public double randomValue(double min, double max) {
      validateRange(min, max);
      return min + ThreadLocalRandom.current().nextDouble() * (max - min);
   }

   public static float randomValue(float min, float max) {
      validateRange((double)min, (double)max);
      return min + ThreadLocalRandom.current().nextFloat() * (max - min);
   }

   public boolean canSeen(Vector3d vec) {
      Vector3d vector3d = IMinecraft.mc.field_71439_g.func_213303_ch().func_72441_c(0.0D, (double)IMinecraft.mc.field_71439_g.func_70047_e(), 0.0D);
      return IMinecraft.mc.field_71441_e.func_217299_a(new RayTraceContext(vector3d, vec, BlockMode.COLLIDER, FluidMode.NONE, IMinecraft.mc.field_71439_g)).func_216346_c() == Type.MISS;
   }

   public static float smoothTransition(float current, float target, int speed) {
      float difference = target - current;
      if (difference > (float)speed) {
         return current + (float)speed;
      } else {
         return difference < (float)(-speed) ? current - (float)speed : target;
      }
   }

   public Vector2f rotationToVec(Vector3d vec) {
      Vector3d eyesPos = IMinecraft.mc.field_71439_g.func_174824_e(1.0F);
      double diffX = vec != null ? vec.field_72450_a - eyesPos.field_72450_a : 0.0D;
      double diffY = vec != null ? vec.field_72448_b - (IMinecraft.mc.field_71439_g.func_226278_cu_() + (double)IMinecraft.mc.field_71439_g.func_70047_e() + 0.5D) : 0.0D;
      double diffZ = vec != null ? vec.field_72449_c - eyesPos.field_72449_c : 0.0D;
      double diffXZ = Math.sqrt(diffX * diffX + diffZ * diffZ);
      float yaw = (float)(Math.toDegrees(Math.atan2(diffZ, diffX)) - 90.0D);
      float pitch = (float)(-Math.toDegrees(Math.atan2(diffY, diffXZ)));
      yaw = IMinecraft.mc.field_71439_g.field_70177_z + MathHelper.func_76142_g(yaw - IMinecraft.mc.field_71439_g.field_70177_z);
      pitch = IMinecraft.mc.field_71439_g.field_70125_A + MathHelper.func_76142_g(pitch - IMinecraft.mc.field_71439_g.field_70125_A);
      pitch = MathHelper.func_76131_a(pitch, -90.0F, 90.0F);
      return new Vector2f(yaw, pitch);
   }

   public static int clamp(int value, int min, int max) {
      return Math.max(min, Math.min(max, value));
   }

   public static float calculatePosition(float sr, float r) {
      float coefficent = r / 2.0F;
      return sr - coefficent;
   }

   public static Vector2f rotationToEntity(Entity target) {
      Vector3d vector3d = target.func_213303_ch().func_178788_d(Minecraft.func_71410_x().field_71439_g.func_213303_ch());
      double magnitude = Math.hypot(vector3d.field_72450_a, vector3d.field_72449_c);
      return new Vector2f((float)Math.toDegrees(Math.atan2(vector3d.field_72449_c, vector3d.field_72450_a)) - 90.0F, (float)(-Math.toDegrees(Math.atan2(vector3d.field_72448_b, magnitude))));
   }

   public Vector2f rotationToVec(Vector2f rotationVector, Vector3d target) {
      double x = target.field_72450_a - IMinecraft.mc.field_71439_g.func_226277_ct_();
      double y = target.field_72448_b - IMinecraft.mc.field_71439_g.func_174824_e(1.0F).field_72448_b;
      double z = target.field_72449_c - IMinecraft.mc.field_71439_g.func_226281_cx_();
      double dst = Math.sqrt(Math.pow(x, 2.0D) + Math.pow(z, 2.0D));
      float yaw = (float)MathHelper.func_76138_g(Math.toDegrees(Math.atan2(z, x)) - 90.0D);
      float pitch = (float)(-Math.toDegrees(Math.atan2(y, dst)));
      float yawDelta = MathHelper.func_76142_g(yaw - rotationVector.field_189982_i);
      float pitchDelta = pitch - rotationVector.field_189983_j;
      if (Math.abs(yawDelta) > 180.0F) {
         yawDelta -= Math.signum(yawDelta) * 360.0F;
      }

      return new Vector2f(yawDelta, pitchDelta);
   }

   public static double round(double num, double increment) {
      double v = (double)Math.round(num / increment) * increment;
      BigDecimal bd = new BigDecimal(v);
      bd = bd.setScale(2, RoundingMode.HALF_UP);
      return bd.doubleValue();
   }

   public double distance(double x1, double y1, double z1, double x2, double y2, double z2) {
      double d0 = x1 - x2;
      double d1 = y1 - y2;
      double d2 = z1 - z2;
      return Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
   }

   public double distance(double x1, double y1, double x2, double y2) {
      double x = x1 - x2;
      double y = y1 - y2;
      return Math.sqrt(x * x + y * y);
   }

   public static double deltaTime() {
      return ((MinecraftAccessor)IMinecraft.mc).getDebugFPS() > 0 ? 1.0D / (double)((MinecraftAccessor)IMinecraft.mc).getDebugFPS() : 1.0D;
   }

   public static float fast(float end, float start, float multiple) {
      return (1.0F - MathHelper.func_76131_a((float)(deltaTime() * (double)multiple), 0.0F, 1.0F)) * end + MathHelper.func_76131_a((float)(deltaTime() * (double)multiple), 0.0F, 1.0F) * start;
   }

   public static Vector3d interpolate(Vector3d end, Vector3d start, float multiple) {
      return new Vector3d(interpolate(end.field_72450_a, start.field_72450_a, (double)multiple), interpolate(end.field_72448_b, start.field_72448_b, (double)multiple), interpolate(end.field_72449_c, start.field_72449_c, (double)multiple));
   }

   public static Vector3d interpolate(float x1, float y1, float z1, float x, float y, float z) {
      float partialTicks = IMinecraft.mc.func_184121_ak();
      Vector3d result = new Vector3d((double)(x1 + (x - x1) * partialTicks), (double)(y1 + (y - y1) * partialTicks), (double)(z1 + (z - z1) * partialTicks));
      return result;
   }

   public static Vector3d fast(Vector3d end, Vector3d start, float multiple) {
      return new Vector3d((double)fast((float)end.field_72450_a, (float)start.field_72450_a, multiple), (double)fast((float)end.field_72448_b, (float)start.field_72448_b, multiple), (double)fast((float)end.field_72449_c, (float)start.field_72449_c, multiple));
   }

   public static float lerp(float end, float start, float multiple) {
      return (float)((double)end + (double)(start - end) * MathHelper.func_151237_a(deltaTime() * (double)multiple, 0.0D, 1.0D));
   }

   public static double lerp(double end, double start, double multiple) {
      return end + (start - end) * MathHelper.func_151237_a(deltaTime() * multiple, 0.0D, 1.0D);
   }

   public static float random(float f, float f2) {
      return (float)(Math.random() * (double)(f2 - f) + (double)f);
   }

   public static int randomInt(int f, int f2) {
      return (int)((double)f + (double)(f2 - f) * Math.random());
   }

   public static long random(long f, long f2) {
      return (long)(Math.random() * (double)(f2 - f) + (double)f);
   }

   public static boolean isInRegion(float mouseX, float mouseY, float x, float y, float width, float height) {
      return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
   }

   public static void scaleElements(float xCenter, float yCenter, float scale, Runnable runnable) {
      RenderSystem.pushMatrix();
      RenderSystem.translatef(xCenter, yCenter, 0.0F);
      RenderSystem.scalef(scale, scale, 1.0F);
      RenderSystem.translatef(-xCenter, -yCenter, 0.0F);
      runnable.run();
      RenderSystem.popMatrix();
   }

   public static void scaleElements(float xCenter, float yCenter, float scaleX, float scaleY, float scaleZ, Runnable runnable) {
      RenderSystem.pushMatrix();
      RenderSystem.translatef(xCenter, yCenter, 0.0F);
      RenderSystem.scalef(scaleX, scaleY, scaleZ);
      RenderSystem.translatef(-xCenter, -yCenter, 0.0F);
      runnable.run();
      RenderSystem.popMatrix();
   }

   public Vector2d getMouse(double mouseX, double mouseY) {
      return new Vector2d(mouseX * IMinecraft.mc.func_228018_at_().func_198100_s() / 2.0D, mouseY * IMinecraft.mc.func_228018_at_().func_198100_s() / 2.0D);
   }

   public static float calculateDelta(float a, float b) {
      return a - b;
   }

   public static Vector3d getVector(LivingEntity target) {
      double wHalf = (double)(target.func_213311_cf() / 2.0F);
      double yExpand = MathHelper.func_151237_a(target.func_226280_cw_() - target.func_226278_cu_(), 0.0D, (double)target.func_213302_cg());
      double xExpand = MathHelper.func_151237_a(IMinecraft.mc.field_71439_g.func_226277_ct_() - target.func_226277_ct_(), -wHalf, wHalf);
      double zExpand = MathHelper.func_151237_a(IMinecraft.mc.field_71439_g.func_226281_cx_() - target.func_226281_cx_(), -wHalf, wHalf);
      return new Vector3d(target.func_226277_ct_() - IMinecraft.mc.field_71439_g.func_226277_ct_() + xExpand, target.func_226278_cu_() - IMinecraft.mc.field_71439_g.func_226280_cw_() + yExpand, target.func_226281_cx_() - IMinecraft.mc.field_71439_g.func_226281_cx_() + zExpand);
   }

   public float clamp01(float x) {
      return (float)this.clamp(0.0D, 1.0D, (double)x);
   }

   public double clamp(double min, double max, double n) {
      return Math.max(min, Math.min(max, n));
   }

   public static Vec2i getMouseScale(int mouseX, int mouseY) {
      return new Vec2i((int)((double)mouseX * Minecraft.func_71410_x().func_228018_at_().func_198100_s() / 2.0D), (int)((double)mouseY * Minecraft.func_71410_x().func_228018_at_().func_198100_s() / 2.0D));
   }

   public static Vector2d getMouseScale(double mouseX, double mouseY) {
      return new Vector2d(mouseX * IMinecraft.mc.func_228018_at_().func_198100_s() / 2.0D, mouseY * IMinecraft.mc.func_228018_at_().func_198100_s() / 2.0D);
   }

   public float invertLinear(float input, float max) {
      return Math.max(0.0F, max - input);
   }

   public static float invertHyperbolic(float input, float max) {
      return max / (input + 1.0F);
   }

   public float invertExp(float input, float max) {
      return max * (float)Math.exp((double)(-input));
   }
}
