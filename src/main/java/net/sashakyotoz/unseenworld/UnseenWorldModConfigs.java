package net.sashakyotoz.unseenworld;

import net.minecraftforge.common.ForgeConfigSpec;

public class UnseenWorldModConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.DoubleValue REDUCING_OF_GRAVITY_CHANCE;
	public static final ForgeConfigSpec.DoubleValue METEORITESTROPHY_CHANCE;
	static {
		BUILDER.push("Configs for Unseen World");
		REDUCING_OF_GRAVITY_CHANCE = BUILDER.comment("Determine chance of appling reducing of gravity effect on player").defineInRange("reducing_of_gravity_chance",0.0035,0.0005, 0.1);
		METEORITESTROPHY_CHANCE = BUILDER.comment("Determine chance of appling meteoritestrophy effect on player").defineInRange("meteoritestrophy_chance",0.0025,0.0005, 0.1);
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
