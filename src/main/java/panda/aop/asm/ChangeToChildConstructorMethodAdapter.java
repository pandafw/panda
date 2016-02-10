package panda.aop.asm;

import static panda.asm.Opcodes.ALOAD;
import static panda.asm.Opcodes.INVOKESPECIAL;
import static panda.asm.Opcodes.RETURN;

import panda.asm.MethodVisitor;

/**
 * @author wendal(wendal1985@gmail.com)
 */
class ChangeToChildConstructorMethodAdapter extends NormalMethodAdapter {

	private String superClassName;

	ChangeToChildConstructorMethodAdapter(MethodVisitor mv, String desc, int access, String superClassName) {
		super(mv, desc, access);
		this.superClassName = superClassName;
	}

	void visitCode() {
		mv.visitCode();
		mv.visitVarInsn(ALOAD, 0);
		loadArgs();
		mv.visitMethodInsn(INVOKESPECIAL, superClassName, "<init>", desc);
		mv.visitInsn(RETURN);
		mv.visitMaxs(2, 2);
		mv.visitEnd();
	}
}
