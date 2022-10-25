package com.gonerd.store.commands.supported_commands;

import com.gonerd.commands.Argument;
import com.gonerd.commands.DataType;
import com.gonerd.store.Main;
import com.gonerd.store.commands.StoreCommand;
import com.gonerd.store.formatting.Format;
import com.gonerd.store.formatting.inflection.ModernEnglish;
import com.gonerd.store.objects.Worker;

import java.util.ArrayList;

public class Hire extends StoreCommand {

	public Hire() {
		this(null);
	}

	public Hire(ArrayList<Argument> args) {
		this.args = args;
		commandID = "hire";
		object = "Store";

		params = new Argument[] {
				new Argument("What would you like to hire?", DataType.STRING, "Worker", null)
		};

		allowRepeats = true;
		// We don't init repeatMessage here as we need to know the arguments first
	}

	@Override
	public void execute() {
		if (validateArgs()) {
			// Get the internal name of the worker
			String intName = Format.toInternal(Format.toTitleCase(args.get(0).getValue()));

			repeatMessage = "How many " + ModernEnglish.plural(intName) + " would you like to hire?";

			int repetitions = repeatPrompt();

			// We need to pop args down, as we have consumed the repeat value if it existed
			if (args.size() > params.length) {
				args.remove(args.size() - 1);
			}

			int workerIndex = -1; // We'll store the worker here once we either find or create them
			// -1 is used here because it can't be an index, so it is a good init value

			int i = 0;
			for (var worker : Main.workers) {
				if (worker.getName().equals(intName)) {
					workerIndex = i;
					break;
				}
				i++;
			}

			// Create the worker if they don't exist and store the index
			if (workerIndex == -1) {
				workerIndex = Main.workers.size();
				Main.workers.add(new Worker(intName));
			}

			// Hire the things
			for (int c = 0; c < repetitions; c++) {
				Main.workers.get(workerIndex).addWorker();
			}

			String amtOfWorkers = ModernEnglish.plural(intName, repetitions);
			if (ModernEnglish.isPlural(amtOfWorkers)) {
				amtOfWorkers = repetitions + " " + amtOfWorkers;
			}

			System.out.println("Hired " + amtOfWorkers + " for " + Format.fmtCurOut(0f) + " /hr (<" + intName + " wage> to change)");
		}
		// If not valid, we do nothing
	}
}