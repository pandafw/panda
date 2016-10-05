package panda.doc.markdown;

/**
 * Block type enum.
 */
enum BlockType {
	/** Unspecified. Used for root block and list items without paragraphs. */
	NONE,
	/** A block quote. */
	BLOCKQUOTE,
	/** A code block. */
	CODE,
	/** A fenced code block. */
	FENCED_CODE,
	/** A headline. */
	HEADLINE,
	/** A list item. */
	LIST_ITEM,
	/** An ordered list. */
	ORDERED_LIST,
	/** A paragraph. */
	PARAGRAPH,
	/** A horizontal ruler. */
	RULER,
	/** An unordered list. */
	UNORDERED_LIST,
	/** A XML block. */
	XML,
	/** A Table block (https://michelf.ca/projects/php-markdown/extra/#table). */
	TABLE,
	/** A Table block. */
	TABLEB,
	/** A plugin block. */
	PLUGIN
}
