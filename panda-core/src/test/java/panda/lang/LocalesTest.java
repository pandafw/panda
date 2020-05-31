package panda.lang;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

/**
 * test class for Locales
 */
public class LocalesTest extends TestCase {
	private static final Locale LOCALE_EN = new Locale("en", "");
	private static final Locale LOCALE_EN_US = new Locale("en", "US");
	private static final Locale LOCALE_EN_US_ZZZZ = new Locale("en", "US", "ZZZZ");
	private static final Locale LOCALE_FR = new Locale("fr", "");
	private static final Locale LOCALE_FR_CA = new Locale("fr", "CA");
	private static final Locale LOCALE_QQ = new Locale("qq", "");
	private static final Locale LOCALE_QQ_ZZ = new Locale("qq", "ZZ");

	@Before
	public void setUp() throws Exception {
		Locales.isAvailableLocale(Locale.getDefault());
	}

	// -----------------------------------------------------------------------
	/**
	 * Pass in a valid language, test toLocale.
	 * 
	 * @param language the language string
	 */
	private void assertValidParseLocale(final String language) {
		final Locale locale = Locales.parseLocale(language);
		assertNotNull("valid locale", locale);
		assertEquals(language.toLowerCase(), locale.getLanguage());
		// country and variant are empty
		assertTrue(locale.getCountry() == null || locale.getCountry().isEmpty());
		assertTrue(locale.getVariant() == null || locale.getVariant().isEmpty());
	}

	/**
	 * Pass in a valid language, test toLocale.
	 * 
	 * @param localeString to pass to toLocale()
	 * @param language of the resulting Locale
	 * @param country of the resulting Locale
	 */
	private void assertValidParseLocale(final String localeString, final String language, final String country) {
		final Locale locale = Locales.parseLocale(localeString);
		assertNotNull("valid locale", locale);
		assertEquals(language, locale.getLanguage());
		assertEquals(country, locale.getCountry());
		// variant is empty
		assertTrue(locale.getVariant() == null || locale.getVariant().isEmpty());
	}

	/**
	 * Pass in a valid language, test toLocale.
	 * 
	 * @param localeString to pass to toLocale()
	 * @param language of the resulting Locale
	 * @param country of the resulting Locale
	 * @param variant of the resulting Locale
	 */
	private void assertValidParseLocale(final String localeString, final String language, final String country,
			final String variant) {
		final Locale locale = Locales.parseLocale(localeString);
		assertNotNull("valid locale", locale);
		assertEquals(language, locale.getLanguage());
		assertEquals(country, locale.getCountry());
		assertEquals(variant, locale.getVariant());

	}

	/**
	 * Test toLocale() method.
	 */
	@Test
	public void testParseLocale_1Part() {
		assertEquals(null, Locales.parseLocale((String)null));

		assertValidParseLocale("us");
		assertValidParseLocale("Us");
		assertValidParseLocale("fr");
		assertValidParseLocale("de");
		assertValidParseLocale("zh");
		// Valid format but lang doesnt exist, should make instance anyway
		assertValidParseLocale("qq");

		try {
			Locales.parseLocale("u");
			fail("Must be 2 chars if less than 5");
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			Locales.parseLocale("uuu");
			fail("Must be 2 chars if less than 5");
		}
		catch (final IllegalArgumentException iae) {
		}

		try {
			Locales.parseLocale("uu_U");
			fail("Must be 2 chars if less than 5");
		}
		catch (final IllegalArgumentException iae) {
		}
	}

	/**
	 * Test toLocale() method.
	 */
	@Test
	public void testParseLocale_2Part() {
		assertValidParseLocale("us_EN", "us", "EN");
		assertValidParseLocale("us_En", "us", "EN");
		// valid though doesnt exist
		assertValidParseLocale("us_ZH", "us", "ZH");
	}

