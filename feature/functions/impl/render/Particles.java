package ru.luminar.feature.functions.impl.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.block.AbstractButtonBlock;
import net.minecraft.block.AbstractPressurePlateBlock;
import net.minecraft.block.AirBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BushBlock;
import net.minecraft.block.CarpetBlock;
import net.minecraft.block.FlowingFluidBlock;
import net.minecraft.block.LeverBlock;
import net.minecraft.block.TorchBlock;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.network.IPacket;
import net.minecraft.network.play.server.SEntityStatusPacket;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import ru.luminar.Luminar;
import ru.luminar.events.Event;
import ru.luminar.events.impl.packet.PacketEvent;
import ru.luminar.events.impl.player.EventMovement;
import ru.luminar.events.impl.player.EventUpdate;
import ru.luminar.events.impl.render.Render3DEvent2;
import ru.luminar.events.system.Subscribe;
import ru.luminar.feature.functions.Category;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.settings.Setting;
import ru.luminar.feature.functions.settings.impl.BooleanSetting;
import ru.luminar.feature.functions.settings.impl.ColorSetting;
import ru.luminar.feature.functions.settings.impl.ModeSetting;
import ru.luminar.feature.functions.settings.impl.SliderSetting;
import ru.luminar.utils.animations.perdezrecode.easing.CompactAnimation;
import ru.luminar.utils.animations.perdezrecode.easing.Easing;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.draw.ColorUtils;
import ru.luminar.utils.draw.DisplayUtils;
import ru.luminar.utils.math.MathUtil;
import ru.luminar.utils.math.TimerUtil;
import ru.luminar.utils.math.vectors.Vector3dCustom;

public class Particles extends Function {
   public ModeSetting mode = new ModeSetting("Мод", "Звезда", new String[]{"Звезда", "Доллар", "Сердечко", "Снежинка", "Корона", "Лого"});
   public BooleanSetting clientColor = new BooleanSetting("Клиентский цвет", true);
   BooleanSetting svetashke = new BooleanSetting("Свечение", false);
   public BooleanSetting move = new BooleanSetting("При движении", false);
   public BooleanSetting attack = new BooleanSetting("При атаке", true);
   public BooleanSetting totem = new BooleanSetting("При сносе тотема", true);
   public ColorSetting color = (new ColorSetting("Цвет", -1)).setVisible(() -> {
      return !(Boolean)this.clientColor.get();
   });
   public SliderSetting count = new SliderSetting("Количество", 25.0F, 10.0F, 50.0F, 1.0F);
   public SliderSetting power = new SliderSetting("Сила разлета", 1.0F, 0.5F, 2.5F, 0.1F);
   public SliderSetting size = new SliderSetting("Размер", 0.5F, 0.1F, 1.0F, 0.01F);
   public SliderSetting lifetime = new SliderSetting("Время жизни(сек)", 5.0F, 1.0F, 7.5F, 0.25F);
   public SliderSetting plotnost = new SliderSetting("Плотность", 2.0F, 1.0F, 3.0F, 1.0F);
   public BooleanSetting zamedlitnahu = new BooleanSetting("Уменьшенная скорость", false);
   public BooleanSetting rotate = new BooleanSetting("Крутиться", true);
   public SliderSetting rotateSpeed = (new SliderSetting("Скорость вращения", 75.0F, 25.0F, 150.0F, 25.0F)).setVisible(() -> {
      return (Boolean)this.rotate.get();
   });
   private final List<Particles.Particle> targetParticles = new ArrayList();
   private final List<Particles.Particle> totemParticles = new ArrayList();
   private final List<Particles.Particle> movementParticles = new ArrayList();

   public Particles() {
      super("Particles", Category.Render);
      this.addSettings(new Setting[]{this.mode, this.svetashke, this.clientColor, this.color, this.move, this.attack, this.totem, this.count, this.power, this.size, this.plotnost, this.lifetime, this.zamedlitnahu, this.rotate, this.rotateSpeed});
   }

   private boolean isSamePos(EventMovement e) {
      return e.x == e.xPrev && e.y == e.yPrev && e.z == e.zPrev;
   }

