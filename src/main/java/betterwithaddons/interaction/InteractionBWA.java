package betterwithaddons.interaction;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.block.BlockAqueduct;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.handler.*;
import betterwithaddons.item.ModItems;
import betterwithaddons.tileentity.TileEntityAqueductWater;
import betterwithaddons.tileentity.TileEntityLureTree;
import betterwithaddons.util.ItemUtil;
import betterwithmods.common.BWMItems;
import betterwithmods.common.items.ItemMaterial;
import betterwithmods.common.registry.steelanvil.SteelCraftingManager;
import betterwithmods.module.compat.minetweaker.SteelAnvil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.BlockQuartz;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.List;
import java.util.Random;

public class InteractionBWA extends Interaction {
    public static boolean GATED_AQUEDUCTS = true;
    public static int AQUEDUCT_MAX_LENGTH = 128;
    public static String[] AQUEDUCT_BIOME_STRINGS = new String[0];
    public static boolean AQUEDUCT_BIOMES_IS_WHITELIST = true;

    public static boolean OBVIOUS_STORMS = false;
    public static boolean OBVIOUS_SAND_STORMS = false;
    public static int DUST_PARTICLES = 2;
    public static int AIR_PARTICLES = 3;

    public static boolean GRASS_TO_CLAY = false;
    public static boolean GRASS_TO_SAND = false;

    public static boolean STONEBRICKS_NEED_SMELTING = false;

    public static boolean ARMOR_SHARD_RENDER = true;
    public static double LEGENDARIUM_MIN_DAMAGE = 0.1f;
    public static int LEGENDARIUM_DAMAGE_PAD = 24;
    public static int LEGENDARIUM_POSTER_RANGE = 16;
    public static int LEGENDARIUM_MIN_QUEUE_SIZE = 7;
    public static int LEGENDARIUM_TURN_IN_DELAY = 24000 * 5;

    public static int RADIUS = 6;
    public static int MAXCHARGE = 600;
    public static int MAXFOOD = 5000;

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public void setEnabled(boolean active) {
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
        MinecraftForge.EVENT_BUS.register(new AssortedHandler());
        MinecraftForge.EVENT_BUS.register(new ToolShardRepairHandler());
        //MinecraftForge.EVENT_BUS.register(new TerratorialHandler()); //TODO: Make this do something
        MinecraftForge.EVENT_BUS.register(new ElytraUpdriftHandler());
        MinecraftForge.EVENT_BUS.register(new HarvestHandler());
        if(OBVIOUS_SAND_STORMS || OBVIOUS_STORMS)
            MinecraftForge.EVENT_BUS.register(new StormHandler());
        if(GRASS_TO_CLAY || GRASS_TO_SAND) {
            PatientiaHandler.addCustomBlock(Blocks.GRASS);
            MinecraftForge.EVENT_BUS.register(new GrassHandler());
        }
    }

