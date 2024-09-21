package surreal.goldenglow.core;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import surreal.goldenglow.mcpatcher.randommobs.RandomMobs;

import java.io.File;

@SuppressWarnings("unused") // Used by transformers
public class GGHooks {

    public static void debug(File f) {
        LogManager.getLogger("TEST").info(f.getAbsolutePath());
    }

    // RandomMobs
    public static void RandomMobs$reloadMap() {
        RandomMobs.reloadTextureMap();
    }

    // Used in FolderResourcePack
    public static void RandomMobs$loadTexture(File folder) {
       RandomMobs.loadTextureFromFolder(folder);
    }

    // Used in FileResourcePack
    public static void RandomMobs$loadTexture(String entryName) {
        RandomMobs.loadTextureFromPath(entryName);
    }

    public static ResourceLocation RandomMobs$getEntityTexture(ResourceLocation defLoc, Entity entity) {
        ResourceLocation location = RandomMobs.getEntityTexture(entity);
        if (location == null) return defLoc;
        return location;
    }
}
