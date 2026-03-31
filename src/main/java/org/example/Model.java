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

    void restart() {
        this.player = new Player(new V2(width /2, height -2));
        this.alienSwarm = new AlienSwarm();
        this.blocks = LevelFactory.generateBlocks(width, height );
        this.rockets = new ArrayList<>();
    }

    public Model(int width, int height) {
        this.width = width;
        this.height = height;
        restart();
    }

    boolean gameWon(){
        return alienSwarm.noAliensLeft();
    }

    public String getEndMessage(){
        if (gameWon()){
            return "You won!";
        }
        return "You lost!";
    }

    private void move(char dir){

        alienSwarm = alienSwarm.moveBounded(width);
        player = player.moveBounded(Utils.charToV2(dir),width);
        rockets = Utils.move(rockets);
    }


    private List<IBasicGameObject> gameObjects(){
        var acc = new ArrayList<IBasicGameObject>();
        acc.addAll(blocks);
        acc.addAll(alienSwarm.aliens());
        acc.addAll(rockets);
        acc.add(player);
        return acc;
    }


    private boolean playerIsAlive() {
        return player.isAlive(gameObjects(),width,height);
    }

    boolean gameLost(){
        return  alienSwarm.aliensAreInLastLine(height) || !playerIsAlive();
    }


    public boolean gameOngoing() {
        return !gameWon() && !gameLost();
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
