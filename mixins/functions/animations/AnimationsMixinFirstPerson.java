package ru.luminar.mixins.functions.animations;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms.TransformType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.Animations;

@Mixin({FirstPersonRenderer.class})
public abstract class AnimationsMixinFirstPerson {
   @Shadow
   private ItemStack field_187467_d;
   @Shadow
   private ItemStack field_187468_e;
   @Shadow
   @Final
   private Minecraft field_78455_a;

   @Shadow
   protected abstract void func_228398_a_(MatrixStack var1, float var2, HandSide var3, ItemStack var4);

   @Shadow
   public abstract void func_228397_a_(LivingEntity var1, ItemStack var2, TransformType var3, boolean var4, MatrixStack var5, IRenderTypeBuffer var6, int var7);

   @Shadow
   protected abstract void func_228402_a_(MatrixStack var1, IRenderTypeBuffer var2, int var3, float var4, HandSide var5, float var6, ItemStack var7);

   @Shadow
   protected abstract void func_228400_a_(MatrixStack var1, IRenderTypeBuffer var2, int var3, float var4, float var5, float var6);

   @Shadow
   protected abstract void func_228401_a_(MatrixStack var1, IRenderTypeBuffer var2, int var3, float var4, float var5, HandSide var6);

   private Animations getSwordAnimation() {
      return Luminar.instance.functions.animations;
   }

   @Overwrite
   private void func_228406_b_(MatrixStack p_228406_1_, HandSide p_228406_2_, float p_228406_3_) {
      Animations swingAnimation = this.getSwordAnimation();
      int i = p_228406_2_ == HandSide.RIGHT ? 1 : -1;
      p_228406_1_.func_227861_a_((double)((float)i * 0.56F), (double)(-0.52F + p_228406_3_ * (swingAnimation.isEnabled() && !swingAnimation.mode.is("Никакой") ? 0.0F : -0.6F)), -0.7200000286102295D);
   }

