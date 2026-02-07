package net.torchednova.cobblemonzones;

import com.google.common.eventbus.Subscribe;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.event.CommandEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.torchednova.cobblemonzones.commands.cobblezones;
import net.torchednova.cobblemonzones.savedata.TargetDataStorage;
import net.torchednova.cobblemonzones.spawning.SpawnManager;
import net.torchednova.cobblemonzones.zones.ZoneManager;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

// The value here should match an entry in the META-INF/neoforge.mods.toml file
@Mod(CobblemonZones.MODID)
public class CobblemonZones {
    // Define mod id in a common place for everything to reference
    public static final String MODID = "cobblemonzones";
    // Directly reference a slf4j logger
    public static final Logger LOGGER = LogUtils.getLogger();

    // The constructor for the mod class is the first code that is run when your mod is loaded.
    // FML will recognize some parameter types like IEventBus or ModContainer and pass them in automatically.
    public CobblemonZones(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in.
        // Note that this is necessary if and only if we want *this* class (ExampleMod) to respond directly to events.
        // Do not add this line if there are no @SubscribeEvent-annotated functions in this class, like onServerStarting() below.
        NeoForge.EVENT_BUS.register(this);


        // Register our mod's ModConfigSpec so that FML can create and load the config file for us
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Some common setup code
        LOGGER.info("HELLO FROM COMMON SETUP");
        SpawnManager.init();

    }

    @SubscribeEvent
    public void onRegisterCommands(RegisterCommandsEvent event)
    {
        cobblezones.register(event.getDispatcher());
    }


    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        LOGGER.info("HELLO from server starting");
        ZoneManager.init(event.getServer());
        NeoIRS.NeoIRSInit(TargetDataStorage.loadIRS(event.getServer()));
    }

    @SubscribeEvent
    public void onServerStopping(ServerStoppingEvent event)
    {
        TargetDataStorage.save(event.getServer());
    }

    @SubscribeEvent
    public void onLevelTick(LevelTickEvent.Post event)
    {
        if (SpawnManager.playersActive.isEmpty()) return;
        if (event.getLevel().dimension() != Level.OVERWORLD) return;
        if (!(event.getLevel() instanceof ServerLevel level)) return;

        for (int i = 0; i < SpawnManager.playersActive.size(); i++)
        {
            SpawnManager.playersActive.get(i).time++;
            if (SpawnManager.playersActive.get(i).time >= SpawnManager.playersActive.get(i).timeAllowed)
            {
                for (int ii = 0; ii < SpawnManager.playersActive.get(i).pokes.size(); ii++)
                {
                    if (level.getEntity(SpawnManager.playersActive.get(i).pokes.get(ii)) == null) continue;
                    level.getEntity(SpawnManager.playersActive.get(i).pokes.get(ii)).remove(Entity.RemovalReason.DISCARDED);
                }
                ServerPlayer sp = event.getLevel().getServer().getPlayerList().getPlayer(SpawnManager.playersActive.get(i).uuid);
                PlayerChatMessage chatMessage = PlayerChatMessage.unsigned(
                        sp.getUUID(),
                        "We hope you enjoyed your time in the safari zone!"
                );
                CommandSourceStack source = sp.createCommandSourceStack();
                source.sendChatMessage(new OutgoingChatMessage.Player(chatMessage),
                        false,
                        ChatType.bind(ChatType.CHAT, sp));
                //sp.moveTo(SpawnManager.playersActive.get(i).z.Entrance);
                //LOGGER.info(SpawnManager.playersActive.get(i).z.Entrance.toString());
                if (SpawnManager.playersActive.get(i).z.Entrance != null)
                {
                    sp.connection.teleport(SpawnManager.playersActive.get(i).z.Entrance.x, SpawnManager.playersActive.get(i).z.Entrance.y, SpawnManager.playersActive.get(i).z.Entrance.z, 0, 0);
                }

                SpawnManager.playersActive.remove(i);
                i--;
            }
        }

    }


}
