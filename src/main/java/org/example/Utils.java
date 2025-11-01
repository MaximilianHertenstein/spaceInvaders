package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Utils {

    public  static V2 charToV2(char dir) {
        return switch (dir){
            case  'a'->  new V2(-1,0);
            case 'd'->  new V2(1,0);
            default -> new V2(0,0);
        };
    }


    public static List<Alien> move(List<Alien> aliens, V2 dir) {
        var res = new ArrayList<Alien>();
        for (var mgo : aliens){
            res.add( mgo.move(dir));
        }
        return res;
    }



    public static List<StringWithLocation>  getStringsWithLocation(List<IBasicGameObject> basicGameObjects) {
        var acc = new ArrayList<StringWithLocation>();
        for (var bgo: basicGameObjects){
            acc.addAll(bgo.show());
        }
        return acc;
    }


    public static List<Integer> getXCoordinates(List<Alien>aliens){
        var acc = new ArrayList<Integer>();
        for (var alien : aliens){
            acc.add(alien.hitBox().pos().x());
        }
        return acc;
    }


    public static List<Integer> getYCoordinates(List<Alien>aliens){
        var acc = new ArrayList<Integer>();
        for (var alien : aliens){
            acc.add(alien.hitBox().pos().y());
        }
        return acc;
    }


    public static V2 computeNextAlienDirection(List<Alien> aliens, V2 currentDirection, int width){
        if (currentDirection.equals(new V2(1,0) )&& getXCoordinates(aliens).contains(width-4) || currentDirection.equals(new V2(-1,0) )&& getXCoordinates(aliens).contains(0)){
            return new V2(0,1);
        }
        if (currentDirection.equals(new V2(0,1) )&& getXCoordinates(aliens).contains(width-4) ){
            return new V2(-1,0);

        }
        if(currentDirection.equals(new V2(0,1) )&& getXCoordinates(aliens).contains(0)){
            return new V2(1,0);}

        return currentDirection;
    }

    public static boolean aliensAreInLastLine(List<Alien> aliens, int height){
        return getYCoordinates(aliens).contains(height -2);
    }

    public static List<BasicGameObject> generateBlock(V2 pos, int width, int height) {
        var acc = new ArrayList<BasicGameObject>();
        for(int y = 0; y < height; y++){
            for(int x = 0; x < width; x++){
                acc.add(new BasicGameObject(pos.plus(new V2(x, y)), List.of("#")));
            }
        }
        return acc;
    }

    public static List<BasicGameObject> generateBlocks(V2 pos, int width, int height, int count) {
        var acc = new ArrayList<BasicGameObject>();
        for(int y = 0; y < count; y++){
            pos = pos.plus(new V2(width + 2, 0));
            acc.addAll(generateBlock(pos,width,height));

        }
        return acc;
    }


    static List<Alien> getLowestAliens(List<Alien> aliens){
        if (aliens.isEmpty()) return List.of();
        int lowestLine = aliens.getFirst().hitBox().pos().y();
        var acc = new ArrayList<Alien>();
        for (var alien: aliens){
            if (alien.hitBox().pos().y() == lowestLine){
                acc.add(alien);
            }
        }
        return acc;
    }


    public static boolean isOnBoard(V2 pos, int width, int height) {

        return pos.x() >= 0 && pos.x() < width && pos.y() >= 0 && pos.y() < height;
    }

    static AlienRocket getRocketOfRandomAlien(List<Alien> aliens){
        var aliensInLowestLine = getLowestAliens(aliens);
        if (aliensInLowestLine.isEmpty()) return null;
        var random = new Random();
        var index = random.nextInt(aliensInLowestLine.size());
        return aliensInLowestLine.get(index).shoot();
    }

    static <T extends  IBasicGameObject>  List<T> removeDeadObjects(List<T> gameObjectsToFilter, List<IBasicGameObject> allGameObjects,int width, int height){
        var acc = new ArrayList<T>();
        for( var go : gameObjectsToFilter){
            if (go.isAlive(allGameObjects,width,height)){
                acc.add(go);
            }
        }
        return acc;
    }

    public static <T extends Rocket> List<T> move(List<T> rockets) {
        var res = new ArrayList<T>();
        for (var rocket : rockets){
            res.add((T) rocket.move());
        }
        return res;
    }
}
