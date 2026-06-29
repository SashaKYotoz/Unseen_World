package net.sashakyotoz.common.config;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.LevelResource;
import net.sashakyotoz.UnseenWorld;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;

public class WorldConfigController {
    private static final Gson gson = new Gson();
    public static final HashMap<Integer,ChimericDarknessData> data = new HashMap<>();

    public static ChimericDarknessData loadData(ServerLevel world) {
        Path pathToWorld = world.getServer().getWorldPath(LevelResource.ROOT).resolve("chimeric_darkness_controller.json");
        if (Files.exists(pathToWorld)) {
            try {
                String jsonString = new String(Files.readAllBytes(pathToWorld));
                JsonObject jsonObject = JsonParser.parseString(jsonString).getAsJsonObject();
                if (jsonObject != null) {
                    return new ChimericDarknessData(
                            jsonObject.get("are_stars_liberated").getAsBoolean(),
                            jsonObject.get("are_sun_liberated").getAsBoolean(),
                            jsonObject.get("are_galactic_liberated").getAsBoolean()
                    );
                } else
                    createController(world);
            } catch (IOException e) {
                createController(world);
            }
        } else
            createController(world);
        return null;
    }
    public static void updateSave(ServerLevel world){
        WorldConfigController.data.put(0, WorldConfigController.loadData(world));
    }
    public static void saveController(ServerLevel world, boolean starsUnlock, boolean sunUnlock, boolean galacticUnlock) {
        Path pathToWorld = world.getServer().getWorldPath(LevelResource.ROOT);
        try {
            if (!Files.exists(pathToWorld))
                createController(world);
            Path dataFile = pathToWorld.resolve("chimeric_darkness_controller.json");
            JsonObject mainObject = new JsonObject();
            mainObject.addProperty("are_stars_liberated", starsUnlock);
            mainObject.addProperty("are_sun_liberated", sunUnlock);
            mainObject.addProperty("are_galactic_liberated", galacticUnlock);
            Files.write(dataFile, gson.toJson(mainObject).getBytes());
        } catch (IOException e) {
            createController(world);
        }
    }

    public static void createController(ServerLevel world) {
        Path pathToWorld = world.getServer().getWorldPath(LevelResource.ROOT);
        try {
            if (!Files.exists(pathToWorld)) {
                Files.createDirectories(pathToWorld);
            }
            Path dataFile = pathToWorld.resolve("chimeric_darkness_controller.json");
            JsonObject mainObject = new JsonObject();
            mainObject.addProperty("are_stars_liberated", false);
            mainObject.addProperty("are_sun_liberated", false);
            mainObject.addProperty("are_galactic_liberated", false);
            Files.write(dataFile, gson.toJson(mainObject).getBytes());
        } catch (IOException e) {
            UnseenWorld.log().debug("Failed to create chimeric_darkness_controller.json");
        }
    }
}