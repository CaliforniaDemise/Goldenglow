package surreal.goldenglow.mcpatcher.randommobs;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.boss.EntityDragon;
import net.minecraft.util.ResourceLocation;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public class RandomMobs {

    // key: entity registry name | value: texture list
    private static final Object2ObjectMap<ResourceLocation, List<ResourceLocation>> TEXTURE_MAP = new Object2ObjectOpenHashMap<>();

    public static void reloadTextureMap() {
        TEXTURE_MAP.clear();
    }

    public static ResourceLocation getEntityTexture(Entity entity, ResourceLocation defLoc) {
        List<ResourceLocation> list = getEntityTextureList(entity, defLoc);
        if (list == null) return null;
        UUID uuid = entity.getUniqueID();
        int random = nextInt(uuid.getMostSignificantBits(), uuid.getLeastSignificantBits(), list.size());
        return list.get(random);
    }

    public static List<ResourceLocation> getEntityTextureList(Entity entity, ResourceLocation defLoc) {
        return TEXTURE_MAP.get(defLoc);
    }

    public static void loadTextureFromFolder(File folder) {
        try (Stream<Path> s = Files.walk(folder.toPath())) {
            s.forEach(p -> {
                File file = p.toFile();
                String absolutePath = file.getPath();
                if (!file.isDirectory()) {
                   loadTextureFromPath(absolutePath.substring(folder.getAbsolutePath().length() + 1));
                }
            });
        }
        catch (IOException e) {
            throw new RuntimeException("Could not walk through file " + folder.getAbsolutePath(), e);
        }
    }

    public static void loadTextureFromPath(String entryName) {
        if (entryName.endsWith(".png") && entryName.contains("mcpatcher/mob/")) {
            String textureLocation;
            {
                char c;
                StringBuilder builder = new StringBuilder();
                boolean check = false;
                for (int i = 7; i < entryName.length(); i++) {
                    c = entryName.charAt(i);
                    if (!check && c == '/') {
                        check = true;
                        builder.append(':');
                    }
                    else builder.append(c);
                }

                textureLocation = builder.toString();
            }

            StringBuilder builder = new StringBuilder(".png");
            boolean check = false;
            boolean hasDigit = false;
            int i = entryName.length() - 5;
            while (i > 6) {
                char c = entryName.charAt(i);
                if (check || !Character.isDigit(c)) {
                    if (builder.length() >= 10 && builder.toString().startsWith("/mcpatcher/")) {
                        builder.delete(0, 14);
                        builder.insert(0, ":textures/entity");
                    }
                    builder.insert(0, c);
                }
                else hasDigit = true;
                if (c == '/') check = true;
                i--;
            }
            
            String defLocation = builder.toString();
            ResourceLocation key = new ResourceLocation(defLocation);
            List<ResourceLocation> list = TEXTURE_MAP.get(key);
            if (list == null) {
                list = new ArrayList<>();
                TEXTURE_MAP.put(key, list);
            }
            
            ResourceLocation value = new ResourceLocation(textureLocation);
            System.out.println(defLocation + "   " + textureLocation);

            if (list.isEmpty()) list.add(null);
            if (!hasDigit) list.set(0, value);
            else list.add(value); 
        }
    }

    private static int nextInt(long most, long least, int bound) {
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
