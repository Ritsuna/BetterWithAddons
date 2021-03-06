package betterwithaddons.interaction;

import betterwithaddons.crafting.conditions.ConditionModule;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.List;

public class InteractionQuark extends Interaction {
    final String modid = "quark";
    public static boolean ENABLED = true;
    public static boolean MIDORI_BLOCKS_NEED_CHUNKS = true;

    @Override
    protected String getName() {
        return "interaction.Quark";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled", "Whether the Quark compat module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.", ENABLED);
        MIDORI_BLOCKS_NEED_CHUNKS = loadPropBool("MidoriBlocksNeedChunks", "Midori blocks require popped Midori chunks.", MIDORI_BLOCKS_NEED_CHUNKS);
    }

    @Override
    public boolean isActive() {
        return ENABLED && Loader.isModLoaded(modid);
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
    }

    @Override
    public List<Interaction> getDependencies() {
        return null;
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        ConditionModule.MODULES.put("MidoriNeedsChunks", () -> MIDORI_BLOCKS_NEED_CHUNKS);
    }

    @Override
    public void init() {
    }

    @Override
    public void postInit() {
    }

    @Override
    public void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        ForgeRegistry<IRecipe> reg = (ForgeRegistry<IRecipe>) event.getRegistry();
        Block midoriBlock = Block.REGISTRY.getObject(new ResourceLocation(modid,"midori_block"));

        if(MIDORI_BLOCKS_NEED_CHUNKS)
            removeRecipeByOutput(reg, new ItemStack(midoriBlock,4),modid);
    }
}
