package org.example;

import java.util.ArrayList;
import java.util.List;

public record Alien(MovableGameObject mgo, int score, int id) implements Movable,IBasicGameObject {


    Alien(V2 pos, List<String> displayStrings, int  score, int id){
        this(new MovableGameObject(pos, displayStrings), score, id);
    }

    @Override
    public Alien move(V2 dir) {
        return new Alien(mgo.move(dir), score, id);
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



    public MovableGameObject shoot() {
        return new MovableGameObject(hitBox().pos().plus(new V2(0,1)), List.of("|", "ˇ"));
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
            int id =   (row*10+col);

            res.add(new Alien(pos, rowToAlienStrings(row), 10 * (row + 1), id)  );

        }
        }return res;

    }





}
