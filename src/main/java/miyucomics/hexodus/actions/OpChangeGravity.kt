package miyucomics.hexodus.actions

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getPositiveDoubleUnderInclusive
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import miyucomics.hexodus.HexodusComponents
import miyucomics.hexodus.HexodusMain.Companion.determineCost
import net.minecraft.entity.Entity
import net.minecraft.util.math.Direction

object OpChangeGravity : SpellAction {
	override val argc = 3
	override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
		val target = args.getEntity(0, argc)
		env.assertEntityInRange(target)
		val offset = args.getBlockPos(1, argc)
		val direction = Direction.fromVector(offset.x, offset.y, offset.z) ?: throw MishapInvalidIota.of(args[1], 1, "axis_vector")
		val strength = args.getPositiveDoubleUnderInclusive(2, 5.0, argc)
		return SpellAction.Result(Spell(target, direction, strength), determineCost(target, env.castingEntity), listOf(ParticleSpray.burst(target.eyePos, 10.0)))
	}

	private data class Spell(val target: Entity, val direction: Direction, val strength: Double) : RenderedSpell {
		override fun cast(env: CastingEnvironment) {
			HexodusComponents.HEXODUS.get(target).setAlteredGravity(target, direction, strength, 50)
		}
	}
}