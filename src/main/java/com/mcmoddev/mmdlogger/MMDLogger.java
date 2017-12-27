package com.mcmoddev.mmdlogger;

import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = MMDLogger.MODID, version = MMDLogger.VERSION)
public class MMDLogger
{
    public static final String MODID = "mmdlogger";
    public static final String VERSION = "1.0";
    
	public static final Logger logger = LogManager.getFormatterLogger(MMDLogger.MODID);
	private boolean loggingOn = false;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		Configuration config = new Configuration(event.getSuggestedConfigurationFile());
		config.load();

		final String OPTIONS = "options";

		loggingOn = config.getBoolean("OREDICT_LOGGING", OPTIONS, loggingOn,
				"If true, then ore dict names and corresponding id's are logged");
		
		config.save();
    }
	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	if (loggingOn)
    		for (String oreName : OreDictionary.getOreNames()) 
    			logger.info("Ore Dictionary Entry: %s, %s", oreName, 
    					OreDictionary.getOreID(oreName));
		
    }
}
