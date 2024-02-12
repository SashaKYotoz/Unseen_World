package net.sashakyotoz.unseenworld;

import net.minecraftforge.common.ForgeConfigSpec;

public class UnseenWorldModConfigs {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;
	public static final ForgeConfigSpec.DoubleValue REDUCING_OF_GRAVITY_CHANCE;
	public static final ForgeConfigSpec.DoubleValue METEORITESTROPHY_CHANCE;
	public static final ForgeConfigSpec.BooleanValue DEACTIVATE_GRAVITY_SPIKES;
	public static final ForgeConfigSpec.BooleanValue DEACTIVATE_LIFE_STEELING;
	public static final ForgeConfigSpec.BooleanValue DEACTIVATE_SHINING_BLADE;
	public static final ForgeConfigSpec.BooleanValue DEACTIVATE_MINING_BOOTS;
	public static final ForgeConfigSpec.IntValue LIFE_STEELING_POWER;
	public static final ForgeConfigSpec.IntValue SHINING_BLADE_POWER;
	public static final ForgeConfigSpec.IntValue GRAVITY_SPIKES_POWER;
	public static final ForgeConfigSpec.DoubleValue HEALTH_ATTRIBUTE_OF_DARK_WARRIOR;
	public static final ForgeConfigSpec.DoubleValue HEALTH_ATTRIBUTE_OF_BLAZER;
	public static final ForgeConfigSpec.DoubleValue HEALTH_ATTRIBUTE_OF_WITHER_KNIGHT;
	static {
		BUILDER.push("Configs for Unseen World");
		BUILDER.comment("--Configuration of Enchants and Effects--");
		REDUCING_OF_GRAVITY_CHANCE = BUILDER.comment("Determine chance of appling reducing of gravity effect on player").defineInRange("reducing_of_gravity_chance",0.0035,0.0005, 0.1);
		METEORITESTROPHY_CHANCE = BUILDER.comment("Determine chance of appling meteoritestrophy effect on player").defineInRange("meteoritestrophy_chance",0.0025,0.0005, 0.1);
		LIFE_STEELING_POWER = BUILDER.comment("Determine extra power of life steeling enchantment").defineInRange("extra_power_of_life_steeling",0,0, 5);
		GRAVITY_SPIKES_POWER = BUILDER.comment("Determine extra power of gravity thorns enchantment").defineInRange("extra_power_of_gravity_thorns",0,-1, 5);
		SHINING_BLADE_POWER = BUILDER.comment("Determine extra power of shining blade enchantment").defineInRange("extra_power_of_shining_blade",0,0, 5);
		DEACTIVATE_GRAVITY_SPIKES = BUILDER.comment("Determine if gravity thorns enchantment has to work").define("deactivate_spikes",false);
		DEACTIVATE_LIFE_STEELING = BUILDER.comment("Determine if life steel enchantment has to work").define("deactivate_life_steeling",false);
		DEACTIVATE_SHINING_BLADE = BUILDER.comment("Determine if shining blade enchantment has to work").define("deactivate_shining_blade",false);
		DEACTIVATE_MINING_BOOTS = BUILDER.comment("Determine if mining boots enchantment has to work").define("deactivate_mining_boots",false);
		BUILDER.comment("--Configuration of Mobs--");
		HEALTH_ATTRIBUTE_OF_DARK_WARRIOR = BUILDER.comment("Value of Dark Warrior Maximum health").defineInRange("warrior_health",300,50,1000d);
		HEALTH_ATTRIBUTE_OF_BLAZER = BUILDER.comment("Value of Blazer Maximum health").defineInRange("blazer_health",450,50,1000d);
		HEALTH_ATTRIBUTE_OF_WITHER_KNIGHT = BUILDER.comment("Value of Wither Knight Maximum health").defineInRange("knight_health",350,50,1000d);
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