    @Override
    public void init() {
        if(PatientiaHandler.shouldRegister())
            MinecraftForge.EVENT_BUS.register(new PatientiaHandler());

        if(!ModInteractions.bwm.isActive()) //add recipes even if better with mods isn't installed.
        {
            String oreArrowhead = "ingotIron";
            if(OreDictionary.doesOreNameExist("ingotSteel"))
                oreArrowhead = "ingotSteel";
            String oreHaft = "stickWood";
            String oreGlue = "slimeball";
            String oreString = "string";
            ItemStack bow = new ItemStack(Items.BOW);
            ItemStack feather = new ItemStack(Items.FEATHER);
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatarrow,1)," a "," b ","cbc",'a',oreArrowhead,'b',oreHaft,'c',feather));
            GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModItems.greatbow,1),"bac","ed ","bac",'a',oreArrowhead,'b',oreHaft,'c',oreString,'d',bow,'e',oreGlue));
            GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.pcbblock),new ItemStack(Blocks.STONEBRICK),new ItemStack(Items.FERMENTED_SPIDER_EYE));

            GameRegistry.addSmelting(ModItems.material.getMaterial("midori"),ModItems.material.getMaterial("midori_popped"),0.1f);

            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScale,1)," i ","iai"," i ",'a',new ItemStack(ModBlocks.worldScaleOre,0,1),'i',new ItemStack(Items.IRON_INGOT));
        }


        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.spade),"x","x","i","i",'x',"ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.matchPick),"xxx","nic"," i "," i ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT),'n',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.NETHERCOAL),'c',"ingotConcentratedHellfire");
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.machete),"   x","  x "," x  ","i   ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.kukri),"xx","x ","xx"," i",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.carpenterSaw),"xxxi","x x ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));
        SteelCraftingManager.getInstance().addSteelShapedOreRecipe(new ItemStack(ModItems.masonPick),"xxxx"," i  "," i  "," i  ",'x', "ingotSoulforgedSteel",'i',ItemMaterial.getMaterial(ItemMaterial.EnumMaterial.HAFT));

        TileEntityLureTree.addTreeFood(new ItemStack(Items.GLOWSTONE_DUST),450);

        //TODO: Make this more sensible holy shit
        TileEntityAqueductWater.reloadBiomeList();

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.legendarium,1) ,"gsg","qqq","gqg",'g',new ItemStack(Items.GOLD_INGOT),'q',new ItemStack(Blocks.QUARTZ_BLOCK,1, BlockQuartz.EnumType.CHISELED.getMetadata()),'s', new ItemStack(Items.NETHER_STAR));
        GameRegistry.addShapedRecipe(new ItemStack(ModItems.artifactFrame,1) ,"gsg","gqg","ggg",'g',new ItemStack(Items.GOLD_NUGGET),'q',new ItemStack(Blocks.WOOL,1,EnumDyeColor.PURPLE.getMetadata()),'s', new ItemStack(Items.SIGN));
        GameRegistry.addShapelessRecipe(new ItemStack(ModBlocks.pondBase),new ItemStack(Blocks.SAND),new ItemStack(Items.CLAY_BALL));

        if(GATED_AQUEDUCTS)
        {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.WHITESTONE_BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(ModBlocks.whiteBrick));
        }
        else {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.STONE_BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(Blocks.STONEBRICK));
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(Blocks.BRICK_BLOCK));
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.QUARTZ.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(Blocks.QUARTZ_BLOCK));
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 3, BlockAqueduct.EnumType.WHITESTONE_BRICKS.getMetadata()), "ccc", "bbb", 'c', new ItemStack(Blocks.CLAY), 'b', new ItemStack(ModBlocks.whiteBrick));
        }
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.STONE_BRICKS.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(Blocks.STONEBRICK));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.BRICKS.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(Blocks.BRICK_BLOCK));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.QUARTZ.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(Blocks.QUARTZ_BLOCK));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.aqueduct, 1, BlockAqueduct.EnumType.WHITESTONE_BRICKS.getMetadata()), "a", "b", 'a', new ItemStack(ModBlocks.aqueduct,1,OreDictionary.WILDCARD_VALUE), 'b', new ItemStack(ModBlocks.whiteBrick));

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScaleOre,1,1) ,"aa ","aaa"," aa",'a',new ItemStack(ModItems.worldShard));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.worldScaleActive,1)," d ","iae"," d ",'a',new ItemStack(ModBlocks.worldScale),'i',new ItemStack(Items.IRON_PICKAXE),'e',new ItemStack(Items.IRON_AXE),'d',new ItemStack(Items.DIAMOND));

        GameRegistry.addSmelting(Items.CARROT,new ItemStack(ModItems.bakedCarrot),0.35f);
        GameRegistry.addSmelting(Items.BEETROOT,new ItemStack(ModItems.bakedBeetroot),0.35f);
        GameRegistry.addSmelting(Blocks.BROWN_MUSHROOM,new ItemStack(ModItems.bakedMushroom),0.35f);
        GameRegistry.addSmelting(Blocks.RED_MUSHROOM,new ItemStack(ModItems.bakedAmanita),0.35f);
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMelon),new ItemStack(Items.MELON),new ItemStack(Items.MELON),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMushroom),new ItemStack(Blocks.BROWN_MUSHROOM),new ItemStack(Blocks.BROWN_MUSHROOM),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieAmanita),new ItemStack(Blocks.RED_MUSHROOM),new ItemStack(Blocks.RED_MUSHROOM),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));
        GameRegistry.addShapelessRecipe(new ItemStack(ModItems.pieMeat),new ItemStack(ModItems.groundMeat),new ItemStack(ModItems.groundMeat),new ItemStack(Items.SUGAR),new ItemStack(Items.EGG));

        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.lattice,2)," a ","aaa"," a ",'a',new ItemStack(Blocks.IRON_BARS));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.elytraMagma,1),"aa","aa",'a',ModItems.material.getMaterial("ender_cream"));
        GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.bannerDetector,1),"aaa","o r","aaa",'a',new ItemStack(Blocks.COBBLESTONE),'o',new ItemStack(Items.ENDER_EYE),'r',new ItemStack(Items.REDSTONE));

        if(STONEBRICKS_NEED_SMELTING) {
            BetterWithAddons.removeCraftingRecipe(new ItemStack(Blocks.STONEBRICK, 4));
            GameRegistry.addShapedRecipe(new ItemStack(Blocks.STONEBRICK, 1), "aa", "aa", 'a', ModItems.material.getMaterial("stone_brick"));
            GameRegistry.addSmelting(Blocks.STONE, ModItems.material.getMaterial("stone_brick", 4), 0.1f);
        }
    }

    @Override
    public void postInit() {

    }
}
