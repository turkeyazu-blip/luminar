package ru.luminar.mixins.functions;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IngameGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.IRenderTypeBuffer.Impl;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.CooldownTracker;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.utils.ICooldownTrackerAccessor;
import ru.luminar.utils.SlotPos;
import ru.luminar.utils.animations.Animation;
import ru.luminar.utils.animations.Direction;
import ru.luminar.utils.animations.impl.DecelerateAnimation;

@Mixin({IngameGui.class})
public abstract class HealingHelperMixin {
   @Final
   @Shadow
   protected Minecraft field_73839_d;
   private final Map<SlotPos, Animation> slotAnimations = new HashMap();

   @Shadow
   public abstract FontRenderer func_175179_f();

   @Inject(
      method = {"renderSlot"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/renderer/ItemRenderer;renderGuiItemDecorations(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;II)V",
   shift = Shift.AFTER
)}
   )
   private void renderCooldownText(int x, int y, float partialTicks, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
      if (Luminar.instance.functions.cooldowns.isEnabled()) {
         if (!stack.func_190926_b()) {
            CooldownTracker tracker = player.func_184811_cZ();
            float seconds = ((ICooldownTrackerAccessor)tracker).getCooldownSeconds(stack.func_77973_b(), partialTicks);
            if (seconds > 0.0F) {
               MatrixStack matrixstack = new MatrixStack();
               matrixstack.func_227861_a_(0.0D, 0.0D, 300.0D);
               String text = String.valueOf((int)seconds);
               Impl buffer = IRenderTypeBuffer.func_228455_a_(Tessellator.func_178181_a().func_178180_c());
               int color = seconds >= 60.0F ? 255 : (seconds >= 30.0F ? '\uff00' : (seconds >= 15.0F ? 16776960 : 16711680));
               if (seconds >= 100.0F) {
                  matrixstack.func_227862_a_(Luminar.scaleFactor, Luminar.scaleFactor, Luminar.scaleFactor);
               }

               this.func_175179_f().func_238405_a_(matrixstack, text, (float)(x + 2), (float)(y + 2), color);
               buffer.func_228461_a_();
            }

         }
      }
   }

   @Inject(
      method = {"renderSlot"},
      at = {@At("HEAD")}
   )
   private void onRenderSlotPre(int x, int y, float partialTicks, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
      if (!stack.func_190926_b() && Luminar.instance.functions.healingHelper.isEnabled() && Luminar.instance.functions.healingHelper.shouldHightLight(stack)) {
         SlotPos pos = new SlotPos(x, y);
         Animation anim = (Animation)this.slotAnimations.computeIfAbsent(pos, (k) -> {
            return new DecelerateAnimation(250, 0.5D, Direction.FORWARDS);
         });
         if (anim.isDone()) {
            anim.setDirection(anim.getDirection().opposite());
         }
      }

   }

   @Inject(
      method = {"renderSlot"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/renderer/ItemRenderer;renderGuiItemDecorations(Lnet/minecraft/client/gui/FontRenderer;Lnet/minecraft/item/ItemStack;II)V",
   shift = Shift.AFTER
)}
   )
   private void onRenderItemPost(int x, int y, float partialTicks, PlayerEntity player, ItemStack stack, CallbackInfo ci) {
      if (!stack.func_190926_b() && Luminar.instance.functions.healingHelper.isEnabled() && Luminar.instance.functions.healingHelper.shouldHightLight(stack)) {
         Animation anim = (Animation)this.slotAnimations.get(new SlotPos(x, y));
         if (anim != null) {
            int color = Luminar.instance.styleManager.getCurrentStyle().getColor(90);
            float alpha = (float)anim.getOutput();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.disableDepthTest();
            AbstractGui.func_238467_a_(new MatrixStack(), x, y, x + 16, y + 16, color & 16777215 | (int)(alpha * 255.0F) << 24);
            RenderSystem.enableDepthTest();
         }
      }

   }
}
