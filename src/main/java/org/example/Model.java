package org.example;

import java.util.ArrayList;
import java.util.List;

public class Model {
    List<Alien> aliens;
    Player player;
    MovableGameObject playerBullet;
    List<MovableGameObject> alienBullets;
    List<BasicGameObject> blocks;
    int width;
    int height;
    V2 aliensDirection;
    int lives;



    public Model(List<Alien> aliens, Player player, MovableGameObject playerBullet, List<MovableGameObject> alienBullets, List<BasicGameObject> blocks) {
        this.aliens = aliens;
        this.player = player;
        this.playerBullet = playerBullet;
        this.alienBullets = alienBullets;
        this.blocks = blocks;
    }



    public Model() {
        this.aliens = Alien.createAliens();
        width = 40;
        height = 27;
        this.player = new Player(new V2(20,25));
        this.playerBullet = null;
        this.alienBullets = List.of();
        this.blocks = List.of();
        this.aliensDirection = new V2(1,0);
        this.lives = 3;
    }


    private List<IBasicGameObject> gameObjects(){
        var acc = new ArrayList<IBasicGameObject>();
        acc.addAll(blocks);
        acc.addAll(aliens);
        acc.add(player);
        acc.addAll(alienBullets);
        return acc;
    }




    public void move(char dir){
        if (playerBullet != null){
            playerBullet = playerBullet.move(new V2(0,-1));
        }
        alienBullets = Utils.move(alienBullets, new V2(0,1));
        aliens = Utils.move(aliens, aliensDirection);
        //player = (Player) player.move(Utils.charToV2(dir));
    }

    public List<StringWithLocation>  getUIState(){
        return Utils.getStringsWithLocation(gameObjects());
    }
//
//    public <T extends IBasicGameObject> List<T> removeDeadObjects(List<T> objectsToFilter ){
//
//
//         List<T> x = Utils.removeDeadObjects(width,height, blocks, gameObjects());
//        return (List<T>) x;
//    }


    public void removeDeadObjects(){
        blocks = Utils.removeDeadObjects(width,height,  blocks, gameObjects());
        aliens = Utils.removeDeadObjects(width,height,  aliens, gameObjects());
        alienBullets = Utils.removeDeadObjects(width,height,  alienBullets, gameObjects());
        if (playerBullet!= null &&  !playerBullet.isAlive(gameObjects(),width,height)){
            playerBullet = null;
        }
    }


    void update(char dir){
        removeDeadObjects();
        move(dir);
        aliensDirection = Utils.computeNextAlienDirection(aliens, width, aliensDirection);
    }



}
