package com.pactstudios.games.tafl.core.es.components.singleton;

import com.artemis.Component;
import com.badlogic.gdx.graphics.Camera;

public interface RenderingComponent extends Component {
    public Camera getCamera();
}
