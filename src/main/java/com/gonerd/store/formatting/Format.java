package com.gonerd.store.formatting;

import com.gonerd.store.formatting.currencies.Currencies;
import com.gonerd.store.formatting.currencies.Currency;
import com.gonerd.store.formatting.inflection.ModernEnglish;
import com.gonerd.store.Main;

public class Format implements Currencies {

	public static float storeCur(float cur) {
		Currency currency = Main.currency;
		cur *= currency.getUsdConversionRate();
		return cur;
	}

	public static String fmtCurOut(float cur) {
		Currency currency = Main.currency;
		cur /= currency.getUsdConversionRate();
		// getFmt() handles locale, whereas getPrecision() handles decimal length
		return currency.getPrefix() + String.format(currency.getFmt(), "%,." + currency.getPrecision() + "f", cur) + currency.getPostfix();
	}

	public static String toInternal(String str) {
		return ModernEnglish.plural(str, 1);
	}

	public static String toTitleCase(String input) {
		StringBuilder titleCase = new StringBuilder(input.length());
		boolean nextTitleCase = true;

		for (char c: input.toCharArray()) {
			if (Character.isSpaceChar(c)) {
				nextTitleCase = true;
			} else if (nextTitleCase) {
				c = Character.toTitleCase(c);
				nextTitleCase = false;
			}

			titleCase.append(c);
		}

		return titleCase.toString();
	}

}
