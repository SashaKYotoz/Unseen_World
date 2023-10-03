package net.sashakyotoz.unseenworld;

import net.minecraftforge.common.ForgeConfigSpec;

public class UnseenWorldModCommonConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.ConfigValue<Double> DARK_GOLEM_HEALTH_POINTS;
	public static final ForgeConfigSpec.ConfigValue<Double> DARK_GOLEM_DAMAGE;
	public static final ForgeConfigSpec.ConfigValue<Double> DARK_GOLEM_SPEED;
	public static final ForgeConfigSpec.ConfigValue<Double> WITHER_KNIGHT_HEALTH_POINTS;
	public static final ForgeConfigSpec.ConfigValue<Double> WITHER_KNIGHT_DAMAGE;
	public static final ForgeConfigSpec.ConfigValue<Double> WITHER_KNIGHT_SPEED;
	public static final ForgeConfigSpec.ConfigValue<Double> BLAZER_HEALTH_POINTS;
	static {
		BUILDER.push("Configs for Unseen World Mod");
		DARK_GOLEM_HEALTH_POINTS = BUILDER.comment("Determine amount of Dark Warrior health points!").defineInRange("Dark Warrior HP", 300.0, 100, 1000);
		DARK_GOLEM_DAMAGE = BUILDER.comment("Determine amount of Dark Warrior damage!").defineInRange("Dark Warrior Damage points", 10.0, 2, 25);
		DARK_GOLEM_SPEED = BUILDER.comment("Determine speed of Dark Warrior movement!").defineInRange("Dark Warrior speed factor", 0.25, 0.1, 1.5);
		WITHER_KNIGHT_HEALTH_POINTS = BUILDER.comment("Determine amount of Wither Knight health points!").defineInRange("Wither Knight HP", 350.0, 100, 1000);
		WITHER_KNIGHT_DAMAGE = BUILDER.comment("Determine amount of Wither Knight damage!").defineInRange("Wither Knight Damage points", 12.0, 2, 25);
		WITHER_KNIGHT_SPEED = BUILDER.comment("Determine speed of Wither Knight movement!").defineInRange("Wither Knight speed factor", 0.2, 0.1, 1.5);
		BLAZER_HEALTH_POINTS = BUILDER.comment("Determine amount of Blazer health points!").defineInRange("Blazer HP", 450.0, 100, 1000);
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
