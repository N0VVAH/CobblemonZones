package net.torchednova.cobblemonzones.savedata;

import net.minecraft.server.MinecraftServer;

import java.nio.file.Path;

public class ModDataPath {
    public static Path getLadderDataFile(MinecraftServer server) {
        return server.getWorldPath(net.minecraft.world.level.storage.LevelResource.ROOT).resolve("data").resolve("cobblemonzones").resolve("zones.json");
    }
}
