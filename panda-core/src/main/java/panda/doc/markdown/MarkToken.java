package panda.doc.markdown;

/**
 * Markdown token enumeration.
 */
enum MarkToken {
	/** No token. */
	NONE,
	/** &#x2a; */
	EM_STAR, // x*x
	/** _ */
	EM_UNDERSCORE, // x_x
	/** &#x2a;&#x2a; */
	STRONG_STAR, // x**x
	/** __ */
	STRONG_UNDERSCORE, // x__x
	/** ~~ */
	STRIKE, // x~~x
	/** ` */
	CODE_SINGLE, // `
	/** `` */
	CODE_DOUBLE, // ``
	/** [ */
	LINK, // [
	/** &lt; */
	HTML, // <
	/** ![ */
	IMAGE, // ![
	/** &amp; */
	ENTITY, // &
	/** \ */
	ESCAPE, // \x
	/** Extended: ^ */
	SUPER, // ^
	/** Extended: (C) */
	X_COPY, // (C)
	/** Extended: (R) */
	X_REG, // (R)
	/** Extended: (TM) */
	X_TRADE, // (TM)
	/** Extended: &lt;&lt; */
	X_LAQUO, // <<
	/** Extended: >> */
	X_RAQUO, // >>
	/** Extended: -- */
	X_NDASH, // --
	/** Extended: --- */
	X_MDASH, // ---
	/** Extended: &#46;&#46;&#46; */
	X_HELLIP, // ...
	/** Extended: "x */
	X_RDQUO, // "
	/** Extended: x" */
	X_LDQUO, // "
	/** [[ */
	X_LINK_OPEN, // [[
	/** ]] */
	X_LINK_CLOSE, // ]]
}
