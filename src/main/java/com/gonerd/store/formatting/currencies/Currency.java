package com.gonerd.store.formatting.currencies;

import java.util.Locale;

public class Currency {
	private String prefix;
	private String postfix;
	private float usdConversionRate;
	private int precision;
	private String regex;
	private String name;
	private Locale fmt;

	public String getPrefix() {
		return prefix;
	}

	public String getPostfix() {
		return postfix;
	}

	public String getName() {
		return name;
	}

	public Locale getFmt() {
		return fmt;
	}

	public float getUsdConversionRate() {
		return usdConversionRate;
	}

	public int getPrecision() {
		return precision;
	}

	public Currency(String prefix, String postfix, String regex, String name, float usdConversionRate, int precision, Locale fmt) {
		this.prefix = prefix;
		this.postfix = postfix;
		this.usdConversionRate = usdConversionRate;
		this.regex = regex;
		this.name = name;
		this.precision = precision;
		this.fmt = fmt;
	}

	public Currency(String prefix, String postfix, String regex, String name, float usdConversionRate, int precision) {
		this(prefix, postfix, regex, name, usdConversionRate, precision, Locale.US);
	}

	public boolean isCurrency(String str) {
		return str.matches(regex);
	}
}
