package surreal.goldenglow.core;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import surreal.goldenglow.mcpatcher.randommobs.RandomMobs;

@SuppressWarnings("unused") // Used by transformers
public class GGHooks {

    // RandomMobs
    public static void RandomMobs$reloadMap() {
        RandomMobs.reloadTextureMap();
    }

    public static void RandomMobs$loadTexture(String entryName) {
        RandomMobs.loadTextureMap(entryName);
    }

    public static ResourceLocation RandomMobs$getEntityTexture(ResourceLocation defLoc, Entity entity) {
        ResourceLocation location = RandomMobs.getEntityTexture(entity);
        if (location == null) return defLoc;
        return location;
    }
}
