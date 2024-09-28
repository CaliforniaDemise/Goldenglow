package surreal.goldenglow.core.transformers;

import org.objectweb.asm.tree.*;
import surreal.goldenglow.core.BasicTransformer;

import java.util.Iterator;

public class RandomMobsTransformer extends BasicTransformer {

    public static byte[] transformSimpleReloadableResourceManager(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("clearResources", "func_110543_a"))) {
                AbstractInsnNode node = method.instructions.getLast();
                while (node.getOpcode() != RETURN) node = node.getPrevious();
                method.instructions.insertBefore(node, hook("RandomMobs$reloadMap", "()V"));
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformFileResourcePack(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("getResourceDomains", "func_110587_b"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == GETSTATIC) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(ALOAD, 5));
                        list.add(hook("RandomMobs$loadTexture", "(Ljava/lang/String;)V"));
                        method.instructions.insertBefore(node, list);
                        break;
                    }
                }
                break;
            }
        }
        return write(cls);
    }

    public static byte[] transformFolderResourcePack(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("getResourceDomains", "func_110587_b"))) {
                AbstractInsnNode node = method.instructions.getFirst();
                while (node.getOpcode() != INVOKESTATIC) node = node.getNext();
                InsnList list = new InsnList();
                list.add(new VarInsnNode(ALOAD, 0));
                list.add(new FieldInsnNode(GETFIELD, "net/minecraft/client/resources/FolderResourcePack", getName("resourcePackFile", "field_110597_b"), "Ljava/io/File;"));
                list.add(hook("RandomMobs$loadTexture", "(Ljava/io/File;)V"));
                method.instructions.insertBefore(node, list);
               break;
            }
        }
        return write(cls);
    }

    public static byte[] transformRender(byte[] basicClass) {
        ClassNode cls = read(basicClass);
        for (MethodNode method : cls.methods) {
            if (method.name.equals(getName("bindEntityTexture", "func_180548_c"))) {
                Iterator<AbstractInsnNode> iterator = method.instructions.iterator();
                while (iterator.hasNext()) {
                    AbstractInsnNode node = iterator.next();
                    if (node.getOpcode() == INVOKEVIRTUAL && ((MethodInsnNode) node).name.equals(getName("getEntityTexture", "func_110775_a"))) {
                        InsnList list = new InsnList();
                        list.add(new VarInsnNode(ALOAD, 1));
                        list.add(hook("RandomMobs$getEntityTexture", "(Lnet/minecraft/util/ResourceLocation;Lnet/minecraft/entity/Entity;)Lnet/minecraft/util/ResourceLocation;"));
                        method.instructions.insert(node, list);
                        break;
                    }
                }
                break;
            }
        }
        return write(cls);
    }
}
