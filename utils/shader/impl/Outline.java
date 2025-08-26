package ru.luminar.utils.shader.impl;

import com.google.common.collect.Queues;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.IRenderCall;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL30;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.shader.ShaderUtil;

public class Outline implements IMinecraft {
   private static final ConcurrentLinkedQueue<IRenderCall> renderQueue = Queues.newConcurrentLinkedQueue();
   private static final Framebuffer inFrameBuffer = new Framebuffer(1, 1, true, false);
   private static final Framebuffer outFrameBuffer = new Framebuffer(1, 1, true, false);

   public static void registerRenderCall(IRenderCall rc) {
      renderQueue.add(rc);
   }

   public static void draw(int radius, int color) {
      if (!renderQueue.isEmpty()) {
         setupBuffer(inFrameBuffer);
         setupBuffer(outFrameBuffer);
         inFrameBuffer.func_147610_a(true);

         while(!renderQueue.isEmpty()) {
            ((IRenderCall)renderQueue.poll()).execute();
         }

         outFrameBuffer.func_147610_a(true);
         ShaderUtil.outline.attach();
         ShaderUtil.outline.setUniformf("size", (float)radius);
         ShaderUtil.outline.setUniform("textureIn", 0);
         ShaderUtil.outline.setUniform("textureToCheck", 20);
         ShaderUtil.outline.setUniformf("texelSize", 1.0F / (float)mc.func_228018_at_().func_198109_k(), 1.0F / (float)mc.func_228018_at_().func_198091_l());
         ShaderUtil.outline.setUniformf("direction", 1.0F, 0.0F);
         float[] col = ColorUtils.rgba(color);
         ShaderUtil.outline.setUniformf("color", col[0], col[1], col[2]);
         GlStateManager.func_227740_m_();
         GlStateManager.func_227676_b_(1, 770);
         GL30.glAlphaFunc(516, 1.0E-4F);
         inFrameBuffer.func_147612_c();
         ShaderUtil.drawQuads();
         mc.func_147110_a().func_147610_a(false);
         GlStateManager.func_227676_b_(770, 771);
         ShaderUtil.outline.setUniformf("direction", 0.0F, 1.0F);
         outFrameBuffer.func_147612_c();
         GL30.glActiveTexture(34004);
         inFrameBuffer.func_147612_c();
         GL30.glActiveTexture(33984);
         ShaderUtil.drawQuads();
         ShaderUtil.outline.detach();
         GlStateManager.func_227760_t_(0);
         GlStateManager.func_227737_l_();
      }
   }

   public static Framebuffer setupBuffer(Framebuffer frameBuffer) {
      if (frameBuffer.field_147622_a == mc.func_228018_at_().func_198109_k() && frameBuffer.field_147620_b == mc.func_228018_at_().func_198091_l()) {
         frameBuffer.func_216493_b(false);
      } else {
         frameBuffer.func_216491_a(Math.max(1, mc.func_228018_at_().func_198109_k()), Math.max(1, mc.func_228018_at_().func_198091_l()), false);
      }

      frameBuffer.func_147604_a(0.0F, 0.0F, 0.0F, 0.0F);
      return frameBuffer;
   }
}
