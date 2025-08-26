package ru.luminar.utils.animations;

public class AnimationUtils {
   long mc;
   public float anim;
   public float to;
   public float speed;

   public AnimationUtils(float ms, float endpoint, float speed) {
      this.anim = ms;
      this.to = endpoint;
      this.speed = speed;
      this.mc = System.currentTimeMillis();
   }

   public float getAnim() {
      int time = (int)((System.currentTimeMillis() - this.mc) / 5L);
      if (time > 0) {
         this.mc = System.currentTimeMillis();
      }

      for(int i = 0; i < time; ++i) {
         this.anim = lerp(this.anim, this.to, this.speed);
      }

      return this.anim;
   }

   public static float lerp(float ms, float to, float sp) {
      return ms + sp * (to - ms);
   }

   public float getAngleAnim() {
      int time = (int)((System.currentTimeMillis() - this.mc) / 5L);
      if (time > 0) {
         this.mc = System.currentTimeMillis();
      }

      for(int i = 0; i < time; ++i) {
         this.anim = (float)this.lerpAngle(this.anim, this.to, this.speed);
      }

      return wrapAngleTo180_float(this.anim);
   }

   public static float wrapAngleTo180_float(float anim) {
      float delta = anim % 360.0F;
      anim = delta;
      if (delta >= 180.0F) {
         anim = delta - 360.0F;
      }

      if (anim < -180.0F) {
         anim += 360.0F;
      }

      return anim;
   }

   public void setAnim(float anim) {
      this.anim = anim;
      this.mc = System.currentTimeMillis();
   }

   double lerpAngle(float from, float to, float speed) {
      return (double)((to - from + 180.0F) % 360.0F - 180.0F * speed + from);
   }
}