	/**
	 * Test toLocale() method.
	 */
	@Test
	public void testParseLocale_3Part() {
		assertValidParseLocale("us_EN_A", "us", "EN", "A");
		assertValidParseLocale("us_EN_a", "us", "EN", "a");
		assertValidParseLocale("us_EN_SFsafdFDsdfF", "us", "EN", "SFsafdFDsdfF");

		try {
			Locales.parseLocale("us_EN-a");
			fail("Should fail as not underscore");
		}
		catch (final IllegalArgumentException iae) {
		}
		try {
			Locales.parseLocale("uu_UU_");
			fail("Must be 3, 5 or 7+ in length");
		}
		catch (final IllegalArgumentException iae) {
		}
	}

	// -----------------------------------------------------------------------
	/**
	 * Helper method for local lookups.
	 * 
	 * @param locale the input locale
	 * @param defaultLocale the input default locale
	 * @param expected expected results
	 */
	private void assertLocaleLookupList(final Locale locale, final Locale defaultLocale, final Locale[] expected) {
		final List<Locale> localeList = defaultLocale == null ? Locales.localeLookupList(locale) : Locales
			.localeLookupList(locale, defaultLocale);

		assertEquals(expected.length, localeList.size());
		assertEquals(Arrays.asList(expected), localeList);
		assertUnmodifiableCollection(localeList);
	}

	// -----------------------------------------------------------------------
	/**
	 * Test localeLookupList() method.
	 */
	@Test
	public void testLocaleLookupList_Locale() {
		assertLocaleLookupList(null, null, new Locale[0]);
		assertLocaleLookupList(LOCALE_QQ, null, new Locale[] { LOCALE_QQ });
		assertLocaleLookupList(LOCALE_EN, null, new Locale[] { LOCALE_EN });
		assertLocaleLookupList(LOCALE_EN, null, new Locale[] { LOCALE_EN });
		assertLocaleLookupList(LOCALE_EN_US, null, new Locale[] { LOCALE_EN_US, LOCALE_EN });
		assertLocaleLookupList(LOCALE_EN_US_ZZZZ, null, new Locale[] { LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN });
	}

	/**
	 * Test localeLookupList() method.
	 */
	@Test
	public void testLocaleLookupList_LocaleLocale() {
		assertLocaleLookupList(LOCALE_QQ, LOCALE_QQ, new Locale[] { LOCALE_QQ });
		assertLocaleLookupList(LOCALE_EN, LOCALE_EN, new Locale[] { LOCALE_EN });

		assertLocaleLookupList(LOCALE_EN_US, LOCALE_EN_US, new Locale[] { LOCALE_EN_US, LOCALE_EN });
		assertLocaleLookupList(LOCALE_EN_US, LOCALE_QQ, new Locale[] { LOCALE_EN_US, LOCALE_EN, LOCALE_QQ });
		assertLocaleLookupList(LOCALE_EN_US, LOCALE_QQ_ZZ, new Locale[] { LOCALE_EN_US, LOCALE_EN, LOCALE_QQ_ZZ });

		assertLocaleLookupList(LOCALE_EN_US_ZZZZ, null, new Locale[] { LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN });
		assertLocaleLookupList(LOCALE_EN_US_ZZZZ, LOCALE_EN_US_ZZZZ, new Locale[] { LOCALE_EN_US_ZZZZ, LOCALE_EN_US,
				LOCALE_EN });
		assertLocaleLookupList(LOCALE_EN_US_ZZZZ, LOCALE_QQ, new Locale[] { LOCALE_EN_US_ZZZZ, LOCALE_EN_US, LOCALE_EN,
				LOCALE_QQ });
		assertLocaleLookupList(LOCALE_EN_US_ZZZZ, LOCALE_QQ_ZZ, new Locale[] { LOCALE_EN_US_ZZZZ, LOCALE_EN_US,
				LOCALE_EN, LOCALE_QQ_ZZ });
		assertLocaleLookupList(LOCALE_FR_CA, LOCALE_EN, new Locale[] { LOCALE_FR_CA, LOCALE_FR, LOCALE_EN });
	}

	// -----------------------------------------------------------------------
	/**
	 * Test availableLocaleList() method.
	 */
	@Test
	public void testAvailableLocaleList() {
		final List<Locale> list = Locales.availableLocaleList();
		final List<Locale> list2 = Locales.availableLocaleList();
		assertNotNull(list);
		assertSame(list, list2);
		assertUnmodifiableCollection(list);

		final Locale[] jdkLocaleArray = Locale.getAvailableLocales();
		final List<Locale> jdkLocaleList = Arrays.asList(jdkLocaleArray);
		assertEquals(jdkLocaleList, list);
	}

