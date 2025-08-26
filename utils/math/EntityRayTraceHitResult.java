package ru.luminar.utils.math;

import net.minecraft.entity.Entity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;

public class EntityRayTraceHitResult extends EntityRayTraceResult {
   public final Direction face;

   public Direction getFace() {
      return this.face;
   }

   public EntityRayTraceHitResult(Entity entity, Vector3d hitVec, Direction face) {
      super(entity, hitVec);
      this.face = face;
   }
}
