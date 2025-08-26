package ru.luminar.utils.animations.impl;

import ru.luminar.utils.animations.Animation;
import ru.luminar.utils.animations.Direction;

public class DecelerateAnimation extends Animation {
   public DecelerateAnimation(int ms, double endPoint) {
      super(ms, endPoint);
   }

   public DecelerateAnimation(int ms, double endPoint, Direction direction) {
      super(ms, endPoint, direction);
   }

   protected double getEquation(double x) {
      double x1 = x / (double)this.duration;
      return 1.0D - (x1 - 1.0D) * (x1 - 1.0D);
   }
}
