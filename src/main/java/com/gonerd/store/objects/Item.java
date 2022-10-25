package com.gonerd.store.objects;

import com.gonerd.commands.CommandObject;
import com.gonerd.store.formatting.Format;
import com.gonerd.store.formatting.inflection.ModernEnglish;

public class Item implements CommandObject {
	private final String name;
	private int count;
	private float price;
	private float markup;

	public Item (String name) {
		this.name = name;
		this.count = 0; // Be careful when initializing an Item as you need to call addItem() for count to be 1
		this.markup = 0f;
		this.price = 0f;
	}

	public String getName() {
		return name;
	}

	public String getListedName() {
		return ModernEnglish.plural(name, count);
	}

	public int getCount() {
		return count;
	}

	public void stockItem() {
		count++;
	}

	public void removeItem() {
		count--;
		if (count < 0) {
			count = 0;
		}
	}

	public float getSalesPrice() {
		return (markup + 1f) * price;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public float getMarkup() {
		return markup;
	}

	public void setMarkup(float markup) {
		this.markup = markup;
	}

	@Override
	public String getID() {
		return getName().toLowerCase();
	}

	@Override
	public String toString() {
		// We display even if the count is 0 to show that we carry the item, and it needs restocking
		return getListedName() +  " : " + count + " (selling for " + Format.fmtCurOut((markup + 1f) * price) + " each)\n";
	}
}