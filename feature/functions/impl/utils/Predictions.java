package ru.luminar.feature.functions.impl.utils;

import java.util.Iterator;
import java.util.Optional;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.item.TridentItem;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import ru.luminar.events.impl.render.Render3DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.utils.math.EntityRayTraceHitResult;

public class Predictions extends Function {
   private static final double GRAVITY = 0.05D;
   private static final double DRAG = 0.99D;
   private static final double WATER_DRAG = 0.6D;

   public Predictions() {
      super("Predictions", Category.Utils);
   }

   @Subscribe
   public void onEvent(Render3DEvent event) {
      if (mc.field_71439_g != null) {
         ItemStack heldItem = mc.field_71439_g.func_184614_ca();
         if (heldItem.func_77973_b() instanceof TridentItem || heldItem.func_77973_b() instanceof CrossbowItem || heldItem.func_77973_b() instanceof EnderPearlItem || heldItem.func_77973_b() instanceof SplashPotionItem) {
            Vector3d startPos = mc.field_71439_g.func_174824_e(event.getPartialTicks());
            if (heldItem.func_77973_b() instanceof CrossbowItem && EnchantmentHelper.func_77506_a(Enchantments.field_222192_G, heldItem) > 0) {
               for(int i = 0; i < 3; ++i) {
                  Vector3d predictedPos = this.predictPosition(i - 1);
                  if (predictedPos != null) {
                     this.renderPrediction(startPos, predictedPos, heldItem);
                  }
               }
            } else {
               Vector3d predictedPos = this.predictPosition(0);
               if (predictedPos != null) {
                  this.renderPrediction(startPos, predictedPos, heldItem);
               }
            }

         }
      }
   }

   private void renderPrediction(Vector3d startPos, Vector3d predictedPos, ItemStack heldItem) {
      BlockRayTraceResult blockHit = mc.field_71441_e.func_217299_a(new RayTraceContext(startPos, predictedPos, BlockMode.COLLIDER, FluidMode.NONE, (Entity)null));
      EntityRayTraceHitResult entityHit = this.rayTraceEntities(startPos, predictedPos);
      Direction face = Direction.UP;
      Vector3d hitPos;
      if (entityHit != null) {
         hitPos = entityHit.func_216347_e();
         face = entityHit.face;
      } else if (blockHit.func_216346_c() != Type.MISS) {
         hitPos = blockHit.func_216347_e();
         face = blockHit.func_216354_b();
      } else {
         hitPos = predictedPos;
      }

      if (hitPos != null) {
         this.drawCircle(hitPos, face, startPos, predictedPos, entityHit != null);
      }

   }

   private Vector3d predictPosition(int spreadIndex) {
      if (mc.field_71439_g == null) {
         return null;
      } else {
         ItemStack heldItem = mc.field_71439_g.func_184614_ca();
         double velocity = 1.5D;
         double gravity = 0.05D;
         double drag = 0.99D;
         boolean isInWater = mc.field_71439_g.func_70090_H();
         if (heldItem.func_77973_b() instanceof TridentItem) {
            velocity = isInWater ? 1.66D : 2.5D;
            gravity = isInWater ? 0.01D : 0.05D;
            drag = isInWater ? 0.6D : 0.99D;
         } else if (heldItem.func_77973_b() instanceof CrossbowItem) {
            velocity = 3.15D;
            gravity = 0.05D;
         } else if (heldItem.func_77973_b() instanceof EnderPearlItem) {
            velocity = 1.5D;
            gravity = 0.03D;
         } else if (heldItem.func_77973_b() instanceof SplashPotionItem) {
            velocity = 0.5D;
            gravity = 0.03D;
         }

         float pitch = mc.field_71439_g.field_70125_A;
         float yaw = mc.field_71439_g.field_70177_z;
         float spreadFactor = (float)spreadIndex * 10.0F;
         Vector3d motion = (new Vector3d(-Math.sin(Math.toRadians((double)(yaw + spreadFactor))) * Math.cos(Math.toRadians((double)pitch)), -Math.sin(Math.toRadians((double)pitch)), Math.cos(Math.toRadians((double)(yaw + spreadFactor))) * Math.cos(Math.toRadians((double)pitch)))).func_72432_b().func_186678_a(velocity);
         Vector3d position = mc.field_71439_g.func_174824_e(1.0F);

         for(int i = 0; i < 100; ++i) {
            motion = motion.func_72441_c(0.0D, -gravity, 0.0D).func_186678_a(drag);
            position = position.func_178787_e(motion);
            if (heldItem.func_77973_b() instanceof TridentItem && this.isInWater(position)) {
               motion = motion.func_186678_a(0.6D);
               gravity = 0.01D;
            }

            if (mc.field_71441_e.func_217299_a(new RayTraceContext(position.func_178788_d(motion), position, BlockMode.COLLIDER, FluidMode.NONE, (Entity)null)).func_216346_c() != Type.MISS) {
               break;
            }
         }

         return position;
      }
   }

