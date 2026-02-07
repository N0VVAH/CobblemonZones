package net.torchednova.cobblemonzones.spawning;

import com.cobblemon.mod.common.api.permission.CobblemonPermissions;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;

import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.torchednova.cobblemonzones.Settings;
import net.torchednova.cobblemonzones.zones.Zone;

import java.util.ArrayList;
import java.util.Random;
import java.util.UUID;

import static com.mojang.text2speech.Narrator.LOGGER;

public class SpawnManager {
    public static ArrayList<Spawns> playersActive;

    public static void init()
    {
        playersActive = new ArrayList<>();
    }


    public static void runSpawn(Zone zone, MinecraftServer server, ServerPlayer sp)
    {
        Vec3 startPos = null;
        Vec3 endPos = null;
        Vec3 tempPosStore = null;
        Vec3 diffVec = null;

        String name = null;

        Spawns tempStor = new Spawns(sp.getUUID(), (Settings.time * 60) * 20, zone);

        Random rnd = new Random();
        for (int i = 0; i < zone.subZones.size(); i++)
        {
            startPos = new Vec3(Math.min(zone.subZones.get(i).poss[0].x, zone.subZones.get(i).poss[1].x), Math.min(zone.subZones.get(i).poss[0].y, zone.subZones.get(i).poss[1].y), Math.min(zone.subZones.get(i).poss[0].z, zone.subZones.get(i).poss[1].z));
            endPos = new Vec3(Math.max(zone.subZones.get(i).poss[0].x, zone.subZones.get(i).poss[1].x), Math.max(zone.subZones.get(i).poss[0].y, zone.subZones.get(i).poss[1].y), Math.max(zone.subZones.get(i).poss[0].z, zone.subZones.get(i).poss[1].z));
            diffVec = new Vec3(Math.round(endPos.x - startPos.x), Math.round(endPos.y - startPos.y), Math.round(endPos.z - startPos.z));
            if (diffVec.x == 0)
            {
                diffVec = new Vec3(diffVec.x + 1, diffVec.y, diffVec.z);
            }
            if (diffVec.y == 0)
            {
                diffVec = new Vec3(diffVec.x, diffVec.y + 1, diffVec.z);
            }
            if (diffVec.z == 0)
            {
                diffVec = new Vec3(diffVec.x, diffVec.y, diffVec.z + 1);
            }


            for (int ii = 0; ii < zone.subZones.get(i).spawnCount; ii++)
            {
                tempPosStore = new Vec3(startPos.x + rnd.nextFloat((float)diffVec.x), startPos.y + rnd.nextFloat((float)diffVec.y), startPos.z + rnd.nextFloat((float)diffVec.z));
                        //new Vec3(startPos.x + rnd.nextFloat((float)diffVec.x), startPos.y + rnd.nextFloat((float)diffVec.y), startPos.z + rnd.nextFloat((float)diffVec.z));

                name = randomString(zone.subZones.get(i).cobblemans);
                tempStor.pokes.add(spawn(name, server, tempPosStore, String.valueOf(randomLevel(name, zone.subZones.get(i).cobblemans)), String.valueOf(rnd.nextFloat(0.5f, 2.0f))));
            }
        }

        playersActive.add(tempStor);
        tempStor = null;

    }

    private static int randomLevel(String name, ArrayList<String> names)
    {
        Random rnd = new Random();
        int level = 3;
        for (int i = 0; i < names.size(); i++)
        {
            if (names.get(i).equals(name))
            {
                level = Integer.parseInt(names.get(i + 1));
            }
        }




        if (level > 2)
        {
            int store = 0;
            for (int i = 0; i < 10; i++)
            {
                store += rnd.nextInt(0, level);
            }

            store = store / 10;

            level = store;
        }

        level += Settings.baseMaxLevel;

        level = rnd.nextInt(2, level);
        return level;
    }


    private static String randomString(ArrayList<String> names)
    {
        String name = null;
        int weight = 0;
        int rand = -1;

        Random rng = new Random();
        boolean done = false;

        while (!done)
        {
            rand = rng.nextInt(names.size());
            if (rand % 2 != 1 && rand != 0) rand--;
            else if (rand % 2 != 1 && rand == 0) rand++;

            weight = Integer.parseInt(names.get(rand));
            name = names.get(rand - 1);

            rand = rng.nextInt(100);

            if (rand >= weight)
            {
                done = true;
                return name;
            }
        }

        return null;
    }


    private static UUID spawn(String name, MinecraftServer server, Vec3 pos, String level, String scale)
    {
        PokemonEntity pokemonEntity = PokemonProperties.Companion.parse("species=" + name + " level=" + level + " scale=" + scale).createEntity(server.overworld());
        pokemonEntity.moveTo(pos.x, pos.y, pos.z);
        pokemonEntity.finalizeSpawn(server.overworld(), server.overworld().getCurrentDifficultyAt(new BlockPos((int)pos.x, (int)pos.y, (int)pos.z)), MobSpawnType.COMMAND, null);
        server.overworld().addFreshEntity(pokemonEntity);
        pokemonEntity.setPersistenceRequired();
        return pokemonEntity.getUUID();
    }
}
