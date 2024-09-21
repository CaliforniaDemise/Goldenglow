package surreal.goldenglow.core;

import net.minecraft.launchwrapper.IClassTransformer;

public class GGTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
            case "net.minecraft.client.resources.FileResourcePack": return ResourceManagerTransformer.transformFileResourcePack(basicClass);
            case "net.minecraft.client.renderer.entity.Render": return ResourceManagerTransformer.transformRender(basicClass);
        }
        return basicClass;
    }
}
