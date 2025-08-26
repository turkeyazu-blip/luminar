package ru.luminar.events.impl.overlay;

import ru.luminar.events.Event;

public class EventOverlay extends Event {
   public final EventOverlay.Overlays overlayType;

   public EventOverlay(EventOverlay.Overlays overlayType) {
      this.overlayType = overlayType;
   }

   public static enum Overlays {
      FIRE_OVERLAY,
      BOSS_LINE,
      SCOREBOARD,
      TOTEM,
      HURT;

      // $FF: synthetic method
      private static EventOverlay.Overlays[] $values() {
         return new EventOverlay.Overlays[]{FIRE_OVERLAY, BOSS_LINE, SCOREBOARD, TOTEM, HURT};
      }
   }
}
