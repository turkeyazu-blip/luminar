package ru.luminar.feature.functions.impl.utils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import java.awt.Color;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SChatPacket;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3i;
import org.lwjgl.opengl.GL11;
import ru.luminar.events.impl.packet.PacketEvent;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.impl.render.Render3DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.command.impl.WaypointCommand;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.RenderUtils;
import ru.luminar.utils.math.MathUtil;

public class FTHelper extends Function {
   BooleanSetting trapZone = new BooleanSetting("Зона трапки", true);
   BooleanSetting dragonTrap = new BooleanSetting("Драконья трапка", false);
   BooleanSetting disorZone = new BooleanSetting("Зона дезорки", true);
   BooleanSetting pilZone = new BooleanSetting("Зона явной пыли", true);
   BooleanSetting auraZone = new BooleanSetting("Зона божьей ауры", true);
   BooleanSetting plastZone = new BooleanSetting("Зона пласта", true);
   public static BooleanSetting ahMe = new BooleanSetting("/ah me", true);
   BooleanSetting autoGPS = new BooleanSetting("Авто гпс", true);
   private final Pattern eventPattern = Pattern.compile("║\\s+\\|{3}\\s+\\[(.+?)\\]\\s+\\|{3}.*?║\\s+Появился на координатах (?:\\[)?(-?\\d+) (-?\\d+) (-?\\d+)(?:\\])?", 32);
   int minuteinsec = 60000;
   int maxminutes;
   private final Map<String, Long> eventTimestamps;
   boolean trap;

   public FTHelper() {
      super("FTHelper", Category.Utils);
      this.maxminutes = this.minuteinsec * 10;
      this.eventTimestamps = new HashMap();
      this.addSettings(new Setting[]{this.trapZone, this.dragonTrap, this.disorZone, this.pilZone, this.auraZone, this.plastZone, this.autoGPS, ahMe});
   }

   @Subscribe
   private void onPacket(PacketEvent e) {
      if ((Boolean)this.autoGPS.get()) {
         IPacket iPacket = e.getPacket();
         if (iPacket instanceof SChatPacket) {
            SChatPacket packet = (SChatPacket)iPacket;
            String chat = packet.func_148915_c().getString();
            this.checkForEvent(chat);
         }
      }

   }

   @Subscribe
   public void onUpdate(EventUpdate e) {
      if ((Boolean)this.autoGPS.get()) {
         long currentTime = System.currentTimeMillis();
         Iterator iterator = this.eventTimestamps.entrySet().iterator();

         while(iterator.hasNext()) {
            Entry<String, Long> entry = (Entry)iterator.next();
            if (currentTime - (Long)entry.getValue() >= (long)this.maxminutes) {
               WaypointCommand.removeGPS((String)entry.getKey());
               iterator.remove();
            }
         }
      }

   }

   private void checkForEvent(String message) {
      Matcher matcher = this.eventPattern.matcher(message);
      if (matcher.find()) {
         String eventName = matcher.group(1).trim();
         int x = Integer.parseInt(matcher.group(2));
         int y = Integer.parseInt(matcher.group(3));
         int z = Integer.parseInt(matcher.group(4));
         if (!WaypointCommand.waypoints.values().contains(new Vector3i(x, y + 10, z))) {
            WaypointCommand.addGPS(eventName, x, y + 10, z);
            this.eventTimestamps.put(eventName, System.currentTimeMillis());
         }
      }

   }

