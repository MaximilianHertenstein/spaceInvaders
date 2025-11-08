package org.example;

import java.util.List;

public record PlayerRocket(MovableGameObject mgo) implements IBasicGameObject, Rocket {

    PlayerRocket(V2 pos){
        this(new MovableGameObject(pos,"|\n^"));
    }

    @Override
    public V2 pos() {
        return mgo.pos();
    }

    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }

    @Override
    public List<V2> hitBox() {
        return mgo.hitBox();
    }

    @Override
    public Rocket move() {
        return new PlayerRocket(mgo.move(new V2(0,-1)));
    }

    @Override
    public boolean isPlayerRocket() {
        return true;
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }
}
