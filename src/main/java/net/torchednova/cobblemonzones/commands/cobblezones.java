package net.torchednova.cobblemonzones.commands;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.torchednova.cobblemonzones.NeoIRS;
import net.torchednova.cobblemonzones.Settings;
import net.torchednova.cobblemonzones.savedata.TargetDataStorage;
import net.torchednova.cobblemonzones.spawning.SpawnManager;
import net.torchednova.cobblemonzones.zones.Zone;
import net.torchednova.cobblemonzones.zones.ZoneManager;
import org.openjdk.nashorn.internal.parser.JSONParser;

import java.util.UUID;

public class cobblezones {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cobblezones")
                        // /referral get
                        .then(
                                Commands.literal("new").requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("name", StringArgumentType.greedyString())
                                        .executes(context -> {
                                            String name = StringArgumentType.getString(context, "name");
                                            int newId = ZoneManager.newZone(name);
                                            if (newId == -1)
                                            {
                                                context.getSource().sendSuccess(
                                                        () -> Component.literal("Failed to create zone"),
                                                        false
                                                );
                                            }
                                            else
                                            {
                                                context.getSource().sendSuccess(
                                                        () -> Component.literal("Zone " + name + " has been created"),
                                                        false
                                                );
                                            }
                                            return 1;
                                        })
                        )
                        )
                        .then(
                                Commands.literal("leave")
                                        .executes(context -> {

                                            UUID player = context.getSource().getPlayer().getUUID();
                                            for (int i = 0; i < SpawnManager.playersActive.size(); i++)
                                            {
                                                if (SpawnManager.playersActive.get(i).uuid == player)
                                                {
                                                    SpawnManager.playersActive.get(i).time = 999999999;
                                                    context.getSource().sendSuccess(
                                                            () -> Component.literal("Thank you for visiting!"),
                                                            false
                                                    );
                                                    return 1;
                                                }
                                            }

                                                    context.getSource().sendSuccess(
                                                            () -> Component.literal("You are not in a zone...."),
                                                            false
                                                    );

                                            return 1;
                                        }
                                        )


                        )
                        .then(
                                Commands.literal("entrance").requires(source -> source.hasPermission(2))
                                        .then(Commands.argument("pos", Vec3Argument.vec3())
                                                .then(Commands.argument("name", StringArgumentType.greedyString())
                                                .executes(context -> {

                                                    Vec3 pos = Vec3Argument.getVec3(context, "pos");

                                                    String name = StringArgumentType.getString(context, "name");
                                                    Zone z = ZoneManager.getZone(name);
                                                    if (z != null)
                                                    {
                                                        z.Entrance = pos;
                                                        context.getSource().sendSuccess(
                                                                () -> Component.literal("Added Entrance to zone " + name),
                                                                false
                                                        );
                                                    }
                                                    else
                                                    {
                                                        context.getSource().sendSuccess(
                                                                () -> Component.literal("failed to get zone " + name),
                                                                false
                                                        );
                                                    }
                                                    TargetDataStorage.save(context.getSource().getServer());
                                                    return 1;
                                                })
                                        )
                                        )
                        )
                        .then(
                                Commands.literal("addbox").requires(source -> source.hasPermission(2))
                                        .then(
                                                Commands.argument("name", StringArgumentType.greedyString())
                                                        .executes(context -> {
                                                            String name =  StringArgumentType.getString(context, "name");

                                                            ZoneManager.addBox(name, ZoneManager.getTempPoss(context.getSource().getPlayer().getUUID()));
                                                            ZoneManager.deleteTempPos(context.getSource().getPlayer().getUUID());


                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal("New Box Create and Allocated to " + name),
                                                                    false
                                                            );
                                                            TargetDataStorage.save(context.getSource().getServer());
                                                            return 1;
                                                        })
                                        )
                        )
                        .then(
                                Commands.literal("subzonespawncount").requires(source -> source.hasPermission(2))
                                        .then(
                                                Commands.argument("count", IntegerArgumentType.integer())
                                                        .then(
                                                                Commands.argument("subzone", IntegerArgumentType.integer())
                                                                        .then(
                                                                                Commands.argument("zone", IntegerArgumentType.integer())
                                                        .executes(context -> {
                                                            int count =  IntegerArgumentType.getInteger(context, "count");
                                                            int subzone =  IntegerArgumentType.getInteger(context, "subzone");
                                                            int zone =  IntegerArgumentType.getInteger(context, "zone");

                                                            if (ZoneManager.getZone(zone).subZones.get(subzone) == null) return 1;


                                                            ZoneManager.getZone(zone).subZones.get(subzone).spawnCount = count;

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal("Set spawn count to " + count + " in subzone " + subzone + " in zone " + zone),
                                                                    false
                                                            );

                                                            TargetDataStorage.save(context.getSource().getServer());
                                                            return 1;
                                                        })
                                        )))
                        )
                        .then(
                                Commands.literal("pos1").requires(source -> source.hasPermission(2))
                                        .then(
                                                Commands.argument("pos1", Vec3Argument.vec3())
                                                        .executes(context -> {
                                                            Vec3 pos1 =  Vec3Argument.getVec3(context, "pos1");
                                                            ZoneManager.storePosOne(pos1, context.getSource().getPlayer().getUUID());
                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal("Position 1 Set"),
                                                                    false
                                                            );
                                                            return 1;
                                                        })
                                        )


                        )
                        .then(
                                Commands.literal("pos2").requires(source -> source.hasPermission(2))
                                        .then(
                                                Commands.argument("pos2", Vec3Argument.vec3())
                                                        .executes(context -> {
                                                            Vec3 pos2 =  Vec3Argument.getVec3(context, "pos2");
                                                            ZoneManager.storePosTwo(pos2, context.getSource().getPlayer().getUUID());
                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal("Position 2 Set"),
                                                                    false
                                                            );
                                                            return 1;
                                                        })
                                        )
                        )
                        .then(
                                Commands.literal("listzones").requires(source -> source.hasPermission(2))
                                        .executes(context -> {
                                            StringBuilder sb = new StringBuilder();
                                            sb.append("A List of all Zones:\n");
                                            for (int i = 0; i < ZoneManager.zones.size(); i++)
                                            {
                                                sb.append("\nName: ");
                                                sb.append(ZoneManager.zones.get(i).name);
                                                sb.append("\nID: ");
                                                sb.append(ZoneManager.zones.get(i).id);
                                                sb.append("\nEntrance Pos: ");
                                                sb.append(ZoneManager.zones.get(i).Entrance);
                                                sb.append("\nSubZones: ");
                                                for (int ii = 0; ii < ZoneManager.zones.get(i).subZones.size(); ii++)
                                                {
                                                    sb.append("\n -  ");
                                                    sb.append(ZoneManager.zones.get(i).subZones.get(ii).id);
                                                    for (int iii = 0; iii < ZoneManager.zones.get(i).subZones.get(ii).cobblemans.size(); iii++)
                                                    {
                                                        sb.append(" -- ");
                                                        sb.append(ZoneManager.zones.get(i).subZones.get(ii).cobblemans.get(iii));
                                                    }
                                                }
                                            }

                                            context.getSource().sendSuccess(
                                                    () -> Component.literal(sb.toString()),
                                                    false
                                            );
                                            return 1;
                                        })


                        )
                        .then(
                                Commands.literal("addcobblemon").requires(source -> source.hasPermission(2))
                                        .then(
                                                Commands.argument("name", StringArgumentType.string())
                                                        .then(Commands.argument("id", IntegerArgumentType.integer())
                                                                .then(Commands.argument("cobblemons", StringArgumentType.greedyString())
                                                                        .executes(context -> {
                                                                            String name =  StringArgumentType.getString(context, "name");
                                                                            int SubZone = IntegerArgumentType.getInteger(context, "id");
                                                                            String cobblemons = StringArgumentType.getString(context, "cobblemons");
                                                                            String[] listedMons = cobblemons.split(",");
                                                                            for (int i = 0; i < listedMons.length; i+=2)
                                                                            {
                                                                                ZoneManager.getZone(name).subZones.get(SubZone).cobblemans.add(listedMons[i]);
                                                                                if (i+1 < listedMons.length)
                                                                                {
                                                                                    ZoneManager.getZone(name).subZones.get(SubZone).cobblemans.add(listedMons[i+1]);
                                                                                }
                                                                            }
                                                                            context.getSource().sendSuccess(() -> Component.literal("added " + cobblemons),
                                                                        false
                                                                            );
                                                                            TargetDataStorage.save(context.getSource().getServer());
                                                            return 1;
                                                        })
                                                                )
                                        )
                                        )
                        )
                        .then(
                                Commands.literal("start").requires(source -> source.hasPermission(2))
                                        .then(
                                                Commands.argument("player", EntityArgument.player())
                                                        .then(Commands.argument("zone", StringArgumentType.greedyString())
                                                        .executes(context -> {

                                                            String zonename =  StringArgumentType.getString(context, "zone");
                                                            Zone zone = ZoneManager.getZone(zonename);
                                                            if (zone == null)
                                                            {
                                                                context.getSource().sendSuccess(
                                                                        () -> Component.literal("Failed to find zone"),
                                                                        false
                                                                );
                                                                return 1;
                                                            }

                                                            ServerPlayer sp = EntityArgument.getPlayer(context, "player");

                                                            for (int i = 0; i < SpawnManager.playersActive.size(); i++ )
                                                            {
                                                                if (SpawnManager.playersActive.get(i).uuid.equals(sp.getUUID()))
                                                                {
                                                                    context.getSource().sendSuccess(
                                                                            () -> Component.literal("You are already in a safari type /cobblezones leave to end it early."),
                                                                            false
                                                                    );
                                                                    return 1;
                                                                }
                                                            }

                                                            String rep = NeoIRS.getMoney((Player)sp, Settings.cost, zone.name + " Safari");
                                                            JsonObject repJSON = JsonParser.parseString(rep).getAsJsonObject();
                                                            if (repJSON.get("success").getAsBoolean() == false)
                                                            {
                                                                context.getSource().sendSuccess(
                                                                        () -> Component.literal("You do NOT have enough credits to enter the safari."),
                                                                        false
                                                                );
                                                                return 1;
                                                            }

                                                            SpawnManager.runSpawn(zone, context.getSource().getServer(), sp);

                                                            context.getSource().sendSuccess(
                                                                    () -> Component.literal("You have 10 minutes enjoy your time in safari zone!"),
                                                                    false
                                                            );
                                                            return 1;
                                                        })
                                        )
                                        )
                        )
        );
    }
}
