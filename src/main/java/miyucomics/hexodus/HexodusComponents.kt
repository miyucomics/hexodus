package miyucomics.hexodus

import dev.onyxstudios.cca.api.v3.component.ComponentKey
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry
import dev.onyxstudios.cca.api.v3.component.ComponentV3
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer
import gravity_changer.GravityComponent
import net.minecraft.entity.Entity
import net.minecraft.nbt.NbtCompound
import net.minecraft.util.math.Direction

class HexodusComponents : EntityComponentInitializer {
	override fun registerEntityComponentFactories(registry: EntityComponentFactoryRegistry) {
		registry.registerFor(Entity::class.java, HEXODUS) { HexodusComponent(Direction.DOWN, 1.0, 0) }
	}

	companion object {
		val HEXODUS: ComponentKey<HexodusComponent> = ComponentRegistry.getOrCreate(HexodusMain.id("hexodus"), HexodusComponent::class.java)
	}
}

class HexodusComponent(var direction: Direction, var strength: Double, var duration: Int) : ComponentV3, AutoSyncedComponent, CommonTickingComponent {
	fun setAlteredGravity(entity: Entity, newDirection: Direction, newStrength: Double, newDuration: Int) {
		direction = newDirection
		duration = newDuration
		strength = newStrength
		HexodusComponents.HEXODUS.sync(entity)
	}

	fun alterGravity(component: GravityComponent) {
		if (duration > 0) {
			component.applyGravityDirectionEffect(direction, null, 200.0)
			component.applyGravityStrengthEffect(strength)
		}
	}

	override fun writeToNbt(compound: NbtCompound) {
		compound.putInt("x", direction.offsetX)
		compound.putInt("y", direction.offsetY)
		compound.putInt("z", direction.offsetZ)
		compound.putDouble("strength", strength)
		compound.putInt("duration", duration)
	}

	override fun readFromNbt(compound: NbtCompound) {
		direction = Direction.fromVector(compound.getInt("x"), compound.getInt("y"), compound.getInt("z")) ?: Direction.DOWN
		strength = compound.getDouble("strength")
		duration = compound.getInt("duration")
	}

	override fun tick() {
		if (duration > 0) {
			duration -= 1
			if (duration == 0) {
				direction = Direction.DOWN
				strength = 1.0
			}
		}
	}
}