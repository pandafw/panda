package panda.aop.asm;

import panda.aop.InterceptorChain;
import panda.asm.Attribute;
import panda.asm.Label;
import panda.asm.MethodVisitor;
import panda.asm.Opcodes;
import panda.asm.Type;

/**
 * @author wendal(wendal1985@gmail.com)
 */
class AopMethodAdapter extends NormalMethodAdapter implements Opcodes {

	int methodIndex;

	String myName;

	String enhancedSuperName;

	String methodName;

	Type returnType;

	AopMethodAdapter(MethodVisitor mv, int access, String methodName, String desc, int methodIndex, String myName,
			String enhancedSuperName) {
		super(mv, desc, access);
		this.methodIndex = methodIndex;
		this.myName = myName;
		this.enhancedSuperName = enhancedSuperName;
		this.methodName = methodName;
		this.returnType = Type.getReturnType(desc);
	}

	static final String AOPIC_CLS_PATH = InterceptorChain.class.getName().replace('.', '/');
	
	void enhandMethod_Void() {
		mv.visitCode();
		Label l0 = new Label();
		Label l1 = new Label();
		Label l2 = new Label();
		mv.visitTryCatchBlock(l0, l1, l2, "java/lang/Throwable");
		mv.visitLabel(l0);
		mv.visitTypeInsn(NEW, AOPIC_CLS_PATH);
		mv.visitInsn(DUP);
		visitX(methodIndex);
		mv.visitVarInsn(ALOAD, 0);
		mv.visitFieldInsn(GETSTATIC, myName, AsmClassAgent.MethodArray_FieldName, "[Ljava/lang/reflect/Method;");
		visitX(methodIndex);
		mv.visitInsn(AALOAD);
		mv.visitFieldInsn(GETSTATIC, myName, AsmClassAgent.MethodInterceptorList_FieldName, "[Ljava/util/List;");
		visitX(methodIndex);
		mv.visitInsn(AALOAD);
		loadArgsAsArray();
		mv.visitMethodInsn(INVOKESPECIAL, AOPIC_CLS_PATH, "<init>",
			"(ILjava/lang/Object;Ljava/lang/reflect/Method;Ljava/util/List;[Ljava/lang/Object;)V");
		mv.visitMethodInsn(INVOKEVIRTUAL, AOPIC_CLS_PATH, "doChain",
			"()L" + AOPIC_CLS_PATH + ";");

		{
			if (Type.getReturnType(desc).equals(Type.VOID_TYPE)) {
				mv.visitInsn(POP);
			}
			else {
				mv.visitMethodInsn(INVOKEVIRTUAL, AOPIC_CLS_PATH, "getReturn", "()Ljava/lang/Object;");
				AsmHelper.checkCast(returnType, mv);
				returnIt();
			}
		}

		mv.visitLabel(l1);
		Label l3 = new Label();
		mv.visitJumpInsn(GOTO, l3);
		mv.visitLabel(l2);
		mv.visitVarInsn(ASTORE, 3);
		mv.visitVarInsn(ALOAD, 3);
		// mv.visitMethodInsn(INVOKESTATIC, AOPIC_CLS_PATH, "wrapThrow",
		// "(Ljava/lang/Throwable;)Ljava/lang/RuntimeException;");
		mv.visitInsn(ATHROW);
		mv.visitLabel(l3);
		mv.visitInsn(RETURN);
		mv.visitMaxs(8, 4);
		mv.visitEnd();
	}

	void visitCode() {
		enhandMethod_Void();
	}

	void loadArgsAsArray() {
		visitX(argumentTypes.length);
		mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
		int index = 1;
		for (int i = 0; i < argumentTypes.length; i++) {
			mv.visitInsn(DUP);
			visitX(i);
			Type t = argumentTypes[i];
			loadInsn(t, index);
			index += t.getSize();
			AsmHelper.packagePrivateData(t, mv);
			mv.visitInsn(AASTORE);
		}
	}

	void visitX(int i) {
		if (i < 6) {
			mv.visitInsn(i + ICONST_0);
		}
		else {
			if (i < Byte.MAX_VALUE)
				mv.visitIntInsn(BIPUSH, i);
			else
				mv.visitIntInsn(SIPUSH, i);
		}
	}

	void returnIt() {
		mv.visitInsn(returnType.getOpcode(IRETURN));
	}

	void visitAttribute() {
		Attribute attr = new Attribute("LocalVariableTable", new byte[] { 0, 0 });
		mv.visitAttribute(attr);
	}
}
