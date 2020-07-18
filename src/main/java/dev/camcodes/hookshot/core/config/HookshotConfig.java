package dev.camcodes.hookshot.core.config;

import blue.endless.jankson.Comment;
import io.github.cottonmc.cotton.config.annotations.ConfigFile;

@ConfigFile(name="Hookshot")
public class HookshotConfig
{
	@Comment(value="The maximum range the Hookshot can reach.")
	public double hsMaxRange = 24D;
	@Comment(value="The maximum pulling speed the Hookshot can do.")
	public double hsMaxVelocity = 10D;

	@Comment(value="The maximum range the Longshot can reach.")
	public double lsMaxRange = 48D;
	@Comment(value="The maximum pulling speed the Longshot can do.")
	public double lsMaxVelocity = 10D;

	@Comment(value="The maximum range the Speedshot can reach.")
	public double ssMaxRange = 24D;
	@Comment(value="The maximum pulling speed the Speedshot can do.")
	public double ssMaxVelocity = 15D;
}
