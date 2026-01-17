package net.torchednova.cobblemonzones.zones;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.phys.Vec3;
import net.torchednova.cobblemonzones.savedata.TargetDataStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class ZoneManager {
    public static ArrayList<Zone> zones;

    public static HashMap<UUID, Vec3[]> tempPosStor;

    public static void storePosOne(Vec3 pos, UUID uuid)
    {
        Vec3 tempPos = null;
        if (tempPosStor.containsKey(uuid))
        {
            tempPos = tempPosStor.get(uuid)[1];
            tempPosStor.remove(uuid);
        }

        tempPosStor.put(uuid, new Vec3[]{pos, tempPos});
    }
    public static void storePosTwo(Vec3 pos, UUID uuid)
    {
        Vec3 tempPos = null;
        if (tempPosStor.containsKey(uuid))
        {
            tempPos = tempPosStor.get(uuid)[0];
            tempPosStor.remove(uuid);
        }

        tempPosStor.put(uuid, new Vec3[]{tempPos, pos});
    }

    public static Vec3[] getTempPoss(UUID uuid)
    {
        if (tempPosStor.containsKey(uuid))
        {
            return tempPosStor.get(uuid);
        }
        else
        {
            return null;
        }
    }

    public static void deleteTempPos(UUID uuid)
    {
        if (tempPosStor.containsKey(uuid))
        {
            tempPosStor.remove(uuid);
        }
    }




    public static void init(MinecraftServer server)
    {
        zones = TargetDataStorage.load(server);
        tempPosStor = new HashMap<>();
    }

    public static int newZone(String name)
    {
        zones.add(new Zone(name, zones.size()));
        return zones.size();
    }

    public static Zone getZone(int id)
    {
        for (int i = 0; i < zones.size(); i++)
        {
            if (zones.get(i).id == id)
            {
                return zones.get(i);
            }
        }

        return null;
    }

    public static Zone getZone(String name)
    {
        for (int i = 0; i < zones.size(); i++)
        {
            if (Objects.equals(zones.get(i).name, name))
            {
                return zones.get(i);
            }
        }

        return null;
    }

    public static void addBox(String name, Vec3[] poss)
    {
        for (int i = 0; i < zones.size(); i++)
        {
            if (Objects.equals(zones.get(i).name, name))
            {
                int newId = zones.get(i).subZones.size();
                zones.get(i).subZones.add(new SubZones(newId, new Vec3[] {poss[0], poss[1]}));
            }
        }
    }

    public static void addEntr(String name, Vec3 entr)
    {
        for (int i = 0; i < zones.size(); i++)
        {
            if (Objects.equals(zones.get(i).name, name))
            {
                zones.get(i).Entrance = entr;
            }
        }
    }






}
