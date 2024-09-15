package surreal.goldenglow.core;

import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;

public abstract class BasicTransformer implements Opcodes {

    protected static MethodInsnNode hook(String name, String desc) {
        return new MethodInsnNode(INVOKESTATIC, "surreal/goldenglow/core/GGHooks", name, desc, false);
    }

    protected static String getName(String mcpName, String srgName) {
        return FMLLaunchHandler.isDeobfuscatedEnvironment() ? mcpName : srgName;
    }

    protected static ClassNode read(byte[] bytes) {
        ClassReader reader = new ClassReader(bytes);
        ClassNode cls = new ClassNode();
        reader.accept(cls, 0);
        return cls;
    }

    protected static byte[] write(ClassNode cls) {
        return write(cls, ClassWriter.COMPUTE_MAXS);
    }

    protected static byte[] write(ClassNode cls, int options) {
        ClassWriter writer = new ClassWriter(options);
        cls.accept(writer);
        return writer.toByteArray();
    }

    protected static void writeClass(ClassNode cls) {
        File file = new File("classOut/" + cls.name + ".class");
        file.getParentFile().mkdirs();
        try (OutputStream stream = Files.newOutputStream(file.toPath())) {
            ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_MAXS);
            cls.accept(writer);
            stream.write(writer.toByteArray());
        } catch (IOException ignored) {
        }
    }

    protected static AbstractInsnNode getTail(MethodNode method) {
        int opcode = -1;
        char lastChar = method.desc.charAt(method.desc.length() - 1);
        switch (lastChar) {
            case 'V': opcode = RETURN; break;
            case ';': opcode = ARETURN; break;
            case 'I':
            case 'Z':
            case 'S':
            case 'B': opcode = IRETURN; break;
            case 'D': opcode = DRETURN; break;
            case 'F': opcode = FRETURN; break;
            case 'J': opcode = LRETURN; break;
        }
        if (opcode == -1) throw new RuntimeException("Could not find the returning type when getting the tail of " + method.name);
        AbstractInsnNode lastNode = method.instructions.getLast();
        while (lastNode.getOpcode() != opcode)  lastNode = lastNode.getPrevious();
        return lastNode;
    }

    protected static MethodNode clinit(ClassNode cls) {
        return cls.methods.get(cls.methods.size() - 1);
    }
}