   @Subscribe
   public void onEvent(Event e) {
      if (e instanceof EventMovement) {
         EventMovement move = (EventMovement)e;
         if (!(Boolean)this.move.get()) {
            return;
         }

         if (this.isSamePos(move)) {
            return;
         }

         float motion = 0.5F;
         int count = 10;

         for(int i = 0; i < count; ++i) {
            this.movementParticles.add(new Particles.Particle(new Vector3dCustom(move.x + (double)MathUtil.random(-0.2F, 0.2F), move.y + (double)MathUtil.random(-0.1F, 0.1F), move.z + (double)MathUtil.random(-0.2F, 0.2F)), new Vector3dCustom((double)MathUtil.random(-motion, motion), (double)MathUtil.random(-0.1F, 0.5F), (double)MathUtil.random(-motion, motion)), this.movementParticles.size(), ColorUtils.random().hashCode()));
         }
      }

      float motion;
      if (e instanceof Render3DEvent2) {
         Render3DEvent2 render = (Render3DEvent2)e;
         MatrixStack matrix = render.getMatrixStack();
         boolean light = GL11.glIsEnabled(2896);
         RenderSystem.pushMatrix();
         matrix.func_227860_a_();
         RenderSystem.enableBlend();
         RenderSystem.disableAlphaTest();
         RenderSystem.depthMask(false);
         RenderSystem.disableCull();
         if (light) {
            RenderSystem.disableLighting();
         }

         GL11.glShadeModel(7425);
         RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
         motion = 0.5F * (Float)this.size.get();
         if (!this.targetParticles.isEmpty()) {
            this.renderParticles(matrix, this.targetParticles, (int)(1000.0F * (Float)this.lifetime.get()), motion);
         }

         if (!this.totemParticles.isEmpty()) {
            this.renderParticles(matrix, this.totemParticles, 3500, motion);
         }

         if (!this.movementParticles.isEmpty()) {
            this.renderParticles(matrix, this.movementParticles, 2000, motion * 0.8F);
         }

         RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA, SourceFactor.ONE, DestFactor.ZERO);
         GlStateManager.func_227628_P_();
         GL11.glShadeModel(7424);
         if (light) {
            RenderSystem.enableLighting();
         }

         RenderSystem.enableCull();
         RenderSystem.depthMask(true);
         RenderSystem.enableAlphaTest();
         matrix.func_227865_b_();
         RenderSystem.popMatrix();
      }

      if (e instanceof EventUpdate) {
         if (!this.targetParticles.isEmpty()) {
            this.removeExpiredParticles(this.targetParticles, (long)(1000.0F * (Float)this.lifetime.get()) + 1000L);
         }

         if (!this.totemParticles.isEmpty()) {
            this.removeExpiredParticles(this.totemParticles, 4500L);
         }

         if (!this.movementParticles.isEmpty()) {
            this.removeExpiredParticles(this.movementParticles, 3000L);
         }
      }

