package org.example;

import java.util.List;

public record AlienRocket(MovableGameObject mgo) implements IBasicGameObject, SelfMoving, Movable {

    AlienRocket(V2 pos){
        this(new MovableGameObject(pos,List.of("|", "ˇ")));
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
        return new AlienRocket(mgo.move(dir));
    }

    @Override
    public SelfMoving move() {
        return new AlienRocket(mgo.move(new V2(0,1)));
    }
}
