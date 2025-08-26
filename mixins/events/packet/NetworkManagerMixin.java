package ru.luminar.mixins.events.packet;

import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import javax.annotation.Nullable;
import net.minecraft.network.IPacket;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.luminar.Luminar;
import ru.luminar.events.impl.packet.PacketEvent;
import ru.luminar.utils.client.ClientUtil;

@Mixin({NetworkManager.class})
public abstract class NetworkManagerMixin {
   @Shadow
   public abstract boolean func_150724_d();

   @Inject(
      method = {"channelRead0(Lio/netty/channel/ChannelHandlerContext;Lnet/minecraft/network/IPacket;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void receive(ChannelHandlerContext p_channelRead0_1_, IPacket<?> p_channelRead0_2_, CallbackInfo ci) {
      if (this.func_150724_d()) {
         PacketEvent packetEvent = new PacketEvent(p_channelRead0_2_, PacketEvent.Type.RECEIVE);
         Luminar.instance.eventBus.post(packetEvent);
         if (p_channelRead0_2_ instanceof SUpdateBossInfoPacket) {
            SUpdateBossInfoPacket packet = (SUpdateBossInfoPacket)p_channelRead0_2_;
            ClientUtil.updateBossInfo(packet);
         }

         if (packetEvent.isCancel()) {
            ci.cancel();
         }

      }
   }

   @Inject(
      method = {"send(Lnet/minecraft/network/IPacket;Lio/netty/util/concurrent/GenericFutureListener;)V"},
      at = {@At("HEAD")},
      cancellable = true
   )
   public void send(IPacket<?> p_201058_1_, @Nullable GenericFutureListener<? extends Future<? super Void>> p_201058_2_, CallbackInfo ci) {
      if (this.func_150724_d()) {
         PacketEvent packetEvent = new PacketEvent(p_201058_1_, PacketEvent.Type.SEND);
         Luminar.instance.eventBus.post(packetEvent);
         if (packetEvent.isCancel()) {
            ci.cancel();
         }

      }
   }
}
