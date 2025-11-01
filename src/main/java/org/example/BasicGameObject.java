package org.example;

import java.util.ArrayList;
import java.util.List;

public record BasicGameObject(V2 pos, List<String> displayStrings) implements IBasicGameObject {



    boolean checkCollision(IBasicGameObject other){
        return hitBox().intersects(other.hitBox());
    }

    boolean checkCollision(List<IBasicGameObject>   others){
        var count = 0;
        for (var other : others) {
            if (checkCollision(other)) {
                count++;
            }
        }
        return count > 1;
    }





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

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return isOnBoard(width, height) && !checkCollision(gameObjects);
    }

    private boolean isOnBoard(int width, int height) {
        return pos.x() >= 0 && pos.x() < width && pos.y() >= 0 && pos.y() < height;
    }

}
