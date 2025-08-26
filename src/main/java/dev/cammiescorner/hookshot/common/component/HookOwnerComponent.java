package dev.cammiescorner.hookshot.common.component;

import dev.onyxstudios.cca.api.v3.component.Component;
import net.minecraft.world.entity.Entity;

public interface HookOwnerComponent extends Component {

    boolean hasHook();

    void setHasHook(boolean hasHook);

    void setTempNoGravity(boolean floating);

    Entity getEntity();
}
