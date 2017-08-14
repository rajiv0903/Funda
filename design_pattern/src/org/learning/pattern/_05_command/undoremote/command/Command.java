package org.learning.pattern._05_command.undoremote.command;

public interface Command {
	public void execute();
	public void undo();
}
