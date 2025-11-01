package org.example;

import java.util.List;

public record Player(MovableGameObject mgo) implements IBasicGameObject {

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


    public Player move(V2 dir, int width) {

        if (dir.equals(new V2(1,0)) && hitBox().pos().x() == width + 1 - hitBox().width() || dir.equals(new V2(-1,0)) && hitBox().pos().x() == 0) { return this;}
        return new Player(mgo().move(dir));
    }

    public PlayerRocket shoot() {
        return new PlayerRocket(hitBox().pos().plus(new V2(0,-2)));

    }
}

