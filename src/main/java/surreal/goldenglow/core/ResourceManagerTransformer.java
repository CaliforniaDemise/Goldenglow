package surreal.goldenglow.core;

import org.objectweb.asm.tree.*;

import java.util.Iterator;

public class ResourceManagerTransformer extends BasicTransformer {

    public static byte[] transformFileResourcePack(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("getResourceDomains")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == GETSTATIC) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(ALOAD, 5));
                        list.add(hook("loadRandomMobs", "(Ljava/lang/String;)V"));
                        method.instructions.insertBefore(node, list);
                        break;
                    }
                }
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformRender(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals("bindEntityTexture")) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).name.equals("getEntityTexture")) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(ALOAD, 1));
                        list.add(hook("getEntityTexture", "(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/ResourceLocation;"));
                        method.instructions.insert(node, list);
                        break;
                    }
                }
                break;
            }
        }
        writeClass(cls);
        return write(cls);
    }
}
