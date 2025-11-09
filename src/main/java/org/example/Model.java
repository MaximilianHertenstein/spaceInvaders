package org.example;

import java.util.ArrayList;
import java.util.List;

public class Model {
    List<Alien> aliens;
    Player player;
    List<Rocket> rockets;
    List<BasicGameObject> blocks;
    final int width;
    final int height;
    V2 aliensDirection;
    private boolean isAlive;
    final CountDown alienRocketCountdown;





    public Model(int width, int height) {
        this.width = width;
        this.height = height;
        this.aliens = Alien.createAliens();
        this.player = new Player(new V2(width/2,height -2));
        //this.playerBullet = null;
        this.rockets = new ArrayList<>();
        this.blocks = Utils.generateBlocks(new V2(1, 3 * height/4),4,3, width/8);
        this.aliensDirection = new V2(1,0);
        this.isAlive = true;
        this.alienRocketCountdown = new CountDown(5);
    }


    boolean gameWon(){
        return aliens.isEmpty();
    }

    boolean gameLost(){
        return  Utils.aliensAreInLastLine(aliens, height) || !isAlive;
    }


    public boolean gameOngoing() {
        return !gameWon() && !gameLost();
    }


    public String getEndMessage(){
        if (gameLost()){
            return "You lost";
        }
        if (gameWon()){
            return "You won";
        }
        return "Error!";
    }


    List<IBasicGameObject> gameObjects(){
        var acc = new ArrayList<IBasicGameObject>();
        acc.addAll(blocks);
        acc.addAll(aliens);
        acc.addAll(rockets);
        acc.add(player);
        return acc;
    }


    public void move(char dir){
        rockets = Utils.move(rockets);
        aliens = Utils.move(aliens, aliensDirection);
        player = player.move(Utils.charToV2(dir),width);
    }

    public List<StringWithLocation>  getUIState(){
        return Utils.getStringsWithLocation(gameObjects());
    }


    public void removeDeadObjects(){
        var newBlocks = Utils.removeDeadObjects(blocks, gameObjects(), width,height);
        var newAliens = Utils.removeDeadObjects(aliens, gameObjects(), width,height);
        var newRockets = Utils.removeDeadObjects(rockets, gameObjects(), width,height);
        isAlive = player.isAlive(gameObjects(),width,height);
        blocks = newBlocks;
        aliens = newAliens;
        rockets = newRockets;
    }

    void update(char key){
        alienRocketCountdown.countDown();
        move(key);
        removeDeadObjects();
        aliensDirection = Utils.computeNextAlienDirection(aliens, aliensDirection, width);
        rockets.addAll(Utils.getNewRockets(key, aliens, rockets, player, alienRocketCountdown.finished()));
    }


}
