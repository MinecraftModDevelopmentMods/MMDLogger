package com.mcmoddev.mmdlogger;

import net.minecraftforge.oredict.OreDictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
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
    
	protected static final Map<String, String> ItemToOreDictMap = new HashMap<>();
    
	private Logger logger;
	private boolean loggingOn = false;
	protected static boolean tooltipsOn = false;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new MMDLoggerEventBusSubscriber());
		
		Configuration modConfig = new Configuration(event.getSuggestedConfigurationFile());
		modConfig.load();

		final String OPTIONS = "options";

		loggingOn = modConfig.getBoolean("OREDICT_LOGGING", OPTIONS, loggingOn,
				"If true, then ore dict names and corresponding id's are logged");
		
		tooltipsOn = modConfig.getBoolean("OREDICT_TOOLTIPS", OPTIONS, tooltipsOn,
				"If true, then ore dict names are displayed in tooltips");
		
		modConfig.save();
		
		if (loggingOn) {
			logger = LogManager.getFormatterLogger(MMDLogger.MODID);
		}
    }
	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	if (loggingOn || tooltipsOn) {
    		for (String oreName : OreDictionary.getOreNames()) {    			
    			int oreID = OreDictionary.getOreID(oreName); 
    			List<ItemStack> items = OreDictionary.getOres(oreName);
    			
    			for (ItemStack itemStack : items) {
    				Item item = itemStack.getItem();
    				
    				int meta = item.getMetadata(itemStack);
    				
    				ItemToOreDictMap.put(item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath() + ":" + meta, oreName);
    				
    				if (loggingOn)
    					logger.info("Ore Dictionary Entry: Ore Name: %s, Ore ID: %s, Unlocalised Name: %s, Block ID: %s, Block Meta: %s, Registry Name: %s", oreName, oreID, item.getUnlocalizedName(), Item.getIdFromItem(item), meta, item.getRegistryName());	
				}
    		}
    	}
    }    
}
