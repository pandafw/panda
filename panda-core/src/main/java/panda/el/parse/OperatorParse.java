package panda.el.parse;

import panda.el.ELException;
import panda.el.opt.LBraceOp;
import panda.el.opt.LBracketOp;
import panda.el.opt.LParenthesisOp;
import panda.el.opt.RBraceOp;
import panda.el.opt.RBracketOp;
import panda.el.opt.RParenthesisOp;
import panda.el.opt.bit.BitAnd;
import panda.el.opt.bit.BitLeft;
import panda.el.opt.bit.BitNot;
import panda.el.opt.bit.BitOr;
import panda.el.opt.bit.BitRight;
import panda.el.opt.bit.BitUnsignedRight;
import panda.el.opt.bit.BitXor;
import panda.el.opt.logic.LogicAnd;
import panda.el.opt.logic.LogicEq;
import panda.el.opt.logic.LogicGt;
import panda.el.opt.logic.LogicGte;
import panda.el.opt.logic.LogicLt;
import panda.el.opt.logic.LogicLte;
import panda.el.opt.logic.LogicNeq;
import panda.el.opt.logic.LogicNot;
import panda.el.opt.logic.LogicNullable;
import panda.el.opt.logic.LogicOr;
import panda.el.opt.logic.LogicOrable;
import panda.el.opt.logic.LogicQuestion;
import panda.el.opt.logic.LogicQuestionSelect;
import panda.el.opt.math.MathAdd;
import panda.el.opt.math.MathDiv;
import panda.el.opt.math.MathMod;
import panda.el.opt.math.MathMul;
import panda.el.opt.math.MathSub;
import panda.el.opt.object.AccessOp;
import panda.el.opt.object.CommaOp;
import panda.el.opt.object.StaticOp;

/**
 * Operator parse
 */
public class OperatorParse implements Parse {

	public Object fetchItem(CharQueue exp) {
		switch (exp.peek()) {
		case '+':
			exp.poll();
			return new MathAdd();
		case '-':
			exp.poll();
			return new MathSub();
		case '*':
			exp.poll();
			return new MathMul();
		case '/':
			exp.poll();
			return new MathDiv();
		case '%':
			exp.poll();
			return new MathMod();
		case '(':
			exp.poll();
			return LParenthesisOp.INSTANCE;
		case ')':
			exp.poll();
			return RParenthesisOp.INSTANCE;
		case '[':
			exp.poll();
			return LBracketOp.INSTANCE;
		case ']':
			exp.poll();
			return RBracketOp.INSTANCE;
		case '{':
			exp.poll();
			return LBraceOp.INSTANCE;
		case '}':
			exp.poll();
			return RBraceOp.INSTANCE;
		case '>':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new LogicGte();
			case '>':
				exp.poll();
				if (exp.peek() == '>') {
					exp.poll();
					return new BitUnsignedRight();
				}
				return new BitRight();
			}
			return new LogicGt();
		case '<':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new LogicLte();
			case '<':
				exp.poll();
				return new BitLeft();
			}
			return new LogicLt();
		case '=':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new LogicEq();
			}
			throw new ELException("Incorrect expression, illegal character after '='");
		case '!':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new LogicNeq();
			case '!':
				exp.poll();
				return new LogicNullable();
			}
			return new LogicNot();
		case '|':
			exp.poll();
			switch (exp.peek()) {
			case '|':
				exp.poll();
				if (exp.peek() == '|') {
					exp.poll();
					return new LogicOrable();
				}
				return new LogicOr();
			}
			return new BitOr();
		case '&':
			exp.poll();
			switch (exp.peek()) {
			case '&':
				exp.poll();
				return new LogicAnd();
			}
			return new BitAnd();
		case '~':
			exp.poll();
			return new BitNot();
		case '^':
			exp.poll();
			return new BitXor();
		case '?':
			exp.poll();
			return new LogicQuestion();
		case ':':
			exp.poll();
			return new LogicQuestionSelect();
		case '.':
			if (!Character.isJavaIdentifierStart(exp.peek(1))) {
				return null;
			}
			exp.poll();
			return new AccessOp();
		case '@':
			if (!Character.isJavaIdentifierStart(exp.peek(1))) {
				return null;
			}
			exp.poll();
			return new StaticOp();
		case ',':
			exp.poll();
			return new CommaOp();
		}
		return null;
	}

}
