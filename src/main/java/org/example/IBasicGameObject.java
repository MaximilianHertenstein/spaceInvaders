package org.example;

import java.util.List;

public interface IBasicGameObject {
    List<StringWithLocation> show();
    HitBox hitBox();
    boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height);
}
