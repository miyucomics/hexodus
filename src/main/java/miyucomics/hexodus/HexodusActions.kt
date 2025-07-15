package miyucomics.hexodus

import at.petrak.hexcasting.api.casting.ActionRegistryEntry
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.Action
import at.petrak.hexcasting.api.casting.math.HexDir
import at.petrak.hexcasting.api.casting.math.HexPattern
import at.petrak.hexcasting.common.lib.hex.HexActions
import miyucomics.hexodus.actions.OpAlterBallast
import miyucomics.hexodus.actions.OpChangeGravity
import miyucomics.hexodus.actions.OpGetGravity
import miyucomics.hexodus.actions.OpGetTrueGravity
import net.minecraft.registry.Registry

object HexodusActions {
	@JvmStatic
	fun init() {
		register("alter_ballast", "wwawwawwqaqwedwwwq", HexDir.NORTH_EAST, OpAlterBallast)
		register("change_gravity", "wawawqaqwa", HexDir.NORTH_EAST, OpChangeGravity)
		register("get_duration", "wdwdwedewdwaaw", HexDir.NORTH_WEST, OpGetGravity { it.duration.asActionResult })
		register("get_direction", "wdwdwedewqq", HexDir.NORTH_WEST, OpGetGravity { if (it.duration > 0) it.direction.unitVector.asActionResult else null.asActionResult })
		register("get_strength", "wdwdwedewdw", HexDir.NORTH_WEST, OpGetGravity { if (it.duration > 0) it.strength.asActionResult else null.asActionResult })
		register("get_true_direction", "weeeewedewqq", HexDir.NORTH_WEST, OpGetTrueGravity { it.currGravityDirection.unitVector.asActionResult })
		register("get_true_strength", "weeeewedewdw", HexDir.NORTH_WEST, OpGetTrueGravity { it.currGravityStrength.asActionResult })
	}

	private fun register(name: String, signature: String, startDir: HexDir, action: Action) =
		Registry.register(
			HexActions.REGISTRY, HexodusMain.id(name),
			ActionRegistryEntry(HexPattern.Companion.fromAngles(signature, startDir), action)
		)
}