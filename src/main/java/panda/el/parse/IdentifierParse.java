package panda.el.parse;

import panda.el.Parse;
import panda.el.obj.AbstractObj;
import panda.el.obj.IdentifierObj;

/**
 * 标识符转换
 * @author juqkai(juqkai@gmail.com)
 *
 */
public class IdentifierParse implements Parse{

    public Object fetchItem(CharQueue exp) {
        StringBuilder sb = new StringBuilder();
        if(Character.isJavaIdentifierStart(exp.peek())){
            sb.append(exp.poll());
            while(!exp.isEmpty() && Character.isJavaIdentifierPart(exp.peek())){
                sb.append(exp.poll());
            }
            if(sb.toString().equals("null")){
                return new IdentifierObj(null);
            }
            if(sb.toString().equals("true")){
                return Boolean.TRUE;
            }
            if(sb.toString().equals("false")){
                return Boolean.FALSE;
            }
            return new AbstractObj(sb.toString());
        }
        return nullobj;
    }

}
