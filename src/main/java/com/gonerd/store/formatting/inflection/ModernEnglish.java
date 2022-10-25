package com.gonerd.store.formatting.inflection;

import org.atteo.evo.inflector.English;
import org.atteo.evo.inflector.TwoFormInflector;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Transforms English words from singular to plural form, or vice versa. Uses a combination of classical and anglicized English to adapt to common usages.
 * <p>
 * Examples:
 * <pre> {@code
 *    English.plural("ox") == "oxen";
 *
 *    English.singular("oxen") == "ox";
 *
 *    English.isPlural("matrices") == true;
 *
 *    English.plural("cats", 1) == "cat";
 *    English.plural("cat", 2) == "cats";}
 * </pre>
 * </p>
 * <p>
 * Based on <a href="http://www.csse.monash.edu.au/~damian/papers/HTML/Plurals.html">
 * An Algorithmic Approach to English Pluralization</a> by Damian Conway.
 * </p>
 * <p>
 * Borrows some code from <pre>org.atteo.evo.inflector.English</pre>
 * </p>
 */
// We don't want spellcheck to correct our regex patterns
@SuppressWarnings("SpellCheckingInspection")
public final class ModernEnglish extends TwoFormInflector {

	private static final List<Rule> singRules = new ArrayList<>();

	private static final String[] CATEGORY_EX_ICES = {
			"codex",
			"murex",
			"silex",
	};

	private static final String[] CATEGORY_IX_ICES = {
			"radix",
			"helix",
	};

	private static final String[] CATEGORY_UM_A = {
			"bacterium",
			"agendum",
			"desideratum",
			"erratum",
			"stratum",
			"datum",
			"ovum",
			"extremum",
			"candelabrum",
	};

	// Always us -> i
	private static final String[] CATEGORY_US_I = {
			"alumnus",
			"alveolus",
			"bacillus",
			"bronchus",
			"locus",
			"nucleus",
			"stimulus",
			"meniscus",
			"thesaurus",
	};

	private static final String[] CATEGORY_ON_A = {
			"criterion",
			"perihelion",
			"aphelion",
			"phenomenon",
			"prolegomenon",
			"noumenon",
			"organon",
			"asyndeton",
			"hyperbaton",
	};

	private static final String[] CATEGORY_A_AE = {
			"alumna",
			"alga",
			"vertebra",
			"persona"
	};

	// Always o -> os
	private static final String[] CATEGORY_O_OS = {
			"albino",
			"archipelago",
			"armadillo",
			"commando",
			"crescendo",
			"fiasco",
			"ditto",
			"dynamo",
			"embryo",
			"ghetto",
			"guano",
			"inferno",
			"jumbo",
			"lumbago",
			"magneto",
			"manifesto",
			"medico",
			"octavo",
			"photo",
			"pro",
			"quarto",
			"canto",
			"lingo",
			"generalissimo",
			"stylo",
			"rhino",
			"casino",
			"auto",
			"macro",
			"zero",
			"todo"
	};

	// Classical o -> i  (normally -> os)
	private static final String[] CATEGORY_O_I = {
			"solo",
			"soprano",
			"basso",
			"alto",
			"contralto",
			"tempo",
			"piano",
			"virtuoso",
	};

	// -a to -as (anglicized) or -ata (classical)
	private static final String[] CATEGORY_A_ATA = {
			"anathema",
			"enema",
			"oedema",
			"bema",
			"enigma",
			"sarcoma",
			"carcinoma",
			"gumma",
			"schema",
			"charisma",
			"lemma",
			"soma",
			"diploma",
			"lymphoma",
			"stigma",
			"dogma",
			"magma",
			"stoma",
			"drama",
			"melisma",
			"trauma",
			"edema",
			"miasma"
	};

	private static final String[] CATEGORY_IS_IDES = {
			"iris",
			"clitoris"
	};

	// -us to -uses (anglicized) or -us (classical)
	private static final String[] CATEGORY_US_US = {
			"apparatus",
			"impetus",
			"prospectus",
			"cantus",
			"nexus",
			"sinus",
			"plexus",
			"status",
			"hiatus"
	};

	private static final String[] CATEGORY_S_ES = {
			"acropolis",
			"chaos",
			"lens",
			"aegis",
			"cosmos",
			"mantis",
			"alias",
			"dais",
			"marquis",
			"asbestos",
			"digitalis",
			"metropolis",
			"atlas",
			"epidermis",
			"pathos",
			"bathos",
			"ethos",
			"pelvis",
			"bias",
			"gas",
			"polis",
			"caddis",
			"glottis",
			"rhinoceros",
			"cannabis",
			"glottis",
			"sassafras",
			"canvas",
			"ibis",
			"trellis"
	};

