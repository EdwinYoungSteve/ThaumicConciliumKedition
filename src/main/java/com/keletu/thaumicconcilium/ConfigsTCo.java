package com.keletu.thaumicconcilium;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = ThaumicConcilium.MODID)
public class ConfigsTCo {

    @Config.LangKey("thaumaturgeSpawnChance")
    @Config.Comment("Thaumaturge Golem spawn chance")
    @Config.RangeInt(min = 0)
    public static int thaumaturgeSpawnChance = 10;

    @Config.LangKey("vengefulGolemSpawnChance")
    @Config.Comment("Vengeful Golem spawn chance")
    @Config.RangeInt(min = 0)
    public static int vengefulGolemSpawnChance = 10;

    @Config.LangKey("quicksilverImmortality")
    @Config.Comment("Will quicksilver elemental be immune to non-fire attacks.")
    public static boolean quicksilverImmortality = true;

    @Config.LangKey("quicksilverElementalSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int quicksilverElementalSpawnChance = 10;
    @Config.LangKey("dissolvedSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int dissolvedSpawnChance = 10;
    @Config.LangKey("overanimatedSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int overanimatedSpawnChance = 10;

    @Config.LangKey("strayedMirrorSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int strayedMirrorSpawnChance = 10;

    @Config.LangKey("paranoidWarriorSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int paranoidWarriorSpawnChance = 10;

    @Config.LangKey("madThaumaturgeSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int madThaumaturgeSpawnChance = 10;

    @Config.LangKey("madThaumaturgeSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int madThaumaturgeReplacesBrainyZombieChance = 80;

    @Config.LangKey("madThaumaturgeSpawnChance")
    @Config.RangeInt(min = 0, max = 99)
    public static int crimsonPaladinReplacesCultistWarriorChance = 80;

    @Mod.EventBusSubscriber
    private static class EventHandler {

        /**
         * Inject the new values and save to the config file when the config has been changed from the GUI.
         *
         * @param event The event
         */
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
            if (event.getModID().equals(ThaumicConcilium.MODID)) {
                ConfigManager.sync(ThaumicConcilium.MODID, Config.Type.INSTANCE);
            }
        }
    }
}