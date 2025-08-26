package ru.luminar.feature.functions.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import ru.luminar.Luminar;
import ru.luminar.events.Event;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.impl.render.Render2DEvent;
import ru.luminar.events.impl.render.Render3DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.utils.animations.Animation;
import ru.luminar.utils.animations.Direction;
import ru.luminar.utils.animations.impl.DecelerateAnimation;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.ProjectionUtil;
import ru.luminar.utils.math.vectors.Vector3dCustom;
import ru.luminar.utils.math.vectors.Vector4i;
import ru.luminar.utils.themes.ThemeManager;

public class TargetESP extends Function {
   ModeSetting mode = new ModeSetting("Мод", "Кругляш", new String[]{"Кругляш", "Квадрат", "Призраки"});
   BooleanSetting wall = new BooleanSetting("Отображать через стены", false);
   BooleanSetting glistMod = new BooleanSetting("Глист мод", false);
   BooleanSetting idkFgPaster = new BooleanSetting("Мне было скучно, я сделал это", false);
   ModeSetting mod = (new ModeSetting("Моды движения", "Обычный", new String[]{"Обычный", "1", "2"})).setVisible(() -> {
      return this.mode.is("Кругляш");
   });
   SliderSetting size = new SliderSetting("Размер", 0.5F, 0.3F, 0.5F, 0.01F);
   LivingEntity target;
   private LivingEntity latestTarget;
   private long targetSetTime;
   private final long TARGET_TIMEOUT = 3000L;
   public static long startTime = System.currentTimeMillis();
   Animation alphaAnim = new DecelerateAnimation(750, 255.0D);
   Animation sizeAnim = new DecelerateAnimation(500, 1.0D);
   private boolean wasAnimating = false;

   public TargetESP() {
      super("TargetESP", Category.Render);
      this.addSettings(new Setting[]{this.mode, this.idkFgPaster, this.mod, this.size});
   }

   public double getScale(Vector3d position, double size) {
      Vector3d cam = mc.func_175598_ae().field_217783_c.func_216785_c();
      double distance = cam.func_72438_d(position);
      double fov = mc.field_71474_y.field_74334_X > 100.0D ? 100.0D : mc.field_71474_y.field_74334_X;
      return Math.max(10.0D, 500.0D / distance) * (size / 30.0D) / (fov == 70.0D ? 1.0D : fov / 70.0D);
   }

   private boolean targetIsInWorld(LivingEntity target) {
      return target != null && target.func_70089_S() && target.field_70170_p == mc.field_71441_e;
   }

