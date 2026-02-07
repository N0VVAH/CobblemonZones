package net.torchednova.cobblemonzones.savedata;

import com.google.common.reflect.TypeToken;
import net.minecraft.server.MinecraftServer;
import net.torchednova.cobblemonzones.zones.Zone;
import net.torchednova.cobblemonzones.zones.ZoneManager;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TargetDataStorage {
    private static final Type LIST_TYPE = new TypeToken<List<Zone>>() {}.getType();

    private static final Type STRING_TYPE = new TypeToken<String>() {}.getType();

    public static void save(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getLadderDataFile(server);

            Path parent = file.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }

            String json = ModJson.GSON.toJson(ZoneManager.zones);
            Files.writeString(file, json);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static ArrayList<Zone> load(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getLadderDataFile(server);

            if (Files.exists(file) == false)
            {
                return new ArrayList<Zone>();
            }

            String json = Files.readString(file);

            ArrayList<Zone> data = ModJson.GSON.fromJson(json, LIST_TYPE);

            return data != null ? data : new ArrayList<>();

        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<Zone>();
        }
    }

    public static void saveIRS(MinecraftServer server)
    {
        try{
            Path file = ModDataPath.getIRSDataFile(server);

            Path parent = file.getParent();
            if (parent == null) {
                Files.createDirectories(parent);
                String json = ModJson.GSON.toJson("ENTER API KEY HERE");
                Files.writeString(file, json);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static String loadIRS(MinecraftServer server)
    {
        saveIRS(server);

        try{
            Path file = ModDataPath.getIRSDataFile(server);

            if (Files.exists(file) == false)
            {
                return "";
            }

            String json = Files.readString(file);

            String data = ModJson.GSON.fromJson(json, STRING_TYPE);

            return data != null ? data : "";

        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
