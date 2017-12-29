package com.mcmoddev.mmdlogger;

import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author SkyBlade1978
 *
 */
@Mod(modid = MMDLogger.MODID, version = MMDLogger.VERSION)
public class MMDLogger {
    public static final String MODID = "mmdlogger";
    public static final String VERSION = "1.0";
    
	private Logger logger;
	private boolean loggingOn = false;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		Configuration modConfig = new Configuration(event.getSuggestedConfigurationFile());
		modConfig.load();

		final String OPTIONS = "options";

		loggingOn = modConfig.getBoolean("OREDICT_LOGGING", OPTIONS, loggingOn,
				"If true, then ore dict names and corresponding id's are logged");
		
		modConfig.save();
		
		if (loggingOn) {
			logger = LogManager.getFormatterLogger(MMDLogger.MODID);
		}
    }
	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	if (loggingOn) {	
    		List<ItemStack> items;
    		int oreID;
    		
    		for (String oreName : OreDictionary.getOreNames()) {    			
    			oreID = OreDictionary.getOreID(oreName); 
    			items = OreDictionary.getOres(oreName);
    			
    			for (ItemStack itemStack : items) {
    				Item item = itemStack.getItem();
    				
    				logger.info("Ore Dictionary Entry: Ore Name: %s, Ore ID: %s, Unlocalised Name: %s, Block ID: %s, Registry Name: %s", oreName, oreID, item.getUnlocalizedName(), Item.getIdFromItem(item), item.getRegistryName());	
				}
    		}
    	}
    }
}