   @Overwrite
   private void func_228399_a_(MatrixStack p_228399_1_, HandSide p_228399_2_, float p_228399_3_) {
      Animations swingAnimation = this.getSwordAnimation();
      int i;
      float f;
      float f1;
      if (swingAnimation.isEnabled() && !swingAnimation.mode.is("Никакой")) {
         i = p_228399_2_ == HandSide.RIGHT ? 1 : -1;
         f = (Float)swingAnimation.power.get() * 10.0F;
         f1 = MathHelper.func_76126_a(p_228399_3_ * p_228399_3_ * 3.1415927F);
         p_228399_1_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)i * (45.0F + f1 * (-f / 4.0F))));
         float f1 = MathHelper.func_76126_a(MathHelper.func_76129_c(p_228399_3_) * 3.1415927F);
         p_228399_1_.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_((float)i * f1 * -(f / 4.0F)));
         p_228399_1_.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(f1 * -f));
         p_228399_1_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)i * -45.0F));
      } else {
         i = p_228399_2_ == HandSide.RIGHT ? 1 : -1;
         f = MathHelper.func_76126_a(p_228399_3_ * p_228399_3_ * 3.1415927F);
         p_228399_1_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)i * (45.0F + f * -20.0F)));
         f1 = MathHelper.func_76126_a(MathHelper.func_76129_c(p_228399_3_) * 3.1415927F);
         p_228399_1_.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_((float)i * f1 * -20.0F));
         p_228399_1_.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(f1 * -80.0F));
         p_228399_1_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)i * -45.0F));
      }

   }

   @Overwrite
   private void func_228405_a_(AbstractClientPlayerEntity p_228405_1_, float p_228405_2_, float p_228405_3_, Hand p_228405_4_, float p_228405_5_, ItemStack p_228405_6_, float p_228405_7_, MatrixStack p_228405_8_, IRenderTypeBuffer p_228405_9_, int p_228405_10_) {
      boolean flag = p_228405_4_ == Hand.MAIN_HAND;
      HandSide handside = flag ? p_228405_1_.func_184591_cq() : p_228405_1_.func_184591_cq().func_188468_a();
      p_228405_8_.func_227860_a_();
      if (p_228405_6_.func_190926_b()) {
         if (flag && !p_228405_1_.func_82150_aj()) {
            this.func_228401_a_(p_228405_8_, p_228405_9_, p_228405_10_, p_228405_7_, p_228405_5_, handside);
         }
      } else if (p_228405_6_.func_77973_b() == Items.field_151098_aY) {
         if (flag && this.field_187468_e.func_190926_b()) {
            this.func_228400_a_(p_228405_8_, p_228405_9_, p_228405_10_, p_228405_3_, p_228405_7_, p_228405_5_);
         } else {
            this.func_228402_a_(p_228405_8_, p_228405_9_, p_228405_10_, p_228405_7_, handside, p_228405_5_, p_228405_6_);
         }
      } else {
         boolean flag3;
         float f12;
         float f7;
         float f11;
         float f14;
         float f17;
         int k;
         if (p_228405_6_.func_77973_b() == Items.field_222114_py) {
            flag3 = CrossbowItem.func_220012_d(p_228405_6_);
            boolean flag2 = handside == HandSide.RIGHT;
            k = flag2 ? 1 : -1;
            if (p_228405_1_.func_184587_cr() && p_228405_1_.func_184605_cv() > 0 && p_228405_1_.func_184600_cs() == p_228405_4_) {
               this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
               p_228405_8_.func_227861_a_((double)((float)k * -0.4785682F), -0.0943870022892952D, 0.05731530860066414D);
               p_228405_8_.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-11.935F));
               p_228405_8_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)k * 65.3F));
               p_228405_8_.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_((float)k * -9.785F));
               f12 = (float)p_228405_6_.func_77988_m() - ((float)this.field_78455_a.field_71439_g.func_184605_cv() - p_228405_2_ + 1.0F);
               f7 = f12 / (float)CrossbowItem.func_220026_e(p_228405_6_);
               if (f7 > 1.0F) {
                  f7 = 1.0F;
               }

               if (f7 > 0.1F) {
                  f11 = MathHelper.func_76126_a((f12 - 0.1F) * 1.3F);
                  f14 = f7 - 0.1F;
                  f17 = f11 * f14;
                  p_228405_8_.func_227861_a_((double)(f17 * 0.0F), (double)(f17 * 0.004F), (double)(f17 * 0.0F));
               }

               p_228405_8_.func_227861_a_((double)(f7 * 0.0F), (double)(f7 * 0.0F), (double)(f7 * 0.04F));
               p_228405_8_.func_227862_a_(1.0F, 1.0F, 1.0F + f7 * 0.2F);
               p_228405_8_.func_227863_a_(Vector3f.field_229180_c_.func_229187_a_((float)k * 45.0F));
            } else {
               f12 = -0.4F * MathHelper.func_76126_a(MathHelper.func_76129_c(p_228405_5_) * 3.1415927F);
               f7 = 0.2F * MathHelper.func_76126_a(MathHelper.func_76129_c(p_228405_5_) * 6.2831855F);
               f11 = -0.2F * MathHelper.func_76126_a(p_228405_5_ * 3.1415927F);
               p_228405_8_.func_227861_a_((double)((float)k * f12), (double)f7, (double)f11);
               this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
               this.func_228399_a_(p_228405_8_, handside, p_228405_5_);
               if (flag3 && p_228405_5_ < 0.001F) {
                  p_228405_8_.func_227861_a_((double)((float)k * -0.641864F), 0.0D, 0.0D);
                  p_228405_8_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)k * 10.0F));
               }
            }

            this.func_228397_a_(p_228405_1_, p_228405_6_, flag2 ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND, !flag2, p_228405_8_, p_228405_9_, p_228405_10_);
         } else {
            flag3 = handside == HandSide.RIGHT;
            Animations animations = this.getSwordAnimation();
            if (animations.isEnabled() && (!p_228405_1_.func_184587_cr() || p_228405_1_.func_184605_cv() <= 0 || p_228405_1_.func_184600_cs() != p_228405_4_)) {
               if (flag3) {
                  p_228405_8_.func_227861_a_((double)(Float)animations.right_x.get(), (double)(Float)animations.right_y.get(), (double)(Float)animations.right_z.get());
               } else {
                  p_228405_8_.func_227861_a_((double)(Float)animations.left_x.get(), (double)(Float)animations.left_y.get(), (double)(Float)animations.left_z.get());
               }
            }

            float f8;
            float f19;
            if (p_228405_1_.func_184587_cr() && p_228405_1_.func_184605_cv() > 0 && p_228405_1_.func_184600_cs() == p_228405_4_) {
               k = flag3 ? 1 : -1;
               switch(p_228405_6_.func_77975_n()) {
               case NONE:
                  this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
                  break;
               case EAT:
               case DRINK:
                  this.func_228398_a_(p_228405_8_, p_228405_2_, handside, p_228405_6_);
                  this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
                  break;
               case BLOCK:
                  this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
                  break;
               case BOW:
                  this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
                  p_228405_8_.func_227861_a_((double)((float)k * -0.2785682F), 0.18344387412071228D, 0.15731531381607056D);
                  p_228405_8_.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-13.935F));
                  p_228405_8_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)k * 35.3F));
                  p_228405_8_.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_((float)k * -9.785F));
                  f8 = (float)p_228405_6_.func_77988_m() - ((float)this.field_78455_a.field_71439_g.func_184605_cv() - p_228405_2_ + 1.0F);
                  f12 = f8 / 20.0F;
                  f12 = (f12 * f12 + f12 * 2.0F) / 3.0F;
                  if (f12 > 1.0F) {
                     f12 = 1.0F;
                  }

                  if (f12 > 0.1F) {
                     f7 = MathHelper.func_76126_a((f8 - 0.1F) * 1.3F);
                     f11 = f12 - 0.1F;
                     f14 = f7 * f11;
                     p_228405_8_.func_227861_a_((double)(f14 * 0.0F), (double)(f14 * 0.004F), (double)(f14 * 0.0F));
                  }

                  p_228405_8_.func_227861_a_((double)(f12 * 0.0F), (double)(f12 * 0.0F), (double)(f12 * 0.04F));
                  p_228405_8_.func_227862_a_(1.0F, 1.0F, 1.0F + f12 * 0.2F);
                  p_228405_8_.func_227863_a_(Vector3f.field_229180_c_.func_229187_a_((float)k * 45.0F));
                  break;
               case SPEAR:
                  this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
                  p_228405_8_.func_227861_a_((double)((float)k * -0.5F), 0.699999988079071D, 0.10000000149011612D);
                  p_228405_8_.func_227863_a_(Vector3f.field_229179_b_.func_229187_a_(-55.0F));
                  p_228405_8_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)k * 35.3F));
                  p_228405_8_.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_((float)k * -9.785F));
                  f7 = (float)p_228405_6_.func_77988_m() - ((float)this.field_78455_a.field_71439_g.func_184605_cv() - p_228405_2_ + 1.0F);
                  f11 = f7 / 10.0F;
                  if (f11 > 1.0F) {
                     f11 = 1.0F;
                  }

                  if (f11 > 0.1F) {
                     f14 = MathHelper.func_76126_a((f7 - 0.1F) * 1.3F);
                     f17 = f11 - 0.1F;
                     f19 = f14 * f17;
                     p_228405_8_.func_227861_a_((double)(f19 * 0.0F), (double)(f19 * 0.004F), (double)(f19 * 0.0F));
                  }

                  p_228405_8_.func_227861_a_(0.0D, 0.0D, (double)(f11 * 0.2F));
                  p_228405_8_.func_227862_a_(1.0F, 1.0F, 1.0F + f11 * 0.2F);
                  p_228405_8_.func_227863_a_(Vector3f.field_229180_c_.func_229187_a_((float)k * 45.0F));
               }
            } else if (p_228405_1_.func_204805_cN()) {
               this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
               k = flag3 ? 1 : -1;
               p_228405_8_.func_227861_a_((double)((float)k * -0.4F), 0.800000011920929D, 0.30000001192092896D);
               p_228405_8_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_((float)k * 65.0F));
               p_228405_8_.func_227863_a_(Vector3f.field_229183_f_.func_229187_a_((float)k * -85.0F));
            } else {
               f19 = -0.4F * MathHelper.func_76126_a(MathHelper.func_76129_c(p_228405_5_) * 3.1415927F);
               f8 = 0.2F * MathHelper.func_76126_a(MathHelper.func_76129_c(p_228405_5_) * 6.2831855F);
               f12 = -0.2F * MathHelper.func_76126_a(p_228405_5_ * 3.1415927F);
               int l = flag3 ? 1 : -1;
               if (!animations.isEnabled() || animations.mode.is("Никакой")) {
                  p_228405_8_.func_227861_a_((double)((float)l * f19), (double)f8, (double)f12);
               }

               this.func_228406_b_(p_228405_8_, handside, p_228405_7_);
               if (animations.isEnabled() && handside == HandSide.RIGHT && !animations.mode.is("Никакой")) {
                  animations.animationProcess(p_228405_8_, p_228405_5_, () -> {
                     this.func_228399_a_(p_228405_8_, handside, p_228405_5_);
                  });
               } else {
                  this.func_228399_a_(p_228405_8_, handside, p_228405_5_);
               }
            }

            this.func_228397_a_(p_228405_1_, p_228405_6_, flag3 ? TransformType.FIRST_PERSON_RIGHT_HAND : TransformType.FIRST_PERSON_LEFT_HAND, !flag3, p_228405_8_, p_228405_9_, p_228405_10_);
         }
      }

      p_228405_8_.func_227865_b_();
   }
}
