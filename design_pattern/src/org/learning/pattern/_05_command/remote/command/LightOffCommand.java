package org.learning.pattern._05_command.remote.command;

import org.learning.pattern._05_command.remote.receiver.Light;

public class LightOffCommand implements Command {
	Light light;
 
	public LightOffCommand(Light light) {
		this.light = light;
	}
 
	public void execute() {
		light.off();
	}
}
