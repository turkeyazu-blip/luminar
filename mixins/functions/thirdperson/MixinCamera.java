package ru.luminar.mixins.functions.thirdperson;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.ThirdPerson;

@Mixin({ActiveRenderInfo.class})
public abstract class MixinCamera {
   @Shadow
   private float field_216797_i;
   @Shadow
   private float field_216798_j;
   @Shadow
   private Entity field_216791_c;
   @Shadow
   private boolean field_216789_a;
   @Shadow
   private IBlockReader field_216790_b;
   @Shadow
   private boolean field_216799_k;
   @Shadow
   private boolean field_216800_l;
   @Shadow
   private float field_216801_m;
   @Shadow
   private float field_216802_n;

   @Shadow
   protected abstract void func_216774_a(Vector3d var1);

   @Shadow
   protected abstract void func_216776_a(float var1, float var2);

   @Shadow
   protected abstract double func_216779_a(double var1);

   @Shadow
   protected abstract void func_216782_a(double var1, double var3, double var5);

   @Inject(
      method = {"setup"},
      at = {@At("HEAD")},
      cancellable = true
   )
   private void onSetup(IBlockReader world, Entity entity, boolean thirdPerson, boolean thirdPersonReverse, float partialTicks, CallbackInfo ci) {
      if (Luminar.instance.functions.thirdPerson.isEnabled() && ThirdPerson.active && Minecraft.func_71410_x().field_71462_r == null) {
         this.field_216789_a = true;
         this.field_216790_b = world;
         this.field_216791_c = entity;
         this.field_216799_k = thirdPerson;
         this.field_216800_l = thirdPersonReverse;
         this.func_216776_a(ThirdPerson.getYaw(partialTicks), ThirdPerson.getPitch(partialTicks));
         this.func_216774_a(new Vector3d(MathHelper.func_219803_d((double)partialTicks, entity.field_70169_q, entity.func_226277_ct_()), MathHelper.func_219803_d((double)partialTicks, entity.field_70167_r, entity.func_226278_cu_()) + (double)MathHelper.func_219799_g(partialTicks, this.field_216802_n, this.field_216801_m), MathHelper.func_219803_d((double)partialTicks, entity.field_70166_s, entity.func_226281_cx_())));
         if (thirdPerson) {
            if (thirdPersonReverse) {
               this.func_216776_a(this.field_216798_j + 180.0F, -this.field_216797_i);
            }

            this.func_216782_a(-this.func_216779_a(4.0D), 0.0D, 0.0D);
         } else if (entity instanceof LivingEntity && ((LivingEntity)entity).func_70608_bn()) {
            Direction direction = ((LivingEntity)entity).func_213376_dz();
            this.func_216776_a(direction != null ? direction.func_185119_l() - 180.0F : 0.0F, 0.0F);
            this.func_216782_a(0.0D, 0.3D, 0.0D);
         }

         ci.cancel();
      }

   }
}
