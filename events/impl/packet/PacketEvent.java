package ru.luminar.events.impl.packet;

import net.minecraft.network.IPacket;
import ru.luminar.events.Event;

public class PacketEvent extends Event {
   private IPacket<?> packet;
   private PacketEvent.Type type;

   public PacketEvent(IPacket packet, PacketEvent.Type type) {
      this.packet = packet;
      this.type = type;
   }

   public boolean isSend() {
      return this.type == PacketEvent.Type.SEND;
   }

   public boolean isReceive() {
      return this.type == PacketEvent.Type.RECEIVE;
   }

   public boolean isSendPacket() {
      return this.type == PacketEvent.Type.SEND;
   }

   public IPacket getPacket() {
      return this.packet;
   }

   public void setPacket(IPacket packet) {
      this.packet = packet;
   }

   public boolean isReceivePacket() {
      return this.type == PacketEvent.Type.RECEIVE;
   }

   public static enum Type {
      RECEIVE,
      SEND;

      // $FF: synthetic method
      private static PacketEvent.Type[] $values() {
         return new PacketEvent.Type[]{RECEIVE, SEND};
      }
   }
}
