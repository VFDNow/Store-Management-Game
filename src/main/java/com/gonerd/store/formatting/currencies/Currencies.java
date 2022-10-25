package com.gonerd.store.formatting.currencies;

public interface Currencies {
	Currency[] currencies = {
			new Currency("$", " USD", "(u([^k]*)dollar(s)?)|(us(a)?d)|(dollar(s)?)|(\\w*\\s*buck\\w*)", "USD", 1.0f, 2),
			new Currency("$", " CAD", "(c(\\w|\\s)*dollar(s)?)|(c(a)?d)|(dallah(s)?)|(\\w*\\s*loon\\w*)", "CAD", 0.73f, 2),
			new Currency("", " EUR", "(eu(\\s+|\\w+){0,})", "EUR", 0.98f, 2), // If you prefer the comma as a decimal seperator, add Locale.GERMANY to the end
			new Currency("", " GBP", "((g)?bp\\w{0,})|((\\s+|\\w+){0,}?pound(s)?)|(lb\\w{0,})|(brit(\\s+|\\w+){0,})", "GBP", 1.13f, 2),
			new Currency("", " UAH", "((\\w|\\s)*ukraine(\\w|\\s)*)|((\\w|\\s)*hryvni(a)?(\\w|\\s)*)|((\\w|\\s)*ua(h)?)", "UAH", 0.027f, 2),
			new Currency("", " RUB (", "((\\w|\\s)*russia(\\w|\\s)*)|((\\w|\\s)*r(o)?uble(\\w|\\s)*)|((\\w|\\s)*ru(h)?)", "RUB", 0.015f, 2),


			// Cryptos
			new Currency("", " BTC", "(b(i)?(t)?\\s*c\\w{0,})|([^a]{0,}crypto(s)?\\s*(currency)?)", "BTC", 19449.0f, 6),
			new Currency("", " DUKK", "(du(c)*(k)*\\s*\\w*)|((a)(\\w+\\s*)crypto(s)?\\s*(currency)?)|((\\w|\\s)*duck(\\w|\\s)*)|((\\w|\\s)*invest(\\w|\\s)*)", "DUKK", 0.00283585567f, 0),
			new Currency("", " DOGE", "((\\w|\\s)*doge(\\w|\\s)*)", "DOGE", 0.06f, 2),

			// Stocks
			new Currency("", " TESLA STOCK", "((\\w|\\s)*elon(\\w|\\s)*)|((\\w|\\s)*t(e)?sl(a)?(\\w|\\s)*)", "TESLA STOCK", 221.72f, 4),
	};
}