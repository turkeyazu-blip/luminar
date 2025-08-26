package ru.luminar.feature.functions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import ru.luminar.Luminar;
import ru.luminar.feature.functions.impl.render.Animations;
import ru.luminar.feature.functions.impl.render.Aspect;
import ru.luminar.feature.functions.impl.render.ChinaHat;
import ru.luminar.feature.functions.impl.render.ClientColor;
import ru.luminar.feature.functions.impl.render.Fullbright;
import ru.luminar.feature.functions.impl.render.HitColor;
import ru.luminar.feature.functions.impl.render.HitboxCustomizer;
import ru.luminar.feature.functions.impl.render.Interface;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.feature.functions.impl.render.JumpCircle;
import ru.luminar.feature.functions.impl.render.Particles;
import ru.luminar.feature.functions.impl.render.Removals;
import ru.luminar.feature.functions.impl.render.TargetESP;
import ru.luminar.feature.functions.impl.render.ThirdPerson;
import ru.luminar.feature.functions.impl.render.Trails;
import ru.luminar.feature.functions.impl.render.WorldParticles;
import ru.luminar.feature.functions.impl.render.WorldTweaks;
import ru.luminar.feature.functions.impl.utils.AutoReissue;
import ru.luminar.feature.functions.impl.utils.AutoRespawn;
import ru.luminar.feature.functions.impl.utils.AutoSwap;
import ru.luminar.feature.functions.impl.utils.Cooldowns;
import ru.luminar.feature.functions.impl.utils.ElytraSwap;
import ru.luminar.feature.functions.impl.utils.FTHelper;
import ru.luminar.feature.functions.impl.utils.FastSwap;
import ru.luminar.feature.functions.impl.utils.Globals;
import ru.luminar.feature.functions.impl.utils.HealingHelper;
import ru.luminar.feature.functions.impl.utils.IRCChat;
import ru.luminar.feature.functions.impl.utils.PVPSafe;
import ru.luminar.feature.functions.impl.utils.Predictions;
import ru.luminar.feature.functions.impl.utils.Sprint;
import ru.luminar.feature.functions.impl.utils.StreamerMode;
import ru.luminar.utils.client.IMinecraft;

public class Functions {
   public List<Function> functions = new ArrayList();
   public Removals removals;
   public Aspect aspect;
   public Sprint sprint;
   public FTHelper helper;
   public Fullbright fullbright;
   public TargetESP targetESP;
   public Animations animations;
   public WorldTweaks worldTweaks;
   public FastSwap fastSwap;
   public ElytraSwap elytraSwap;
   public PVPSafe pvpSafe;
   public Interface anInterface;
   public AutoRespawn autoRespawn;
   public AutoSwap autoSwap;
   public Globals globals;
   public IRCChat ircChat;
   public StreamerMode streamerMode;
   public Island island;
   public Predictions predictions;
   public HitboxCustomizer hitboxCustomizer;
   public ClientColor clientColor;
   public HealingHelper healingHelper;
   public Cooldowns cooldowns;
   public AutoReissue autoReissue;
   public Particles particles;
   public ChinaHat chinaHat;
   public HitColor hitColor;
   public Trails trails;
   public JumpCircle jumpCircle;
   public ThirdPerson thirdPerson;
   public WorldParticles worldParticles;

   public void init() {
      this.registerAll(this.removals = new Removals(), this.aspect = new Aspect(), this.sprint = new Sprint(), this.helper = new FTHelper(), this.fullbright = new Fullbright(), this.targetESP = new TargetESP(), this.animations = new Animations(), this.worldTweaks = new WorldTweaks(), this.fastSwap = new FastSwap(), this.elytraSwap = new ElytraSwap(), this.pvpSafe = new PVPSafe(), this.anInterface = new Interface(), this.autoRespawn = new AutoRespawn(), this.autoSwap = new AutoSwap(), this.globals = new Globals(), this.ircChat = new IRCChat(), this.streamerMode = new StreamerMode(), this.island = new Island(), this.predictions = new Predictions(), this.hitboxCustomizer = new HitboxCustomizer(), this.clientColor = new ClientColor(), this.healingHelper = new HealingHelper(), this.cooldowns = new Cooldowns(), this.autoReissue = new AutoReissue(), this.particles = new Particles(), this.jumpCircle = new JumpCircle(), this.trails = new Trails(), this.thirdPerson = new ThirdPerson(), this.chinaHat = new ChinaHat(), this.hitColor = new HitColor(), this.worldParticles = new WorldParticles());
   }

   private void registerAll(Function... funcs) {
      Arrays.sort(funcs, Comparator.comparing(Function::getName));
      this.functions.addAll(List.of(funcs));
   }

   public void key(int key, int action) {
      Iterator var3 = this.functions.iterator();

      while(var3.hasNext()) {
         Function f = (Function)var3.next();
         if (key == f.bind && action == 0) {
            if (f.isPendingRemoval()) {
               if (Luminar.instance.functions.island.isEnabled()) {
                  Island.islandManager.add("Функция " + f.name + " недоступна на этом сервере!", 3);
               } else {
                  IMinecraft.print2("Функция " + f.name + " недоступна на этом сервере!");
               }

               return;
            }

            f.toggle();
         }
      }

   }
}
