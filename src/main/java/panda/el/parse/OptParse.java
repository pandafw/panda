package panda.el.parse;

import panda.el.ElException;
import panda.el.Parse;
import panda.el.opt.arithmetic.DivOpt;
import panda.el.opt.arithmetic.LBracketOpt;
import panda.el.opt.arithmetic.ModOpt;
import panda.el.opt.arithmetic.MulOpt;
import panda.el.opt.arithmetic.PlusOpt;
import panda.el.opt.arithmetic.RBracketOpt;
import panda.el.opt.arithmetic.SubOpt;
import panda.el.opt.bit.BitAnd;
import panda.el.opt.bit.BitNot;
import panda.el.opt.bit.BitOr;
import panda.el.opt.bit.BitXro;
import panda.el.opt.bit.LeftShift;
import panda.el.opt.bit.RightShift;
import panda.el.opt.bit.UnsignedLeftShift;
import panda.el.opt.logic.AndOpt;
import panda.el.opt.logic.EQOpt;
import panda.el.opt.logic.GTEOpt;
import panda.el.opt.logic.GTOpt;
import panda.el.opt.logic.LTEOpt;
import panda.el.opt.logic.LTOpt;
import panda.el.opt.logic.NEQOpt;
import panda.el.opt.logic.NotOpt;
import panda.el.opt.logic.OrOpt;
import panda.el.opt.logic.QuestionOpt;
import panda.el.opt.logic.QuestionSelectOpt;
import panda.el.opt.object.AccessOpt;
import panda.el.opt.object.ArrayOpt;
import panda.el.opt.object.CommaOpt;
import panda.el.opt.object.FetchArrayOpt;

/**
 * 操作符转换器
 * 
 * @author juqkai(juqkai@gmail.com)
 */
public class OptParse implements Parse {

	public Object fetchItem(CharQueue exp) {
		switch (exp.peek()) {
		case '+':
			exp.poll();
			return new PlusOpt();
		case '-':
			exp.poll();
			return new SubOpt();
		case '*':
			exp.poll();
			return new MulOpt();
		case '/':
			exp.poll();
			return new DivOpt();
		case '%':
			exp.poll();
			return new ModOpt();
		case '(':
			exp.poll();
			return new LBracketOpt();
		case ')':
			exp.poll();
			return new RBracketOpt();
		case '>':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new GTEOpt();
			case '>':
				exp.poll();
				if (exp.peek() == '>') {
					exp.poll();
					return new UnsignedLeftShift();
				}
				return new RightShift();
			}
			return new GTOpt();
		case '<':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new LTEOpt();
			case '<':
				exp.poll();
				return new LeftShift();
			}
			return new LTOpt();
		case '=':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new EQOpt();
			}
			throw new ElException("Incorrect expression, illegal character after '='");
		case '!':
			exp.poll();
			switch (exp.peek()) {
			case '=':
				exp.poll();
				return new NEQOpt();
			}
			return new NotOpt();
		case '|':
			exp.poll();
			switch (exp.peek()) {
			case '|':
				exp.poll();
				return new OrOpt();
			}
			return new BitOr();
		case '&':
			exp.poll();
			switch (exp.peek()) {
			case '&':
				exp.poll();
				return new AndOpt();
			}
			return new BitAnd();
		case '~':
			exp.poll();
			return new BitNot();
		case '^':
			exp.poll();
			return new BitXro();
		case '?':
			exp.poll();
			return new QuestionOpt();
		case ':':
			exp.poll();
			return new QuestionSelectOpt();

		case '.':
			if (!Character.isJavaIdentifierStart(exp.peek(1))) {
				return nullobj;
			}
			exp.poll();
			return new AccessOpt();
		case ',':
			exp.poll();
			return new CommaOpt();
		case '[':
			exp.poll();
			return new Object[] { new ArrayOpt(), new LBracketOpt() };
		case ']':
			exp.poll();
			return new Object[] { new RBracketOpt(), new FetchArrayOpt() };
		}
		return nullobj;
	}

}
