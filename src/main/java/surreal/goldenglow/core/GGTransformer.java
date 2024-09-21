package surreal.goldenglow.core;

import net.minecraft.launchwrapper.IClassTransformer;
import surreal.goldenglow.core.transformers.RandomMobsTransformer;

public class GGTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
            case "net.minecraft.client.resources.SimpleReloadableResourceManager": return RandomMobsTransformer.transformSimpleReloadableResourceManager(basicClass);
            case "net.minecraft.client.resources.FileResourcePack": return RandomMobsTransformer.transformFileResourcePack(basicClass);
            case "net.minecraft.client.resources.FolderResourcePack": return RandomMobsTransformer.transformFolderResourcePack(basicClass);
            case "net.minecraft.client.renderer.entity.Render": return RandomMobsTransformer.transformRender(basicClass);
        }
        return basicClass;
    }
}
