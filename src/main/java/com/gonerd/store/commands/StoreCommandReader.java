package com.gonerd.store.commands;

import com.gonerd.commands.Command;
import com.gonerd.commands.CommandReader;
import com.gonerd.store.Main;
import com.gonerd.store.commands.supported_commands.*;

import java.util.List;

public class StoreCommandReader extends CommandReader {
	static {
		supportedCommands = new Command[] {
				new Help(Main.scanner),
				new Hire(Main.scanner),
				new Wage(Main.scanner),
				new CurrencyCommand(Main.scanner),
				new ListCurrency(Main.scanner),
				new Stock(Main.scanner),
				new Exit(Main.scanner),
				new Open(Main.scanner)
		};

		objects = List.of(Main.workers, Main.items, List.of(() -> "store"));
	}
}
