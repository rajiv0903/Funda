package org.learning.pattern._05_command.remote.command;

import org.learning.pattern._05_command.remote.receiver.Light;

public class LightOnCommand implements Command {
	Light light;

	public LightOnCommand(Light light) {
		this.light = light;
	}

	public void execute() {
		light.on();
	}
}
