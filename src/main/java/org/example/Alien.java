package org.example;

import java.util.ArrayList;
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

    public AlienRocket shoot() {
        return new AlienRocket(pos().plus(new V2(0,2)));
    }


    static String rowToAlienStrings(int i){
        return switch (i){
            case 2 -> "{@@}\n/\"\"\\" ;
            case 1 -> "/MM\\\n\\~~/";
            case 0 -> "{OO}\n/VV\\";
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };

    }

    public static  List<Alien>createAliens (){
        var res = new ArrayList<Alien>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 10; col++) {
                var x = 36 - col* 4;
                var y = 8 - row * 4;
                var pos = new V2(x, y);
                res.add(new Alien(pos, rowToAlienStrings(row)));

            }
        }return res;
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
        int lastPossibleLine = height - show().size();
        return Utils.getYCoordinates(hitBox()).contains(lastPossibleLine);
    }


}
