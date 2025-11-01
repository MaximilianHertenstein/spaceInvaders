package org.example;

import java.util.ArrayList;
import java.util.List;

public record BasicGameObject(V2 pos, List<String> displayStrings) implements IBasicGameObject {



    @Override
    public  List<StringWithLocation> show(){
        var acc = new ArrayList<StringWithLocation>();
        for (int i = 0; i < displayStrings.size(); i++) {
            acc.add(new StringWithLocation(displayStrings.get(i), pos.plus(new V2(0, i))));
        }
        return acc;
    }

    @Override
    public HitBox hitBox() {
        return new HitBox(pos, displayStrings.getFirst().length(), displayStrings.size());
    }




    boolean checkCollision(IBasicGameObject other){
        return hitBox().intersects(other.hitBox());
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
