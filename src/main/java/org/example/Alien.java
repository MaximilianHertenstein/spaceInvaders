package org.example;

import java.util.ArrayList;
import java.util.List;

public record Alien(MovableGameObject mgo, int score) implements IBasicGameObject {


    Alien(V2 pos, List<String> displayStrings, int  score){
        this(new MovableGameObject(pos, displayStrings), score);
    }

    public Alien move(V2 dir) {
        return new Alien(mgo.move(dir), score);
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



    public AlienRocket shoot() {
        return new AlienRocket(hitBox().pos().plus(new V2(0,2)));
    }


    static List<String> rowToAlienStrings(int i){
        return switch (i){
            case 2 -> List.of("{@@}","/\"\"\\") ;
            case 1 -> List.of("/MM\\","\\~~/");
            case 0 -> List.of("{OO}","/VV\\");
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
            res.add(new Alien(pos, rowToAlienStrings(row), 10 * (row + 1))  );

        }
        }return res;

    }





}
