package panda.doc.markdown.html;

import panda.doc.markdown.Decorator;

/**
 * Default Decorator implementation.
 * <p>
 * Example for a user Decorator having a class attribute on &lt;p> tags.
 * </p>
 * 
 * <pre>
 * <code>public class MyDecorator extends DefaultDecorator
 * {
 *     &#64;Override
 *     public void openParagraph(StringBuilder out)
 *     {
 *         out.append("&lt;p class=\"myclass\">");
 *     }
 * }
 * </code>
 * </pre>
 */
public class DefaultDecorator implements Decorator {
	/** Constructor. */
	public DefaultDecorator() {
		// empty
	}

	/** @see panda.doc.markdown.Decorator#openParagraph(StringBuilder) */
	@Override
	public void openParagraph(StringBuilder out) {
		out.append("<p>");
	}

	/** @see panda.doc.markdown.Decorator#closeParagraph(StringBuilder) */
	@Override
	public void closeParagraph(StringBuilder out) {
		out.append("</p>\n");
	}

	/** @see panda.doc.markdown.Decorator#openBlockquote(StringBuilder) */
	@Override
	public void openBlockquote(StringBuilder out) {
		out.append("<blockquote>");
	}

	/** @see panda.doc.markdown.Decorator#closeBlockquote(StringBuilder) */
	@Override
	public void closeBlockquote(StringBuilder out) {
		out.append("</blockquote>\n");
	}

	/** @see panda.doc.markdown.Decorator#openCodeBlock(StringBuilder) */
	@Override
	public void openCodeBlock(StringBuilder out) {
		out.append("<pre><code>");
	}

	/** @see panda.doc.markdown.Decorator#closeCodeBlock(StringBuilder) */
	@Override
	public void closeCodeBlock(StringBuilder out) {
		out.append("</code></pre>\n");
	}

	/** @see panda.doc.markdown.Decorator#openCodeSpan(StringBuilder) */
	@Override
	public void openCodeSpan(StringBuilder out) {
		out.append("<code>");
	}

	/** @see panda.doc.markdown.Decorator#closeCodeSpan(StringBuilder) */
	@Override
	public void closeCodeSpan(StringBuilder out) {
		out.append("</code>");
	}

	/**
	 * @see panda.doc.markdown.Decorator#openHeadline(StringBuilder, int)
	 */
	@Override
	public void openHeadline(StringBuilder out, int level) {
		out.append("<h");
		out.append(level);
	}

	/**
	 * @see panda.doc.markdown.Decorator#closeHeadline(StringBuilder, int)
	 */
	@Override
	public void closeHeadline(StringBuilder out, int level) {
		out.append("</h");
		out.append(level);
		out.append(">\n");
	}

	/** @see panda.doc.markdown.Decorator#openStrong(StringBuilder) */
	@Override
	public void openStrong(StringBuilder out) {
		out.append("<strong>");
	}

	/** @see panda.doc.markdown.Decorator#closeStrong(StringBuilder) */
	@Override
	public void closeStrong(StringBuilder out) {
		out.append("</strong>");
	}

	/** @see panda.doc.markdown.Decorator#openStrike(StringBuilder) */
	@Override
	public void openStrike(StringBuilder out) {
		out.append("<s>");
	}

	/** @see panda.doc.markdown.Decorator#closeStrike(StringBuilder) */
	@Override
	public void closeStrike(StringBuilder out) {
		out.append("</s>");
	}

	/** @see panda.doc.markdown.Decorator#openEmphasis(StringBuilder) */
	@Override
	public void openEmphasis(StringBuilder out) {
		out.append("<em>");
	}

	/** @see panda.doc.markdown.Decorator#closeEmphasis(StringBuilder) */
	@Override
	public void closeEmphasis(StringBuilder out) {
		out.append("</em>");
	}

	/** @see panda.doc.markdown.Decorator#openSuper(StringBuilder) */
	@Override
	public void openSuper(StringBuilder out) {
		out.append("<sup>");
	}

	/** @see panda.doc.markdown.Decorator#closeSuper(StringBuilder) */
	@Override
	public void closeSuper(StringBuilder out) {
		out.append("</sup>");
	}

	/** @see panda.doc.markdown.Decorator#openOrderedList(StringBuilder) */
	@Override
	public void openOrderedList(StringBuilder out) {
		out.append("<ol>\n");
	}

	/** @see panda.doc.markdown.Decorator#closeOrderedList(StringBuilder) */
	@Override
	public void closeOrderedList(StringBuilder out) {
		out.append("</ol>\n");
	}

	/** @see panda.doc.markdown.Decorator#openUnorderedList(StringBuilder) */
	@Override
	public void openUnorderedList(StringBuilder out) {
		out.append("<ul>\n");
	}

	/** @see panda.doc.markdown.Decorator#closeUnorderedList(StringBuilder) */
	@Override
	public void closeUnorderedList(StringBuilder out) {
		out.append("</ul>\n");
	}

	/** @see panda.doc.markdown.Decorator#openListItem(StringBuilder) */
	@Override
	public void openListItem(StringBuilder out) {
		out.append("<li");
	}

	/** @see panda.doc.markdown.Decorator#closeListItem(StringBuilder) */
	@Override
	public void closeListItem(StringBuilder out) {
		out.append("</li>\n");
	}

	/** @see panda.doc.markdown.Decorator#horizontalRuler(StringBuilder) */
	@Override
	public void horizontalRuler(StringBuilder out) {
		out.append("<hr />\n");
	}

	/** @see panda.doc.markdown.Decorator#openLink(StringBuilder) */
	@Override
	public void openLink(StringBuilder out) {
		out.append("<a");
	}

	/** @see panda.doc.markdown.Decorator#closeLink(StringBuilder) */
	@Override
	public void closeLink(StringBuilder out) {
		out.append("</a>");
	}

	/** @see panda.doc.markdown.Decorator#openImage(StringBuilder) */
	@Override
	public void openImage(StringBuilder out) {
		out.append("<img");
	}

	/** @see panda.doc.markdown.Decorator#closeImage(StringBuilder) */
	@Override
	public void closeImage(StringBuilder out) {
		out.append(" />");
	}
}
