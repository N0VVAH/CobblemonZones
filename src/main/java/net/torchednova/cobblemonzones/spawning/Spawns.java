package net.torchednova.cobblemonzones.spawning;

import net.torchednova.cobblemonzones.zones.Zone;

import java.util.ArrayList;
import java.util.UUID;

public class Spawns {
    public Spawns(UUID uuid, int timeAllowed, Zone z)
    {
        this.z = z;
        this.uuid = uuid;
        this.time = 0;
        this.timeAllowed = timeAllowed;
        this.pokes = new ArrayList<>();
    }
    public UUID uuid;
    public int time;
    public int timeAllowed;
    public ArrayList<UUID> pokes;
    public Zone z;
}
