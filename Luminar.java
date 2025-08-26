package ru.luminar;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Desktop.Action;
import java.io.File;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.ControlsScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.client.event.InputEvent.KeyInputEvent;
import net.minecraftforge.client.event.InputEvent.MouseInputEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.spongepowered.asm.launch.MixinBootstrap;
import ru.luminar.events.system.EventBus;
import ru.luminar.feature.command.api.impl.AdviceCommandFactoryImpl;
import ru.luminar.feature.command.api.impl.ConsoleLogger;
import ru.luminar.feature.command.api.impl.MinecraftLogger;
import ru.luminar.feature.command.api.impl.MultiLogger;
import ru.luminar.feature.command.api.impl.ParametersFactoryImpl;
import ru.luminar.feature.command.api.impl.PrefixImpl;
import ru.luminar.feature.command.api.impl.StandaloneCommandDispatcher;
import ru.luminar.feature.command.api.interfaces.AdviceCommandFactory;
import ru.luminar.feature.command.api.interfaces.Command;
import ru.luminar.feature.command.api.interfaces.CommandDispatcher;
import ru.luminar.feature.command.api.interfaces.Logger;
import ru.luminar.feature.command.api.interfaces.ParametersFactory;
import ru.luminar.feature.command.api.interfaces.Prefix;
import ru.luminar.feature.command.impl.BroadcastCommand;
import ru.luminar.feature.command.impl.CloudCommand;
import ru.luminar.feature.command.impl.DMCommand;
import ru.luminar.feature.command.impl.IRCCommand;
import ru.luminar.feature.command.impl.WaypointCommand;
import ru.luminar.feature.functions.Function;
import ru.luminar.feature.functions.Functions;
import ru.luminar.feature.functions.impl.render.Island;
import ru.luminar.feature.managers.EventManager;
import ru.luminar.feature.managers.config.cloud.CloudAPI;
import ru.luminar.feature.managers.config.cloud.CloudConfigFileManager;
import ru.luminar.feature.managers.config.cloud.CloudConfigManager;
import ru.luminar.feature.managers.config.files.ConfigManager;
import ru.luminar.feature.managers.island.IslandManager;
import ru.luminar.feature.managers.waypoint.WaypointManager;
import ru.luminar.ui.GuiScreen;
import ru.luminar.utils.client.IMinecraft;
import ru.luminar.utils.client.MouseReleaseHandler;
import ru.luminar.utils.draggables.DragManager;
import ru.luminar.utils.draggables.Dragging;
import ru.luminar.utils.themes.Theme;
import ru.luminar.utils.themes.ThemeFactory;
import ru.luminar.utils.themes.ThemeFactoryImpl;
import ru.luminar.utils.themes.ThemeManager;

@Mod("luminar")
public class Luminar {
   public static boolean useCloudCfg;
   public ThemeManager styleManager;
   GuiScreen screen;
   public static Luminar instance;
   public EventBus eventBus = new EventBus();
   public static String MOD_ID = "luminar";
   public static float scaleFactor = 1.0F;
   private final File filesDir;
   private final File cfgDir;
   public CloudConfigFileManager cloudConfigFileManager;
   public Functions functions;
   public CommandDispatcher commandDispatcher;
   public List<Command> commands;
   public static List<Theme> themes = new ArrayList();
   KeyBinding GUI_KEY;

