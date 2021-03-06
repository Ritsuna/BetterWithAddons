package betterwithaddons.item;

import betterwithmods.module.tweaks.Dung;
import betterwithmods.module.tweaks.Dung.DungProducer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;

public class ItemLaxative extends ItemFood {
    public ItemLaxative(int amount, float saturation, boolean isWolfFood) {
        super(amount, saturation, isWolfFood);
    }

    @Override
    public boolean itemInteractionForEntity(ItemStack stack, EntityPlayer playerIn, EntityLivingBase target, EnumHand hand) {
        if (feedLaxative(target)) {
            stack.shrink(1);
            return true;
        }

        return super.itemInteractionForEntity(stack, playerIn, target, hand);
    }

    public static boolean feedLaxative(EntityLivingBase target) {
        if(target.hasCapability(Dung.DUNG_PRODUCER_CAP,null))
        {
            DungProducer poopCapability = target.getCapability(Dung.DUNG_PRODUCER_CAP,null);
            if(poopCapability.nextPoop > 4000) {
                poopCapability.nextPoop = poopCapability.nextPoop / 3;
            }
            return true;
        }
        return false;
    }
}
