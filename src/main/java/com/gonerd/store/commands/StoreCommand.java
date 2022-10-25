package com.gonerd.store.commands;

import com.gonerd.commands.Command;
import com.gonerd.store.Main;

import java.util.List;

public abstract class StoreCommand extends Command {

	static {
		commandReader = new StoreCommandReader();
	}
}
