package surreal.goldenglow.mcpatcher.randommobs;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// TODO Make it work with FolderResourcePack
public class RandomMobs {

    // key: entity registry name | value: texture list
    private static Object2ObjectMap<ResourceLocation, List<ResourceLocation>> TEXTURE_MAP = new Object2ObjectOpenHashMap<>();

    public static void reloadTextureMap() {
        TEXTURE_MAP = new Object2ObjectOpenHashMap<>();
    }

    public static void loadTextureMap(String entryName) {
        if (entryName.endsWith(".png") && entryName.contains("mcpatcher/mob/")) {
            StringBuilder builder = new StringBuilder();
            int i = entryName.length() - 5;
            char c;
            while ((c = entryName.charAt(i)) != '/') {
                if (Character.isAlphabetic(c)) builder.insert(0, c);
                i--;
            }

            String path = builder.toString();

            builder = new StringBuilder();

            i = 7;
            while ((c = entryName.charAt(i)) != '/') {
                builder.append(c);
                i++;
            }

            String domain = builder.toString();

            ResourceLocation entityLoc = new ResourceLocation(domain, path);
            List<ResourceLocation> list = TEXTURE_MAP.get(entityLoc);
            if (list == null) {
                list = new ArrayList<>();
                TEXTURE_MAP.put(entityLoc, list);
            }

            list.add(new ResourceLocation(domain, entryName.substring(i + 1)));
        }
    }

    public static ResourceLocation getEntityTexture(Entity entity) {
        List<ResourceLocation> list = getEntityTextureList(entity);
        if (list == null) return null;
        UUID uuid = entity.getUniqueID();
        int random = nextInt(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), list.size() + 1);
        if (random == 0) return null;
        return list.get(random - 1);
    }

    public static List<ResourceLocation> getEntityTextureList(Entity entity) {
        ResourceLocation entityLoc = EntityList.getKey(entity);
        return TEXTURE_MAP.get(entityLoc);
    }

    public static int nextInt(long most, long least, int bound) {
        most ^= most << 13;
        most ^= most >> 7;
        most ^= most << 17;
        least ^= least << 13;
        least ^= least >> 7;
        least ^= least << 17;
        int out = (int) (most | least);
        if (out < 0) out *= -1;
        return out % bound;
    }
}