   @Subscribe
   public void onEvent(Event event) {
      if (event instanceof EventUpdate) {
         this.getTarget();
         this.checkTargetTimeout();
         if (this.target != null) {
            this.alphaAnim.setDirection(Direction.FORWARDS);
            this.sizeAnim.setDirection(Direction.FORWARDS);
            this.wasAnimating = true;
         } else if (this.latestTarget != null) {
            this.alphaAnim.setDirection(Direction.BACKWARDS);
            this.sizeAnim.setDirection(Direction.BACKWARDS);
            this.wasAnimating = true;
         } else if (this.wasAnimating) {
            this.alphaAnim.setDirection(Direction.BACKWARDS);
            this.sizeAnim.setDirection(Direction.BACKWARDS);
            if (this.alphaAnim.finished(Direction.BACKWARDS)) {
               this.wasAnimating = false;
            }
         }

         if (this.latestTarget != null && this.alphaAnim.finished(Direction.BACKWARDS) && this.alphaAnim.getDirection() == Direction.BACKWARDS) {
            this.latestTarget = null;
         }
      }

      LivingEntity renderTarget;
      double x;
      if (event instanceof Render2DEvent) {
         Render2DEvent e = (Render2DEvent)event;
         renderTarget = this.target != null ? this.target : this.latestTarget;
         if (renderTarget != null && this.alphaAnim.getOutput() > 0.01D) {
            if (renderTarget.func_82150_aj() && renderTarget.func_184582_a(EquipmentSlotType.HEAD).func_190926_b() && renderTarget.func_184582_a(EquipmentSlotType.CHEST).func_190926_b() && renderTarget.func_184582_a(EquipmentSlotType.FEET).func_190926_b() && renderTarget.func_184582_a(EquipmentSlotType.LEGS).func_190926_b()) {
               return;
            }

            if (this.mode.is("Квадрат")) {
               Vector3d interpolated = renderTarget.func_242282_l(e.getPartialTicks());
               x = Math.sin((double)System.currentTimeMillis() / 2500.0D);
               float size = (float)this.getScale(interpolated, 12.0D);
               Vector2f pos = ProjectionUtil.project(interpolated.field_72450_a, interpolated.field_72448_b + (double)(renderTarget.func_213302_cg() / 2.0F), interpolated.field_72449_c);
               ThemeManager themeManager = Luminar.instance.styleManager;
               GlStateManager.func_227626_N_();
               GlStateManager.func_227688_c_(pos.field_189982_i, pos.field_189983_j, 0.0F);
               GlStateManager.func_227689_c_((float)x * 540.0F, 0.0F, 0.0F, 1.0F);
               GlStateManager.func_227688_c_(-pos.field_189982_i, -pos.field_189983_j, 0.0F);
               DisplayUtils.drawImage(new ResourceLocation("luminar/images/marker.png"), pos.field_189982_i - size / 2.0F, pos.field_189983_j - size / 2.0F, size, size, new Vector4i(ColorUtils.setAlpha(ColorUtils.gradient(themeManager.getCurrentStyle().getFirstColor().getRGB(), themeManager.getCurrentStyle().getSecondColor().getRGB(), 0, 10), (int)(this.alphaAnim.getOutput() / 1.5D)), ColorUtils.setAlpha(ColorUtils.gradient(themeManager.getCurrentStyle().getFirstColor().getRGB(), themeManager.getCurrentStyle().getSecondColor().getRGB(), 90, 10), (int)(this.alphaAnim.getOutput() / 1.5D)), ColorUtils.setAlpha(ColorUtils.gradient(themeManager.getCurrentStyle().getFirstColor().getRGB(), themeManager.getCurrentStyle().getSecondColor().getRGB(), 180, 10), (int)(this.alphaAnim.getOutput() / 1.5D)), ColorUtils.setAlpha(ColorUtils.gradient(themeManager.getCurrentStyle().getFirstColor().getRGB(), themeManager.getCurrentStyle().getSecondColor().getRGB(), 270, 10), (int)(this.alphaAnim.getOutput() / 1.5D))));
               GlStateManager.func_227627_O_();
            }
         }
      }

      if (event instanceof Render3DEvent) {
         Render3DEvent e = (Render3DEvent)event;
         renderTarget = this.target != null ? this.target : this.latestTarget;
         if (renderTarget != null && this.alphaAnim.getOutput() > 0.01D) {
            if (renderTarget.func_82150_aj() && renderTarget.func_184582_a(EquipmentSlotType.HEAD).func_190926_b() && renderTarget.func_184582_a(EquipmentSlotType.CHEST).func_190926_b() && renderTarget.func_184582_a(EquipmentSlotType.FEET).func_190926_b() && renderTarget.func_184582_a(EquipmentSlotType.LEGS).func_190926_b()) {
               return;
            }

            MatrixStack ms = new MatrixStack();
            double radius;
            float speed;
            float size;
            double distance;
            int i;
            Quaternion r;
            double angle;
            double s;
            double c;
            double y;
            double z;
            if (this.mode.is("Кругляш")) {
               ms.func_227860_a_();
               RenderSystem.pushMatrix();
               RenderSystem.disableLighting();
               RenderSystem.depthMask(false);
               if ((Boolean)this.wall.get()) {
                  RenderSystem.disableDepthTest();
               }

               RenderSystem.enableBlend();
               RenderSystem.shadeModel(7425);
               RenderSystem.disableCull();
               RenderSystem.disableAlphaTest();
               if (!(Boolean)this.idkFgPaster.get()) {
                  RenderSystem.blendFuncSeparate(770, 1, 0, 1);
               }

               x = renderTarget.func_226277_ct_();
               y = renderTarget.func_226278_cu_() + (double)(renderTarget.func_213302_cg() / 2.0F);
               z = renderTarget.func_226281_cx_();
               radius = 0.699999988079071D;
               speed = 30.0F;
               size = (Float)this.size.get();
               distance = 25.0D;
               int length = 7;
               ActiveRenderInfo camera = mc.func_175598_ae().field_217783_c;
               ms.func_227861_a_(-mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c);
               Vector3dCustom interpolated = new Vector3dCustom(MathUtil.interpolate(renderTarget.func_213303_ch(), new Vector3d(renderTarget.field_70142_S, renderTarget.field_70137_T, renderTarget.field_70136_U), e.getPartialTicks()));
               interpolated.y += 0.75D;
               float rotationTime = (float)(System.currentTimeMillis() - startTime) / 1250.0F;
               float currentRotation = (float)Math.sin((double)rotationTime * 3.141592653589793D) * 3.1415927F / 4.0F;
               ms.func_227861_a_(interpolated.x + 0.20000000298023224D, interpolated.y + 0.5D, interpolated.z);
               mc.func_110434_K().func_110577_a(new ResourceLocation("luminar/images/glow.png"));

               double rotatedY;
               double rotatedZ;
               double verticalFactor;
               int alpha;
               double verticalOffset;
               int color;
               float[] rgba;
               for(i = 0; i < length; ++i) {
                  r = camera.func_227995_f_().func_227068_g_();
                  angle = 0.15000000596046448D * ((double)(System.currentTimeMillis() - startTime) - (double)i * distance) / (double)speed;
                  s = (double)MathHelper.func_76126_a((float)angle) * radius;
                  c = (double)MathHelper.func_76134_b((float)angle) * radius;
                  rotatedY = c * Math.sin((double)currentRotation);
                  rotatedZ = c * Math.cos((double)currentRotation);
                  if (this.mod.is("1")) {
                     rotatedY = c * Math.sin((double)currentRotation) + Math.sin(angle * 2.0D) * 0.2D;
                  } else if (this.mod.is("2")) {
                     verticalFactor = 1.0D - Math.abs(Math.sin(angle));
                     verticalOffset = Math.sin((double)(System.currentTimeMillis() - startTime) / 1000.0D) * 0.3D * (1.0D - verticalFactor);
                     rotatedY = c * Math.sin((double)currentRotation) + verticalOffset;
                  }

                  ms.func_227861_a_(s, rotatedY, rotatedZ);
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  color = ColorUtils.getColor(i);
                  alpha = (int)this.alphaAnim.getOutput();
                  rgba = ColorUtils.rgba(DisplayUtils.reAlphaInt(color, alpha));
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  r.func_195892_e();
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  ms.func_227861_a_(-s, -rotatedY, -rotatedZ);
               }

               for(i = 0; i < length; ++i) {
                  r = camera.func_227995_f_().func_227068_g_();
                  angle = 0.15000000596046448D * ((double)(System.currentTimeMillis() - startTime) - (double)i * distance) / (double)speed + 2.0943951023931953D;
                  s = (double)MathHelper.func_76126_a((float)angle) * radius;
                  c = (double)MathHelper.func_76134_b((float)angle) * radius;
                  rotatedY = c * Math.sin((double)currentRotation);
                  rotatedZ = c * Math.cos((double)currentRotation);
                  if (this.mod.is("1")) {
                     rotatedY = c * Math.sin((double)currentRotation) + Math.sin(angle * 2.0D) * 0.2D;
                  } else if (this.mod.is("2")) {
                     verticalFactor = 1.0D - Math.abs(Math.sin(angle));
                     verticalOffset = Math.sin((double)(System.currentTimeMillis() - startTime) / 1000.0D) * 0.3D * (1.0D - verticalFactor);
                     rotatedY = c * Math.sin((double)currentRotation) + verticalOffset;
                  }

                  ms.func_227861_a_(s, rotatedY, rotatedZ);
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  color = ColorUtils.getColor(i);
                  alpha = (int)this.alphaAnim.getOutput();
                  rgba = ColorUtils.rgba(DisplayUtils.reAlphaInt(color, alpha));
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  r.func_195892_e();
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  ms.func_227861_a_(-s, -rotatedY, -rotatedZ);
               }

               for(i = 0; i < length; ++i) {
                  r = camera.func_227995_f_().func_227068_g_();
                  angle = 0.15000000596046448D * ((double)(System.currentTimeMillis() - startTime) - (double)i * distance) / (double)speed + 4.1887902047863905D;
                  s = (double)MathHelper.func_76126_a((float)angle) * radius;
                  c = (double)MathHelper.func_76134_b((float)angle) * radius;
                  rotatedY = c * Math.sin((double)currentRotation);
                  rotatedZ = c * Math.cos((double)currentRotation);
                  if (this.mod.is("1")) {
                     rotatedY = c * Math.sin((double)currentRotation) + Math.sin(angle * 2.0D) * 0.2D;
                  } else if (this.mod.is("2")) {
                     verticalFactor = 1.0D - Math.abs(Math.sin(angle));
                     verticalOffset = Math.sin((double)(System.currentTimeMillis() - startTime) / 1000.0D) * 0.3D * (1.0D - verticalFactor);
                     rotatedY = c * Math.sin((double)currentRotation) + verticalOffset;
                  }

                  ms.func_227861_a_(s, rotatedY, rotatedZ);
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  color = ColorUtils.getColor(i);
                  alpha = (int)this.alphaAnim.getOutput();
                  rgba = ColorUtils.rgba(DisplayUtils.reAlphaInt(color, alpha));
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(rgba[0], rgba[1], rgba[2], rgba[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  r.func_195892_e();
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  ms.func_227861_a_(-s, -rotatedY, -rotatedZ);
               }

               ms.func_227861_a_(-x, -y, -z);
               if (!(Boolean)this.idkFgPaster.get()) {
                  RenderSystem.defaultBlendFunc();
               }

               RenderSystem.disableBlend();
               RenderSystem.enableCull();
               RenderSystem.enableAlphaTest();
               if ((Boolean)this.wall.get()) {
                  RenderSystem.enableDepthTest();
               }

               RenderSystem.depthMask(true);
               RenderSystem.popMatrix();
               ms.func_227865_b_();
            }

            if (this.mode.is("Призраки")) {
               ms.func_227860_a_();
               RenderSystem.pushMatrix();
               RenderSystem.disableLighting();
               RenderSystem.depthMask(false);
               RenderSystem.enableBlend();
               RenderSystem.shadeModel(7425);
               RenderSystem.disableCull();
               RenderSystem.disableAlphaTest();
               if (!(Boolean)this.idkFgPaster.get()) {
                  RenderSystem.blendFuncSeparate(770, 1, 0, 1);
               }

               x = renderTarget.func_226277_ct_();
               y = renderTarget.func_226278_cu_() + (double)(renderTarget.func_213302_cg() / 2.0F);
               z = renderTarget.func_226281_cx_();
               radius = 1.100000023841858D;
               speed = 30.0F;
               size = (Float)this.size.get();
               distance = 20.0D;
               int length = (int)(30.0F * (float)this.sizeAnim.getOutput());
               int maxAlpha = (int)this.alphaAnim.getOutput();
               int alphaFactor = 22;
               ActiveRenderInfo camera = mc.func_175598_ae().field_217783_c;
               ms.func_227861_a_(-mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c);
               Vector3dCustom interpolated = new Vector3dCustom(MathUtil.interpolate(renderTarget.func_213303_ch(), new Vector3d(renderTarget.field_70142_S, renderTarget.field_70137_T, renderTarget.field_70136_U), e.getPartialTicks()));
               interpolated.y += 0.75D;
               ms.func_227861_a_(interpolated.x + 0.20000000298023224D, interpolated.y + 0.5D, interpolated.z);
               mc.func_110434_K().func_110577_a(new ResourceLocation("luminar/images/glow.png"));

               int color;
               int alpha;
               float[] colors;
               for(i = 0; i < length; ++i) {
                  r = camera.func_227995_f_().func_227068_g_();
                  angle = 0.15000000596046448D * ((double)(System.currentTimeMillis() - startTime) - (double)i * distance) / (double)speed;
                  s = (double)MathHelper.func_76126_a((float)angle) * radius * 0.6D;
                  c = (double)MathHelper.func_76134_b((float)angle) * radius * 0.5D;
                  ms.func_227861_a_(s, c, -c);
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  color = ColorUtils.getColor(i);
                  alpha = MathHelper.func_76125_a(maxAlpha - i * alphaFactor, 0, maxAlpha);
                  colors = ColorUtils.rgba(DisplayUtils.reAlphaInt(color, alpha));
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  r.func_195892_e();
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  ms.func_227861_a_(-s, -c, c);
               }

               for(i = 0; i < length; ++i) {
                  r = camera.func_227995_f_().func_227068_g_();
                  angle = 0.15000000596046448D * ((double)(System.currentTimeMillis() - startTime) - (double)i * distance) / (double)speed;
                  s = (double)MathHelper.func_76126_a((float)angle) * radius * 0.6D;
                  c = (double)MathHelper.func_76134_b((float)angle) * radius * 0.5D;
                  ms.func_227861_a_(-s, -s, c);
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  color = ColorUtils.getColor(i);
                  alpha = MathHelper.func_76125_a(maxAlpha - i * alphaFactor, 0, maxAlpha);
                  colors = ColorUtils.rgba(DisplayUtils.reAlphaInt(color, alpha));
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  r.func_195892_e();
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  ms.func_227861_a_(s, s, -c);
               }

               for(i = 0; i < length; ++i) {
                  r = camera.func_227995_f_().func_227068_g_();
                  angle = 0.15000000596046448D * ((double)(System.currentTimeMillis() - startTime) - (double)i * distance) / (double)speed;
                  s = (double)MathHelper.func_76126_a((float)angle) * radius * 0.6D;
                  c = (double)MathHelper.func_76134_b((float)angle) * radius * 0.5D;
                  ms.func_227861_a_(-s, -c, -c);
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  color = ColorUtils.getColor(i);
                  alpha = MathHelper.func_76125_a(maxAlpha - i * alphaFactor, 0, maxAlpha);
                  colors = ColorUtils.rgba(DisplayUtils.reAlphaInt(color, alpha));
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  buffer.func_181668_a(7, DefaultVertexFormats.field_227851_o_);
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, -size, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), -size, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
                  buffer.func_227888_a_(ms.func_227866_c_().func_227870_a_(), 0.0F, 0.0F, 0.0F).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
                  tessellator.func_78381_a();
                  ms.func_227861_a_((double)(-size / 2.0F), (double)(-size / 2.0F), 0.0D);
                  r.func_195892_e();
                  ms.func_227863_a_(r);
                  ms.func_227861_a_((double)(size / 2.0F), (double)(size / 2.0F), 0.0D);
                  ms.func_227861_a_(s, c, c);
               }

               ms.func_227861_a_(-x, -y, -z);
               if (!(Boolean)this.idkFgPaster.get()) {
                  RenderSystem.defaultBlendFunc();
               }

               RenderSystem.disableBlend();
               RenderSystem.enableCull();
               RenderSystem.enableAlphaTest();
               RenderSystem.depthMask(true);
               RenderSystem.popMatrix();
               ms.func_227865_b_();
            }
         }
      }

   }

   private void getTarget() {
      if (mc.field_71476_x != null && mc.field_71476_x.func_216346_c() == Type.ENTITY) {
         Entity entity = ((EntityRayTraceResult)mc.field_71476_x).func_216348_a();
         if (entity instanceof PlayerEntity) {
            LivingEntity newTarget = (LivingEntity)entity;
            if (this.target != null && this.target.equals(newTarget)) {
               this.targetSetTime = System.currentTimeMillis();
            } else {
               if (this.target != null) {
                  this.latestTarget = this.target;
               }

               this.target = newTarget;
               this.targetSetTime = System.currentTimeMillis();
            }
         }
      }

   }

   private void checkTargetTimeout() {
      if (this.target != null) {
         if (!this.targetIsInWorld(this.target)) {
            this.latestTarget = this.target;
            this.target = null;
            this.alphaAnim.setDirection(Direction.BACKWARDS);
            this.sizeAnim.setDirection(Direction.BACKWARDS);
            this.wasAnimating = true;
            return;
         }

         long currentTime = System.currentTimeMillis();
         if (currentTime - this.targetSetTime >= 3000L) {
            this.latestTarget = this.target;
            this.target = null;
         }
      }

   }
}
