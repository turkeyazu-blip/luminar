package ru.luminar.mixins.events.player;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.player.EventMovement;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.utils.client.IMinecraft;

@Mixin({ClientPlayerEntity.class})
public class ClientPlayerEntityMixin {
   @Shadow
   private double field_175172_bI;
   @Shadow
   private double field_175166_bJ;
   @Shadow
   private double field_175167_bK;
   EventMovement movement = new EventMovement(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);

   @Inject(
      method = {"tick"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/world/World;hasChunkAt(Lnet/minecraft/util/math/BlockPos;)Z",
   shift = Shift.AFTER
)}
   )
   private void afterWorldLoadedCheck(CallbackInfo ci) {
      ClientPlayerEntity player = (ClientPlayerEntity)this;
      if (player.field_70170_p.func_175667_e(new BlockPos(player.func_226277_ct_(), 0.0D, player.func_226281_cx_()))) {
         Luminar.instance.eventBus.post(new EventUpdate());
      }

   }

   @Inject(
      method = {"sendPosition"},
      at = {@At(
   value = "INVOKE",
   target = "Lnet/minecraft/client/entity/player/ClientPlayerEntity;isControlledCamera()Z"
)}
   )
   public void move(CallbackInfo ci) {
      this.movement.setX(Minecraft.func_71410_x().field_71439_g.func_226277_ct_());
      this.movement.setY(Minecraft.func_71410_x().field_71439_g.func_226278_cu_());
      this.movement.setZ(Minecraft.func_71410_x().field_71439_g.func_226281_cx_());
      this.movement.setXPrev(IMinecraft.mc.field_71439_g.field_70142_S);
      this.movement.setYPrev(IMinecraft.mc.field_71439_g.field_70137_T);
      this.movement.setZPrev(IMinecraft.mc.field_71439_g.field_70136_U);
      Luminar.instance.eventBus.post(this.movement);
   }
}
