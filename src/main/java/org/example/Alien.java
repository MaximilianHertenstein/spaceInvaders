package org.example;

import java.util.List;

public record Alien(MovableGameObject mgo) implements IBasicGameObject, Shooting {


    Alien(V2 pos, String displayString){
        this(new MovableGameObject(pos, displayString));
    }

    public Alien move(V2 dir) {
        return new Alien(mgo.move(dir));
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
    public V2 pos() {
        return mgo.pos();
    }
    @Override
    public AlienRocket shoot() {
        return new AlienRocket(pos().plus(new V2(0,2)));
    }




    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }


    boolean touchesLeftBorder() {
        return mgo.touchesLeftBorder();
    }

    boolean touchesRightBorder(int width) {
        return mgo.touchesRightBorder(width);
    }

    /**
     * Returns true if any part of this alien's hitbox reaches the last playable line.
     *
     * We define the last playable line as y == height - vertical sie of the alien.
     */

    boolean isInLastLine(int height) {
        int lastPossibleLine = height - 1;
        return Utils.getYCoordinates(hitBox()).contains(lastPossibleLine);
    }


}
