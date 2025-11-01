package org.example;

import java.util.List;

public record PlayerRocket(MovableGameObject mgo) implements IBasicGameObject, Rocket {


    PlayerRocket(V2 pos){
        this(new MovableGameObject(pos,List.of("|", "^")));
    }


    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }

    @Override
    public HitBox hitBox() {
        return mgo.hitBox();
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }


    @Override
    public Rocket move() {
        return new PlayerRocket(mgo.move(new V2(0,-1)));
    }
}
