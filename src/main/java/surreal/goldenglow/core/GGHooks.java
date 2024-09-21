package surreal.goldenglow.core;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import surreal.goldenglow.mcpatcher.randommobs.RandomMobs;

public class GGHooks {

    public static void loadRandomMobs(String entryName) {
        RandomMobs.loadTextureMap(entryName);
    }

    public static ResourceLocation getEntityTexture(ResourceLocation defLoc, Entity entity) {
        ResourceLocation location = RandomMobs.getEntityTexture(entity);
        if (location == null) return defLoc;
        return location;
    }
}
