package org.example;

import java.util.List;

public record MovableGameObject(BasicGameObject basicGameObject) implements Movable, IBasicGameObject{

    public MovableGameObject(V2 pos, List<String> displayStrings) {
        this(new BasicGameObject(pos,displayStrings));
    }

    @Override
    public MovableGameObject move(V2 dir){
        return new MovableGameObject(hitBox().pos().plus(dir), basicGameObject.displayStrings());
    }




    @Override
    public List<StringWithLocation> show() {
        return basicGameObject().show();
    }



    @Override
    public HitBox hitBox() {
        return basicGameObject.hitBox();
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return basicGameObject.isAlive(gameObjects, width, height);
    }
}
