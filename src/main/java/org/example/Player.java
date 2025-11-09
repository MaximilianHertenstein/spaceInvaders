package org.example;

import java.util.List;

import static java.lang.IO.println;

public record Player(MovableGameObject mgo) implements IBasicGameObject, Shooting {

    Player(V2 pos){
        this(new MovableGameObject(pos, "_/MM\\_\nqWAAWp"));
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

    public Player move(V2 dir, int width) {

        if (dir.equals(new V2(1,0)) && pos().x() == width -4|| dir.equals(new V2(-1,0)) && pos().x() == 0) { return this;}
        return new Player(mgo().move(dir));
    }

    public PlayerRocket shoot() {
        return new PlayerRocket(pos().plus(new V2(0,-2)));
    }



    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }

}

