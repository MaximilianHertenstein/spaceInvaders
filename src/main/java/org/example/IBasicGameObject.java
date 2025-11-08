package org.example;

import java.util.List;

public interface IBasicGameObject {
    V2 pos();
    List<StringWithLocation> show();
    List<V2> hitBox();
    boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height);
}