	// -----------------------------------------------------------------------
	/**
	 * Test availableLocaleSet() method.
	 */
	@Test
	public void testAvailableLocaleSet() {
		final Set<Locale> set = Locales.availableLocaleSet();
		final Set<Locale> set2 = Locales.availableLocaleSet();
		assertNotNull(set);
		assertSame(set, set2);
		assertUnmodifiableCollection(set);

		final Locale[] jdkLocaleArray = Locale.getAvailableLocales();
		final List<Locale> jdkLocaleList = Arrays.asList(jdkLocaleArray);
		final Set<Locale> jdkLocaleSet = new HashSet<Locale>(jdkLocaleList);
		assertEquals(jdkLocaleSet, set);
	}

	// -----------------------------------------------------------------------
	/**
	 * Test availableLocaleSet() method.
	 */
	@SuppressWarnings("boxing")
	// JUnit4 does not support primitive equality testing apart from long
	@Test
	public void testIsAvailableLocale() {
		final Set<Locale> set = Locales.availableLocaleSet();
		assertEquals(set.contains(LOCALE_EN), Locales.isAvailableLocale(LOCALE_EN));
		assertEquals(set.contains(LOCALE_EN_US), Locales.isAvailableLocale(LOCALE_EN_US));
		assertEquals(set.contains(LOCALE_EN_US_ZZZZ), Locales.isAvailableLocale(LOCALE_EN_US_ZZZZ));
		assertEquals(set.contains(LOCALE_FR), Locales.isAvailableLocale(LOCALE_FR));
		assertEquals(set.contains(LOCALE_FR_CA), Locales.isAvailableLocale(LOCALE_FR_CA));
		assertEquals(set.contains(LOCALE_QQ), Locales.isAvailableLocale(LOCALE_QQ));
		assertEquals(set.contains(LOCALE_QQ_ZZ), Locales.isAvailableLocale(LOCALE_QQ_ZZ));
	}

	// -----------------------------------------------------------------------
	/**
	 * Make sure the language by country is correct. It checks that the
	 * Locales.languagesByCountry(country) call contains the array of languages passed in. It may
	 * contain more due to JVM variations.
	 * 
	 * @param country
	 * @param languages array of languages that should be returned
	 */
	private void assertLanguageByCountry(final String country, final String[] languages) {
		final List<Locale> list = Locales.languagesByCountry(country);
		final List<Locale> list2 = Locales.languagesByCountry(country);
		assertNotNull(list);
		assertSame(list, list2);
		// search through langauges
		for (final String language : languages) {
			final Iterator<Locale> iterator = list.iterator();
			boolean found = false;
			// see if it was returned by the set
			while (iterator.hasNext()) {
				final Locale locale = iterator.next();
				// should have an en empty variant
				assertTrue(locale.getVariant() == null || locale.getVariant().isEmpty());
				assertEquals(country, locale.getCountry());
				if (language.equals(locale.getLanguage())) {
					found = true;
					break;
				}
			}
			if (!found) {
				fail("Cound not find language: " + language + " for country: " + country);
			}
		}
		assertUnmodifiableCollection(list);
	}

	/**
	 * Test languagesByCountry() method.
	 */
	@Test
	public void testLanguagesByCountry() {
		assertLanguageByCountry(null, new String[0]);
		assertLanguageByCountry("GB", new String[] { "en" });
		assertLanguageByCountry("ZZ", new String[0]);
		assertLanguageByCountry("CH", new String[] { "fr", "de", "it" });
	}

