package org.example;

import java.util.ArrayList;
import java.util.List;

public record BasicGameObject(V2 pos, String displayString) implements IBasicGameObject {



    @Override
    public  List<StringWithLocation> show(){
        var lines = displayString.lines().toList();
        var acc = new ArrayList<StringWithLocation>();
        for (int rowIndex = 0; rowIndex < lines.size(); rowIndex++) {
            acc.add(new StringWithLocation(lines.get(rowIndex), pos.plus(new V2(0, rowIndex))));
        }
        return acc;
    }

    @Override
    public  List<V2> hitBox() {
        var acc = new ArrayList<V2>();
        for (var stringWithLocation : show()) {
            for (int colIndex = 0; colIndex < stringWithLocation.string().length(); colIndex++) {
                acc.add(stringWithLocation.location().plus(new V2(colIndex, 0)));
            }
        }
        return acc;
    }




    boolean checkCollision(IBasicGameObject other){
        return Utils.intersect(hitBox(),other.hitBox());
    }

    // returns true if the object collides with another object in the list.
    // We expect "all" to contain this object itself, so we check "collisionCount > 1".
    boolean checkCollision(List<IBasicGameObject>  all){
        var collisionCount = 0;
        for (var other : all) {
            if (checkCollision(other)) {
                collisionCount++;
            }
        }
        return collisionCount > 1;
    }


    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return Utils.isOnBoard(hitBox(), width, height) && !(checkCollision(gameObjects));
    }

}
