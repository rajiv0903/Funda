package org.learning.pattern._05_command.macroremote.command;

public interface Command {
	public void execute();
	public void undo();
}
