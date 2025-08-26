package ru.luminar.feature.functions.impl.render.hud;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.scoreboard.Score;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector4f;
import org.lwjgl.opengl.GL11;
import ru.luminar.Luminar;
import ru.luminar.events.impl.render.Render2DEvent;
import ru.luminar.feature.functions.impl.utils.globalsapi.ClientAPI;
import ru.luminar.ui.GuiScreen;
import ru.luminar.utils.animations.Animation;
import ru.luminar.utils.animations.Direction;
import ru.luminar.utils.animations.impl.DecelerateAnimation;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draggables.Dragging;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.TimerUtil;
import ru.luminar.utils.math.vectors.Vector4i;
import ru.luminar.utils.themes.Theme;

public class TargetHUD {
   final Dragging dragging;
   float posX;
   float posY;
   float width = 100.0F;
   float height = 35.0F;
   LivingEntity target;
   TimerUtil stopWatch = new TimerUtil();
   boolean shouldToBack;
   long targetSetTime;
   float healthAnimation = 0.0F;
   Animation animation = new DecelerateAnimation(500, 1.0D);

   public TargetHUD(Dragging dragging) {
      this.dragging = dragging;
   }

   public void draw(Render2DEvent render2DEvent) {
      this.posX = this.dragging.getX();
      this.posY = this.dragging.getY();
      this.target = this.getTarget(this.target);
      boolean backAnimation = !this.shouldToBack;
      this.animation.setDuration(backAnimation ? 400 : 300);
      this.animation.setDirection(backAnimation ? Direction.BACKWARDS : Direction.FORWARDS);
      if (this.animation.getOutput() == 0.0D) {
         this.target = null;
      }

      if (this.target != null) {
         if (!this.target.func_82150_aj() || !this.target.func_184582_a(EquipmentSlotType.HEAD).func_190926_b() || !this.target.func_184582_a(EquipmentSlotType.CHEST).func_190926_b() || !this.target.func_184582_a(EquipmentSlotType.FEET).func_190926_b() || !this.target.func_184582_a(EquipmentSlotType.LEGS).func_190926_b()) {
            Vector4i vecD = new Vector4i(DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())));
            Vector4i vecW = new Vector4i(DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())));
            Vector4i vec = ColorUtils.interpolate(vecD, vecW, GuiScreen.whiteAnim);
            DisplayUtils.drawRoundedRect(this.posX, this.posY, this.width, this.height, 5.0F, vec);
            this.drawTargetHead(this.target, this.posX + 7.5F, this.posY + 7.5F, 20.0F, 20.0F);
            float hp = this.target.func_110143_aJ();
            float maxHp = this.target.func_110138_aP();
            Score score = IMinecraft.mc.field_71441_e.func_96441_U().func_96529_a(this.target.func_195047_I_(), IMinecraft.mc.field_71441_e.func_96441_U().func_96539_a(2));
            if (IMinecraft.mc.func_147104_D() != null && IMinecraft.mc.func_147104_D().field_78845_b != null && IMinecraft.mc.func_147104_D().field_78845_b.contains("funtime") && this.target instanceof PlayerEntity) {
               hp = (float)score.func_96652_c();
            }

            Object[] var10001 = new Object[]{hp};
            String hpStr = "HP: " + String.format("%.1f", var10001);
            if (this.target.func_82150_aj()) {
               hpStr = "Unknown hp";
            }

            this.healthAnimation = MathUtil.fast(this.healthAnimation, MathHelper.func_76131_a(hp / maxHp, 0.0F, 1.0F), 10.0F);
            int textColor = ColorUtils.interpolate((new Color(255, 255, 255, (int)(255.0D * this.animation.getOutput()))).getRGB(), (new Color(106, 106, 106, (int)(255.0D * this.animation.getOutput()))).getRGB(), GuiScreen.whiteAnim);
            float textX = this.posX + 5.0F + 20.0F + 7.5F;
            if (Luminar.instance.functions.globals.isEnabled()) {
               Iterator var12 = (new ArrayList(ClientAPI.USERS.keySet())).iterator();

               while(var12.hasNext()) {
                  String user = (String)var12.next();
                  if (this.target.func_145748_c_().getString().contains(user)) {
                     DisplayUtils.drawImage(new ResourceLocation("luminar/images/luminar.png"), textX - 4.0F, (float)((double)this.posY + 7.5D) - 3.5F, 12.0F, 12.0F, textColor);
                     textX += 7.0F;
                  }
               }
            }

            Scissor.push();
            Scissor.setFromComponentCoordinates((double)(this.posX + 5.0F + 20.0F + 7.5F), (double)(this.posY + 7.5F), 60.0D, 8.0D);
            Fonts.sfbold.drawText(new MatrixStack(), this.target.func_200200_C_().getString(), textX, this.posY + 7.5F, textColor, 6.0F, 0.05F);
            Scissor.unset();
            Scissor.pop();
            Fonts.sfMedium.drawText(new MatrixStack(), hpStr, this.posX + 5.0F + 20.0F + 7.5F, this.posY + 7.5F + 8.0F + 1.0F, textColor, 5.0F, 0.05F);
            Theme style = Luminar.instance.styleManager.getCurrentStyle();
            Vector4i vector4i = new Vector4i(DisplayUtils.reAlphaInt(style.getColor(70), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(style.getColor(70), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(style.getColor(140), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(style.getColor(140), (int)(255.0D * this.animation.getOutput())));
            int rcolor = ColorUtils.interpolate(ColorUtils.rgba(Color.darkGray, (int)(255.0D * this.animation.getOutput())), ColorUtils.rgba(new Color(230, 230, 230), (int)(255.0D * this.animation.getOutput())), GuiScreen.whiteAnim);
            DisplayUtils.drawRoundedRect(this.posX + 20.0F + 5.0F + 7.5F, this.posY + this.height - 7.5F - 4.0F, 60.0F, 4.0F, new Vector4f(2.75F, 2.75F, 2.75F, 2.75F), rcolor);
            DisplayUtils.drawRoundedRect(this.posX + 20.0F + 5.0F + 7.5F, this.posY + this.height - 7.5F - 4.0F, this.target.func_82150_aj() ? 60.0F : 60.0F * this.healthAnimation, 4.0F, new Vector4f(2.75F, 2.75F, 2.75F, 2.75F), vector4i);
            this.dragging.setWidth(this.width);
            this.dragging.setHeight(this.height);
         }
      }
   }

   private LivingEntity getTarget(LivingEntity nullTarget) {
      this.checkTargetTimeout();
      LivingEntity target = nullTarget;
      if (IMinecraft.mc.field_71476_x != null && IMinecraft.mc.field_71476_x.func_216346_c() == Type.ENTITY) {
         Entity entity = ((EntityRayTraceResult)IMinecraft.mc.field_71476_x).func_216348_a();
         if (entity instanceof LivingEntity) {
            LivingEntity newTarget = (LivingEntity)entity;
            if (this.target != null && this.target.equals(newTarget)) {
               this.targetSetTime = System.currentTimeMillis();
            } else {
               this.target = newTarget;
               this.targetSetTime = System.currentTimeMillis();
               this.stopWatch.reset();
               this.shouldToBack = true;
            }

            target = this.target;
         }
      } else if (this.target != null) {
         target = this.target;
      } else if (IMinecraft.mc.field_71462_r instanceof ChatScreen) {
         this.stopWatch.reset();
         this.shouldToBack = true;
         target = IMinecraft.mc.field_71439_g;
      } else {
         this.shouldToBack = false;
      }

      return (LivingEntity)target;
   }

   private void checkTargetTimeout() {
      if (this.target != null) {
         long currentTime = System.currentTimeMillis();
         if (currentTime - this.targetSetTime >= 3000L) {
            this.target = null;
         }
      }

   }

   public void drawTargetHead(LivingEntity entity, float x, float y, float width, float height) {
      if (entity != null) {
         EntityRenderer<? super LivingEntity> rendererManager = IMinecraft.mc.func_175598_ae().func_78713_a(entity);
         this.drawFace(rendererManager.func_110775_a(entity), x, y, 8.0F, 8.0F, 8.0F, 8.0F, width, height, 64.0F, 64.0F, entity);
      }

   }

   public void drawFace(ResourceLocation res, float d, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight, LivingEntity target) {
      GL11.glPushMatrix();
      GL11.glEnable(3042);
      IMinecraft.mc.func_110434_K().func_110577_a(res);
      float hurtPercent = ((float)target.field_70737_aN - (target.field_70737_aN != 0 ? 1.0F : 0.0F)) / 10.0F;
      GL11.glColor4f(1.0F, 1.0F - hurtPercent, 1.0F - hurtPercent, (float)this.animation.getOutput());
      drawScaledCustomSizeModalRect(d, y, u, v, uWidth, vHeight, width, height, tileWidth, tileHeight);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
      GL11.glPopMatrix();
   }

   public static void drawScaledCustomSizeModalRect(float x, float y, float u, float v, float uWidth, float vHeight, float width, float height, float tileWidth, float tileHeight) {
      float f = 1.0F / tileWidth;
      float f1 = 1.0F / tileHeight;
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder bufferbuilder = tessellator.func_178180_c();
      bufferbuilder.func_181668_a(7, DefaultVertexFormats.field_181707_g);
      bufferbuilder.func_225582_a_((double)x, (double)(y + height), 0.0D).func_225583_a_(u * f, (v + vHeight) * f1).func_181675_d();
      bufferbuilder.func_225582_a_((double)(x + width), (double)(y + height), 0.0D).func_225583_a_((u + uWidth) * f, (v + vHeight) * f1).func_181675_d();
      bufferbuilder.func_225582_a_((double)(x + width), (double)y, 0.0D).func_225583_a_((u + uWidth) * f, v * f1).func_181675_d();
      bufferbuilder.func_225582_a_((double)x, (double)y, 0.0D).func_225583_a_(u * f, v * f1).func_181675_d();
      tessellator.func_78381_a();
   }
}
