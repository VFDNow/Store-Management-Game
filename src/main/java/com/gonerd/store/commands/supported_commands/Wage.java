package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.commands.DataType;
import com.gonerd.store.Main;
import com.gonerd.store.commands.StoreCommand;
import com.gonerd.store.formatting.Format;
import com.gonerd.store.objects.Worker;

import java.util.ArrayList;
import java.util.Scanner;

public class Wage extends StoreCommand {

	public Wage(Scanner scanner) {
		this(null, scanner);
	}

	public Wage(ArrayList<Argument> args, Scanner scanner) {
		this.args = args;
		commandID = "wage";
		object = "Worker";

		params = new Argument[] {
				new Argument("Who's wage do you want to change?", DataType.OBJECT, "Worker", null),
				new Argument("What do you want their hourly wage to be?", DataType.FLOAT)
		};

		allowRepeats = false;
	}

	@Override
	public void execute() {
		if (validateArgs()) {
			// Get the internal name of the worker
			String intName = Format.toInternal(Format.toTitleCase(args.get(0).getValue()));

			int workerIndex = -1; // We'll store the worker here once we find them
			// -1 is used here because it can't be an index, so it is a good init value

			int i = 0;
			for (var worker : Main.workers) {
				if (worker.getName().equals(intName)) {
					workerIndex = i;
					break;
				}
				i++;
			}

			// Change salary
			float salary = Format.storeCur(args.get(1).getFValue());
			Worker worker = Main.workers.get(workerIndex);
			worker.setSalary(salary);

			System.out.println("Now you are paying " + worker.getListedName() + ' ' + Format.fmtCurOut(salary) + " /hr" + ((worker.getCount() > 1)? " each": ""));
		}
		// If not valid, we do nothing
	}
}