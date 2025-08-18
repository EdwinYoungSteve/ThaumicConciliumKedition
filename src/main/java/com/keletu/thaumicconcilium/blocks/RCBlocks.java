package com.keletu.thaumicconcilium.blocks;

import com.keletu.thaumicconcilium.ThaumicConcilium;
import net.minecraft.block.Block;

public class RCBlocks {
    public static final Block destabilized_crystal = new BlockDestabilizedCrystal().setRegistryName("destabilized_crystal").setTranslationKey("destabilized_crystal").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Block vis_condenser = new BlockVisCondenser().setRegistryName("vis_condenser").setTranslationKey("vis_condenser").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Block hex_of_predictability = new BlockHexOfPredictability().setRegistryName("hex_of_predictability").setTranslationKey("hex_of_predictability").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);


    public static final Block quicksilver_crucible = new BlockQuicksilverCrucible().setRegistryName("quicksilver_crucible").setTranslationKey("quicksilver_crucible").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);

}
