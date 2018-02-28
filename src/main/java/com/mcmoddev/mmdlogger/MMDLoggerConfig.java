package com.mcmoddev.mmdlogger;

import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Config(modid = MMDLogger.MODID)
public class MMDLoggerConfig {
    public static Options options = new Options();

    public static class Options {
        @Config.Comment("If true, then ore dict names and corresponding id's are logged.")
        public boolean loggingOn = false;

        @Config.Comment("If true, then ore dict names are displayed in tooltips.")
        public boolean oreDictTooltipsOn = false;

        @Config.Comment("If true, then item stack's nbt is displayed in tooltips.")
        public boolean nbtTooltipsOn = false;

        @Config.Comment("If true, then item block stack's block info is displayed in tooltips.")
        public boolean blockTooltipsOn = false;
    }

    @Mod.EventBusSubscriber(modid = MMDLogger.MODID)
    public static class ConfigSyncHandler
    {
        @SubscribeEvent
        public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event)
        {
            if(event.getModID().equals(MMDLogger.MODID))
            {
                ConfigManager.sync(MMDLogger.MODID, Config.Type.INSTANCE);
            }
        }
    }
}