   @Subscribe
   public void onWorld(Render3DEvent e) {
      float radius = 0.0F;
      int color = Color.white.getRGB();
      ItemStack mainHand = mc.field_71439_g.func_184614_ca();
      ItemStack offHand = mc.field_71439_g.func_184592_cb();
      if (this.hasItem(mainHand, offHand, Items.field_151061_bv)) {
         radius = (Boolean)this.disorZone.get() ? 10.0F : 0.0F;
         this.trap = false;
      } else if (this.hasItem(mainHand, offHand, Items.field_151102_aT)) {
         radius = (Boolean)this.pilZone.get() ? 10.0F : 0.0F;
         this.trap = false;
      } else if (this.hasItem(mainHand, offHand, Items.field_204840_eX)) {
         radius = (Boolean)this.auraZone.get() ? 2.0F : 0.0F;
         this.trap = false;
      } else if (this.hasItem(mainHand, offHand, Items.field_234760_kn_)) {
         radius = 2.0F;
         this.trap = true;
      }

      AxisAlignedBB trapBox = null;
      AxisAlignedBB plastBox = null;
      if ((Boolean)this.trapZone.get() && mainHand.func_77973_b() == Items.field_234760_kn_) {
         BlockPos pos = mc.field_71439_g.func_233580_cy_();
         trapBox = new AxisAlignedBB((double)(pos.func_177958_n() - 2), (double)(pos.func_177956_o() - 1), (double)(pos.func_177952_p() - 2), (double)(pos.func_177958_n() + 3), (double)(pos.func_177956_o() + 4), (double)(pos.func_177952_p() + 3));
         if ((Boolean)this.dragonTrap.get()) {
            trapBox = new AxisAlignedBB((double)(pos.func_177958_n() - 4), (double)pos.func_177956_o(), (double)(pos.func_177952_p() - 4), (double)(pos.func_177958_n() + 4), (double)(pos.func_177956_o() + 6), (double)(pos.func_177952_p() + 4));
         }
      }

      float yaw;
      float pitch;
      boolean lookingUp;
      if ((Boolean)this.plastZone.get() && mainHand.func_77973_b() == Items.field_203180_bP) {
         float yaw = mc.field_71439_g.field_70177_z;
         yaw = mc.field_71439_g.field_70125_A;
         pitch = (float)Math.round(yaw / 45.0F) * 45.0F;
         boolean isCardinalDirection = true;
         if (isCardinalDirection) {
            float cardinalYaw = (float)Math.round(yaw / 90.0F) * 90.0F;
            BlockPos pos = mc.field_71439_g.func_233580_cy_();

            int direction;
            for(direction = (int)(cardinalYaw / 90.0F); direction < 0; direction += 4) {
            }

            direction %= 4;
            double x1 = (double)pos.func_177958_n();
            double y1 = (double)pos.func_177956_o();
            double z1 = (double)pos.func_177952_p();
            double x2 = (double)pos.func_177958_n();
            double y2 = (double)pos.func_177956_o();
            double z2 = (double)pos.func_177952_p();
            double height = 3.0D;
            boolean lookingUp = yaw < -45.0F;
            lookingUp = yaw > 45.0F;
            if (!lookingUp && !lookingUp) {
               isCardinalDirection = pitch % 90.0F == 0.0F;
               if (isCardinalDirection) {
                  y1 = (double)(pos.func_177956_o() - 1);
                  y2 = (double)(pos.func_177956_o() + 4);
                  switch(direction) {
                  case 0:
                     x1 -= 2.0D;
                     z1 += 4.0D;
                     x2 += 3.0D;
                     z2 += 2.0D;
                     break;
                  case 1:
                     x1 -= 3.0D;
                     z1 -= 2.0D;
                     --x2;
                     z2 += 3.0D;
                     break;
                  case 2:
                     x1 -= 3.0D;
                     z1 -= 2.0D;
                     x2 += 3.0D;
                     z2 -= 4.0D;
                     break;
                  case 3:
                     x1 += 2.0D;
                     z1 -= 2.0D;
                     x2 += 4.0D;
                     z2 += 3.0D;
                  }
               }
            } else {
               y1 = lookingUp ? (double)pos.func_177956_o() - height - 1.0D : (double)(pos.func_177956_o() + 3);
               y2 = lookingUp ? (double)(pos.func_177956_o() - 1) : (double)pos.func_177956_o() + height + 2.0D;
               x1 -= 2.0D;
               z1 -= 2.0D;
               x2 += 3.0D;
               z2 += 3.0D;
            }

            plastBox = new AxisAlignedBB(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2));
         }
      }

