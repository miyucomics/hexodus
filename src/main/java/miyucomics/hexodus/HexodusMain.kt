package miyucomics.hexodus

import gravity_changer.GravityComponent
import net.fabricmc.api.ModInitializer
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier

class HexodusMain : ModInitializer {
	override fun onInitialize() {
		HexodusActions.init()
		Registry.register(Registries.ITEM, id("ballast"), BALLAST_ITEM)

		GravityComponent.GRAVITY_UPDATE_EVENT.register { entity, component ->
			HexodusComponents.HEXODUS.get(entity).alterGravity(component)

			if (entity.isPlayer) {
				val inventory = (entity as PlayerEntity).inventory
				inventory.offHand.toMutableList().also {
					it.addAll(inventory.main)
					it.addAll(inventory.armor)
				}.forEach {
					if (BallastItem.alterGravity(it, component))
						return@register
				}
			}
		}
	}

	companion object {
		const val MOD_ID: String = "hexodus"
		fun id(string: String) = Identifier(MOD_ID, string)

		@JvmField
		val BALLAST_ITEM = BallastItem
	}
}