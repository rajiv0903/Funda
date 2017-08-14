package org.learning.pattern._05_command.macroremote.entry;

import org.learning.pattern._05_command.macroremote.command.Command;
import org.learning.pattern._05_command.macroremote.command.HottubOffCommand;
import org.learning.pattern._05_command.macroremote.command.HottubOnCommand;
import org.learning.pattern._05_command.macroremote.command.LightOffCommand;
import org.learning.pattern._05_command.macroremote.command.LightOnCommand;
import org.learning.pattern._05_command.macroremote.command.MacroCommand;
import org.learning.pattern._05_command.macroremote.command.StereoOffCommand;
import org.learning.pattern._05_command.macroremote.command.StereoOnCommand;
import org.learning.pattern._05_command.macroremote.command.TVOffCommand;
import org.learning.pattern._05_command.macroremote.command.TVOnCommand;
import org.learning.pattern._05_command.macroremote.invoker.RemoteControl;
import org.learning.pattern._05_command.macroremote.receiver.Hottub;
import org.learning.pattern._05_command.macroremote.receiver.Light;
import org.learning.pattern._05_command.macroremote.receiver.Stereo;
import org.learning.pattern._05_command.macroremote.receiver.TV;

public class RemoteLoader {

	public static void main(String[] args) {

		RemoteControl remoteControl = new RemoteControl();

		Light light = new Light("Living Room");
		TV tv = new TV("Living Room");
		Stereo stereo = new Stereo("Living Room");
		Hottub hottub = new Hottub();
 
		LightOnCommand lightOn = new LightOnCommand(light);
		StereoOnCommand stereoOn = new StereoOnCommand(stereo);
		TVOnCommand tvOn = new TVOnCommand(tv);
		HottubOnCommand hottubOn = new HottubOnCommand(hottub);
		LightOffCommand lightOff = new LightOffCommand(light);
		StereoOffCommand stereoOff = new StereoOffCommand(stereo);
		TVOffCommand tvOff = new TVOffCommand(tv);
		HottubOffCommand hottubOff = new HottubOffCommand(hottub);

		Command[] partyOn = { lightOn, stereoOn, tvOn, hottubOn};
		Command[] partyOff = { lightOff, stereoOff, tvOff, hottubOff};
  
		MacroCommand partyOnMacro = new MacroCommand(partyOn);
		MacroCommand partyOffMacro = new MacroCommand(partyOff);
 
		remoteControl.setCommand(0, partyOnMacro, partyOffMacro);
  
		System.out.println(remoteControl);
		System.out.println("--- Pushing Macro On---");
		remoteControl.onButtonWasPushed(0);
		System.out.println("--- Pushing Macro Off---");
		remoteControl.offButtonWasPushed(0);
	}
}
