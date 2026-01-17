package net.torchednova.cobblemonzones.zones;

import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class SubZones {
    public SubZones(int id, Vec3[] poss)
    {
        this.id = id;
        this.poss = poss;
        this.cobblemans = new ArrayList<>();
    }

    public int id;
    public Vec3[] poss;
    public ArrayList<String> cobblemans;

}
