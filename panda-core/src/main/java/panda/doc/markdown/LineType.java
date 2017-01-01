package panda.doc.markdown;

/**
 * Line type enumeration.
 */
enum LineType {
	/** Empty line. */
	EMPTY,
	/** Undefined content. */
	OTHER,
	/** A markdown headline. */
	HEADLINE, HEADLINE1, HEADLINE2,
	/** A code block line. */
	CODE,
	/** A list. */
	ULIST, OLIST,
	/** A block quote. */
	BQUOTE,
	/** A horizontal ruler. */
	HR,
	/** Start of a XML block. */
	XML,
	/** Fenced code block start/end */
	FENCED_CODE,
	/** Table */
	TABLE,
	/** Table another */
	TABLEB,
	/** plugin block */
	PLUGIN
}