      boolean hasPlayerInRadius = false;
      Iterator var34 = mc.field_71441_e.func_217416_b().iterator();

      while(var34.hasNext()) {
         Entity entity = (Entity)var34.next();
         if (entity instanceof PlayerEntity && entity != mc.field_71439_g) {
            if (trapBox != null && trapBox.func_197744_e(entity.func_226277_ct_(), entity.func_226278_cu_(), entity.func_226281_cx_())) {
               hasPlayerInRadius = true;
               break;
            }

            if (plastBox != null && plastBox.func_197744_e(entity.func_226277_ct_(), entity.func_226278_cu_(), entity.func_226281_cx_())) {
               hasPlayerInRadius = true;
               break;
            }

            if (radius > 0.0F && !this.trap && mc.field_71439_g.func_70032_d(entity) < radius) {
               hasPlayerInRadius = true;
               break;
            }
         }
      }

      color = hasPlayerInRadius ? Color.green.getRGB() : Color.white.getRGB();
      if ((Boolean)this.trapZone.get() && mainHand.func_77973_b() == Items.field_234760_kn_) {
         BlockPos pos = mc.field_71439_g.func_233580_cy_();
         AxisAlignedBB axis = new AxisAlignedBB((double)(pos.func_177958_n() - 2), (double)pos.func_177956_o(), (double)(pos.func_177952_p() - 2), (double)(pos.func_177958_n() + 3), (double)(pos.func_177956_o() + 4), (double)(pos.func_177952_p() + 3));
         if ((Boolean)this.dragonTrap.get()) {
            axis = new AxisAlignedBB((double)(pos.func_177958_n() - 4), (double)pos.func_177956_o(), (double)(pos.func_177952_p() - 4), (double)(pos.func_177958_n() + 4), (double)(pos.func_177956_o() + 6), (double)(pos.func_177952_p() + 4));
         }

         RenderUtils.drawBBCorrect(axis, color);
      }

      if ((Boolean)this.plastZone.get() && mainHand.func_77973_b() == Items.field_203180_bP) {
         yaw = mc.field_71439_g.field_70177_z;
         pitch = mc.field_71439_g.field_70125_A;
         float roundedYaw = (float)Math.round(yaw / 45.0F) * 45.0F;
         boolean isCardinalDirection = true;
         if (isCardinalDirection) {
            float cardinalYaw = (float)Math.round(yaw / 90.0F) * 90.0F;
            BlockPos pos = mc.field_71439_g.func_233580_cy_();

            int direction;
            for(direction = (int)(cardinalYaw / 90.0F); direction < 0; direction += 4) {
            }

            direction %= 4;
            double x1 = (double)pos.func_177958_n();
            double y1 = (double)pos.func_177956_o();
            double z1 = (double)pos.func_177952_p();
            double x2 = (double)pos.func_177958_n();
            double y2 = (double)pos.func_177956_o();
            double z2 = (double)pos.func_177952_p();
            double height = 3.0D;
            lookingUp = pitch < -45.0F;
            boolean lookingDown = pitch > 45.0F;
            if (!lookingUp && !lookingDown) {
               isCardinalDirection = roundedYaw % 90.0F == 0.0F;
               if (isCardinalDirection) {
                  y1 = (double)(pos.func_177956_o() - 1);
                  y2 = (double)(pos.func_177956_o() + 4);
                  switch(direction) {
                  case 0:
                     x1 -= 2.0D;
                     z1 += 4.0D;
                     x2 += 3.0D;
                     z2 += 2.0D;
                     break;
                  case 1:
                     x1 -= 3.0D;
                     z1 -= 2.0D;
                     --x2;
                     z2 += 3.0D;
                     break;
                  case 2:
                     x1 -= 3.0D;
                     z1 -= 2.0D;
                     x2 += 3.0D;
                     z2 -= 4.0D;
                     break;
                  case 3:
                     x1 += 2.0D;
                     z1 -= 2.0D;
                     x2 += 4.0D;
                     z2 += 3.0D;
                  }
               }
            } else {
               y1 = lookingDown ? (double)pos.func_177956_o() - height - 1.0D : (double)(pos.func_177956_o() + 3);
               y2 = lookingDown ? (double)(pos.func_177956_o() - 1) : (double)pos.func_177956_o() + height + 2.0D;
               x1 -= 2.0D;
               z1 -= 2.0D;
               x2 += 3.0D;
               z2 += 3.0D;
            }

            RenderUtils.drawBBCorrect(new AxisAlignedBB(Math.min(x1, x2), Math.min(y1, y2), Math.min(z1, z2), Math.max(x1, x2), Math.max(y1, y2), Math.max(z1, z2)), color);
         }
      }