	private static final String[] CATEGORY_MAN_MANS = {
			"human",
			"Alabaman",
			"Bahaman",
			"Burman",
			"German",
			"Hiroshiman",
			"Liman",
			"Nakayaman",
			"Oklahoman",
			"Panaman",
			"Selman",
			"Sonaman",
			"Tacoman",
			"Yakiman",
			"Yokohaman",
			"Yuman"
	};

	private static final English inflector = new English();

	public ModernEnglish() {

		// Singularize rules
		addSingularize("children", "child");
		addSingularize("(vert|ind)ices$", "$1ex");
		addSingularize("(matr)ices$", "$1ix");
		addSingularize("(vert|ind)icies$", "$1ex");
		addSingularize("(matr)icies$", "$1ix");
		addSingularize("s$", "");
		addSingularize("(s|si|u)s$", "$1s"); // '-us' and '-ss' are already singular
		addSingularize("(n)ews$", "$1ews");
		addSingularize("([ti])a$", "$1um");
		addSingularize("((a)naly|(b)a|(d)iagno|(p)arenthe|(p)rogno|(s)ynop|(t)he)ses$", "$1$2sis");
		addSingularize("(^analy)ses$", "$1sis");
		addSingularize("(^analy)sis$", "$1sis"); // already singular, but ends in 's'
		addSingularize("([^f])ves$", "$1fe");
		addSingularize("(hive)s$", "$1");
		addSingularize("(tive)s$", "$1");
		addSingularize("(g|m)(ee)se$", "$1oose");
		addSingularize("([lr])ves$", "$1f");
		addSingularize("([^aeiouy]|qu)ies$", "$1y");
		addSingularize("(s)eries$", "$1eries");
		addSingularize("(m)ovies$", "$1ovie");
		addSingularize("(x|ch|ss|sh)es$", "$1");
		addSingularize("([m|l])ice$", "$1ouse");
		addSingularize("(bus)es$", "$1");
		addSingularize("(o)es$", "$1");
		addSingularize("(shoe)s$", "$1");
		addSingularize("(cris|ax|test)is$", "$1is"); // already singular, but ends in 's'
		addSingularize("(cris|ax|test)es$", "$1is");
		addSingularize("(octop|vir)i$", "$1us");
		addSingularize("(octop|vir)us$", "$1us"); // already singular, but ends in 's'
		addSingularize("(alias|status)es$", "$1");
		addSingularize("(alias|status)$", "$1"); // already singular, but ends in 's'
		addSingularize("^(ox)en", "$1");
		addSingularize("(quiz)zes$", "$1");

		uncountable(new String[] {
				// 2. Handle words that do not inflect in the plural (such as fish, travois, chassis, nationalities ending
				// endings
				"fish",
				"ois",
				"sheep",
				"deer",
				"pox",
				"itis",
				"moose",

				// words
				"bison",
				"flounder",
				"pliers",
				"bream",
				"gallows",
				"proceedings",
				"breeches",
				"graffiti",
				"rabies",
				"britches",
				"headquarters",
				"salmon",
				"carp",
				"herpes",
				"scissors",
				"chassis",
				"high-jinks",
				"sea-bass",
				"clippers",
				"homework",
				"series",
				"cod",
				"innings",
				"shears",
				"contretemps",
				"jackanapes",
				"species",
				"corps",
				"mackerel",
				"swine",
				"debris",
				"measles",
				"trout",
				"diabetes",
				"mews",
				"tuna",
				"djinn",
				"mumps",
				"whiting",
				"eland",
				"news",
				"wildebeest",
				"elk",
				"pincers",
				"sugar"
		});

		// 4. Handle standard irregular plurals (mongooses, oxen, etc.)

		irregular(new String[][] {
				{
						"child",
						"children"
				}, // classical
				{
						"ephemeris",
						"ephemerides"
				}, // classical
				{
						"mongoose",
						"mongoose"
				}, // anglicized
				{
						"mythos",
						"mythoi"
				}, // classical
				{"ox", "oxen"}, // classical, but still used now
				{
						"soliloquy",
						"soliloquies"
				}, // anglicized
				{
						"trilby",
						"trilbys"
				}, // anglicized
				{
						"genus",
						"genera"
				}, // classical
				{
						"quiz",
						"quizzes"
				},
		});

		// Anglicized plural
		irregular(new String[][] {
				{
						"beef",
						"beefs"
				}, {
				"brother",
				"brothers"
		}, {
				"cow",
				"cows"
		}, {
				"genie",
				"genies"
		}, {
				"money",
				"moneys"
		}, {
				"octopus",
				"octopuses"
		}, {
				"opus",
				"opuses"
		},
		});

		categoryRule(CATEGORY_MAN_MANS, "", "s");

		// 5. Handle irregular inflections for common suffixes
		rule(new String[][] {
				{
						"man$",
						"men"
				}, {
				"([lm])ouse$",
				"$1ice"
		}, {
				"tooth$",
				"teeth"
		}, {
				"goose$",
				"geese"
		}, {
				"foot$",
				"feet"
		}, {
				"zoon$",
				"zoa"
		}, {
				"([csx])is$",
				"$1es"
		},
		});

		// 6. Handle fully assimilated classical inflections
		categoryRule(CATEGORY_EX_ICES, "ex", "ices");
		categoryRule(CATEGORY_IX_ICES, "ix", "ices");
		categoryRule(CATEGORY_UM_A, "um", "a");
		categoryRule(CATEGORY_ON_A, "on", "a");
		categoryRule(CATEGORY_A_AE, "a", "ae");

		// 7. Handle classical variants of modern inflections

		categoryRule(CATEGORY_US_I, "us", "i");

		rule("([cs]h|[zx])$", "$1es");
		categoryRule(CATEGORY_S_ES, "", "es");
		categoryRule(CATEGORY_IS_IDES, "", "es");
		categoryRule(CATEGORY_US_US, "", "es");
		rule("(us)$", "$1es");
		categoryRule(CATEGORY_A_ATA, "", "s");

		// The suffixes -ch, -sh, and -ss all take -es in the plural (churches,
		// classes, etc)...
		rule(new String[][] {
				{
						"([cs])h$",
						"$1hes"
				}, {
				"ss$",
				"sses"
		}
		});

		// Certain words ending in -f or -fe take -ves in the plural (lives,
		// wolves, etc)...
		rule(new String[][] {
				{
						"([aeo]l)f$",
						"$1ves"
				}, {
				"([^d]ea)f$",
				"$1ves"
		}, {
				"(ar)f$",
				"$1ves"
		}, {
				"([nlw]i)fe$",
				"$1ves"
		}
		});

		// Words ending in -y take -ys
		rule(new String[][] {
				{
						"([aeiou]y)$",
						"$1s"
				}, {
				"y$",
				"ies"
		},
		});

		// Some words ending in -o take -os (including does precede by a vowel)
		categoryRule(CATEGORY_O_I, "o", "os");
		categoryRule(CATEGORY_O_OS, "o", "os");
		rule("([aeiouz]o)$", "$1s");
		// The rest take -oes
		rule("(o)$", "$1es");

		rule("(ul)um$", "$1a");

		categoryRule(CATEGORY_A_ATA, "", "es");

		rule("(s)$", "$1es");

		// Return empty string for empty string input
		rule("^$", "");
		// Otherwise, assume that the plural just adds -s
		rule("$", "s");
	}

