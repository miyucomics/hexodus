package miyucomics.hexodus

import at.petrak.hexcasting.api.misc.MediaConstants
import gravity_changer.GravityComponent
import net.fabricmc.api.ModInitializer
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.util.Identifier

class HexodusMain : ModInitializer {
	override fun onInitialize() {
		HexodusActions.init()
		GravityComponent.GRAVITY_UPDATE_EVENT.register { entity, component -> HexodusComponents.HEXODUS.get(entity).alterGravity(component) }
	}

	companion object {
		const val MOD_ID: String = "hexodus"
		fun id(string: String) = Identifier(MOD_ID, string)

		fun determineCost(target: Entity, caster: LivingEntity?): Long {
			if (target == caster)
				return MediaConstants.DUST_UNIT / 4
			if (target is PlayerEntity)
				return MediaConstants.SHARD_UNIT
			return MediaConstants.DUST_UNIT
		}
	}
}