   private boolean isInWater(Vector3d pos) {
      BlockPos blockPos = new BlockPos(pos.field_72450_a, pos.field_72448_b, pos.field_72449_c);
      return mc.field_71441_e.func_180495_p(blockPos).func_185904_a() == Material.field_151586_h;
   }

   private EntityRayTraceHitResult rayTraceEntities(Vector3d start, Vector3d end) {
      EntityRayTraceHitResult result = null;
      double closestDistance = Double.MAX_VALUE;
      Iterator var6 = mc.field_71441_e.func_72839_b((Entity)null, (new AxisAlignedBB(Math.min(start.field_72450_a, end.field_72450_a), Math.min(start.field_72448_b, end.field_72448_b), Math.min(start.field_72449_c, end.field_72449_c), Math.max(start.field_72450_a, end.field_72450_a), Math.max(start.field_72448_b, end.field_72448_b), Math.max(start.field_72449_c, end.field_72449_c))).func_186662_g(1.0D)).iterator();

      while(var6.hasNext()) {
         Entity entity = (Entity)var6.next();
         if (entity.func_70067_L() && entity != mc.field_71439_g) {
            AxisAlignedBB bb = entity.func_174813_aQ().func_186662_g((double)entity.func_70111_Y());
            Optional<Vector3d> hit = bb.func_216365_b(start, end);
            if (hit.isPresent()) {
               double distance = start.func_72436_e((Vector3d)hit.get());
               if (distance < closestDistance) {
                  closestDistance = distance;
                  Vector3d hitPos = (Vector3d)hit.get();
                  Vector3d entityCenter = new Vector3d(entity.func_226277_ct_(), entity.func_226278_cu_() + (double)(entity.func_213302_cg() / 2.0F), entity.func_226281_cx_());
                  Vector3d relHit = hitPos.func_178788_d(entityCenter).func_72432_b();
                  Direction face = Math.abs(relHit.field_72448_b) > 0.9D ? (relHit.field_72448_b > 0.0D ? Direction.UP : Direction.DOWN) : Direction.func_210769_a(relHit.field_72450_a, 0.0D, relHit.field_72449_c);
                  result = new EntityRayTraceHitResult(entity, hitPos, face);
               }
            }
         }
      }

      return result;
   }

   public void drawCircle(Vector3d vector3d, Direction face, Vector3d vecpos, Vector3d vecnext, boolean isEntityHit) {
      Vector3d vecRender = mc.func_175598_ae().field_217783_c.func_216785_c();
      double x = vector3d.field_72450_a - vecRender.field_72450_a;
      double y = vector3d.field_72448_b - vecRender.field_72448_b;
      double z = vector3d.field_72449_c - vecRender.field_72449_c;
      GL11.glPushMatrix();
      GL11.glTranslated(x, y, z);
      alignToFace(face);
      GL11.glDisable(3553);
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 771);
      float r = 0.7F;
      float crossLength = r * 0.9F;
      GL11.glBegin(6);
      GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
      GL11.glVertex3d(0.0D, 0.0D, 0.0D);

      double rad;
      int i;
      for(i = 0; i <= 360; i += 10) {
         rad = Math.toRadians((double)i);
         GL11.glVertex3d(Math.cos(rad) * (double)r, 0.0D, Math.sin(rad) * (double)r);
      }

      GL11.glEnd();
      GL11.glLineWidth(2.0F);
      GL11.glBegin(2);
      GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);

      for(i = 0; i <= 360; i += 10) {
         rad = Math.toRadians((double)i);
         GL11.glVertex3d(Math.cos(rad) * (double)r, 0.0D, Math.sin(rad) * (double)r);
      }

      GL11.glEnd();
      GL11.glLineWidth(3.0F);
      GL11.glBegin(1);
      GL11.glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
      double angle = 0.7853981633974483D;
      GL11.glVertex3d((double)(-crossLength) * Math.cos(angle), 0.0D, (double)(-crossLength) * Math.sin(angle));
      GL11.glVertex3d((double)crossLength * Math.cos(angle), 0.0D, (double)crossLength * Math.sin(angle));
      GL11.glVertex3d((double)crossLength * Math.cos(angle), 0.0D, (double)(-crossLength) * Math.sin(angle));
      GL11.glVertex3d((double)(-crossLength) * Math.cos(angle), 0.0D, (double)crossLength * Math.sin(angle));
      GL11.glEnd();
      GL11.glEnable(3553);
      GL11.glDisable(3042);
      GL11.glPopMatrix();
   }

   public static void alignToFace(Direction face) {
      switch(face) {
      case DOWN:
         GL11.glRotated(180.0D, 1.0D, 0.0D, 0.0D);
         break;
      case EAST:
         GL11.glRotated(90.0D, 0.0D, 0.0D, 1.0D);
         break;
      case WEST:
         GL11.glRotated(-90.0D, 0.0D, 0.0D, 1.0D);
         break;
      case SOUTH:
         GL11.glRotated(-90.0D, 1.0D, 0.0D, 0.0D);
         break;
      case NORTH:
         GL11.glRotated(90.0D, 1.0D, 0.0D, 0.0D);
      }

   }
}