   public Luminar() {
      this.filesDir = new File(Minecraft.func_71410_x().field_71412_D + "\\luminar");
      this.cfgDir = new File(Minecraft.func_71410_x().field_71412_D + "\\luminar\\configs");
      this.commands = new ArrayList();
      instance = this;
      if (!this.filesDir.exists()) {
         this.filesDir.mkdirs();
      }

      if (!this.cfgDir.exists()) {
         this.cfgDir.mkdirs();
      }

      this.GUI_KEY = this.registerKeyBinding(new KeyBinding("Кнопка гуи", 344, "Luminar visuals"));
      this.cloudConfigFileManager = new CloudConfigFileManager();
      this.cloudConfigFileManager.syncWithLuminar();
      Island.islandManager = new IslandManager();
      this.addThemes();
      this.addCommands();
      if (useCloudCfg && !CloudAPI.connected) {
         CloudAPI.init();
      }

      this.functions = new Functions();
      this.functions.init();
      MixinBootstrap.init();
      DragManager.load();
      if (CloudAPI.connected) {
         CloudConfigManager.getInstance().tryLoadLastConfig();
      }

      if (!useCloudCfg) {
         boolean var1 = ConfigManager.getInstance().loadLastConfigLocally();
      }

      this.screen = new GuiScreen(new StringTextComponent("Clickgui"));

      try {
         if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI("https://t.me/luminarvisuals"));
         }
      } catch (Exception var2) {
      }

      MinecraftForge.EVENT_BUS.register(this);
      this.eventBus.register(this);
      this.eventBus.register(new EventManager());
      MinecraftForge.EVENT_BUS.register(new EventManager());
   }

   public Dragging createDrag(Function module, String name, float x, float y) {
      DragManager.draggables.put(name, new Dragging(module, name, x, y));
      return (Dragging)DragManager.draggables.get(name);
   }

   private void addCommands() {
      Logger logger = new MultiLogger(List.of(new ConsoleLogger(), new MinecraftLogger()));
      Prefix prefix = new PrefixImpl();
      this.commands.add(new WaypointCommand(logger));
      this.commands.add(new IRCCommand(logger));
      this.commands.add(new DMCommand(logger));
      this.commands.add(new BroadcastCommand(logger));
      this.commands.add(new CloudCommand(logger));
      this.eventBus.register(new WaypointManager());
      MinecraftForge.EVENT_BUS.register(new WaypointManager());
      AdviceCommandFactory adviceCommandFactory = new AdviceCommandFactoryImpl(logger);
      ParametersFactory parametersFactory = new ParametersFactoryImpl();
      this.commandDispatcher = new StandaloneCommandDispatcher(this.commands, adviceCommandFactory, prefix, parametersFactory, logger);
   }

   private void addThemes() {
      ThemeFactory styleFactory = new ThemeFactoryImpl();
      themes.add(styleFactory.createStyle("Аметист", new Color(140, 27, 250), new Color(98, 49, 145)));
      themes.add(styleFactory.createStyle("Кровавый", new Color(170, 10, 10), new Color(120, 5, 5)));
      themes.add(styleFactory.createStyle("Бирюзовый", new Color(0, 180, 150), new Color(0, 140, 110)));
      themes.add(styleFactory.createStyle("Мятный", new Color(100, 220, 180), new Color(70, 180, 140)));
      themes.add(styleFactory.createStyle("Лавандовый", new Color(180, 150, 255), new Color(140, 110, 220)));
      themes.add(styleFactory.createStyle("Весенний луг", new Color(160, 220, 100), new Color(255, 215, 80)));
      themes.add(styleFactory.createStyle("Рубин", new Color(180, 40, 50), new Color(240, 180, 80)));
      themes.add(styleFactory.createStyle("Летний сад", new Color(80, 180, 100), new Color(220, 210, 170)));
      themes.add(styleFactory.createStyle("Грозовая туча", new Color(50, 70, 100), new Color(200, 220, 240)));
      themes.add(styleFactory.createStyle("Горячий песок", new Color(230, 180, 100), new Color(160, 120, 60)));
      themes.add(styleFactory.createStyle("Кастом", new Color(255, 255, 255), new Color(255, 255, 255)));
      this.styleManager = new ThemeManager(themes, (Theme)themes.get(0));
   }

   @SubscribeEvent
   public void onKey(KeyInputEvent e) {
      if (e.getKey() == this.GUI_KEY.getKey().func_197937_c()) {
         if (IMinecraft.mc.field_71462_r instanceof ControlsScreen) {
            return;
         }

         IMinecraft.mc.func_147108_a(this.screen);
      }

      if (IMinecraft.mc.field_71462_r == null) {
         if (e.getKey() != -1) {
            this.functions.key(e.getKey(), e.getAction());
            Iterator var2 = this.functions.functions.iterator();

            while(var2.hasNext()) {
               Function f = (Function)var2.next();
               if (f.isEnabled() && e.getAction() == 1) {
                  f.onKey(e.getKey());
               }
            }

         }
      }
   }

   @SubscribeEvent
   public void onMouse(MouseInputEvent e) {
      if (e.getAction() == 0) {
         Screen var3 = Minecraft.func_71410_x().field_71462_r;
         if (var3 instanceof ChatScreen) {
            ChatScreen chatScreen = (ChatScreen)var3;
            ((MouseReleaseHandler)chatScreen).onMouseReleased(e.getButton());
         }
      }

      if (IMinecraft.mc.field_71462_r == null) {
         if (e.getButton() != -1) {
            this.functions.key(e.getButton(), e.getAction());
            Iterator var4 = this.functions.functions.iterator();

            while(var4.hasNext()) {
               Function f = (Function)var4.next();
               if (f.isEnabled() && e.getAction() == 1) {
                  f.onKey(e.getButton());
               }
            }

         }
      }
   }

   public KeyBinding registerKeyBinding(KeyBinding keyBinding) {
      ClientRegistry.registerKeyBinding(keyBinding);
      return keyBinding;
   }
}
