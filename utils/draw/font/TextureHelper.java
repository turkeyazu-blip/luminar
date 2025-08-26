package ru.luminar.utils.draw.font;

import com.mojang.blaze3d.platform.GlStateManager;
import java.awt.image.BufferedImage;
import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ReadOnlyBufferException;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL30;

public final class TextureHelper {
   public static int loadTexture(BufferedImage image) {
      int[] pixels = image.getRGB(0, 0, image.getWidth(), image.getHeight(), (int[])null, 0, image.getWidth());
      ByteBuffer buffer = BufferUtils.createByteBuffer(pixels.length * 4);

      try {
         int[] var3 = pixels;
         int var4 = pixels.length;
         int var5 = 0;

         while(true) {
            if (var5 >= var4) {
               buffer.flip();
               break;
            }

            int pixel = var3[var5];
            buffer.put((byte)(pixel >> 16 & 255));
            buffer.put((byte)(pixel >> 8 & 255));
            buffer.put((byte)(pixel & 255));
            buffer.put((byte)(pixel >> 24 & 255));
            ++var5;
         }
      } catch (ReadOnlyBufferException | BufferOverflowException var7) {
         return -1;
      }

      int textureID = GlStateManager.func_227622_J_();
      GlStateManager.func_227760_t_(textureID);
      GlStateManager.func_227677_b_(3553, 10241, 9729);
      GlStateManager.func_227677_b_(3553, 10240, 9729);
      GL30.glTexImage2D(3553, 0, 32856, image.getWidth(), image.getHeight(), 0, 6408, 5121, buffer);
      GlStateManager.func_227760_t_(0);
      return textureID;
   }
}
