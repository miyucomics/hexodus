package miyucomics.hexodus.actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.env.PlayerBasedCastEnv
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadCaster
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexodus.HexodusMain
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.util.math.Direction

object OpAlterBallast : SpellAction {
	override val argc = 2
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		if (env !is PlayerBasedCastEnv)
			throw MishapBadCaster()

		val offset = args.getBlockPos(0, argc)
		val direction = Direction.fromVector(offset.x, offset.y, offset.z) ?: throw MishapInvalidIota.of(args[0], 1, "axis_vector")
		val strength = args.getPositiveDoubleUnderInclusive(1, 5.0, argc)
		return SpellAction.Result(Spell(direction, strength), 0, listOf(ParticleSpray.burst(env.castingEntity!!.pos, 10.0)))
	}

	private data class Spell(val direction: Direction, val strength: Double) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			val inventory = (env.castingEntity as ServerPlayerEntity).inventory
			inventory.offHand.toMutableList().also {
				it.addAll(inventory.main)
				it.addAll(inventory.armor)
			}.forEach {
				if (it.isOf(HexodusMain.BALLAST_ITEM)) {
					it.orCreateNbt.putInt("direction", direction.id)
					it.orCreateNbt.putDouble("strength", strength)
					return
				}
			}
		}
	}
}