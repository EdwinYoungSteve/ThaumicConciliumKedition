package com.keletu.thaumicconcilium.items;

import com.keletu.thaumicconcilium.ThaumicConcilium;
import com.keletu.thaumicconcilium.blocks.TCBlocks;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.util.EnumHelper;

public class TCItems {
    public static final EnumRarity rarityCrimson = EnumHelper.addRarity("CRIMSON", TextFormatting.DARK_RED, "Crimson");
    public static final Item pontifex_hood = new ItemPontifexRobe(0, EntityEquipmentSlot.HEAD).setRegistryName("pontifex_hood").setTranslationKey("pontifex_hood").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item pontifex_robe = new ItemPontifexRobe(0, EntityEquipmentSlot.CHEST).setRegistryName("pontifex_robe").setTranslationKey("pontifex_robe").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item pontifex_legs = new ItemPontifexRobe(1, EntityEquipmentSlot.LEGS).setRegistryName("pontifex_legs").setTranslationKey("pontifex_legs").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item pontifex_boots = new ItemPontifexRobe(0, EntityEquipmentSlot.FEET).setRegistryName("pontifex_boots").setTranslationKey("pontifex_boots").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item molot = new ItemPontifexHammer().setRegistryName("molot").setTranslationKey("molot").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item crimson_annales = new ItemCrimsonAnnales().setRegistryName("crimson_annales").setTranslationKey("crimson_annales").setMaxStackSize(1).setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item research_notes_crimson = new ItemResources(rarityCrimson).setRegistryName("research_notes_crimson").setTranslationKey("research_notes_crimson").setMaxStackSize(1).setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item runic_chestplate = new ItemRunicWindings(0, EntityEquipmentSlot.CHEST).setRegistryName("runic_chestplate").setTranslationKey("runic_chestplate").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item runic_leggings = new ItemRunicWindings(1, EntityEquipmentSlot.LEGS).setRegistryName("runic_leggings").setTranslationKey("runic_leggings").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item bottle_of_thick_taint = new ItemBottleOfThickTaint().setRegistryName("bottle_thick_taint").setTranslationKey("bottle_thick_taint").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item research_page = new ItemResearchPage().setRegistryName("research_page").setTranslationKey(ThaumicConcilium.MODID + "." + "research_page").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item vis_conductor = new ItemVisConductor().setRegistryName("vis_conductor").setTranslationKey("vis_conductor").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item dump_jackboots = new ItemDumpJackboots().setRegistryName("dump_jackboots").setTranslationKey("dump_jackboots").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item tight_belt = new ItemTightBelt().setRegistryName("tight_belt").setTranslationKey("tight_belt").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item burdening_amulet = new ItemBurdeningAmulet().setRegistryName("burdening_amulet").setTranslationKey("burdening_amulet").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item void_slag = new Item().setRegistryName("void_slag").setTranslationKey("void_slag").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item primordial_life = new ItemPrimordialLife().setRegistryName("primordial_life").setTranslationKey("primordial_life").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item blustering_ring = new RingOfBlusteringLight().setRegistryName("ring_of_blustering_light").setTranslationKey("ring_of_blustering_light").setCreativeTab(ThaumicConcilium.tabThaumicConcilium);
    public static final Item item_icon = new ItemIcon().setRegistryName("icon").setTranslationKey("icon");

    public static final Item quicksilverCrucible = new TCItemBlocks(TCBlocks.quicksilver_crucible);
    public static final Item destabilizedCrystal = new ItemBlockDestabilizedCrystal(TCBlocks.destabilized_crystal);
    public static final Item visCondenser = new ItemBlockDestabilizedCrystal(TCBlocks.vis_condenser);
    public static final Item hexOfPredictor = new ItemBlockDestabilizedCrystal(TCBlocks.hex_of_predictability);
}
