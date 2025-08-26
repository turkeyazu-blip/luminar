package ru.luminar.feature.functions.impl.utils;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Iterator;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import org.lwjgl.opengl.GL11;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.impl.render.Render3DEvent2;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.impl.utils.globalsapi.ClientAPI;
import ru.luminar.feature.functions.impl.utils.globalsapi.ServerAPI;
import ru.luminar.mixins.accessors.FrustumOtdai;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.math.TimerUtil;

public class Globals extends Function {
   TimerUtil timer = new TimerUtil();
   boolean init;

   public Globals() {
      super("Globals", Category.Utils);
   }

   @Subscribe
   public void drawTag(Render3DEvent2 e) {
      Iterator var2 = mc.field_71441_e.func_217416_b().iterator();

      while(true) {
         Entity ent;
         do {
            if (!var2.hasNext()) {
               return;
            }

            ent = (Entity)var2.next();
         } while(!(ent instanceof PlayerEntity));

         Vector3d interpolated = ent.func_242282_l(mc.func_184121_ak());
         float size = 0.25F;
         Iterator var6 = (new ArrayList(ClientAPI.USERS.keySet())).iterator();

         while(var6.hasNext()) {
            String user = (String)var6.next();
            if (ent.func_145748_c_().getString().contains(user)) {
               if (ent.func_82150_aj()) {
                  return;
               }

               if (ent == mc.field_71439_g) {
                  return;
               }

               if (((FrustumOtdai)mc.field_71438_f).getCapturedFrustum() != null && !((FrustumOtdai)mc.field_71438_f).getCapturedFrustum().func_228957_a_(new AxisAlignedBB(interpolated, new Vector3d(ent.func_226277_ct_(), ent.func_226278_cu_() + 3.0D, ent.func_226281_cx_())))) {
                  return;
               }

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
               mc.func_110434_K().func_110577_a(new ResourceLocation("luminar/images/luminar.png"));
               Vector3d v = new Vector3d(interpolated.field_72450_a, interpolated.field_72448_b + 2.5D, interpolated.field_72449_c);
               matrix.func_227860_a_();
               EntityRendererManager rendererManager = mc.func_175598_ae();
               Vector3d renderPos = rendererManager.field_217783_c.func_216785_c();
               float i = ent.func_213283_Z() == Pose.CROUCHING ? -0.5F : 0.0F;
               matrix.func_227861_a_((double)((float)v.field_72450_a) - renderPos.field_72450_a, (double)((float)v.field_72448_b) - renderPos.field_72448_b + 0.5D + (double)i, (double)((float)v.field_72449_c) - renderPos.field_72449_c);
               matrix.func_227863_a_(mc.func_175598_ae().func_229098_b_());
               matrix.func_227862_a_(-1.0F, -1.0F, 1.0F);
               GL11.glBlendFunc(770, 1);
               DisplayUtils.drawRect(matrix, -size, -size, size, size, -1, -1, -1, -1, true, true);
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

   @Subscribe
   public void onUpdate(EventUpdate e) {
      if (this.timer.passed(20000L) && this.init) {
         (new Thread(() -> {
            ClientAPI.update(ServerAPI.getClients());
         })).start();
         this.timer.reset();
      }

      if (!this.init) {
         ServerAPI.init();
         ServerAPI.updateName();
         ClientAPI.update(ServerAPI.getClients());
         this.init = true;
      }

   }

   public void onDisable() {
      super.onDisable();
      this.init = false;
      ServerAPI.finish();
   }
}
