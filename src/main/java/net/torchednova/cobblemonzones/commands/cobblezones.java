package net.torchednova.cobblemonzones.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.Vec3Argument;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import net.torchednova.cobblemonzones.savedata.TargetDataStorage;
import net.torchednova.cobblemonzones.spawning.SpawnManager;
import net.torchednova.cobblemonzones.zones.Zone;
import net.torchednova.cobblemonzones.zones.ZoneManager;

public class cobblezones {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("cobblezones")
                        // /referral get
                        .then(
                                Commands.literal("new")
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
                                Commands.literal("entrance")
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
                                                    return 1;
                                                })
                                        )
                                        )
                        )
                        .then(
                                Commands.literal("addbox")
                                        .then(
                                                Commands.argument("name", StringArgumentType.greedyString())
                                                        .executes(context -> {
                                                            String name =  StringArgumentType.getString(context, "name");

                                                            ZoneManager.addBox(name, ZoneManager.getTempPoss(context.getSource().getPlayer().getUUID()));
                                                            ZoneManager.deleteTempPos(context.getSource().getPlayer().getUUID());

                                                            SpawnManager.runSpawn(ZoneManager.getZone(name), context.getSource().getServer());
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
                                Commands.literal("pos1")
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
                                Commands.literal("pos2")
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
                                Commands.literal("addcobblemon")
                                        .then(
                                                Commands.argument("name", StringArgumentType.string())
                                                        .then(Commands.argument("id", IntegerArgumentType.integer())
                                                                .then(Commands.argument("cobblemons", StringArgumentType.greedyString())
                                                                        .executes(context -> {
                                                                            String name =  StringArgumentType.getString(context, "name");
                                                                            int SubZone = IntegerArgumentType.getInteger(context, "id");
                                                                            String cobblemons = StringArgumentType.getString(context, "cobblemons");
                                                                            String[] listedMons = cobblemons.split(",");
                                                                            for (int i = 0; i < listedMons.length; i++)
                                                                            {
                                                                                ZoneManager.getZone(name).subZones.get(SubZone).cobblemans.add(listedMons[i]);
                                                                            }
                                                                            context.getSource().sendSuccess(() -> Component.literal("Position 2 Set"),
                                                                        false
                                                                            );
                                                            return 1;
                                                        })
                                        )
                        )
        );
    }
}
