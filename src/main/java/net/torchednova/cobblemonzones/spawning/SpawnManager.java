package net.torchednova.cobblemonzones.spawning;

import com.cobblemon.mod.common.api.permission.CobblemonPermissions;
import com.cobblemon.mod.common.api.pokemon.PokemonProperties;
import com.cobblemon.mod.common.api.pokemon.PokemonSpecies;
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity;

import com.cobblemon.mod.common.pokemon.Species;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.phys.Vec3;
import net.torchednova.cobblemonzones.zones.Zone;

import static com.mojang.text2speech.Narrator.LOGGER;

public class SpawnManager {
    public static void runSpawn(Zone zone, MinecraftServer server)
    {

    }

    private static void spawn(String name, MinecraftServer server, Vec3 pos)
    {
        PokemonEntity pokemonEntity = PokemonProperties.Companion.parse("species=Turtwig level=10").createEntity(server.overworld());
        pokemonEntity.moveTo(pos.x, pos.y, pos.z);
        pokemonEntity.finalizeSpawn(server.overworld(), server.overworld().getCurrentDifficultyAt(new BlockPos((int)pos.x, (int)pos.y, (int)pos.z)), MobSpawnType.COMMAND, null);
        server.overworld().addFreshEntity(pokemonEntity);
    }


}