      if (e instanceof PacketEvent) {
         PacketEvent p = (PacketEvent)e;
         IPacket var11 = p.getPacket();
         if (var11 instanceof SEntityStatusPacket) {
            SEntityStatusPacket status = (SEntityStatusPacket)var11;
            if (status.func_149160_c() == 35) {
               if (!(Boolean)this.totem.get()) {
                  return;
               }

               Entity target = status.func_149161_a(mc.field_71441_e);
               motion = 2.0F;
               (new Thread(() -> {
                  try {
                     int particlesToSpawn = 250;
                     int durationMs = 2000;
                     int batches = 15;

                     for(int batch = 0; batch < batches; ++batch) {
                        int particlesInBatch = particlesToSpawn / batches;

                        for(int i = 0; i < particlesInBatch; ++i) {
                           this.totemParticles.add(new Particles.Particle(new Vector3dCustom(target.func_226277_ct_() + (double)MathUtil.random(-0.5F, 0.5F), target.func_226278_cu_() + (double)MathUtil.random(0.0F, target.func_213302_cg()), target.func_226281_cx_() + (double)MathUtil.random(-0.5F, 0.5F)), new Vector3dCustom((double)MathUtil.random(-motion, motion), (double)MathUtil.random(0.0F, (float)((double)motion * 1.5D)), (double)MathUtil.random(-motion, motion)), this.totemParticles.size(), ColorUtils.random().hashCode()));
                        }

                        Thread.sleep((long)(durationMs / batches));
                     }
                  } catch (InterruptedException var9) {
                     var9.printStackTrace();
                  }

               })).start();
            }
         }
      }

   }

   @SubscribeEvent
   public void onAttack(AttackEntityEvent e) {
      if ((Boolean)this.attack.get()) {
         Entity target = e.getTarget();
         float motion = (Float)this.power.get();

         for(int i = 0; (float)i < (Float)this.count.get(); ++i) {
            this.targetParticles.add(new Particles.Particle(new Vector3dCustom(target.func_226277_ct_(), target.func_226278_cu_() + (double)MathUtil.random(0.0F, target.func_213302_cg()), target.func_226281_cx_()), new Vector3dCustom((double)MathUtil.random(-motion, motion), (double)MathUtil.random(-2.0F, 0.1F), (double)MathUtil.random(-motion, motion)), this.targetParticles.size(), ColorUtils.random().hashCode()));
         }

      }
   }

   private void removeExpiredParticles(List<Particles.Particle> particles, long lifespan) {
      particles.removeIf((particle) -> {
         return particle.time.passed(lifespan);
      });
   }

   private void renderParticles(MatrixStack matrix, List<Particles.Particle> particles, int fadeOutTime, float size) {
      if (!particles.isEmpty()) {
         List<Particles.Particle> particlesCopy = new ArrayList(particles);
         particlesCopy.forEach(Particles.Particle::update);
         Iterator var6 = particlesCopy.iterator();

         while(var6.hasNext()) {
            Particles.Particle particle = (Particles.Particle)var6.next();
            mc.func_110434_K().func_110577_a(particle.texture);
            if ((int)particle.animation.value != 255 && !particle.time.passed(500L)) {
               particle.animation.run(255.0D);
            }

            if ((int)particle.animation.value != 0 && particle.time.passed((long)fadeOutTime)) {
               particle.animation.run(0.0D);
            }

            int color = (Boolean)this.clientColor.get() ? ColorUtils.setAlpha(Luminar.instance.styleManager.getCurrentStyle().getColor(20, 10), (int)particle.animation.value) : ColorUtils.setAlpha((Integer)this.color.get(), (int)particle.animation.value);
            Vector3dCustom v = particle.position;
            matrix.func_227860_a_();
            EntityRendererManager rendererManager = mc.func_175598_ae();
            Vector3d renderPos = rendererManager.field_217783_c.func_216785_c();
            matrix.func_227861_a_((double)((float)v.x) - renderPos.field_72450_a, (double)((float)v.y) - renderPos.field_72448_b, (double)((float)v.z) - renderPos.field_72449_c);
            matrix.func_227863_a_(mc.func_175598_ae().func_229098_b_());
            if ((Boolean)this.rotate.get()) {
               matrix.func_227863_a_(new Quaternion(new Vector3f(0.0F, 0.0F, 1.0F), particle.rotation, false));
            }

            GL11.glBlendFunc(770, 1);

            for(int i = 0; (float)i < (Float)this.plotnost.get(); ++i) {
               DisplayUtils.drawRect(matrix, -size, -size, size, size, color, color, color, color, true, true);
               if ((Boolean)this.svetashke.get()) {
                  mc.func_110434_K().func_110577_a(new ResourceLocation("luminar/images/glow.png"));
                  int color2 = (Boolean)this.clientColor.get() ? ColorUtils.setAlpha(Luminar.instance.styleManager.getCurrentStyle().getColor(20, 10), (int)(particle.animation.value / 2.25D)) : ColorUtils.setAlpha((Integer)this.color.get(), (int)(particle.animation.value / 2.25D));
                  DisplayUtils.drawRect(matrix, -size * 1.5F, -size * 1.5F, size * 1.5F, size * 1.5F, color2, color2, color2, color2, true, true);
               }
            }

            GL11.glBlendFunc(770, 771);
            matrix.func_227865_b_();
         }

      }
   }

   private ResourceLocation texture() {
      if (this.mode.is("Звезда")) {
         return new ResourceLocation("luminar/images/star.png");
      } else if (this.mode.is("Доллар")) {
         return new ResourceLocation("luminar/images/dollar.png");
      } else if (this.mode.is("Сердечко")) {
         return new ResourceLocation("luminar/images/heart.png");
      } else if (this.mode.is("Лого")) {
         return new ResourceLocation("luminar/images/luminar.png");
      } else if (this.mode.is("Снежинка")) {
         return new ResourceLocation("luminar/images/snowflake.png");
      } else {
         return this.mode.is("Корона") ? new ResourceLocation("luminar/images/crown.png") : new ResourceLocation("luminar/images/star.png");
      }
   }

   class Particle {
      private final int index;
      private final int color;
      private final TimerUtil time = new TimerUtil();
      private final CompactAnimation animation;
      private ResourceLocation texture;
      public final Vector3dCustom position;
      private final Vector3dCustom delta;
      private float rotate;
      private float rotation;
      private int lifetime;

      public Particle(Vector3dCustom position, Vector3dCustom velocity, int index, int color) {
         this.animation = new CompactAnimation(Easing.LINEAR, 500L);
         this.rotate = 0.0F;
         this.position = position;
         this.delta = new Vector3dCustom(velocity.x * 0.01D, velocity.y * 0.01D, velocity.z * 0.01D);
         this.index = index;
         this.color = color;
         this.texture = Particles.this.texture();
         this.time.reset();
      }

      public void update() {
         this.rotation = this.rotate % 1000.0F / (Float)Particles.this.rotateSpeed.get();
         if (IMinecraft.mc.field_71441_e != null) {
            ++this.lifetime;
            Vector3dCustom var10000;
            float particleSize;
            if (this.lifetime > 500) {
               particleSize = 0.99F;
               float targetDeceleration = 0.99F;
               float currentDeceleration = 1.0F - (1.0F - targetDeceleration) * Math.min(1.0F, (float)(this.lifetime - 500) / 100.0F);
               var10000 = this.delta;
               var10000.x *= (double)currentDeceleration;
               var10000 = this.delta;
               var10000.z *= (double)currentDeceleration;
            }

            particleSize = 0.5F * (Float)Particles.this.size.get();
            AxisAlignedBB particleBB = (new AxisAlignedBB(this.position.x - (double)(particleSize / 2.0F), this.position.y - (double)(particleSize / 2.0F), this.position.z - (double)(particleSize / 2.0F), this.position.x + (double)(particleSize / 2.0F), this.position.y + (double)(particleSize / 2.0F), this.position.z + (double)(particleSize / 2.0F))).func_72317_d(this.delta.x, this.delta.y, this.delta.z);
            BlockPos zPos = new BlockPos(this.position.x, this.position.y, this.position.z + this.delta.z);
            if (this.shouldCollide(IMinecraft.mc.field_71441_e, zPos, particleBB)) {
               var10000 = this.delta;
               var10000.z *= -0.8D / (double)((Boolean)Particles.this.zamedlitnahu.get() ? 2 : 1);
            }

            BlockPos yPos = new BlockPos(this.position.x, this.position.y + this.delta.y, this.position.z);
            if (this.shouldCollide(IMinecraft.mc.field_71441_e, yPos, particleBB)) {
               var10000 = this.delta;
               var10000.x *= (double)(0.999F / (float)((Boolean)Particles.this.zamedlitnahu.get() ? 2 : 1));
               var10000 = this.delta;
               var10000.z *= (double)(0.999F / (float)((Boolean)Particles.this.zamedlitnahu.get() ? 2 : 1));
               var10000 = this.delta;
               var10000.y *= -0.6D;
            }

            BlockPos xPos = new BlockPos(this.position.x + this.delta.x, this.position.y, this.position.z);
            if (this.shouldCollide(IMinecraft.mc.field_71441_e, xPos, particleBB)) {
               var10000 = this.delta;
               var10000.x *= -0.8D / (double)((Boolean)Particles.this.zamedlitnahu.get() ? 2 : 1);
            }

            this.updateWithoutPhysics();
            ++this.rotate;
         }
      }

      private boolean isValidBlock(Block block) {
         return !(block instanceof AirBlock) && !(block instanceof BushBlock) && !(block instanceof AbstractButtonBlock) && !(block instanceof TorchBlock) && !(block instanceof LeverBlock) && !(block instanceof AbstractPressurePlateBlock) && !(block instanceof CarpetBlock) && !(block instanceof FlowingFluidBlock);
      }

      private boolean shouldCollide(World world, BlockPos pos, AxisAlignedBB particleBB) {
         BlockState state = world.func_180495_p(pos);
         if (!state.func_196958_f() && state.func_185904_a().func_76230_c() && state.func_185904_a().func_76220_a()) {
            VoxelShape collisionShape = state.func_196952_d(world, pos);
            if (collisionShape.func_197766_b()) {
               return false;
            } else {
               Iterator var6 = collisionShape.func_197756_d().iterator();

               AxisAlignedBB blockBB;
               do {
                  if (!var6.hasNext()) {
                     return false;
                  }

                  AxisAlignedBB bb = (AxisAlignedBB)var6.next();
                  blockBB = bb.func_72317_d((double)pos.func_177958_n(), (double)pos.func_177956_o(), (double)pos.func_177952_p());
               } while(!blockBB.func_72326_a(particleBB));

               return true;
            }
         } else {
            return false;
         }
      }

      public void updateWithoutPhysics() {
         Vector3dCustom var10000 = this.position;
         var10000.x += this.delta.x;
         var10000 = this.position;
         var10000.y += this.delta.y;
         var10000 = this.position;
         var10000.z += this.delta.z;
         var10000 = this.delta;
         var10000.x /= 0.9999989867210388D;
         var10000 = this.delta;
         var10000.y -= 4.999999873689376E-5D;
         var10000 = this.delta;
         var10000.z /= 0.9999989867210388D;
      }
   }
}
