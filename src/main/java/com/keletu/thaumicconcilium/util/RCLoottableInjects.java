package com.keletu.thaumicconcilium.util;

import com.keletu.thaumicconcilium.ThaumicConcilium;
import com.keletu.thaumicconcilium.items.TCItems;
import net.minecraft.world.storage.loot.LootEntryItem;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraft.world.storage.loot.functions.LootFunction;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import thecodex6824.thaumicaugmentation.api.TALootTables;

@EventBusSubscriber(modid = ThaumicConcilium.MODID)
public class RCLoottableInjects {
    @SubscribeEvent
    public static void onLootTableLoad(LootTableLoadEvent event) {
        if (event.getName().equals(TALootTables.PEDESTAL_RARE)) {
            LootPool main = event.getTable().getPool("pedestal_rare");
            main.addEntry(new LootEntryItem(TCItems.crimson_annales, 5, 0, new LootFunction[0], new LootCondition[0], "loottable:crimson_annales_pedestal"));
        }
        if (event.getName().equals(TALootTables.LOOT_RARE)) {
            LootPool main = event.getTable().getPool("loot_rare");
            main.addEntry(new LootEntryItem(TCItems.crimson_annales, 5, 0, new LootFunction[0], new LootCondition[0], "loottable:crimson_annales_urn"));
        }
    }
}