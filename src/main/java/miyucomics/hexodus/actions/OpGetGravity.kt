package miyucomics.hexodus.actions

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import miyucomics.hexodus.HexodusComponent
import miyucomics.hexodus.HexodusComponents

class OpGetGravity(private val operate: (HexodusComponent) -> List<Iota>) : ConstMediaAction {
	override val argc = 1
	override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
		val target = args.getEntity(0, argc)
		env.assertEntityInRange(target)
		HexodusComponents.HEXODUS.get(target)
		return operate(HexodusComponents.HEXODUS.get(target))
	}
}