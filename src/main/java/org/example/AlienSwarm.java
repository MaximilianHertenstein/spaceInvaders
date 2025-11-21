package org.example;

import java.util.ArrayList;
import java.util.List;

public record AlienManager(
        V2 aliensDirection,
        List<Alien> aliens,
        CountDown alienRocketCountdown
) {
    AlienManager(){
        this(new V2(1, 0), Alien.createAliens(), new CountDown(25));
    }

    public boolean noAliensLeft() {
        return aliens.isEmpty();
    }

    AlienManager move(int width) {
        alienRocketCountdown.countDown();
        V2 nextAlienDirection = computeNextAlienDirection(aliens, aliensDirection, width);
        return new AlienManager(nextAlienDirection,move(nextAlienDirection),this.alienRocketCountdown);
    }

    public boolean countDownFinished() {
        return alienRocketCountdown.finished();
    }

    public List<Alien> move(V2 dir) {
        var res = new ArrayList<Alien>();
        for (var mgo : aliens){
            res.add( mgo.move(dir));
        }
        return res;
    }

     boolean aliensAreInLastLine(int height){
        return getYCoordinates().contains(height -2);
    }

    public List<Integer> getXCoordinates(){
        var acc = new ArrayList<Integer>();
        for (var alien : aliens){
            acc.add(alien.pos().x());
        }
        return acc;
    }

     List<Alien> getLowestAliens() {
        if (aliens.isEmpty()) return List.of();
        int lowestLine = aliens.getFirst().pos().y();
        var acc = new ArrayList<Alien>();
        for (var alien : aliens) {
            if (alien.pos().y() == lowestLine) {
                acc.add(alien);
            }
        }
        return acc;
    }


    public List<Integer> getYCoordinates(){
        var acc = new ArrayList<Integer>();
        for (var alien : aliens){
            acc.add(alien.pos().y());
        }
        return acc;
    }


    public V2 computeNextAlienDirection(List<Alien> aliens, V2 currentDirection, int width){
        if (currentDirection.equals(new V2(1,0) )&& getXCoordinates().contains(width-4) || currentDirection.equals(new V2(-1,0) )&& getXCoordinates().contains(0)){
            return new V2(0,1);
        }
        if (currentDirection.equals(new V2(0,1) )&& getXCoordinates().contains(width-4) ){
            return new V2(-1,0);

        }
        if(currentDirection.equals(new V2(0,1) )&& getXCoordinates().contains(0)){
            return new V2(1,0);}

        return currentDirection;
    }

     Alien getRandomAlienInLowestLine() {
        var aliensInLowestLine = getLowestAliens();
        return Utils.random(aliensInLowestLine);
    }

    public AlienManager removeDeadAliens(List<IBasicGameObject> allGameObjects, int width, int heigth) {
        var newAliens = Utils.removeDeadObjects(aliens, allGameObjects, width, heigth);
        return new AlienManager(aliensDirection, newAliens, alienRocketCountdown);
    }
}
