package org.example;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public  static V2 charToV2(char dir) {
        return switch (dir){
            case  'a'->  new V2(-1,0);
            case 'd'->  new V2(1,0);
            default -> new V2(0,0);
        };
    }

    public static List<StringWithLocation>  getStringsWithLocation(List<IBasicGameObject> bgos) {
        var acc = new ArrayList<StringWithLocation>();
        for (var bgo: bgos){
            acc.addAll(bgo.show());
        }
        return acc;
    }


    static <T extends  IBasicGameObject>  List<T> removeDeadObjects(int width, int height, List<T> gameObjectsToFilter, List<IBasicGameObject> allGameObjects){
        var acc = new ArrayList<T>();
        for( var go : gameObjectsToFilter){
            if (go.isAlive(allGameObjects,width,height)){
                acc.add(go);
            }
        }
        return acc;
    }

    public static <T extends Movable> List<T> move(List<T> movableGameObjects, V2 dir) {
        var res = new ArrayList<T>();
        for (var mgo : movableGameObjects){
            res.add((T) mgo.move(dir));
        }
        return res;
    }

    public static List<Integer> getXCoordinates(List<Alien>aliens){
        var acc = new ArrayList<Integer>();
        for (var alien : aliens){
            acc.add(alien.hitBox().pos().x());
        }
        return acc;
    }

    public static V2 computeNextAlienDirection(List<Alien> aliens, int width, V2 currentDirection){
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



}
