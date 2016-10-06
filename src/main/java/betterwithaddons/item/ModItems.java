package betterwithaddons.item;

import betterwithaddons.BetterWithAddons;
import betterwithaddons.lib.Reference;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.ArrayList;

public class ModItems
{
    public static ArrayList<Item> LIST = new ArrayList<Item>();

    public static Item.ToolMaterial bambooToolMaterial = EnumHelper.addToolMaterial("bamboo", 0, 51, 0.5f, -4.0f, 1);
    {
        bambooToolMaterial.setRepairItem(japanMaterial.getMaterial("bamboo_slats"));
    }
    public static Item.ToolMaterial tamahaganeToolMaterial = EnumHelper.addToolMaterial("tamahagane", 3, 1210, 8.0f, 2.5f, 9);
    {
        tamahaganeToolMaterial.setRepairItem(japanMaterial.getMaterial("tamahagane_finished"));
    }
    public static Item.ToolMaterial japansteelToolMaterial = EnumHelper.addToolMaterial("japansteel", 3, 1710, 8.0f, 4.0f, 10);
    {
        japansteelToolMaterial.setRepairItem(japanMaterial.getMaterial("tamahagane_finished"));
    }

    public static ItemGreatbow greatbow;
    public static ItemGreatarrow greatarrow;
    public static ItemMonument monument;
    public static ItemFood bakedMushroom;
    public static ItemFood bakedAmanita;
    public static ItemFood cookedBeetroot;
    public static ItemFood bakedBeetroot;
    public static ItemFood cookedCarrot;
    public static ItemFood bakedCarrot;
    public static ItemFood cookedPotato;
    public static ItemFood pieMelon;
    public static ItemFood pieMeat;
    public static ItemFood pieMushroom;
    public static ItemFood pieAmanita;
    public static ItemFood chocolate;
    public static ItemFood meatballs;
    public static ItemFood cookedEgg;
    public static ItemFood groundMeat;
    public static ItemFood sashimi;
    public static ItemStainedBrick stainedBrick;
    public static ItemMaterial material;
    public static ItemMaterial japanMaterial;
    public static ItemKatana katana;
    public static ItemWakizashi wakizashi;
    public static ItemTanto tanto;
    public static ItemShinai shinai;
    public static ItemYumi yumi;
    public static ItemYa ya;

    public static void load(FMLPreInitializationEvent event)
    {
        /*arrowhead = registerItem("arrowhead",new Item());
        midori = registerItem("midori",new Item());
        midori_popped = registerItem("midori_popped",new Item());
        stone_brick = registerItem("stone_brick",new Item());
        bone_ingot = registerItem("bone_ingot",new Item());
        ender_cream = registerItem("ender_cream",new Item());
        thornrose = registerItem("thornrose",new Item());*/

        material = (ItemMaterial)registerItem("material",new ItemMaterial(
                new String[]{"arrowhead","midori","midori_popped","thornrose","stone_brick","bone_ingot","ender_cream"}
        ));

        greatbow = (ItemGreatbow)registerItem("greatbow",new ItemGreatbow());
        greatarrow = (ItemGreatarrow)registerItem("greatarrow",new ItemGreatarrow());
        monument = (ItemMonument)registerItem("monument",new ItemMonument());
        //Food
        bakedMushroom = (ItemFood)registerItem("food_mushroom_baked",new ItemFood(3, 3.6F, false));
        bakedAmanita = (ItemFood)registerItem("food_amanita_baked",new ItemFood(3, 3.6F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1), 1.0F));
        cookedBeetroot = (ItemFood)registerItem("food_beetroot_cooked",new ItemFood(4, 1.2F, false));
        bakedBeetroot = (ItemFood)registerItem("food_beetroot_baked",new ItemFood(5, 3.6F, false));
        cookedCarrot = (ItemFood)registerItem("food_carrot_cooked",new ItemFood(3, 4.5F, false));
        bakedCarrot = (ItemFood)registerItem("food_carrot_baked",new ItemFood(4, 5.0F, false));
        cookedPotato = (ItemFood)registerItem("food_potato_cooked",new ItemFood(4, 5.0F, false));
        cookedEgg = (ItemFood)registerItem("food_egg_cooked",new ItemFood(4, 4.0F, false));
        chocolate = (ItemFood)registerItem("food_chocolate",new ItemFood(5, 2.4F, false));
        meatballs = (ItemFood)registerItem("food_meatballs",new ItemFood(6, 9.6F, true));

        groundMeat = (ItemFood)registerItem("food_ground_meat",new ItemFood(2, 0.8F, true));

        pieMushroom = (ItemFood)registerItem("food_pie_mushroom",new ItemFood(8, 4.8F, false));
        pieAmanita = (ItemFood)registerItem("food_pie_amanita",new ItemFood(8, 4.8F, false).setPotionEffect(new PotionEffect(MobEffects.POISON, 100, 1), 1.0F));
        pieMeat = (ItemFood)registerItem("food_pie_meat",new ItemFood(9, 5F, true));
        pieMelon = (ItemFood)registerItem("food_pie_melon",new ItemFood(8, 4.8F, false));

        sashimi = (ItemFood)registerItem("food_sashimi",new ItemFood(2, 2.4F, false));

        stainedBrick = (ItemStainedBrick)registerItem("brick_stained",new ItemStainedBrick());

        shinai = (ItemShinai) registerItem("shinai",new ItemShinai());
        katana = (ItemKatana) registerItem("katana",new ItemKatana());
        wakizashi = (ItemWakizashi) registerItem("wakizashi",new ItemWakizashi());
        tanto = (ItemTanto) registerItem("tanto",new ItemTanto());
        yumi = (ItemYumi) registerItem("yumi",new ItemYumi());
        ya = (ItemYa) registerItem("ya",new ItemYa());

        japanMaterial = (ItemMaterial)registerItem("japanmat",new ItemMaterial(
                new String[]{"rice","soaked_rice","rice_stalk","rice_hay","rice_ash","rush",
                        "soaked_bamboo","bamboo_slats","soaked_mulberry","mulberry_paste","mulberry_sheet","washi",
                        "iron_scales","lamellar","paper_lamellar","tsuka","half_katana_blade","ya_head","yumi_top","yumi_bottom",
                        "tamahagane","tamahagane_heated", "tamahagane_folded","tamahagane_reheated","tamahagane_finished","tamahagane_wrapped",
                        "hocho_tetsu","hocho_tetsu_heated","hocho_tetsu_fold_1","hocho_tetsu_fold_2","hocho_tetsu_finished"}
        ));
    }

    private static Item registerItem(String name,Item item)
    {
        item.setUnlocalizedName(name);
        item.setCreativeTab(BetterWithAddons.instance.creativeTab);
        GameRegistry.register(item, new ResourceLocation(Reference.MOD_ID,name));
        LIST.add(item);

        return item;
    }
}