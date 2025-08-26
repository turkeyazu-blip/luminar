package ru.luminar.utils.shader.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.shader.Framebuffer;
import org.lwjgl.opengl.GL30;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.shader.ShaderUtil;

public class Mask implements IMinecraft {
   private final Framebuffer in = new Framebuffer(1, 1, true, false);
   private final Framebuffer out = new Framebuffer(1, 1, true, false);

   public void renderMask(float x, float y, float width, float height, Runnable mask) {
      this.setupBuffer(this.in);
      this.setupBuffer(this.out);
      this.in.func_147610_a(true);
      mask.run();
      this.out.func_147610_a(true);
      ShaderUtil.mask.attach();
      ShaderUtil.mask.setUniformf("location", (float)((double)x * mc.func_228018_at_().func_198100_s()), (float)((double)mc.func_228018_at_().func_198091_l() - (double)height * mc.func_228018_at_().func_198100_s() - (double)y * mc.func_228018_at_().func_198100_s()));
      ShaderUtil.mask.setUniformf("rectSize", (double)width * mc.func_228018_at_().func_198100_s(), (double)height * mc.func_228018_at_().func_198100_s());
      GlStateManager.func_227740_m_();
      GlStateManager.func_227676_b_(1, 770);
      GL30.glAlphaFunc(516, 1.0E-4F);
      this.in.func_147612_c();
      ShaderUtil.drawQuads();
      mc.func_147110_a().func_147610_a(false);
      GlStateManager.func_227676_b_(770, 771);
      this.out.func_147612_c();
      GL30.glActiveTexture(34004);
      this.in.func_147612_c();
      GL30.glActiveTexture(33984);
      ShaderUtil.drawQuads();
      ShaderUtil.mask.detach();
      GlStateManager.func_227760_t_(0);
      GlStateManager.func_227737_l_();
   }

   private Framebuffer setupBuffer(Framebuffer frameBuffer) {
      if (frameBuffer.field_147622_a == mc.func_228018_at_().func_198109_k() && frameBuffer.field_147620_b == mc.func_228018_at_().func_198091_l()) {
         frameBuffer.func_216493_b(false);
      } else {
         frameBuffer.func_216491_a(Math.max(1, mc.func_228018_at_().func_198109_k()), Math.max(1, mc.func_228018_at_().func_198091_l()), false);
      }

      frameBuffer.func_147604_a(0.0F, 0.0F, 0.0F, 0.0F);
      return frameBuffer;
   }
}
