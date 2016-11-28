package betterwithaddons.handler;

import betterwithaddons.block.BetterRedstone.BlockPCB;
import betterwithaddons.block.BlockLattice;
import betterwithaddons.block.ModBlocks;
import betterwithaddons.item.ModItems;
import betterwithaddons.potion.ModPotions;
import betterwithaddons.util.BannerUtil;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBanner;
import net.minecraft.block.BlockRedstoneWire;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BossInfo.Color;
import net.minecraft.world.BossInfo.Overlay;
import net.minecraft.world.BossInfoServer;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class AssortedHandler
{
	static Random rng = new Random();

	private HashMap<UUID,BossInfoServer> BossList = new HashMap<UUID,BossInfoServer>();
	private final int BossCleanupThreshold = 10;
	private final float HardnessThreshold = 5.0f;

	@SubscribeEvent
	public void onEntityDrop(LivingDropsEvent event) {
		EntityLivingBase living = event.getEntityLiving();
		if(living == null || living.worldObj == null) return;
		Random rand = living.worldObj.rand;

		if (living instanceof EntityShulker){
			if (rand.nextFloat() < 1.0f) {
				event.getEntityLiving().entityDropItem(ModItems.material.getMaterial("ender_cream",1+rand.nextInt(2)), 0);
			}
		}
	}

	@SubscribeEvent
	public void blockNeighborUpdate(BlockEvent.NeighborNotifyEvent notifyEvent)
	{
		World world = notifyEvent.getWorld();
		makeRedstonePCB(world,notifyEvent.getPos());
	}

	private void makeRedstonePCB(World world, BlockPos pos)
	{
		IBlockState blockstate = world.getBlockState(pos);
		Block block = blockstate.getBlock();
		Block bottomblock = world.getBlockState(pos.down()).getBlock();
		if (!world.isRemote && block instanceof BlockRedstoneWire && bottomblock instanceof BlockPCB) {
			world.setBlockState(pos, ModBlocks.pcbwire.getDefaultState());
		}
	}

	@SubscribeEvent
	public void beginTrack(PlayerEvent.StartTracking trackEvent)
	{
		if(!trackEvent.getEntityPlayer().getEntityWorld().isRemote) {
			Entity target = trackEvent.getTarget();
			EntityPlayerMP player = (EntityPlayerMP) trackEvent.getEntityPlayer();

			UUID uuid = target.getUniqueID();

			if(BossList.containsKey(uuid))
			{
				BossInfoServer bossInfo = BossList.get(uuid);
				bossInfo.addPlayer(player);
			}
		}
	}

	@SubscribeEvent
	public void endTrack(PlayerEvent.StopTracking trackEvent)
	{
		if(!trackEvent.getEntityPlayer().getEntityWorld().isRemote) {
			Entity target = trackEvent.getTarget();
			EntityPlayerMP player = (EntityPlayerMP) trackEvent.getEntityPlayer();

			UUID uuid = target.getUniqueID();

			if(BossList.containsKey(uuid))
			{
				BossInfoServer bossInfo = BossList.get(uuid);
				bossInfo.removePlayer(player);
			}
		}
	}

	@SubscribeEvent
	public void worldTick(TickEvent.WorldTickEvent tickEvent)
	{
		World world = tickEvent.world;
		WorldScaleData.getInstance(world).cleanup();
	}

	@SubscribeEvent
	public void breakBlock(PlayerEvent.BreakSpeed breakEvent)
	{
		World world = breakEvent.getEntity().getEntityWorld();
		Entity entity = breakEvent.getEntity();
		WorldScaleData scaledata = WorldScaleData.getInstance(world);
		BlockPos worldscale = scaledata.getNearbyScale(breakEvent.getPos());
		ItemStack banner;
		BlockPos breakpos = breakEvent.getPos();
		IBlockState blockstate = world.getBlockState(breakpos);
		/*if(entity instanceof EntityPlayer) {
			ItemStack stack = breakEvent.getEntityPlayer().getHeldItemMainhand();
			if (toolShouldntBreak(stack) && breakEvent.getState().getBlockHardness(world,breakEvent.getPos()) > 0.0f) {
				breakEvent.setNewSpeed(0f);
				return;
			}
		}*/
		float hardnessmod = 1.0f;

		if(!(blockstate.getBlock() instanceof BlockLattice) && isNextToHardener(world,breakpos))
		{
			hardnessmod = 2.0f;
		}

		float hardness = blockstate.getBlockHardness(world,breakEvent.getPos()) * hardnessmod;
		float newspeed = breakEvent.getOriginalSpeed() * (1.0f / hardnessmod);

		if(worldscale != null && (banner = BannerUtil.getBannerItemFromBlock(world,worldscale.up())) != null)
		{
			if(!BannerUtil.isSameBanner(banner,entity))
			{
				newspeed = breakEvent.getOriginalSpeed() * (Math.max(0f,HardnessThreshold - hardness) / HardnessThreshold) * 0.3333f * (1.0f / hardnessmod);
			}
		}

		breakEvent.setNewSpeed(newspeed);
	}

	private boolean isNextToHardener(World world, BlockPos pos)
	{
		for(EnumFacing facing : EnumFacing.VALUES)
		{
			BlockPos nextpos = pos.offset(facing);
			IBlockState state = world.getBlockState(nextpos);
			if(state.getBlock() instanceof BlockLattice)
				return true;
		}

		return false;
	}

	@SubscribeEvent
	public void livingTick(LivingEvent.LivingUpdateEvent updateEvent) {
		final EntityLivingBase entity = updateEvent.getEntityLiving();
		World world = entity.getEntityWorld();
		UUID uuid = entity.getUniqueID();
		BlockPos pos = entity.getPosition();

		if (!world.isRemote) {
			if (entity.isPotionActive(ModPotions.boss)) {
				if (!BossList.containsKey(uuid)) {
					BossInfoServer displayData = (BossInfoServer) new BossInfoServer(entity.getDisplayName(), Color.PURPLE, Overlay.PROGRESS).setDarkenSky(false);
					BossList.put(uuid, displayData);
					List<EntityPlayerMP> entities = world.getEntitiesWithinAABB(EntityPlayerMP.class, new AxisAlignedBB(pos).expand(24, 24, 24));
					if (entities != null)
						for (EntityPlayerMP ply : entities) {
							displayData.addPlayer(ply);
						}
				} else {
					BossInfoServer bossInfo = BossList.get(uuid);
					bossInfo.setPercent(entity.getHealth() / entity.getMaxHealth());
				}

			} else if (world.getMinecraftServer().getTickCounter() % BossCleanupThreshold == 0 && BossList.containsKey(uuid)) {
				BossInfoServer bossInfo = BossList.get(uuid);
				for (EntityPlayerMP ply : bossInfo.getPlayers()) {
					bossInfo.removePlayer(ply);
				}
				BossList.remove(uuid);
			}
		}
	}

	@SubscribeEvent
	public void livingAttacked(LivingAttackEvent event)
	{
		if (!event.getEntityLiving().worldObj.isRemote)
		{
			if (!event.isCanceled() && event.getAmount() > 0)
			{
				EntityLivingBase living = event.getEntityLiving();

				if (living.isPotionActive(ModPotions.cannonball) && (event.getSource().isExplosion() || event.getSource() == DamageSource.fall))
				{
					if(event.getSource() == DamageSource.fall) //No you don't get to have superbuffs that make you immune to creepers and falldamage.
						living.removePotionEffect(ModPotions.cannonball);
					event.setCanceled(true);
				}
			}
		}
	}



	@SubscribeEvent
	public void onBlockInteract(PlayerInteractEvent event) {

		World world = event.getWorld();
		BlockPos pos = event.getPos();
		IBlockState blockstate = world.getBlockState(pos);
		EntityPlayer player = event.getEntityPlayer();

		if(blockstate.getBlock() instanceof BlockBanner)
		{
			EnumHand hand = EnumHand.MAIN_HAND;
			ItemStack stack = player.getHeldItemMainhand();
			BlockBanner bannerblock = (BlockBanner)blockstate.getBlock();

			if(stack == null && player.isSneaking() && player.getItemStackFromSlot(EntityEquipmentSlot.HEAD) == null) {
				ItemStack bannerstack = bannerblock.getItem(world,pos,blockstate);
				player.swingArm(hand);

				world.removeTileEntity(pos);
				world.setBlockToAir(pos);

				player.setItemStackToSlot(EntityEquipmentSlot.HEAD, bannerstack);

				if(!event.getWorld().isRemote) {
					event.setCanceled(true);
				}
			}
		}
	}
}