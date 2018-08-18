package com.mcmoddev.mmdlogger;

import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
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
        ItemStack stack = event.getItemStack();
        //noinspection ConstantConditions
        if ((stack == null) || stack.isEmpty()) {
            return;
        }

        // TODO: consider testing for advanced tooltips being turned on also
        // if (!event.getFlags().isAdvanced()) return;

		if (MMDLoggerConfig.options.oreDictTooltipsOn) {
            Item item = stack.getItem();
            //noinspection ConstantConditions
            if (item == null)
                return;

            String blockName = item.getRegistryName().getNamespace() + ":" + item.getRegistryName().getPath();
            int meta = item.getMetadata(stack);

            String oreName = MMDLogger.ItemToOreDictMap.get(blockName + ":" + meta);

            if (oreName != null) {
                tooltips.add(TextFormatting.BLUE.toString() + TextFormatting.ITALIC.toString() + oreName);
            }
        }

        if (MMDLoggerConfig.options.nbtTooltipsOn) {
			NBTTagCompound stackNbt = event.getItemStack().getTagCompound();
	    	if ((stackNbt != null) && !stackNbt.isEmpty()) {
	    		tooltips.add(TextFormatting.GRAY + "NBT: " + stackNbt.toString());
			}
		}

		if (MMDLoggerConfig.options.blockTooltipsOn && (stack.getItem() instanceof ItemBlock)) {
            Block block = ((ItemBlock)stack.getItem()).getBlock();
            IBlockState state = block.getDefaultState();

            tooltips.add(TextFormatting.GRAY + "Occlusion: " + state.getAmbientOcclusionLightValue());
            tooltips.add(TextFormatting.DARK_GRAY + "Light: " + state.getLightValue());

            try {
                Field hardness = Block.class.getDeclaredField("blockHardness");
                hardness.setAccessible(true);
                tooltips.add(TextFormatting.GRAY + "Hardness: " + hardness.get(block));
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}

            try {
                Field resistance = Block.class.getDeclaredField("blockResistance");
                resistance.setAccessible(true);
                tooltips.add(TextFormatting.GRAY + "Resistance: " + resistance.get(block));
            } catch (NoSuchFieldException | IllegalAccessException ignored) {}

            tooltips.add(TextFormatting.DARK_GRAY + "Flammability: " + Blocks.FIRE.getFlammability(block));

            tooltips.add(TextFormatting.GRAY + "Max stack size: " + stack.getMaxStackSize());
        }
    }
}
