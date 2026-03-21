package org.example;

import java.util.List;

public record AlienRocket(MovableGameObject mgo) implements IBasicGameObject, Rocket {

    AlienRocket(V2 pos){
        this(new MovableGameObject(pos,"|\nˇ"));
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
    public boolean isPlayerRocket() {
        return false;
    }

    @Override
    public AlienRocket move() {
        return new AlienRocket(mgo.move(new V2(0,1)));
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }


}
