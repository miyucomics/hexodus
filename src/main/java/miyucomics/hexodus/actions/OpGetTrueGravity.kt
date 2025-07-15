package miyucomics.hexodus.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import gravity_changer.GravityChangerComponents
import gravity_changer.GravityComponent

class OpGetTrueGravity(private val operate: (GravityComponent) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val target = args.getEntity(0, argc)
		env.assertEntityInRange(target)
		return operate(GravityChangerComponents.GRAVITY_COMP_KEY.get(target))
	}
}