      if (radius > 0.0F && !this.trap) {
         Vector3d playerPos = MathUtil.interpolate(mc.field_71439_g.func_213303_ch(), new Vector3d(mc.field_71439_g.field_70142_S, mc.field_71439_g.field_70137_T, mc.field_71439_g.field_70136_U), e.getPartialTicks());
         this.renderCircle(playerPos, radius, color);
      }

   }

   private boolean hasItem(ItemStack mainHand, ItemStack offHand, Item item) {
      return !mainHand.func_190926_b() && mainHand.func_77973_b() == item || !offHand.func_190926_b() && offHand.func_77973_b() == item;
   }

   private void renderCircle(Vector3d centerPos, float radius, int fillColor) {
      GlStateManager.func_227626_N_();
      RenderSystem.translated(-mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c);
      Vector3d adjustedPos = centerPos.func_72441_c(0.0D, -1.4D, 0.0D);
      RenderSystem.translated(adjustedPos.field_72450_a, adjustedPos.field_72448_b + (double)mc.field_71439_g.func_213302_cg(), adjustedPos.field_72449_c);
      GL11.glRotatef(-mc.func_175598_ae().field_217783_c.func_216778_f(), 0.0F, 1.0F, 0.0F);
      RenderSystem.translated(-adjustedPos.field_72450_a, -(adjustedPos.field_72448_b + (double)mc.field_71439_g.func_213302_cg()), -adjustedPos.field_72449_c);
      RenderSystem.enableBlend();
      RenderSystem.depthMask(false);
      RenderSystem.disableTexture();
      RenderSystem.disableCull();
      RenderSystem.blendFunc(770, 771);
      RenderSystem.shadeModel(7425);
      RenderSystem.lineWidth(3.0F);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      buffer.func_181668_a(2, DefaultVertexFormats.field_181706_f);
      float[] colors = ColorUtils.rgba(fillColor);

      for(int i = 0; i <= 360; ++i) {
         float x = (float)(adjustedPos.field_72450_a + (double)(MathHelper.func_76126_a((float)Math.toRadians((double)i)) * radius));
         float z = (float)(adjustedPos.field_72449_c + (double)(-MathHelper.func_76134_b((float)Math.toRadians((double)i)) * radius));
         buffer.func_225582_a_((double)x, adjustedPos.field_72448_b + (double)mc.field_71439_g.func_213302_cg(), (double)z).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_181675_d();
      }

      tessellator.func_78381_a();
      GL11.glHint(3154, 4352);
      GL11.glDisable(2848);
      RenderSystem.enableTexture();
      RenderSystem.disableBlend();
      RenderSystem.enableCull();
      RenderSystem.depthMask(true);
      RenderSystem.shadeModel(7424);
      GlStateManager.func_227627_O_();
   }
}
