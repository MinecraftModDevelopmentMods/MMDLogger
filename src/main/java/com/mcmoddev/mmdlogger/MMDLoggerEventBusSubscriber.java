package com.mcmoddev.mmdlogger;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MMDLogger.MODID)
public class MMDLoggerEventBusSubscriber {

	@SubscribeEvent
    public void onToolTip(ItemTooltipEvent event)
    {
		if (MMDLogger.tooltipsOn) {
			ItemStack stack = event.getItemStack();
			Item item = stack.getItem();
			
			if (item == null)
				return;
			
			String blockName = item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
	    	int meta = item.getMetadata(stack);
	    	
	    	
	    	String oreName = MMDLogger.ItemToOreDictMap.get(blockName + ":" + meta);
	    	
	    	List<String> tooltips = event.getToolTip();
		    
	    	if (oreName != null) {
	    		tooltips.add(TextFormatting.BLUE.toString() + TextFormatting.ITALIC.toString() + oreName);
	    	}
    	
		}
    }
}
