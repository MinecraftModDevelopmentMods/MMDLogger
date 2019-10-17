package com.mcmoddev.mmdlogger;

import net.minecraftforge.oredict.OreDictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
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
	protected static boolean oreDictTooltipsOn = false;
	protected static boolean nbtTooltipsOn = false;
	protected static boolean blockTooltipsOn = false;
	
	@EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
		MinecraftForge.EVENT_BUS.register(new MMDLoggerEventBusSubscriber());
		
		Configuration modConfig = new Configuration(event.getSuggestedConfigurationFile());
		modConfig.load();

		final String OPTIONS = "options";
		
		loggingOn = modConfig.getBoolean("OREDICT_LOGGING", OPTIONS, loggingOn,
				"If true, then ore dict names and corresponding id's are logged");
		
		oreDictTooltipsOn = modConfig.getBoolean("OREDICT_TOOLTIPS", OPTIONS, oreDictTooltipsOn,
				"If true, then ore dict names are displayed in tooltips");
		
		nbtTooltipsOn = modConfig.getBoolean("NBT_TOOLTIPS", OPTIONS, nbtTooltipsOn,
				"If true, then item stack's nbt is displayed in tooltips");
		
		blockTooltipsOn = modConfig.getBoolean("BLOCK_TOOLTIPS", OPTIONS, blockTooltipsOn,
				"If true, then item block stack's block info is displayed in tooltips");
		
		modConfig.save();
		
		if (loggingOn) {
			logger = LogManager.getFormatterLogger(MMDLogger.MODID);
		}
    }
	
    @EventHandler
    public void postInit(FMLPostInitializationEvent event)
    {
    	if (loggingOn || oreDictTooltipsOn) {
    		for (String oreName : OreDictionary.getOreNames()) {   
    			
        		if (loggingOn) {
    	    		for (ResourceLocation entry : Block.REGISTRY.getKeys()) {
    	    			Block block = Block.REGISTRY.getObject(entry);
    	    			logger.info("Block Registry Entry: " + "ID: " + Block.REGISTRY.getIDForObject(block) + " Name: " + entry.toString() );
    	    		}
    	    		
    	    		for (ResourceLocation entry : Item.REGISTRY.getKeys()) {
    	    			Item item = Item.REGISTRY.getObject(entry);
    	    			logger.info("Item Registry Entry: " + "ID: " + Item.REGISTRY.getIDForObject(item) + "  Name: " + entry.toString() );
    	    		}
        		}    		
        		
    			int oreID = OreDictionary.getOreID(oreName); 
    			List<ItemStack> items = OreDictionary.getOres(oreName);
    			
    			for (ItemStack itemStack : items) {
    				Item item = itemStack.getItem();
    				
    				int meta = item.getMetadata(itemStack);
    				
    				String blockName = item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
    				
    				ItemToOreDictMap.put(blockName + ":" + meta, oreName);
    				
    				if (loggingOn)
    					logger.info("Ore Dictionary Entry: Ore Name: " + oreName + ", Ore ID: " + oreID + ", Unlocalised Name: " + item.getUnlocalizedName() + ", Block ID: " + Item.getIdFromItem(item) + ", Block Meta: " + meta + ", Registry Name: " + item.getRegistryName());	
				}
    		}
    	}
    }    
}
