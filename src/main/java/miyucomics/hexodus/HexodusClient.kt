package miyucomics.hexodus

import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.item.ModelPredicateProviderRegistry
import net.minecraft.util.Identifier

class HexodusClient : ClientModInitializer {
	override fun onInitializeClient() {
		ModelPredicateProviderRegistry.register(HexodusMain.BALLAST_ITEM, Identifier("active")) { stack, _, _, _ ->
			if (stack.nbt?.getBoolean("active") == true) 1.0f else 0.0f
		}
	}
}