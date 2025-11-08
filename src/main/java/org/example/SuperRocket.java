package org.example;

import java.util.List;

public record SuperRocket(PlayerRocket playerRocket) implements IBasicGameObject, Rocket {


    SuperRocket(V2 pos){
        var playerRocket = new PlayerRocket(new MovableGameObject(pos,"/|\\\n|||\n|||\n|||\n|||"));
        this(playerRocket);
    }
    @Override
    public V2 pos() {
        return playerRocket.pos();
    }

    @Override
    public Rocket move() {
        return new SuperRocket((PlayerRocket) playerRocket.move());
    }

    @Override
    public boolean isPlayerRocket() {
        return true;
    }



    @Override
    public List<StringWithLocation> show() {
        return playerRocket.show();
    }

    @Override
    public List<V2> hitBox() {
        return playerRocket.hitBox();
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return Utils.isOnBoard(playerRocket.pos(), width, height);
    }
}
