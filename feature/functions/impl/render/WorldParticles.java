package ru.luminar.feature.functions.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.RayTraceContext;
import net.minecraft.util.math.RayTraceContext.BlockMode;
import net.minecraft.util.math.RayTraceContext.FluidMode;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import ru.luminar.Luminar;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.impl.render.Render3DEvent2;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.TimerUtil;

public class WorldParticles extends Function {
   public SliderSetting time = new SliderSetting("Спавнить каждые(сек)", 3.0F, 0.1F, 10.0F, 0.1F);
   public SliderSetting size = new SliderSetting("Количество", 25.0F, 5.0F, 200.0F, 5.0F);
   BooleanSetting svetashke = new BooleanSetting("Свечение", false);
   public ModeSetting mode = new ModeSetting("Мод", "Звезда", new String[]{"Звезда", "Доллар", "Сердечко", "Снежинка", "Корона", "Лого"});
   public BooleanSetting clientColor = new BooleanSetting("Клиентский цвет", true);
   public ColorSetting color = (new ColorSetting("Цвет", -1)).setVisible(() -> {
      return !(Boolean)this.clientColor.get();
   });
   private final CopyOnWriteArrayList<WorldParticles.Particle> particles = new CopyOnWriteArrayList();
   TimerUtil timer = new TimerUtil();

   public WorldParticles() {
      super("WorldParticles", Category.Render);
      this.addSettings(new Setting[]{this.mode, this.svetashke, this.time, this.size, this.clientColor, this.color});
   }

   @Subscribe
   public void on3d(Render3DEvent2 e) {
      if (mc.field_71439_g != null && mc.field_71441_e != null) {
         Iterator var2 = this.particles.iterator();

         while(var2.hasNext()) {
            WorldParticles.Particle p = (WorldParticles.Particle)var2.next();
            if (System.currentTimeMillis() - p.time > 2500L) {
               this.particles.remove(p);
            } else if (mc.field_71439_g.func_213303_ch().func_72438_d(p.pos) > 30.0D) {
               this.particles.remove(p);
            } else if (!this.canSee(p.pos)) {
               this.particles.remove(p);
            } else {
               float lifeProgress = (float)(System.currentTimeMillis() - p.time) / 2500.0F;
               float fadeIn = Math.min(1.0F, lifeProgress * 5.0F);
               float fadeOut = 1.0F - Math.max(0.0F, (lifeProgress - 0.8F) * 5.0F);
               float alphaFactor = fadeIn * fadeOut;
               float size = 0.5F - lifeProgress * 0.4F;
               MatrixStack matrix = e.getMatrixStack();
               boolean light = GL11.glIsEnabled(2896);
               RenderSystem.pushMatrix();
               matrix.func_227860_a_();
               RenderSystem.enableBlend();
               RenderSystem.disableAlphaTest();
               RenderSystem.depthMask(false);
               RenderSystem.disableCull();
               if (light) {
                  RenderSystem.disableLighting();
               }

               GL11.glShadeModel(7425);
               RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
               mc.func_110434_K().func_110577_a(this.texture());
               int color = (Boolean)this.clientColor.get() ? ColorUtils.setAlpha(Luminar.instance.styleManager.getCurrentStyle().getColor(20, 10), (int)(255.0F * alphaFactor)) : ColorUtils.setAlpha((Integer)this.color.get(), (int)(255.0F * alphaFactor));
               Vector3d v = p.pos;
               matrix.func_227860_a_();
               EntityRendererManager rendererManager = mc.func_175598_ae();
               Vector3d renderPos = rendererManager.field_217783_c.func_216785_c();
               matrix.func_227861_a_((double)((float)v.field_72450_a) - renderPos.field_72450_a, (double)((float)v.field_72448_b) - renderPos.field_72448_b, (double)((float)v.field_72449_c) - renderPos.field_72449_c);
               matrix.func_227863_a_(mc.func_175598_ae().func_229098_b_());
               matrix.func_227862_a_(-1.0F, -1.0F, 1.0F);
               GL11.glBlendFunc(770, 1);
               DisplayUtils.drawRect(matrix, -size, -size, size, size, color, color, color, color, true, true);
               if ((Boolean)this.svetashke.get()) {
                  mc.func_110434_K().func_110577_a(new ResourceLocation("luminar/images/glow.png"));
                  int color2 = (Boolean)this.clientColor.get() ? ColorUtils.setAlpha(Luminar.instance.styleManager.getCurrentStyle().getColor(20, 10), (int)(100.0F * alphaFactor)) : ColorUtils.setAlpha((Integer)this.color.get(), (int)(100.0F * alphaFactor));
                  DisplayUtils.drawRect(matrix, -size * 1.5F, -size * 1.5F, size * 1.5F, size * 1.5F, color2, color2, color2, color2, true, true);
               }

               GL11.glBlendFunc(770, 771);
               matrix.func_227865_b_();
               RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
               GlStateManager.func_227628_P_();
               GL11.glShadeModel(7424);
               if (light) {
                  RenderSystem.enableLighting();
               }

               RenderSystem.enableCull();
               RenderSystem.depthMask(true);
               RenderSystem.enableAlphaTest();
               matrix.func_227865_b_();
               RenderSystem.popMatrix();
            }
         }

      }
   }

