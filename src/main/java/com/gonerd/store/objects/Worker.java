package com.gonerd.store.objects;

import com.gonerd.commands.CommandObject;
import com.gonerd.store.Main;
import com.gonerd.store.formatting.Format;
import com.gonerd.store.formatting.inflection.ModernEnglish;

public class Worker implements CommandObject {
	private final String name;
	private int count;
	private float salary;

	public Worker (String name) {
		this.name = name;
		this.count = 0; // Be careful when initializing a Worker as you need to call addWorker() for count to be 1
		this.salary = 0f;
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

	public void addWorker() {
		count++;
	}

	public void fireWorker() throws RuntimeException {
		count--;
		if (count < 0) {
			count = 0;
		}
		if (count == 0) {
			// Remove worker from Main
			int i = 0;

			for (var worker : Main.workers) {
				if (worker.getID().equals(getID())) {
					break;
				}
				i++;
			}

			if (i >= Main.workers.size()) {
				throw new RuntimeException("Worker not found in Main.workers");
			}

			Main.workers.remove(i);
		}
	}

	public float getSalary() {
		return salary;
	}

	public void setSalary(float salary) {
		this.salary = salary;
	}

	@Override
	public String getID() {
		return getName().toLowerCase();
	}

	@Override
	public String toString() {
		return getListedName() +  " : " + count + " (-" + Format.fmtCurOut(salary * count) + " /hr" + ((count > 1)? " total" : "") + ")\n";
	}
}