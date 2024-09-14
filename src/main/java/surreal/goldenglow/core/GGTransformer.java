package surreal.goldenglow.core;

import net.minecraft.launchwrapper.IClassTransformer;

public class GGTransformer implements IClassTransformer {

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        switch (transformedName) {
            default: return basicClass;
        }
    }
}
