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



//    public Model(List<Alien> aliens, Player player, MovableGameObject playerBullet, List<MovableGameObject> alienBullets, List<BasicGameObject> blocks) {
//        this.aliens = aliens;
//        this.player = player;
//        this.playerBullet = playerBullet;
//        this.alienBullets = alienBullets;
//        this.blocks = blocks;
//    }



    public Model(int width, int height) {
        this.width = width;
        this.height = height;
        this.aliens = Alien.createAliens();
        this.player = new Player(new V2(width/2,height -2));
        this.playerBullet = null;
        this.alienBullets = List.of();
        this.blocks = List.of();
        this.aliensDirection = new V2(1,0);
    }


    List<IBasicGameObject> gameObjects(){
        var acc = new ArrayList<IBasicGameObject>();
        acc.addAll(blocks);
        acc.addAll(aliens);
        acc.add(player);
        acc.addAll(alienBullets);
        if( playerBullet != null){
            acc.add(playerBullet);}
        return acc;
    }




    public void move(char dir){
        if (playerBullet != null){
            playerBullet = playerBullet.move(new V2(0,-1));
        }
        alienBullets = Utils.move(alienBullets, new V2(0,1));
        aliens = Utils.move(aliens, aliensDirection);
        player = (Player) player.move(Utils.charToV2(dir));
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
        var newBlocks = Utils.removeDeadObjects(width,height,  blocks, gameObjects());
        var newAliens = Utils.removeDeadObjects(width,height,  aliens, gameObjects());
        var newAlienBullets = Utils.removeDeadObjects(width,height,  alienBullets, gameObjects());
        if (playerBullet!= null &&  !playerBullet.isAlive(gameObjects(),width,height)){
            playerBullet = null;
        }
        blocks = newBlocks;
        aliens = newAliens;
        alienBullets = newAlienBullets;
    }


    void update(char dir){
        removeDeadObjects();
        move(dir);
        aliensDirection = Utils.computeNextAlienDirection(aliens, width, aliensDirection);
        if (dir == 'k' && playerBullet == null){
            playerBullet = player.shoot();
        }
    }



}
