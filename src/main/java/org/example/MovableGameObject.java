package org.example;

import java.util.List;

public record MovableGameObject(BasicGameObject basicGameObject) implements IBasicGameObject{

    public MovableGameObject(V2 pos, String displayString) {
        this(new BasicGameObject(pos,displayString));
    }

    @Override
    public V2 pos() {
        return basicGameObject.pos();
    }

    public MovableGameObject move(V2 dir){
        return new MovableGameObject(pos().plus(dir), basicGameObject.displayString());
    }



    @Override
    public List<StringWithLocation> show() {
        return basicGameObject().show();
    }

    @Override
    public List<V2> hitBox() {
        return basicGameObject.hitBox();
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return basicGameObject.isAlive(gameObjects, width, height);
    }
}
