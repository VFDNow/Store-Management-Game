package com.gonerd.commands;

public interface CommandObject {
	// This MUST return all lowercase characters
	// It is up to the subclass to make sure it does
	String getID();
}