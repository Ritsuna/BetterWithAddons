package betterwithaddons.interaction;

import betterwithaddons.block.ModBlocks;
import betterwithaddons.crafting.conditions.ConditionModule;
import betterwithaddons.crafting.manager.CraftingManagerSpindle;
import betterwithaddons.crafting.recipes.HopperCratingRecipe;
import betterwithaddons.item.ModItems;
import betterwithaddons.lib.Reference;
import betterwithaddons.util.IngredientSized;
import betterwithmods.common.BWMBlocks;
import betterwithmods.common.BWMItems;
import betterwithmods.common.BWRegistry;
import betterwithmods.common.blocks.BlockAesthetic;
import betterwithmods.common.blocks.BlockRawPastry;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.HopperInteractions;
import betterwithmods.util.StackIngredient;
import com.google.common.collect.Lists;
import net.minecraft.block.BlockPlanks;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;
import net.minecraftforge.registries.ForgeRegistry;

import java.util.Arrays;
import java.util.List;

public class InteractionCondensedOutputs extends Interaction {
    public static boolean ENABLED = true;
    public static boolean LOSE_BINDER = false;
    public static boolean CAULDRON_COMPRESSES_SLIME = true;
    public static boolean SPINDLE_COMPRESSES_BOLTS = true;
    public static boolean HOPPER_COMPRESSES_CRATES = true;
    public static int SPINUP_TIME = 40;

    public ItemStack bagStack;
    public ItemStack crateStack;
    public ItemStack congealedStack;
    public ItemStack boltStack;
    public ItemStack bundleStack;

    public InteractionCondensedOutputs() {
    }

    @Override
    protected String getName() {
        return "addons.CondensedOutputs";
    }

    @Override
    void setupConfig() {
        ENABLED = loadPropBool("Enabled", "Whether the Condensed Outputs module is on. DISABLING THIS WILL DISABLE THE WHOLE MODULE.", ENABLED);
        LOSE_BINDER = loadPropBool("LoseBinder", "When uncrafting condensed materials, the binding material is not returned.", LOSE_BINDER);
        SPINDLE_COMPRESSES_BOLTS = loadPropBool("Spinning", "Whether the spindle can compress items into bolts.", SPINDLE_COMPRESSES_BOLTS);
        CAULDRON_COMPRESSES_SLIME = loadPropBool("Congealing", "Whether cauldrons can congeal items.", CAULDRON_COMPRESSES_SLIME);
        HOPPER_COMPRESSES_CRATES = loadPropBool("Crating", "Whether filtered hoppers can fill crates.", HOPPER_COMPRESSES_CRATES);
        doesNotNeedRestart(() -> {
            SPINUP_TIME = loadPropInt("SpindleTime", "The amount of time in ticks it takes for the spindle to spin up once.", SPINUP_TIME);
        });
    }

    @Override
    public boolean isActive() {
        return ENABLED;
    }

    @Override
    public void setEnabled(boolean active) {
        ENABLED = active;
        super.setEnabled(active);
    }

    @Override
    public List<Interaction> getDependencies() {
        return Arrays.asList(new Interaction[]{ModInteractions.bwm});
    }

    @Override
    public List<Interaction> getIncompatibilities() {
        return null;
    }

    @Override
    public void preInit() {
        ConditionModule.MODULES.put("CondensedOutputs", this::isActive);
    }

    @Override
    public void init() {
        if (!LOSE_BINDER) {
            ModItems.materialBag.setContainer(bagStack);
            ModItems.materialCrate.setContainer(crateStack);
            ModItems.materialCongealed.setContainer(congealedStack);
            ModItems.materialBolt.setContainer(boltStack);
            ModItems.materialBundle.setContainer(bundleStack);
        }

        BWRegistry.CAULDRON.addUnstokedRecipe(StackIngredient.fromOre(9,"dung"),new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.DUNG.getMeta()));

        CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH)}, IngredientSized.fromOredict("fiberHemp", 9), false);
        CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{new ItemStack(BWMBlocks.AESTHETIC, 1, BlockAesthetic.EnumType.ROPE.getMeta())}, IngredientSized.fromStacks(new ItemStack(BWMBlocks.ROPE, 9)), false);
    }

    @Override
    public void postInit() {

    }

    @Override
    void modifyRecipes(RegistryEvent.Register<IRecipe> event) {
        ForgeRegistry<IRecipe> registry = (ForgeRegistry<IRecipe>) event.getRegistry();

        bagStack = betterwithmods.common.items.ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH, 1);
        crateStack = new ItemStack(Blocks.PLANKS, 1);
        congealedStack = new ItemStack(Items.SLIME_BALL, 1);
        boltStack = new ItemStack(ModBlocks.spindle, 1);
        bundleStack = betterwithmods.common.items.ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_FIBERS, 1);

        addBaggingRecipe(registry, "seed", new ItemStack(Items.WHEAT_SEEDS));
        addBaggingRecipe(registry, "seed_hemp", new ItemStack(BWMBlocks.HEMP));
        addBaggingRecipe(registry, "seed_melon", new ItemStack(Items.MELON_SEEDS));
        addBaggingRecipe(registry, "seed_pumpkin", new ItemStack(Items.PUMPKIN_SEEDS));
        addBaggingRecipe(registry, "seed_beets", new ItemStack(Items.BEETROOT_SEEDS));
        addBaggingRecipe(registry, "cocoa", new ItemStack(Items.DYE, 1, EnumDyeColor.BROWN.getDyeDamage()));
        addBaggingRecipe(registry, "redstone", new ItemStack(Items.REDSTONE));
        addBaggingRecipe(registry, "glowstone", new ItemStack(Items.GLOWSTONE_DUST));
        addBaggingRecipe(registry, "sugar", new ItemStack(Items.SUGAR));
        addBaggingRecipe(registry, "gunpowder", new ItemStack(Items.GUNPOWDER));
        addBaggingRecipe(registry, "flour", new ItemStack(BWMBlocks.RAW_PASTRY, 1, BlockRawPastry.EnumType.BREAD.getMetadata()));
        addBaggingRecipe(registry, "sulfur", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.BRIMSTONE));
        addBaggingRecipe(registry, "nitre", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NITER));
        addBaggingRecipe(registry, "sawdust", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SAWDUST));
        addBaggingRecipe(registry, "sawdust_soul", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SOUL_DUST));
        addBaggingRecipe(registry, "potash", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.POTASH));
        addBaggingRecipe(registry, "hellfire", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HELLFIRE_DUST));
        addBaggingRecipe(registry, "kibble", new ItemStack(BWMItems.KIBBLE));

        addCratingRecipe(registry, "pork", new ItemStack(Items.COOKED_PORKCHOP));
        addCratingRecipe(registry, "pork_raw", new ItemStack(Items.PORKCHOP));
        addCratingRecipe(registry, "steak", new ItemStack(Items.COOKED_BEEF));
        addCratingRecipe(registry, "steak_raw", new ItemStack(Items.BEEF));
        addCratingRecipe(registry, "chicken", new ItemStack(Items.COOKED_CHICKEN));
        addCratingRecipe(registry, "chicken_raw", new ItemStack(Items.CHICKEN));
        addCratingRecipe(registry, "mutton", new ItemStack(Items.COOKED_MUTTON));
        addCratingRecipe(registry, "mutton_raw", new ItemStack(Items.MUTTON));
        addCratingRecipe(registry, "rabbit", new ItemStack(Items.COOKED_RABBIT));
        addCratingRecipe(registry, "rabbit_raw", new ItemStack(Items.RABBIT));
        addCratingRecipe(registry, "egg", new ItemStack(Items.EGG));
        addCratingRecipe(registry, "slime", new ItemStack(Items.SLIME_BALL));
        addCratingRecipe(registry, "enderpearl", new ItemStack(Items.ENDER_PEARL));
        addCratingRecipe(registry, "cactus", new ItemStack(Blocks.CACTUS));
        addCratingRecipe(registry, "inksac", new ItemStack(Items.DYE, 1, EnumDyeColor.BLACK.getDyeDamage()));

        addCongealingRecipe(registry, "mushroom", new ItemStack(Blocks.BROWN_MUSHROOM));
        addCongealingRecipe(registry, "amanita", new ItemStack(Blocks.RED_MUSHROOM));
        addCongealingRecipe(registry, "bone", new ItemStack(Items.BONE));
        addCongealingRecipe(registry, "flesh", new ItemStack(Items.ROTTEN_FLESH));
        addCongealingRecipe(registry, "eye", new ItemStack(Items.SPIDER_EYE));
        addCongealingRecipe(registry, "wart", new ItemStack(Items.NETHER_WART));

        addRollupRecipe(registry, "fabric", "fabricHemp", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HEMP_CLOTH));
        addRollupRecipe(registry, "vine", new ItemStack(Blocks.VINE));
        addRollupRecipe(registry, "paper", "paper", new ItemStack(Items.PAPER));
        addRollupRecipe(registry, "leather", new ItemStack(Items.LEATHER));
        addRollupRecipe(registry, "scoured_leather", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.SCOURED_LEATHER));
        addRollupRecipe(registry, "tanned_leather", ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.TANNED_LEATHER));
        addRollupRecipe(registry, "string", "string", new ItemStack(Items.STRING));

        addBundlingRecipe(registry, "feather", new ItemStack(Items.FEATHER));
        addBundlingRecipe(registry, "blazerods", new ItemStack(Items.BLAZE_ROD));
        addBundlingRecipe(registry, "arrows", new ItemStack(Items.ARROW));
        addBundlingRecipe(registry, "oak", new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.OAK.getMetadata()));
        addBundlingRecipe(registry, "birch", new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.BIRCH.getMetadata()));
        addBundlingRecipe(registry, "spruce", new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.SPRUCE.getMetadata()));
        addBundlingRecipe(registry, "jungle", new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.JUNGLE.getMetadata()));
        addBundlingRecipe(registry, "acacia", new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.ACACIA.getMetadata()));
        addBundlingRecipe(registry, "darkoak", new ItemStack(Blocks.SAPLING, 1, BlockPlanks.EnumType.DARK_OAK.getMetadata()));

    }

    private void addBaggingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material) {
        ItemStack output = ModItems.materialBag.getMaterial(id);

        addCondensingRecipe(registry, id, output, material, bagStack);
        addUncondensingRecipe(registry, id, output, material);
    }

    private void addCratingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material) {
        ItemStack output = ModItems.materialCrate.getMaterial(id);

        addCondensingRecipe(registry, id, output, material, crateStack);
        addUncondensingRecipe(registry, id, output, material);

        if (HOPPER_COMPRESSES_CRATES) {
            HopperInteractions.addHopperRecipe(new HopperCratingRecipe(Ingredient.fromStacks(material), output));
        }
    }

    private void addCongealingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material) {
        ItemStack output = ModItems.materialCongealed.getMaterial(id);

        addCondensingRecipe(registry, id, output, material, congealedStack);
        addUncondensingRecipe(registry, id, output, material);

        if (CAULDRON_COMPRESSES_SLIME) {
            ItemStack material8 = material.copy();
            material8.setCount(8);
            BWRegistry.CAULDRON.addUnstokedRecipe(Lists.newArrayList(getIngredient(congealedStack),getIngredient(material8)),Lists.newArrayList(output));
        }
    }

    private StackIngredient getIngredient(ItemStack stack) {
        return StackIngredient.fromStacks(stack);
    }

    private void addRollupRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material) {
        ItemStack output = ModItems.materialBolt.getMaterial(id);

        addCondensingRecipe(registry, id, output, material, boltStack);
        addUncondensingRecipe(registry, id, output, material);

        if (SPINDLE_COMPRESSES_BOLTS) {
            ItemStack material8 = material.copy();
            material8.setCount(8);
            CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{output}, IngredientSized.fromStacks(material8), true);
        }
    }

    private void addRollupRecipe(ForgeRegistry<IRecipe> registry, String id, String material, ItemStack materialStack) {
        ItemStack output = ModItems.materialBolt.getMaterial(id);

        addCondensingRecipe(registry, id, output, material, boltStack);
        addUncondensingRecipe(registry, id, output, materialStack);

        if (SPINDLE_COMPRESSES_BOLTS) {
            CraftingManagerSpindle.getInstance().addRecipe(new ItemStack[]{output}, IngredientSized.fromOredict(material, 8), true);
        }
    }

    private void addBundlingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack material) {
        ItemStack output = ModItems.materialBundle.getMaterial(id);

        addCondensingRecipe(registry, id, output, material, bundleStack);
        addUncondensingRecipe(registry, id, output, material);
    }

    private void addCondensingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack output, ItemStack material, ItemStack frame) {
        ItemStack outmaterial = material.copy();
        outmaterial.setCount(8);
        ResourceLocation compressLoc = new ResourceLocation(Reference.MOD_ID, "compress_" + id);

        registry.register(new ShapedOreRecipe(compressLoc, output, "aaa", "aba", "aaa", 'a', material, 'b', frame).setRegistryName(compressLoc));
    }

    private void addCondensingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack condensed, String material, ItemStack frame) {
        ResourceLocation compressLoc = new ResourceLocation(Reference.MOD_ID, "compress_" + id);

        registry.register(new ShapedOreRecipe(compressLoc, condensed, "aaa", "aba", "aaa", 'a', material, 'b', frame).setRegistryName(compressLoc));
    }

    private void addUncondensingRecipe(ForgeRegistry<IRecipe> registry, String id, ItemStack condensed, ItemStack material) {
        ItemStack outmaterial = material.copy();
        outmaterial.setCount(8);

        ResourceLocation uncompressLoc = new ResourceLocation(Reference.MOD_ID, "uncompress_" + id);

        registry.register(new ShapelessOreRecipe(uncompressLoc, outmaterial, condensed).setRegistryName(uncompressLoc));
    }
}
