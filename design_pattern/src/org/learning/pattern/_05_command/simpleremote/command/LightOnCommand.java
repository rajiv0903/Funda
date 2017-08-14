package org.learning.pattern._05_command.simpleremote.command;

import org.learning.pattern._05_command.simpleremote.receiver.Light;

public class LightOnCommand implements Command {
	Light light;
  
	public LightOnCommand(Light light) {
		this.light = light;
	}
 
	public void execute() {
		light.on();
	}
}
