package ru.luminar.utils.math.vectors;

import net.minecraft.util.math.vector.Vector3d;

public class Vector3dCustom {
   public double x;
   public double y;
   public double z;

   public Vector3dCustom(Vector3d vec) {
      this.x = vec.field_72450_a;
      this.y = vec.field_72448_b;
      this.z = vec.field_72449_c;
   }

   public Vector3dCustom(double x, double y, double z) {
      this.x = x;
      this.y = y;
      this.z = z;
   }
}
