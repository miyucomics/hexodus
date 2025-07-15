package miyucomics.hexodus

import at.petrak.hexcasting.api.casting.iota.DoubleIota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.utils.asTranslatedComponent
import gravity_changer.GravityComponent
import miyucomics.hexodus.mixin.GravityComponentAccessor
import net.minecraft.client.item.TooltipContext
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.sound.SoundCategory
import net.minecraft.sound.SoundEvents
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Hand
import net.minecraft.util.Rarity
import net.minecraft.util.TypedActionResult
import net.minecraft.util.math.Direction
import net.minecraft.util.math.Vec3d
import net.minecraft.world.World

object BallastItem : Item(Settings().maxCount(1).rarity(Rarity.UNCOMMON)) {
	override fun use(world: World, player: PlayerEntity, hand: Hand): TypedActionResult<ItemStack> {
		val stack = player.getStackInHand(hand)
		stack.orCreateNbt.putBoolean("active", !stack.orCreateNbt.getBoolean("active"))
		world.playSound(null, player.x, player.y, player.z, SoundEvents.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.PLAYERS, 1f, 1f)
		return TypedActionResult.success(stack)
	}

	override fun appendTooltip(stack: ItemStack, world: World?, list: MutableList<Text>, context: TooltipContext) {
		val direction = Vec3Iota.display(Vec3d.of(getDirection(stack).vector))
		val strength = DoubleIota.display(getStrength(stack))
		list.add("hexodus.ballast.tooltip".asTranslatedComponent(direction, strength).formatted(Formatting.GRAY))
	}

	fun alterGravity(stack: ItemStack, component: GravityComponent): Boolean {
		if (!stack.isOf(HexodusMain.BALLAST_ITEM))
			return false
		if (!stack.orCreateNbt.getBoolean("active"))
			return false
		component.applyGravityDirectionEffect(getDirection(stack), null, 100000.0)
		(component as GravityComponentAccessor).setCurrGravityStrength(getStrength(stack))
		return true
	}

	private fun getDirection(stack: ItemStack): Direction {
		if (!stack.orCreateNbt.contains("direction"))
			stack.orCreateNbt.putInt("direction", 0)
		return Direction.byId(stack.orCreateNbt.getInt("direction"))
	}

	private fun getStrength(stack: ItemStack): Double {
		if (!stack.orCreateNbt.contains("strength"))
			stack.orCreateNbt.putDouble("strength", 1.0)
		return stack.orCreateNbt.getDouble("strength")
	}
}