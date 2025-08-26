package ru.luminar.utils.animations.perdezrecode.easing;

public class CompactAnimation {
   public Easing easing;
   public long duration;
   public long millis;
   public long startTime;
   public double startValue;
   public double destinationValue;
   public double value;
   public boolean finished;

   public CompactAnimation(Easing easing, long duration) {
      this.easing = easing;
      this.startTime = System.currentTimeMillis();
      this.duration = duration;
   }

   public void run(double destinationValue) {
      this.millis = System.currentTimeMillis();
      if (this.destinationValue != destinationValue) {
         this.destinationValue = destinationValue;
         this.reset();
      } else {
         this.finished = this.millis - this.duration > this.startTime;
         if (this.finished) {
            this.value = destinationValue;
            return;
         }
      }

      double result = (Double)this.easing.function.apply(this.getProgress());
      if (this.value > destinationValue) {
         this.value = this.startValue - (this.startValue - destinationValue) * result;
      } else {
         this.value = this.startValue + (destinationValue - this.startValue) * result;
      }

   }

   public double getProgress() {
      return (double)(System.currentTimeMillis() - this.startTime) / (double)this.duration;
   }

   public void reset() {
      this.startTime = System.currentTimeMillis();
      this.startValue = this.value;
      this.finished = false;
   }
}
