package com.pactstudios.games.tafl.core.es.components.utils;

import com.artemis.Component;

public interface VisitableComponent extends Component {

    public void accept(ComponentVisitor visitor);

}
