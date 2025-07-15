package miyucomics.hexodus.mixin;

import gravity_changer.GravityComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(GravityComponent.class)
public interface GravityComponentAccessor {
	@Accessor("currGravityStrength")
	public void setCurrGravityStrength(double strength);
}