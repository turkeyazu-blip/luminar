package ru.luminar.feature.managers.island;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.Minecraft;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.mixins.accessors.BossOverlayGuiAccessor;
import ru.luminar.ui.GuiScreen;
import ru.luminar.utils.animations.Animation;
import ru.luminar.utils.animations.Direction;
import ru.luminar.utils.animations.impl.EaseInOutQuad;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.draw.Scissor;
import ru.luminar.utils.draw.font.Fonts;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.vectors.Vector4i;

public class IslandManager {
   private final CopyOnWriteArrayList<IslandManager.IslandNotification> notifications = new CopyOnWriteArrayList();
   private final Minecraft mc = Minecraft.func_71410_x();
   private IslandManager.IslandNotification currentNotification = null;
   private float currentWidth = 0.0F;
   private float currentX = 0.0F;
   private float currentAlpha = 0.0F;
   private Animation fadeAnimation;
   float y;

   public IslandManager() {
      this.fadeAnimation = new EaseInOutQuad(400, 1.0D, Direction.FORWARDS);
      this.y = 10.0F;
   }

   public void add(String text, int time) {
      if (this.currentNotification != null) {
         this.fadeAnimation.setDirection(Direction.BACKWARDS);
         this.fadeAnimation.reset();
      }

      IslandManager.IslandNotification newNotification = new IslandManager.IslandNotification(text, time);
      this.notifications.add(0, newNotification);
   }

   public void draw(MatrixStack matrixStack) {
      this.updateState();
      SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
      String timeString = sdf.format(new Date());
      SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm");
      String timeWithoutSeconds = sdf2.format(new Date());
      String formattedtime = (Boolean)Island.time.get() ? timeString : timeWithoutSeconds;
      String displayText = this.currentNotification != null ? this.currentNotification.getText() : formattedtime;
      float targetAlpha = this.currentNotification != null ? 1.0F : 0.0F;
      this.currentAlpha = (float)this.fadeAnimation.getOutput();
      float targetWidth = Fonts.sfbold.getWidth(displayText, 8.0F) + 10.0F;
      float targetX = ((float)this.mc.func_228018_at_().func_198107_o() - targetWidth) / 2.0F;
      this.currentWidth = MathUtil.fast(this.currentWidth, targetWidth, 10.0F);
      this.currentX = MathUtil.fast(this.currentX, targetX, 10.0F);
      float renderAlpha = this.currentAlpha;
      float targetY = 10.0F;
      int bossInfo = ((BossOverlayGuiAccessor)Minecraft.func_71410_x().field_71456_v.func_184046_j()).getEvents().size();
      if (((BossOverlayGuiAccessor)Minecraft.func_71410_x().field_71456_v.func_184046_j()).getEvents().size() > 1) {
         bossInfo = (int)((float)bossInfo * ((float)((BossOverlayGuiAccessor)Minecraft.func_71410_x().field_71456_v.func_184046_j()).getEvents().size() / 1.95F));
      }

      if (Luminar.instance.functions.removals.isEnabled() && (Boolean)Luminar.instance.functions.removals.bossbar.get()) {
         bossInfo = 0;
      }

      targetY += (float)(bossInfo * 10);
      this.y = MathUtil.fast(this.y, targetY, 10.0F);
      Vector4i darkVec = new Vector4i(Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB(), Color.darkGray.darker().darker().brighter().darker().getRGB());
      Vector4i whiteVec = new Vector4i((new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB(), (new Color(247, 247, 247)).getRGB());
      Vector4i vec = ColorUtils.interpolate(darkVec, whiteVec, GuiScreen.whiteAnim);
      if (this.currentWidth > 5.0F) {
         DisplayUtils.drawRoundedRect(this.currentX, this.y, this.currentWidth, 15.0F, 6.0F, vec);
         int textColor = ColorUtils.interpolate(ColorUtils.rgba(255, 255, 255, (int)(255.0F * renderAlpha)), ColorUtils.rgba(106, 106, 106, (int)(255.0F * renderAlpha)), GuiScreen.whiteAnim);
         Scissor.push();
         Scissor.setFromComponentCoordinates((double)this.currentX, (double)this.y, (double)this.currentWidth, 15.0D);
         Fonts.sfbold.drawText(matrixStack, displayText, this.currentX + (this.currentWidth - Fonts.sfbold.getWidth(displayText, 8.0F)) / 2.0F, this.y + 4.0F, textColor, 8.0F);
         Scissor.unset();
         Scissor.pop();
      }

   }

   private void updateState() {
      if (this.notifications.isEmpty()) {
         if (this.currentNotification != null) {
            this.currentNotification = null;
            this.fadeAnimation.setDirection(Direction.FORWARDS);
            this.fadeAnimation.reset();
         }

      } else {
         IslandManager.IslandNotification newestNotification = (IslandManager.IslandNotification)this.notifications.get(0);
         if (this.currentNotification == null || !this.currentNotification.equals(newestNotification)) {
            this.currentNotification = newestNotification;
            this.fadeAnimation.setDirection(Direction.FORWARDS);
            this.fadeAnimation.reset();
         }

         this.notifications.removeIf((notification) -> {
            if (notification.equals(this.currentNotification)) {
               return System.currentTimeMillis() - notification.getStartTime() > (long)notification.getDisplayTime() * 1000L;
            } else {
               return true;
            }
         });
      }
   }

   private static class IslandNotification {
      private final String text;
      private final int displayTime;
      private final long startTime;

      public IslandNotification(String text, int time) {
         this.text = text;
         this.displayTime = time;
         this.startTime = System.currentTimeMillis();
      }

      public String getText() {
         return this.text;
      }

      public int getDisplayTime() {
         return this.displayTime;
      }

      public long getStartTime() {
         return this.startTime;
      }
   }
}
