package ru.luminar.mixins.functions.streamermode;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.longs.LongSet;
import it.unimi.dsi.fastutil.longs.LongSets;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.client.ClientBrandRetriever;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.overlay.DebugOverlayGui;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.network.NetworkManager;
import net.minecraft.server.integrated.IntegratedServer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SharedConstants;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.MutableRegistry;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner.EntityDensityManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.luminar.Luminar;

@Mixin({DebugOverlayGui.class})
public abstract class DebugOverlayGuiMixin {
   Minecraft minecraft = Minecraft.func_71410_x();
   @Shadow
   private ChunkPos field_212924_f;
   @Shadow
   @Final
   private static Map<Type, String> field_212923_a;

   @Shadow
   public abstract void func_212921_a();

   @Shadow
   @Nullable
   protected abstract String func_223101_g();

   @Shadow
   protected abstract World func_212922_g();

   @Shadow
   protected abstract Chunk func_212916_i();

   @Shadow
   @Nullable
   protected abstract Chunk func_212919_h();

   @Shadow
   @Nullable
   protected abstract ServerWorld func_238515_d_();

   @Inject(
      method = {"getGameInformation"},
      at = {@At("HEAD")},
      cancellable = true
   )
   protected void getGameInformation(CallbackInfoReturnable<List<String>> cir) {
      IntegratedServer integratedserver = this.minecraft.func_71401_C();
      NetworkManager networkmanager = this.minecraft.func_147114_u().func_147298_b();
      float f = networkmanager.func_211390_n();
      float f1 = networkmanager.func_211393_m();
      String s;
      if (integratedserver != null) {
         s = String.format("Integrated server @ %.0f ms ticks, %.0f tx, %.0f rx", integratedserver.func_211149_aT(), f, f1);
      } else {
         s = String.format("\"%s\" server, %.0f tx, %.0f rx", this.minecraft.field_71439_g.func_142021_k(), f, f1);
      }

      BlockPos blockpos = this.minecraft.func_175606_aa().func_233580_cy_();
      if (this.minecraft.func_189648_am()) {
         String[] var10001 = new String[9];
         String var10004 = SharedConstants.func_215069_a().getName();
         var10001[0] = "Minecraft " + var10004 + " (" + this.minecraft.func_175600_c() + "/" + ClientBrandRetriever.getClientModName() + ")";
         var10001[1] = this.minecraft.field_71426_K;
         var10001[2] = s;
         var10001[3] = this.minecraft.field_71438_f.func_72735_c();
         var10001[4] = this.minecraft.field_71438_f.func_72723_d();
         var10004 = this.minecraft.field_71452_i.func_78869_b();
         var10001[5] = "P: " + var10004 + ". T: " + this.minecraft.field_71441_e.func_217425_f();
         var10001[6] = this.minecraft.field_71441_e.func_72827_u();
         var10001[7] = "";
         var10001[8] = String.format("Chunk-relative: %d %d %d", blockpos.func_177958_n() & 15, blockpos.func_177956_o() & 15, blockpos.func_177952_p() & 15);
         cir.setReturnValue(Lists.newArrayList(var10001));
      } else {
         Entity entity = this.minecraft.func_175606_aa();
         Direction direction = entity.func_174811_aO();
         String s1;
         switch(direction) {
         case NORTH:
            s1 = "Towards negative Z";
            break;
         case SOUTH:
            s1 = "Towards positive Z";
            break;
         case WEST:
            s1 = "Towards negative X";
            break;
         case EAST:
            s1 = "Towards positive X";
            break;
         default:
            s1 = "Invalid";
         }

         ChunkPos chunkpos = new ChunkPos(blockpos);
         if (!Objects.equals(this.field_212924_f, chunkpos)) {
            this.field_212924_f = chunkpos;
            this.func_212921_a();
         }

         World world = this.func_212922_g();
         LongSet longset = world instanceof ServerWorld ? ((ServerWorld)world).func_217469_z() : LongSets.EMPTY_SET;
         String[] var10000 = new String[7];
         String var10003 = SharedConstants.func_215069_a().getName();
         var10000[0] = "Minecraft " + var10003 + " (" + this.minecraft.func_175600_c() + "/" + ClientBrandRetriever.getClientModName() + ("release".equalsIgnoreCase(this.minecraft.func_184123_d()) ? "" : "/" + this.minecraft.func_184123_d()) + ")";
         var10000[1] = this.minecraft.field_71426_K;
         var10000[2] = s;
         var10000[3] = this.minecraft.field_71438_f.func_72735_c();
         var10000[4] = this.minecraft.field_71438_f.func_72723_d();
         var10003 = this.minecraft.field_71452_i.func_78869_b();
         var10000[5] = "P: " + var10003 + ". T: " + this.minecraft.field_71441_e.func_217425_f();
         var10000[6] = this.minecraft.field_71441_e.func_72827_u();
         List<String> list = Lists.newArrayList(var10000);
         String s2 = this.func_223101_g();
         if (s2 != null) {
            list.add(s2);
         }

         ResourceLocation var30 = this.minecraft.field_71441_e.func_234923_W_().func_240901_a_();
         list.add(var30 + " FC: " + ((LongSet)longset).size());
         list.add("");
         if (!Luminar.instance.functions.streamerMode.isEnabled()) {
            list.add(String.format(Locale.ROOT, "XYZ: %.3f / %.5f / %.3f", this.minecraft.func_175606_aa().func_226277_ct_(), this.minecraft.func_175606_aa().func_226278_cu_(), this.minecraft.func_175606_aa().func_226281_cx_()));
            list.add(String.format("Block: %d %d %d", blockpos.func_177958_n(), blockpos.func_177956_o(), blockpos.func_177952_p()));
            list.add(String.format("Chunk: %d %d %d in %d %d %d", blockpos.func_177958_n() & 15, blockpos.func_177956_o() & 15, blockpos.func_177952_p() & 15, blockpos.func_177958_n() >> 4, blockpos.func_177956_o() >> 4, blockpos.func_177952_p() >> 4));
         }

         list.add(String.format(Locale.ROOT, "Facing: %s (%s) (%.1f / %.1f)", direction, s1, MathHelper.func_76142_g(entity.field_70177_z), MathHelper.func_76142_g(entity.field_70125_A)));
         int l;
         if (this.minecraft.field_71441_e == null) {
            list.add("Outside of world...");
         } else if (!this.minecraft.field_71441_e.func_175667_e(blockpos)) {
            list.add("Outside of world...");
         } else {
            Chunk chunk = this.func_212916_i();
            if (chunk.func_76621_g()) {
               list.add("Waiting for chunk...");
            } else {
               int i = this.minecraft.field_71441_e.func_72863_F().func_212863_j_().func_227470_b_(blockpos, 0);
               int j = this.minecraft.field_71441_e.func_226658_a_(LightType.SKY, blockpos);
               l = this.minecraft.field_71441_e.func_226658_a_(LightType.BLOCK, blockpos);
               list.add("Client Light: " + i + " (" + j + " sky, " + l + " block)");
               Chunk chunk1 = this.func_212919_h();
               if (chunk1 != null) {
                  WorldLightManager worldlightmanager = world.func_72863_F().func_212863_j_();
                  int var32 = worldlightmanager.func_215569_a(LightType.SKY).func_215611_b(blockpos);
                  list.add("Server Light: (" + var32 + " sky, " + worldlightmanager.func_215569_a(LightType.BLOCK).func_215611_b(blockpos) + " block)");
               } else {
                  list.add("Server Light: (?? sky, ?? block)");
               }

               StringBuilder stringbuilder = new StringBuilder("CH");
               Type[] var21 = Type.values();
               int var22 = var21.length;

               int var23;
               Type heightmap$type1;
               for(var23 = 0; var23 < var22; ++var23) {
                  heightmap$type1 = var21[var23];
                  if (heightmap$type1.func_222681_b()) {
                     stringbuilder.append(" ").append((String)field_212923_a.get(heightmap$type1)).append(": ").append(chunk.func_201576_a(heightmap$type1, blockpos.func_177958_n(), blockpos.func_177952_p()));
                  }
               }

               list.add(stringbuilder.toString());
               stringbuilder.setLength(0);
               stringbuilder.append("SH");
               var21 = Type.values();
               var22 = var21.length;

               for(var23 = 0; var23 < var22; ++var23) {
                  heightmap$type1 = var21[var23];
                  if (heightmap$type1.func_222683_c()) {
                     stringbuilder.append(" ").append((String)field_212923_a.get(heightmap$type1)).append(": ");
                     if (chunk1 != null) {
                        stringbuilder.append(chunk1.func_201576_a(heightmap$type1, blockpos.func_177958_n(), blockpos.func_177952_p()));
                     } else {
                        stringbuilder.append("??");
                     }
                  }
               }

               list.add(stringbuilder.toString());
               if (blockpos.func_177956_o() >= 0 && blockpos.func_177956_o() < 256) {
                  MutableRegistry var33 = this.minecraft.field_71441_e.func_241828_r().func_243612_b(Registry.field_239720_u_);
                  Biome var10002 = this.minecraft.field_71441_e.func_226691_t_(blockpos);
                  list.add("Biome: " + var33.func_177774_c(var10002));
                  long i1 = 0L;
                  float f2 = 0.0F;
                  if (chunk1 != null) {
                     f2 = world.func_242413_ae();
                     i1 = chunk1.func_177416_w();
                  }

                  DifficultyInstance difficultyinstance = new DifficultyInstance(world.func_175659_aa(), world.func_72820_D(), i1, f2);
                  list.add(String.format(Locale.ROOT, "Local Difficulty: %.2f // %.2f (Day %d)", difficultyinstance.func_180168_b(), difficultyinstance.func_180170_c(), this.minecraft.field_71441_e.func_72820_D() / 24000L));
               }
            }
         }

         ServerWorld serverworld = this.func_238515_d_();
         if (serverworld != null) {
            EntityDensityManager worldentityspawner$entitydensitymanager = serverworld.func_72863_F().func_241101_k_();
            if (worldentityspawner$entitydensitymanager != null) {
               Object2IntMap<EntityClassification> object2intmap = worldentityspawner$entitydensitymanager.func_234995_b_();
               l = worldentityspawner$entitydensitymanager.func_234988_a_();
               list.add("SC: " + l + ", " + (String)Stream.of(EntityClassification.values()).map((p_238511_1_) -> {
                  char var10000 = Character.toUpperCase(p_238511_1_.func_220363_a().charAt(0));
                  return var10000 + ": " + object2intmap.getInt(p_238511_1_);
               }).collect(Collectors.joining(", ")));
            } else {
               list.add("SC: N/A");
            }
         }

         ShaderGroup shadergroup = this.minecraft.field_71460_t.func_147706_e();
         if (shadergroup != null) {
            list.add("Shader: " + shadergroup.func_148022_b());
         }

         String var35 = this.minecraft.func_147118_V().func_215293_f();
         list.add(var35 + String.format(" (Mood %d%%)", Math.round(this.minecraft.field_71439_g.func_239206_w_() * 100.0F)));
         cir.setReturnValue(list);
      }

   }
}
