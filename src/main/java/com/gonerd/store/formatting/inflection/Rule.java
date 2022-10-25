package com.gonerd.store.formatting.inflection;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Rule {

	private final Pattern singular;
	private final String plural;

	Rule(Pattern singular, String plural) {
		this.singular = singular;
		this.plural = plural;
	}

	Rule(String singular, String plural) {
		this.singular = Pattern.compile(singular);
		this.plural = plural;
	}

	public String apply(String word) {
		StringBuilder builder = new StringBuilder();
		Matcher matcher = singular.matcher(word);
		if (matcher.find()) {
			matcher.appendReplacement(builder, plural);
			matcher.appendTail(builder);
			return builder.toString();
		}
		return null;
	}

}