	/**
	 * Returns plural form of the word.
	 * <p>
	 * For instance:
	 * <pre>
	 * {@code
	 * English.plural("cats") == "cats";
	 * English.plural("cat") == "cats";
	 * }
	 * </pre>
	 * </p>
	 *
	 * @param word word in singular or plural form
	 * @return the word in plural form
	 */
	public static String plural(String word) {
		return plural(word, 2);
	}

	/**
	 * Returns singular or plural form of the word based on count.
	 * <p>
	 * For instance:
	 * <pre>
	 * {@code
	 * English.plural("cats", 1) == "cat";
	 * English.plural("cat", 2) == "cats";
	 * }
	 * </pre>
	 * </p>
	 *
	 * @param word  word in singular or plural form
	 * @param count word count
	 * @return form of the word correct for given count
	 */
	public static String plural(String word, int count) {
		if (count == 1) {
			return (isPlural(word)) ? singularize(word) : word;
		}
		return (isPlural(word)) ? word : inflector.getPlural(word);
	}

	private static String pluralNoCheck(String word) {
		return inflector.getPlural(word);
	}

	/**
	 * Determines whether the input is singular or plural
	 * <p>
	 * For instance:
	 * <pre>
	 * {@code
	 * English.isPlural("cats") == true;
	 * English.isPlural("cat") == false;
	 * }
	 * </pre>
	 * </p>
	 *
	 * @param word word in singular or plural form
	 * @return true if plural; false if singular
	 */
	public static boolean isPlural(String str) {
		return str.equals(pluralNoCheck(singularize(str)));
	}

	/**
	 * Returns singular form of the word.
	 * <p>
	 * For instance:
	 * <pre>
	 * {@code
	 * English.singular("cats") == "cat";
	 * English.singular("cat") == "cat";
	 * }
	 * </pre>
	 * </p>
	 *
	 * @param word word in singular or plural form
	 * @return the word in singular form
	 */
	public static String singular(String word) {
		return plural(word, 1);
	}

	// For internal use only, see singular(String word) for external use
	private static String singularize(String wordStr) {
		for (Rule rule : singRules) {
			String result = rule.apply(wordStr);
			if (result != null) return result;
		}
		return wordStr;
	}

	@Override
	protected String getPlural(String word) {
		return super.getPlural(word);
	}

	private void addSingularize(String plural, String singular) {
		singRules.add(new Rule(Pattern.compile(plural, Pattern.CASE_INSENSITIVE), singular));
	}

}
