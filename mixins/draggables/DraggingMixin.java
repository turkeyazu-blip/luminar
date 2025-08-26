package ru.luminar.mixins.draggables;

import com.mojang.blaze3d.matrix.MatrixStack;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.luminar.utils.client.MouseReleaseHandler;
import ru.luminar.utils.draggables.DragManager;
import ru.luminar.utils.draggables.Dragging;
import ru.luminar.utils.draw.Cursors;
import ru.luminar.utils.math.MathUtil;

@Mixin({ChatScreen.class})
public abstract class DraggingMixin implements MouseReleaseHandler {
   @Inject(
      method = {"removed"},
      at = {@At("TAIL")}
   )
   public void release(CallbackInfo ci) {
      MainWindow mainWindow = Minecraft.func_71410_x().func_228018_at_();
      Iterator var3 = DragManager.draggables.values().iterator();

      while(var3.hasNext()) {
         Dragging dragging = (Dragging)var3.next();
         if (dragging.getModule() != null && dragging.getModule().isEnabled()) {
            dragging.onRelease(0, mainWindow);
         }
      }

   }

   @Inject(
      method = {"mouseClicked"},
      at = {@At("TAIL")}
   )
   public void click(double p_231044_1_, double p_231044_3_, int p_231044_5_, CallbackInfoReturnable<Boolean> cir) {
      Iterator var7 = DragManager.draggables.values().iterator();

      while(var7.hasNext()) {
         Dragging dragging = (Dragging)var7.next();
         if (dragging.getModule().isEnabled() && dragging.onClick(p_231044_1_, p_231044_3_, p_231044_5_)) {
            break;
         }
      }

   }

   public void onMouseReleased(int button) {
      MainWindow mainWindow = Minecraft.func_71410_x().func_228018_at_();
      Iterator var3 = DragManager.draggables.values().iterator();

      while(var3.hasNext()) {
         Dragging dragging = (Dragging)var3.next();
         if (dragging.getModule().isEnabled()) {
            dragging.onRelease(button, mainWindow);
         }
      }

   }

   @Inject(
      method = {"render"},
      at = {@At("TAIL")}
   )
   public void draw(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_, CallbackInfo ci) {
      AtomicBoolean anyHovered = new AtomicBoolean(false);
      DragManager.draggables.values().forEach((dragging) -> {
         if (dragging.getModule() != null && dragging.getModule().isEnabled()) {
            if (MathUtil.isHovered((float)p_230430_2_, (float)p_230430_3_, dragging.getX(), dragging.getY(), dragging.getWidth(), dragging.getHeight())) {
               anyHovered.set(true);
            }

            dragging.onDraw(p_230430_2_, p_230430_3_, Minecraft.func_71410_x().func_228018_at_());
         }

      });
      if (anyHovered.get()) {
         GLFW.glfwSetCursor(Minecraft.func_71410_x().func_228018_at_().func_198092_i(), Cursors.HAND);
      } else {
         GLFW.glfwSetCursor(Minecraft.func_71410_x().func_228018_at_().func_198092_i(), Cursors.ARROW);
      }

   }
}