	// -----------------------------------------------------------------------
	/**
	 * Make sure the country by language is correct. It checks that the
	 * Locales.countryByLanguage(language) call contains the array of countries passed in. It may
	 * contain more due to JVM variations.
	 * 
	 * @param language
	 * @param countries array of countries that should be returned
	 */
	private void assertCountriesByLanguage(final String language, final String[] countries) {
		final List<Locale> list = Locales.countriesByLanguage(language);
		final List<Locale> list2 = Locales.countriesByLanguage(language);
		assertNotNull(list);
		assertSame(list, list2);
		// search through langauges
		for (final String countrie : countries) {
			final Iterator<Locale> iterator = list.iterator();
			boolean found = false;
			// see if it was returned by the set
			while (iterator.hasNext()) {
				final Locale locale = iterator.next();
				// should have an en empty variant
				assertTrue(locale.getVariant() == null || locale.getVariant().isEmpty());
				assertEquals(language, locale.getLanguage());
				if (countrie.equals(locale.getCountry())) {
					found = true;
					break;
				}
			}
			if (!found) {
				fail("Cound not find language: " + countrie + " for country: " + language);
			}
		}
		assertUnmodifiableCollection(list);
	}

	/**
	 * Test countriesByLanguage() method.
	 */
	@Test
	public void testCountriesByLanguage() {
		assertCountriesByLanguage(null, new String[0]);
		assertCountriesByLanguage("de", new String[] { "DE", "CH", "AT", "LU" });
		assertCountriesByLanguage("zz", new String[0]);
		assertCountriesByLanguage("it", new String[] { "IT", "CH" });
	}

	/**
	 * @param coll the collection to check
	 */
	private static void assertUnmodifiableCollection(final Collection<?> coll) {
		try {
			coll.add(null);
			fail();
		}
		catch (final UnsupportedOperationException ex) {
		}
	}

	/**
	 * Tests #LANG-328 - only language+variant
	 */
	@Test
	public void testLang328() {
		assertValidParseLocale("fr__P", "fr", "", "P");
		assertValidParseLocale("fr__POSIX", "fr", "", "POSIX");
	}

	/**
	 * Tests #LANG-865, strings starting with an underscore.
	 */
	@Test
	public void testLang865() {
		assertValidParseLocale("_GB", "", "GB", "");
		assertValidParseLocale("_GB_P", "", "GB", "P");
		assertValidParseLocale("_GB_POSIX", "", "GB", "POSIX");
		try {
			Locales.parseLocale("_G");
			fail("Must be at least 3 chars if starts with underscore");
		}
		catch (final IllegalArgumentException iae) {
		}
		try {
			Locales.parseLocale("_GB_");
			fail("Must be at least 5 chars if starts with underscore");
		}
		catch (final IllegalArgumentException iae) {
		}
		try {
			Locales.parseLocale("_GBAP");
			fail("Must have underscore after the country if starts with underscore and is at least 5 chars");
		}
		catch (final IllegalArgumentException iae) {
		}
	}

	@Test
	public void testParseAllLocales() {
		Locale[] locales = Locale.getAvailableLocales();
		int failures = 0;
		for (Locale l : locales) {
			// Check if it's possible to recreate the Locale using just the standard constructor
			Locale locale = new Locale(l.getLanguage(), l.getCountry(), l.getVariant());
			if (l.equals(locale)) { // it is possible for Locales.parseLocale to handle these Locales
				String str = l.toString();
				// Look for the script/extension suffix
				int suff = str.indexOf("_#");
				if (suff == -1) {
					suff = str.indexOf("#");
				}
				if (suff >= 0) { // we have a suffix
					try {
						Locales.parseLocale(str); // shouuld cause IAE
						System.out.println("Should not have parsed: " + str);
						failures++;
						continue; // try next Locale
					}
					catch (IllegalArgumentException iae) {
						// expected; try without suffix
						str = str.substring(0, suff);
					}
				}
				Locale loc = Locales.parseLocale(str);
				if (!l.equals(loc)) {
					System.out.println("Failed to parse: " + str);
					failures++;
				}
			}
		}
		if (failures > 0) {
			fail("Failed " + failures + " test(s)");
		}
	}

	/**
	 * @see Locales#localeFromFileName(String)
	 */
	public void testLocaleFromFileName() {
		Locale locale;

		locale = Locales.localeFromFileName(new File("abc_en_US.txt"));
		assertEquals("en_US", locale.toString());
	}
}
