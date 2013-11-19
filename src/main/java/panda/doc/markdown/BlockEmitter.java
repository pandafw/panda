package panda.doc.markdown;

import java.util.List;

/**
 * Block emitter interface. An example for a code block emitter is given below:
 * 
 * <pre>
 * <code>public void emitBlock(StringBuilder out, List&lt;String&gt; lines, String meta)
 * {
 *     out.append("&lt;pre>&lt;code>");
 *     for(final String s : lines)
 *     {
 *         for(int i = 0; i < s.length(); i++)
 *         {
 *             final char c = s.charAt(i);
 *             switch(c)
 *             {
 *             case '&':
 *                 out.append("&amp;amp;");
 *                 break;
 *             case '&lt;':
 *                 out.append("&amp;lt;");
 *                 break;
 *             case '&gt;':
 *                 out.append("&amp;gt;");
 *                 break;
 *             default:
 *                 out.append(c);
 *                 break;
 *             }
 *         }
 *         out.append('\n');
 *     }
 *     out.append("&lt;/code>&lt;/pre>\n");
 * }
 * </code>
 * </pre>
 * 
 * @author René Jeschke <rene_jeschke@yahoo.de>
 */
public interface BlockEmitter {
	/**
	 * This method is responsible for outputting a markdown block and for any needed pre-processing
	 * like escaping HTML special characters.
	 * 
	 * @param out The StringBuilder to append to
	 * @param lines List of lines
	 * @param meta Meta information as a single String (if any) or empty String
	 */
	public void emitBlock(StringBuilder out, List<String> lines, String meta);
}
