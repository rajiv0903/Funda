package org.learning.pattern._05_command.remote.command;

import org.learning.pattern._05_command.remote.receiver.Light;

public class LivingroomLightOnCommand implements Command {
	Light light;

	public LivingroomLightOnCommand(Light light) {
		this.light = light;
	}

	public void execute() {
		light.on();
	}
}
