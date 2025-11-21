package org.example;

import java.util.ArrayList;
import java.util.List;

public class Model {
    private final int width;
    private final int height;
    private Player player;
    private AlienSwarm alienSwarm;
    private List<BasicGameObject> blocks;
    private List<Rocket> rockets;


    public Model(int width, int height) {
        this.width = width;
        this.height = height;
        this.player = new Player(new V2(width/2,height -2));
        this.alienSwarm = new AlienSwarm();
        this.blocks = Utils.generateBlocks(new V2(1, 3 * height/4),4,3, width/8);
        this.rockets = new ArrayList<>();

    }

    boolean gameWon(){
        return alienSwarm.noAliensLeft();
    }

    boolean gameLost(){
        return  alienSwarm.aliensAreInLastLine(height) || !playerIsAlive();
    }

    private boolean playerIsAlive() {
        return player.isAlive(gameObjects(),width,height);
    }

    public boolean gameOngoing() {
        return !gameWon() && !gameLost();
    }

    public String getEndMessage(){
        if (gameWon()){
            return "You won!";
        }
        return "You lost!";
    }


    private List<IBasicGameObject> gameObjects(){
        var acc = new ArrayList<IBasicGameObject>();
        acc.addAll(blocks);
        acc.addAll(alienSwarm.aliens());
        acc.addAll(rockets);
        acc.add(player);
        return acc;
    }


    private void move(char dir){
        rockets = Utils.move(rockets);
        alienSwarm = alienSwarm.moveBounded(width);
        player = player.moveBounded(Utils.charToV2(dir),width);
    }

    public List<StringWithLocation>  getUIState(){
        return Utils.getStringsWithLocation(gameObjects());
    }


    private void removeDeadObjects(){
        List<IBasicGameObject> allGameObjects = gameObjects();
        blocks = Utils.removeDeadObjects(blocks, allGameObjects, width,height);
        alienSwarm = alienSwarm.removeDeadAliens(allGameObjects,width, height);
        rockets = Utils.removeDeadObjects(rockets, allGameObjects, width,height);
    }

    public void update(char key){
        removeDeadObjects();
        move(key);
        rockets.addAll(Utils.getNewRockets(rockets, alienSwarm.getShootingAlien(), player, key));
    }


}
