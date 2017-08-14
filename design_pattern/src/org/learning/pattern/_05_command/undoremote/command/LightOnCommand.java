package org.learning.pattern._05_command.undoremote.command;

import org.learning.pattern._05_command.undoremote.receiver.Light;

public class LightOnCommand implements Command {
	Light light;
 
	public LightOnCommand(Light light) {
		this.light = light;
	}
 
	public void execute() {
		light.on();
	}
 
	public void undo() {
		light.off();
	}
}
