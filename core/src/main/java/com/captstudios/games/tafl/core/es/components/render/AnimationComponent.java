package com.captstudios.games.tafl.core.es.components.render;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent implements Component {

    public Animation animation;
    public float stateTime;
    public String name;

    @Override
    public void reset() {
        name = null;
        stateTime = 0;
        animation = null;
    }

    public void incrementStateTime(float delta) {
        this.stateTime += delta;
    }

    public TextureRegion getFrame() {
        return animation.getKeyFrame(stateTime);
    }

    public boolean isFinished() {
        return animation.getPlayMode() != Animation.LOOP && animation.isAnimationFinished(stateTime);
    }

    @Override
    public String toString() {
        return "Animation[name=" + name + "]";
    }
}
