package com.mcmoddev.mmdlogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.oredict.OreDictionary;

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
//	private boolean loggingOn = true;
//	protected static boolean oreDictTooltipsOn = true;
//	protected static boolean nbtTooltipsOn = true;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new MMDLoggerEventBusSubscriber());
		
//		Configuration modConfig = new Configuration(event.getSuggestedConfigurationFile());
//		modConfig.load();

//		final String OPTIONS = "options";

//		loggingOn = modConfig.getBoolean("OREDICT_LOGGING", OPTIONS, loggingOn,
//				"If true, then ore dict names and corresponding id's are logged.");
//
//		oreDictTooltipsOn = modConfig.getBoolean("OREDICT_TOOLTIPS", OPTIONS, oreDictTooltipsOn,
//				"If true, then ore dict names are displayed in tooltips.");
//
//		nbtTooltipsOn = modConfig.getBoolean("NBT_TOOLTIPS", OPTIONS, nbtTooltipsOn,
//			"If true, then item stack's nbt is displayed in tooltips.");
//
//		modConfig.save();
		
		if (MMDLoggerConfig.options.loggingOn) {
			logger = LogManager.getFormatterLogger(MMDLogger.MODID);
		}
    }
	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	if (MMDLoggerConfig.options.loggingOn || MMDLoggerConfig.options.oreDictTooltipsOn) {
    		for (String oreName : OreDictionary.getOreNames()) {    			
    			int oreID = OreDictionary.getOreID(oreName); 
    			List<ItemStack> items = OreDictionary.getOres(oreName);
    			
    			for (ItemStack itemStack : items) {
    				Item item = itemStack.getItem();
    				
    				int meta = item.getMetadata(itemStack);
    				
    				ItemToOreDictMap.put(item.getRegistryName().getNamespace() + ":" + item.getRegistryName().getPath() + ":" + meta, oreName);
    				
    				if (MMDLoggerConfig.options.loggingOn)
    					logger.info("Ore Dictionary Entry: Ore Name: %s, Ore ID: %s, Unlocalised Name: %s, Block ID: %s, Block Meta: %s, Registry Name: %s", oreName, oreID, item.getTranslationKey(), Item.getIdFromItem(item), meta, item.getRegistryName());	
				}
    		}
    	}
    }    
}