   private ResourceLocation texture() {
      if (this.mode.is("Звезда")) {
         return new ResourceLocation("luminar/images/star.png");
      } else if (this.mode.is("Доллар")) {
         return new ResourceLocation("luminar/images/dollar.png");
      } else if (this.mode.is("Сердечко")) {
         return new ResourceLocation("luminar/images/heart.png");
      } else if (this.mode.is("Лого")) {
         return new ResourceLocation("luminar/images/luminar.png");
      } else if (this.mode.is("Снежинка")) {
         return new ResourceLocation("luminar/images/snowflake.png");
      } else {
         return this.mode.is("Корона") ? new ResourceLocation("luminar/images/crown.png") : new ResourceLocation("luminar/images/star.png");
      }
   }

   public boolean canSee(Vector3d p_70685_1_) {
      Vector3d vector3d = new Vector3d(mc.field_71439_g.func_226277_ct_(), mc.field_71439_g.func_226280_cw_(), mc.field_71439_g.func_226281_cx_());
      Vector3d vector3d1 = new Vector3d(p_70685_1_.field_72450_a, p_70685_1_.field_72448_b, p_70685_1_.func_82616_c());
      return mc.field_71441_e.func_217299_a(new RayTraceContext(vector3d, vector3d1, BlockMode.COLLIDER, FluidMode.NONE, mc.field_71439_g)).func_216346_c() == Type.MISS;
   }

   @Subscribe
   public void create(EventUpdate e) {
      if (this.timer.passed(((Float)this.time.get()).longValue() * 1000L)) {
         for(int i = 0; (float)i < (Float)this.size.get(); ++i) {
            this.particles.add(new WorldParticles.Particle());
         }

         this.timer.reset();
      }

      Iterator var4 = this.particles.iterator();

      while(var4.hasNext()) {
         WorldParticles.Particle p = (WorldParticles.Particle)var4.next();
         p.update();
      }

   }

   private class Particle {
      private Vector3d pos;
      private final Vector3d end;
      private final long time;
      private float alpha;

      public Particle() {
         this.pos = IMinecraft.mc.field_71439_g.func_213303_ch().func_72441_c((double)(-ThreadLocalRandom.current().nextFloat(-20.0F, 20.0F)), (double)ThreadLocalRandom.current().nextFloat(-5.0F, 20.0F), (double)(-ThreadLocalRandom.current().nextFloat(-20.0F, 20.0F)));
         this.end = this.pos.func_72441_c((double)(-ThreadLocalRandom.current().nextFloat(-3.0F, 3.0F)), (double)(-ThreadLocalRandom.current().nextFloat(-3.0F, 3.0F)), (double)(-ThreadLocalRandom.current().nextFloat(-3.0F, 3.0F)));
         this.time = System.currentTimeMillis();
      }

      public void update() {
         this.alpha = MathUtil.fast(this.alpha, 1.0F, 10.0F);
         this.pos = MathUtil.fast(this.pos, this.end, 0.25F);
      }
   }
}
