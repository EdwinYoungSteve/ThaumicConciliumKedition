package com.keletu.thaumicconcilium.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class TCItemBlocks extends ItemBlock {
    public TCItemBlocks(Block block) {
        super(block);
        this.setTranslationKey(block.getTranslationKey());
        this.setRegistryName(block.getRegistryName());
    }
}
