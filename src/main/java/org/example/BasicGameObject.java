package org.example;

import java.util.ArrayList;
import java.util.List;

public record BasicGameObject(V2 pos, String displayString) implements IBasicGameObject {



    @Override
    public  List<StringWithLocation> show(){
        var lines = displayString.lines().toList();
        var acc = new ArrayList<StringWithLocation>();
        for (int i = 0; i < lines.size(); i++) {
            acc.add(new StringWithLocation(lines.get(i), pos.plus(new V2(0, i))));
        }
        return acc;
    }

    public  List<V2> hitBox() {
        var acc = new ArrayList<V2>();
        for (var stringWithLocation : show()) {
            for (int i = 0; i < stringWithLocation.string().length(); i++) {
                acc.add(stringWithLocation.location().plus(new V2(i, 0)));
            }
        }
        return acc;
    }




    boolean checkCollision(IBasicGameObject other){
        return Utils.intersect(hitBox(),other.hitBox());
    }

    int countCollisions(List<IBasicGameObject>   others){
        var count = 0;
        for (var other : others) {
            if (checkCollision(other)) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return Utils.isOnBoard(pos, width, height) && !(countCollisions(gameObjects)  > 1);
    }

}
