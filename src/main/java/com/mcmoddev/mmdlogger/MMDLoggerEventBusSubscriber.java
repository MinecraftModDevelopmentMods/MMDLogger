package com.mcmoddev.mmdlogger;

import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber(modid = MMDLogger.MODID)
public class MMDLoggerEventBusSubscriber {
	@SubscribeEvent
    public static void onToolTip(ItemTooltipEvent event)
    {
        List<String> tooltips = event.getToolTip();

		if (MMDLoggerConfig.options.oreDictTooltipsOn) {
            ItemStack stack = event.getItemStack();
            Item item = stack.getItem();

            if (item == null)
                return;

            // ore dictionary
            String blockName = item.getRegistryName().getResourceDomain() + ":" + item.getRegistryName().getResourcePath();
            int meta = item.getMetadata(stack);

            String oreName = MMDLogger.ItemToOreDictMap.get(blockName + ":" + meta);

            if (oreName != null) {
                tooltips.add(TextFormatting.BLUE.toString() + TextFormatting.ITALIC.toString() + oreName);
            }
        }

        if (MMDLoggerConfig.options.nbtTooltipsOn) {
	    	// nbt
			NBTTagCompound stackNbt = event.getItemStack().getTagCompound();
	    	if ((stackNbt != null) && !stackNbt.hasNoTags()) {
	    		tooltips.add(TextFormatting.GRAY + "NBT: " + stackNbt.toString());
			}
		}
    }
}
