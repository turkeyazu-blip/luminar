package ru.luminar.feature.functions.impl.render;

import com.mojang.blaze3d.platform.GlStateManager;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import ru.luminar.Luminar;
import ru.luminar.events.impl.render.Render3DEvent;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.utils.animations.Animation;
import ru.luminar.utils.animations.impl.DecelerateAnimation;
import ru.luminar.utils.draw.ColorUtils;

public class JumpCircle extends Function {
   public SliderSetting size = new SliderSetting("Размер", 3.0F, 1.0F, 5.0F, 0.1F);
   public BooleanSetting clientColor = new BooleanSetting("Клиентский цвет", true);
   public ColorSetting color = (new ColorSetting("Цвет", -1)).setVisible(() -> {
      return !(Boolean)this.clientColor.get();
   });
   private final List<JumpCircle.Circle> circles = new CopyOnWriteArrayList();
   private final ResourceLocation circle = new ResourceLocation("luminar/images/circle.png");

   public JumpCircle() {
      super("JumpCircle", Category.Render);
      this.addSettings(new Setting[]{this.size, this.clientColor, this.color});
   }

   @SubscribeEvent
   public void onJump(LivingJumpEvent e) {
      if (e.getEntityLiving() == mc.field_71439_g) {
         this.circles.add(new JumpCircle.Circle(mc.field_71439_g.func_213303_ch().func_72441_c(0.0D, 0.05D, 0.0D)));
      }
   }

   @Subscribe
   private void onRender(Render3DEvent e) {
      GlStateManager.func_227626_N_();
      GlStateManager.func_227762_u_(7425);
      GlStateManager.func_227676_b_(770, 771);
      GlStateManager.func_227667_a_(false);
      GlStateManager.func_227740_m_();
      GlStateManager.func_227700_d_();
      GlStateManager.func_227605_A_();
      GlStateManager.func_227670_b_(-mc.func_175598_ae().field_217783_c.func_216785_c().field_72450_a, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72448_b, -mc.func_175598_ae().field_217783_c.func_216785_c().field_72449_c);
      Iterator var2 = this.circles.iterator();

      while(var2.hasNext()) {
         JumpCircle.Circle c = (JumpCircle.Circle)var2.next();
         ResourceLocation texture = this.circle;
         mc.func_110434_K().func_110577_a(texture);
         long currentTime = System.currentTimeMillis();
         long circleLifetime = currentTime - c.time;
         long totalLifetime = 2250L;
         if (circleLifetime > totalLifetime) {
            this.circles.remove(c);
         } else {
            float rad = (float)c.animation.getOutput();
            float alphaFactor = (float)Math.pow((double)(1.0F - (float)circleLifetime / (float)totalLifetime), 0.8D);
            float combinedAlpha = MathHelper.func_76131_a(rad, 0.0F, 1.0F) * alphaFactor;
            int finalAlpha = (int)(255.0F * combinedAlpha);
            Vector3d vector3d = c.vector3d.func_72441_c((double)(-rad / 2.0F), 0.0D, (double)(-rad / 2.0F));
            buffer.func_181668_a(6, DefaultVertexFormats.field_227851_o_);
            float[] colors = ColorUtils.rgba(ColorUtils.setAlpha((Boolean)this.clientColor.get() ? Luminar.instance.styleManager.getCurrentStyle().getColor(20, 10) : (Integer)this.color.get(), finalAlpha));
            buffer.func_225582_a_(vector3d.field_72450_a, vector3d.field_72448_b, vector3d.field_72449_c).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 0.0F).func_181675_d();
            buffer.func_225582_a_(vector3d.field_72450_a + (double)rad, vector3d.field_72448_b, vector3d.field_72449_c).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 0.0F).func_181675_d();
            buffer.func_225582_a_(vector3d.field_72450_a + (double)rad, vector3d.field_72448_b, vector3d.field_72449_c + (double)rad).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(1.0F, 1.0F).func_181675_d();
            buffer.func_225582_a_(vector3d.field_72450_a, vector3d.field_72448_b, vector3d.field_72449_c + (double)rad).func_227885_a_(colors[0], colors[1], colors[2], colors[3]).func_225583_a_(0.0F, 1.0F).func_181675_d();
            tessellator.func_78381_a();
         }
      }

      GlStateManager.func_227737_l_();
      GlStateManager.func_227762_u_(7424);
      GlStateManager.func_227667_a_(true);
      GlStateManager.func_227709_e_();
      GlStateManager.func_227771_z_();
      GlStateManager.func_227627_O_();
   }

   private class Circle {
      private final Vector3d vector3d;
      private final long time;
      private final Animation animation = new DecelerateAnimation(600, 0.0D);

      public Circle(Vector3d vector3d) {
         this.vector3d = vector3d;
         this.time = System.currentTimeMillis();
         this.animation.setEndPoint((double)(Float)JumpCircle.this.size.get());
      }
   }
}
