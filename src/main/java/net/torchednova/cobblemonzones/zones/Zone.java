package net.torchednova.cobblemonzones.zones;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.HashMap;

public class Zone {
    Zone(String name, int id) {
        this.name = name;
        this.id = id;
        this.Entrance = null;

        subZones = new ArrayList<>();


    }

    public Vec3 Entrance;

    public String name;
    public int id;
    public ArrayList<SubZones> subZones;

}
