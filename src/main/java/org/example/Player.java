package org.example;

import java.util.List;

public record Player(MovableGameObject mgo) implements Movable,IBasicGameObject,Shooting {

    Player(V2 pos){
        this(new MovableGameObject(pos, List.of("_/MM\\_", "qWAAWp")));
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
    public Movable move(V2 dir) {
        return mgo.move(dir);
    }

    @Override
    public MovableGameObject shoot() {
        return null;
    }
}

