package ru.luminar.utils.draw.font;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.SimpleTexture;
import net.minecraft.client.renderer.texture.Texture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.IResource;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

public final class IOUtils {
   private static final IResourceManager RES_MANAGER = Minecraft.func_71410_x().func_195551_G();
   private static final TextureManager TEX_MANAGER = Minecraft.func_71410_x().func_110434_K();
   private static final Gson GSON = new Gson();

   private IOUtils() {
   }

   public static String toString(ResourceLocation location) {
      return toString(location, "\n");
   }

   public static String toString(ResourceLocation location, String delimiter) {
      try {
         IResource resource = RES_MANAGER.func_199002_a(location);

         String var4;
         try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.func_199027_b()));

            try {
               var4 = (String)reader.lines().collect(Collectors.joining(delimiter));
            } catch (Throwable var8) {
               try {
                  reader.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }

               throw var8;
            }

            reader.close();
         } catch (Throwable var9) {
            if (resource != null) {
               try {
                  resource.close();
               } catch (Throwable var6) {
                  var9.addSuppressed(var6);
               }
            }

            throw var9;
         }

         if (resource != null) {
            resource.close();
         }

         return var4;
      } catch (IOException var10) {
         var10.printStackTrace();
         return "";
      }
   }

   public static <T> T fromJsonToInstance(ResourceLocation location, Class<T> clazz) {
      try {
         return GSON.fromJson(toString(location), clazz);
      } catch (JsonSyntaxException var3) {
         var3.printStackTrace();
         return null;
      }
   }

   public static Texture toTexture(ResourceLocation location) {
      Texture texture = TEX_MANAGER.func_229267_b_(location);
      if (texture == null) {
         texture = new SimpleTexture(location);
         TEX_MANAGER.func_229263_a_(location, (Texture)texture);
      }

      return (Texture)texture;
   }
}
