package com.gonerd.store.commands;

import com.gonerd.commands.Command;
import com.gonerd.commands.CommandReader;
import com.gonerd.store.Main;
import com.gonerd.store.commands.supported_commands.*;

import java.util.List;

public class StoreCommandReader extends CommandReader {
	static {
		supportedCommands = new Command[] {
				new Help(),
				new Hire(),
				new Wage(),
				new CurrencyCommand(),
				new ListCurrency(),
				new Stock(),
				new Exit(),
				new Open()
		};

		objects = List.of(Main.workers, Main.items, List.of(() -> "store"));
	}
}
