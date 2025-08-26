package ru.luminar.feature.functions.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.ModelRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;
import org.lwjgl.opengl.GL11;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.themes.Theme;

public class ChinaHat extends Function {
   public SliderSetting width = new SliderSetting("Ширина", 0.6F, 0.5F, 0.7F, 0.05F);

   public ChinaHat() {
      super("ChinaHat", Category.Render);
      this.addSettings(new Setting[]{this.width});
   }

   public static void draw(MatrixStack p_225628_1_, ModelRenderer m) {
      Theme style = Luminar.instance.styleManager.getCurrentStyle();
      float width = (Float)Luminar.instance.functions.chinaHat.width.get();
      Tessellator tessellator = Tessellator.func_178181_a();
      BufferBuilder buffer = tessellator.func_178180_c();
      RenderSystem.enableDepthTest();
      RenderSystem.disableTexture();
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.disableCull();
      RenderSystem.shadeModel(7425);
      GL11.glEnable(2848);
      RenderSystem.lineWidth(3.0F);
      p_225628_1_.func_227860_a_();
      float offset = ((ItemStack)Minecraft.func_71410_x().field_71439_g.field_71071_by.field_70460_b.get(3)).func_190926_b() ? -0.41F : -0.5F;
      m.func_228307_a_(p_225628_1_);
      p_225628_1_.func_227861_a_(0.0D, (double)offset, 0.0D);
      p_225628_1_.func_227863_a_(Vector3f.field_229182_e_.func_229187_a_(180.0F));
      p_225628_1_.func_227863_a_(Vector3f.field_229181_d_.func_229187_a_(90.0F));
      buffer.func_181668_a(6, DefaultVertexFormats.field_181706_f);
      float[] colors1 = ColorUtils.rgba(ColorUtils.replAlpha(style.getFirstColor().getRGB(), 235));
      buffer.func_227888_a_(p_225628_1_.func_227866_c_().func_227870_a_(), 0.0F, 0.3F, 0.0F).func_227885_a_(colors1[0], colors1[1], colors1[2], colors1[3]).func_181675_d();
      int i = 0;

      for(short size = 360; i <= size; ++i) {
         float[] colors2 = ColorUtils.rgba(ColorUtils.replAlpha(style.getSecondColor().getRGB(), 235));
         buffer.func_227888_a_(p_225628_1_.func_227866_c_().func_227870_a_(), -MathHelper.func_76126_a((float)i * roundToFloat(6.283185307179586D) / (float)size) * width, 0.0F, MathHelper.func_76134_b((float)i * roundToFloat(6.283185307179586D) / (float)size) * width).func_227885_a_(colors2[0], colors2[1], colors2[2], colors2[3]).func_181675_d();
      }

      tessellator.func_78381_a();
      p_225628_1_.func_227865_b_();
      RenderSystem.disableDepthTest();
      RenderSystem.disableBlend();
      RenderSystem.enableTexture();
      RenderSystem.shadeModel(7424);
      RenderSystem.enableCull();
   }

   private static float roundToFloat(double d) {
      return (float)((double)Math.round(d * 1.0E8D) / 1.0E8D);
   }
}
