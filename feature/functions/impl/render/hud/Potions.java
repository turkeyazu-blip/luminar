package ru.luminar.feature.functions.impl.render.hud;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.renderer.texture.PotionSpriteUploader;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.I18n;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import ru.luminar.ui.GuiScreen;
import ru.luminar.utils.animations.Animation;
import ru.luminar.utils.animations.Direction;
import ru.luminar.utils.animations.impl.DecelerateAnimation;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draggables.Dragging;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.vectors.Vector4i;

public class Potions {
   final Dragging dragging;
   float width;
   float height;
   Animation animation = new DecelerateAnimation(500, 1.0D);

   public Potions(Dragging dragging) {
      this.dragging = dragging;
   }

   public void draw(MatrixStack matrixStack) {
      float posX = this.dragging.getX();
      float posY = this.dragging.getY();
      float fontSize = 7.0F;
      float padding = 5.0F;
      Collection<EffectInstance> list = IMinecraft.mc.field_71439_g.func_70651_bq();
      this.animation.setDirection(list.isEmpty() && !(IMinecraft.mc.field_71462_r instanceof ChatScreen) ? Direction.BACKWARDS : Direction.FORWARDS);
      Vector4i vecD = new Vector4i(DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().brighter().darker().getRGB(), (int)(255.0D * this.animation.getOutput())));
      Vector4i vecW = new Vector4i(DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(247, 247, 247)).getRGB(), (int)(255.0D * this.animation.getOutput())));
      Vector4i vec = ColorUtils.interpolate(vecD, vecW, GuiScreen.whiteAnim);
      DisplayUtils.drawRoundedRect(posX, posY, this.width, this.height, 5.0F, vec);
      int textColor = ColorUtils.interpolate(DisplayUtils.reAlphaInt(-1, (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(106, 106, 106)).getRGB(), (int)(255.0D * this.animation.getOutput())), GuiScreen.whiteAnim);
      Fonts.sfbold.drawText(matrixStack, "Potions", posX + 5.0F, posY + 5.0F, textColor, fontSize);
      posY += fontSize + padding * 2.0F;
      float maxWidth = Fonts.sfbold.getWidth("Potions", fontSize) + padding * 2.0F;
      float localHeight = fontSize + padding * 1.5F;

      for(Iterator var13 = list.iterator(); var13.hasNext(); localHeight += fontSize + padding) {
         EffectInstance ef = (EffectInstance)var13.next();
         int amp = ef.func_76458_c();
         String ampStr = "";
         String var10000;
         if (amp >= 1 && amp <= 9) {
            var10000 = "enchantment.level." + (amp + 1);
            ampStr = " " + I18n.func_135052_a(var10000, new Object[0]);
         }

         var10000 = I18n.func_135052_a(ef.func_188419_a().func_199286_c().getString(), new Object[0]);
         String nameText = var10000 + ampStr;
         float nameWidth = Fonts.sf_medium.getWidth(nameText, fontSize);
         String bindText = EffectUtils.func_188410_a(ef, 1.0F);
         float bindWidth = Fonts.sf_medium.getWidth(bindText, fontSize);
         float localWidth = nameWidth + bindWidth + padding * 4.0F + 9.0F;
         PotionSpriteUploader potionSpriteUploader = IMinecraft.mc.func_213248_ap();
         TextureAtlasSprite textureAtlasSprite = potionSpriteUploader.func_215288_a(ef.func_188419_a());
         if (GuiScreen.whiteTheme) {
            int shadowColor = DisplayUtils.reAlphaInt((new Color(220, 220, 220)).getRGB(), (int)(255.0D * this.animation.getOutput()));
            DisplayUtils.drawRoundedRect(posX + padding - 2.0F, posY - 2.0F, 10.0F, 10.0F, 2.0F, shadowColor);
         }

         IMinecraft.mc.func_110434_K().func_110577_a(textureAtlasSprite.func_229241_m_().func_229223_g_());
         DisplayEffectsScreen.func_238470_a_(matrixStack, (int)(posX + padding - 1.0F), (int)posY - 1, 14, 8, 8, textureAtlasSprite);
         Vector4i vec2D = new Vector4i(DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().darker().darker().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().darker().darker().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().darker().darker().darker().getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt(Color.darkGray.darker().darker().darker().darker().darker().getRGB(), (int)(255.0D * this.animation.getOutput())));
         Vector4i vec2W = new Vector4i(DisplayUtils.reAlphaInt((new Color(237, 238, 235)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(237, 238, 235)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(237, 238, 235)).getRGB(), (int)(255.0D * this.animation.getOutput())), DisplayUtils.reAlphaInt((new Color(237, 238, 235)).getRGB(), (int)(255.0D * this.animation.getOutput())));
         Vector4i vec2 = ColorUtils.interpolate(vec2D, vec2W, GuiScreen.whiteAnim);
         DisplayUtils.drawRoundedRect(posX + this.width - padding - bindWidth - 2.0F, posY - 2.0F, bindWidth + 4.0F, fontSize + 4.0F, 4.0F, vec2);
         Fonts.sf_semibold.drawText(matrixStack, nameText, posX + padding + 9.0F, posY, textColor, fontSize);
         Fonts.sf_semibold.drawText(matrixStack, bindText, posX + 0.25F + this.width - padding - bindWidth, posY, textColor, fontSize);
         if (localWidth > maxWidth) {
            maxWidth = localWidth;
         }

         posY += fontSize + padding;
      }

      this.width = MathUtil.lerp(this.width, Math.max(maxWidth, 80.0F), 20.0F);
      this.height = MathUtil.lerp(this.height, Math.max(localHeight + 2.5F, 10.0F), 20.0F);
      this.dragging.setWidth(this.width);
      this.dragging.setHeight(this.height);
   }
}
