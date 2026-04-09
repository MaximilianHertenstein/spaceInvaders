---
title: Space-Invaders
codebraid:
  jupyter: true
---
 





```{.java .cb-run}
import static java.lang.IO.println;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;

public record V2(int x, int y) {

    V2 plus(V2 other) {
        return new V2(x + other.x(), y + other.y());
    }

}

public record StringWithLocation(String string, V2 location) {
}

public interface IBasicGameObject {
    V2 pos();
    List<StringWithLocation> show();
    List<V2> hitBox();
    boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height);
}



public interface Rocket extends IBasicGameObject {
    Rocket move();
    boolean isPlayerRocket();
}

public interface Shooting {
    Rocket shoot();


}

```
```{.java .cb-run}


public class Utils {

    public static <T> boolean intersect(List<T> xs, List<T> ys) {
        for (var x : xs) {
            if (ys.contains(x)) {
                return true;
            }
        }
        return false;
    }

    public static V2 charToV2(char dir) {
        return switch (dir) {
            case 'a' -> new V2(-1, 0);
            case 'd' -> new V2(1, 0);
            default -> new V2(0, 0);
        };
    }

    public static boolean isOnBoard(V2 v, int width, int height) {
        return v.x() >= 0 && v.x() < width && v.y() >= 0 && v.y() < height;
    }

    /**
     * Returns the x-coordinates of all points in the given list.
     * Useful for expressing "some x == value" as "xCoordinates.contains(value)".
     */
    public static List<Integer> getXCoordinates( List<V2>  xs) {
        var acc = new ArrayList<Integer>();
        for (var v : xs) {
            acc.add(v.x());
        }
        return acc;
    }

    public static List<Integer> getYCoordinates( List<V2>  xs) {
        var acc = new ArrayList<Integer>();
        for (var v : xs) {
            acc.add(v.y());
        }
        return acc;
    }

    public static boolean isOnBoard(List<V2> xs, int width, int height) {
        for (var v : xs) {
            if (isOnBoard(v, width, height)) {
                return true;
            }
        }
        return false;
    }

    public static <T> T random(List<T> xs) {
        if (xs.isEmpty()) return null;
        var random = new Random();
        var index = random.nextInt(xs.size());
        return xs.get(index);
    }

    public static String repeat(String s, int count){
        var acc = "";
        for (int i =0; i < count; i++){
            acc += s;
        }
        return acc;
    }
    

    public static List<StringWithLocation> getStringsWithLocation(List<IBasicGameObject> basicGameObjects) {
        var acc = new ArrayList<StringWithLocation>();
        for (var bgo : basicGameObjects) {
            acc.addAll(bgo.show());
        }
        return acc;
    }

    static <T extends IBasicGameObject> List<T> removeDeadObjects(List<T> gameObjectsToFilter, List<IBasicGameObject> allGameObjects, int width, int height) {
        var acc = new ArrayList<T>();
        for (var go : gameObjectsToFilter) {
            if (go.isAlive(allGameObjects, width, height)) {
                acc.add(go);
            }
        }
        return acc;
    }


    static boolean containsNoPlayerRocket(List<Rocket> rockets) {
        for (var rocket : rockets) {
            if (rocket.isPlayerRocket()) {
                return false;
            }
        }
        return true;
    }


    public static List<Rocket> move(List<Rocket> rockets) {
        var res = new ArrayList<Rocket>();
        for (var rocket : rockets) {
            res.add(rocket.move());
        }
        return res;
    }



}


```
```{.java .cb-run}



public record BasicGameObject(V2 pos, String displayString) implements IBasicGameObject {



    @Override
    public  List<StringWithLocation> show(){
        var lines = displayString.lines().toList();
        var acc = new ArrayList<StringWithLocation>();
        for (int rowIndex = 0; rowIndex < lines.size(); rowIndex++) {
            acc.add(new StringWithLocation(lines.get(rowIndex), pos.plus(new V2(0, rowIndex))));
        }
        return acc;
    }

    @Override
    public  List<V2> hitBox() {
        var acc = new ArrayList<V2>();
        for (var stringWithLocation : show()) {
            for (int colIndex = 0; colIndex < stringWithLocation.string().length(); colIndex++) {
                acc.add(stringWithLocation.location().plus(new V2(colIndex, 0)));
            }
        }
        return acc;
    }




    public boolean checkCollision(IBasicGameObject other){
        return Utils.intersect(hitBox(),other.hitBox());
    }

    // returns true if the object collides with another object in the list.
    // We expect "all" to contain this object itself, so we check "collisionCount > 1".
    public boolean checkCollision(List<IBasicGameObject>  all){
        var collisionCount = 0;
        for (var other : all) {
            if (checkCollision(other)) {
                collisionCount++;
            }
        }
        return collisionCount > 1;
    }


    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return Utils.isOnBoard(hitBox(), width, height) && !(checkCollision(gameObjects));
    }

}

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


    boolean touchesLeftBorder() {
        // hitbox touches x == 0
        return Utils.getXCoordinates(hitBox()).contains(0);
    }

    boolean touchesRightBorder(int width) {
        // hitbox touches the rightmost valid x coordinate
        return Utils.getXCoordinates(hitBox()).contains(width -1); }
}



public record AlienRocket(MovableGameObject mgo) implements IBasicGameObject, Rocket {

    AlienRocket(V2 pos){
        this(new MovableGameObject(pos,"|\nˇ"));
    }

    @Override
    public V2 pos() {
        return mgo.pos();
    }

    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }

    @Override
    public List<V2> hitBox() {
        return mgo.hitBox();
    }



    @Override
    public boolean isPlayerRocket() {
        return false;
    }

    @Override
    public AlienRocket move() {
        return new AlienRocket(mgo.move(new V2(0,1)));
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }


}

public interface Shooting {
    Rocket shoot();
}


public record Alien(MovableGameObject mgo) implements IBasicGameObject, Shooting {


    Alien(V2 pos, String displayString){
        this(new MovableGameObject(pos, displayString));
    }

    public Alien move(V2 dir) {
        return new Alien(mgo.move(dir));
    }


    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }


    @Override
    public List<V2> hitBox() {
        return mgo.hitBox();
    }

    @Override
    public V2 pos() {
        return mgo.pos();
    }
    @Override
    public AlienRocket shoot() {
        return new AlienRocket(pos().plus(new V2(0,2)));
    }




    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }


    boolean touchesLeftBorder() {
        return mgo.touchesLeftBorder();
    }

    boolean touchesRightBorder(int width) {
        return mgo.touchesRightBorder(width);
    }

    /**
     * Returns true if any part of this alien's hitbox reaches the last playable line.
     *
     * We define the last playable line as y == height - vertical sie of the alien.
     */

    boolean isInLastLine(int height) {
        int lastPossibleLine = height - 1;
        return Utils.getYCoordinates(hitBox()).contains(lastPossibleLine);
    }


}


public record PlayerRocket(MovableGameObject mgo) implements IBasicGameObject, Rocket {

    PlayerRocket(V2 pos){
        this(new MovableGameObject(pos,"|\n^"));
    }

    @Override
    public V2 pos() {
        return mgo.pos();
    }

    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }

    @Override
    public List<V2> hitBox() {
        return mgo.hitBox();
    }

    @Override
    public Rocket move() {
        return new PlayerRocket(mgo.move(new V2(0,-1)));
    }

    @Override
    public boolean isPlayerRocket() {
        return true;
    }

    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }
}


public record Player(MovableGameObject mgo) implements IBasicGameObject, Shooting {

    Player(V2 pos){
        this(new MovableGameObject(pos, "_/MM\\_\nqWAAWp"));
    }

    @Override
    public V2 pos() {
        return mgo.pos();
    }

    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }


    @Override
    public List<V2> hitBox() {
        return mgo.hitBox();
    }

    public Player move(V2 dir) {
        return new Player(mgo().move(dir));
    }

    Player reactToBorder(int width) {
       if (mgo.touchesRightBorder(width))
           return  move(new V2(-1, 0));
       else if (mgo.touchesLeftBorder())
           return move(new V2(1, 0));
       else
           return this;
    }

    public Player moveBounded(V2 dir, int width) {
        return move(dir).reactToBorder(width);
    }

    public PlayerRocket shoot() {
        return new PlayerRocket(pos().plus(new V2(0,-2)));
    }



    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return mgo.isAlive(gameObjects, width, height);
    }

}

public record Plasma(MovableGameObject mgo) implements IBasicGameObject, Rocket {


    Plasma(V2 pos, int height){


        String rocket =  Utils.repeat("(((||||||||||)))\n", height);


//        var s =
//                     """
//                     #**
//                   :#****-
//                  #*.....**
//                 #:........*
//                ::...........
//                :.... : .....
//               :.. #****** ...
//               :..##*******...
//              ::.. ###**** ....
//              :.....##### .....
//              :................
//              ::...............
//              ::.....***.......
//             *::.....***.......*
//            ***::....***......***
//          *****::...#***#.....*****
//         ****###::..=***+....###****
//         **#    :::..***.....    #**
//         **      ::::#*#::::      **
//         *#       ***#*#***       #*
//         *#        :==*==:        #*
//                   :=====:
//                   ::===::
//                   :::=:::
//                    :::::
//                     :::
//         """;


        var mgo = new MovableGameObject(pos,rocket); // "/|\\\n|||\n|||\n|||\n|||"));
        this(mgo);
    }
    @Override
    public V2 pos() {
        return mgo.pos();
    }

    @Override
    public Plasma move() {
        return new Plasma((mgo.move(new V2(0,-3))));
    }

    @Override
    public boolean isPlayerRocket() {
        return true;
    }



    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }

    @Override
    public List<V2> hitBox() {
        return mgo.hitBox();
    }


    // the super rocket is always alive. it flyes to the edge of the board.
    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return Utils.isOnBoard(hitBox(), width, height);
    }
}
```

```{.java .cb-run line_numbers=false}
public record CountDown(int start,
    int current){

    public CountDown(int start) {
        this(start, start);
    }


    public CountDown countDown(){
        if (current > 0) {
            return new CountDown(start, current -1);
        }
        else {
            return new CountDown(start);
        }

    }

    public boolean finished(){
        return this.current == 0;
    }
}

```

```{.java .cb-run line_numbers=false}

public record AlienSwarm(
V2 aliensDirection,
List<Alien> aliens,
CountDown alienRocketCountdown
) {


    static String rowToAlienStrings(int i){
        return switch (i){
            case 2 -> "{@@}\n/\"\"\\" ;
            case 1 -> "/MM\\\n\\~~/";
            case 0 -> "{OO}\n/VV\\";
            default -> throw new IllegalStateException("Unexpected value: " + i);
        };

    }

    public static  List<Alien>createAliens (){
        var res = new ArrayList<Alien>();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 10; col++) {
                var x = 36 - col* 4;
                var y = 8 - row * 4;
                var pos = new V2(x, y);
                res.add(new Alien(pos, rowToAlienStrings(row)));

            }
        }return res;
    }

    AlienSwarm() {
        this(new V2(1, 0), createAliens(), new CountDown(5));
    }

    public boolean noAliensLeft() {
        return aliens.isEmpty();
    }


    public boolean aliensAreInLastLine(int height) {
        for (var alien : aliens) {
            if (alien.isInLastLine(height)) {
                return true;
            }
        }
        return false;
    }


    public AlienSwarm move(){
        var res = new ArrayList<Alien>();
        for (var mgo : aliens) {
            res.add(mgo.move(aliensDirection));
        }
        return new AlienSwarm(aliensDirection, res, alienRocketCountdown.countDown());
    }

    private boolean touchesLeftBorder() {
        for (var alien : aliens) {
            if (alien.touchesLeftBorder()) {
                return true;
            }
        }
        return false;
    }

    private boolean touchesRightBorder(int width) {
        for (var alien : aliens) {
            if (alien.touchesRightBorder(width)) {
                return true;
            }
        }
        return false;
    }


        V2 computeNextAlienDirection(int width) {
            boolean movingRight = aliensDirection.equals(new V2(1, 0));
            boolean movingLeft  = aliensDirection.equals(new V2(-1, 0));
            boolean movingDown  = aliensDirection.equals(new V2(0, 1));

            boolean atRightBorder = touchesRightBorder(width);
            boolean atLeftBorder  = touchesLeftBorder();

            // When moving horizontally and touching a side, start moving down
            if ((movingRight && atRightBorder) || (movingLeft && atLeftBorder)) {
                return new V2(0, 1);
            }

            // When moving down on the right side, start moving left
            if (movingDown && atRightBorder) {
                return new V2(-1, 0);
            }

            // When moving down on the left side, start moving right
            if (movingDown && atLeftBorder) {
                return new V2(1, 0);
            }

            return aliensDirection;
        }

    private AlienSwarm reactToBorder(int width){
        return new AlienSwarm(computeNextAlienDirection(width), aliens,alienRocketCountdown);
    }

    public AlienSwarm moveBounded(int width) {
        return move().reactToBorder(width);
    }

    public boolean countDownFinished() {
        return alienRocketCountdown.finished();
    }

    /**
     * Returns all aliens in the lowest row.
     * <p>
     * Invariant: aliens are ordered so that all aliens in the lowest row
     * come first in the list. Therefore, we can look only at the first element's y.
     */
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

    public Alien getShootingAlien() {
        if (countDownFinished()){
            return Utils.random(getLowestAliens());}
        return null;
    }

    public AlienSwarm removeDeadAliens(List<IBasicGameObject> allGameObjects, int width, int height) {
        var newAliens = Utils.removeDeadObjects(aliens, allGameObjects, width, height);
        return new AlienSwarm(aliensDirection, newAliens, alienRocketCountdown);
    }
}
```




```{.java .cb-run line_numbers=false}
public class LevelFactory {
public static List<BasicGameObject> generateBlock(V2 pos, int cols, int rows) {
var acc = new ArrayList<BasicGameObject>();
for (int y = 0; y < rows; y++) {
for (int x = 0; x < cols; x++) {
acc.add(new BasicGameObject(pos.plus(new V2(x, y)), "#"));
}
}
return acc;
}

    public static List<BasicGameObject> generateBlocks(V2 startPos, int cols, int rows, int count) {
        var acc = new ArrayList<BasicGameObject>();
        var pos = startPos;
        for (int i = 0; i < count; i++) {

            acc.addAll(generateBlock(pos, cols, rows));
            pos = pos.plus(new V2(cols + 2, 0));
        }
        return acc;
    }


    public static List<BasicGameObject> generateBlocks(int width, int height) {
        int blockCols = 4;
        int blockGap = 2;
        int blockRows = 3;
        int blockCount = width / (blockCols + blockGap);
        return generateBlocks(new V2(1, 3 * height / 4), blockCols, blockRows, blockCount);
    }


}
```


```{.java .cb-run line_numbers=false}
public class Model {
private final int width;
private final int height;
Player player;
AlienSwarm alienSwarm;
List<BasicGameObject> blocks;
List<Rocket> rockets;

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

    void move(char dir){

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
        //rockets.addAll(Utils.getNewRockets(rockets, alienSwarm.getShootingAlien(), player, key));
    }


}
```





Definiere jedes Interface in einer eigenen `.java`-Datei, die den Namen des Interfaces trägt, z. B. `IBasicGameObject.java` für `IBasicGameObject`.


#  IBasicGameObject

Aufgabe: Lege das Interface `IBasicGameObject` an und deklariere die Methoden

- `List<StringWithLocation> show()`
- `List<V2> hitBox()`
- `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)`


# BasicGameObject

## BasicGameObject — isAlive

Aufgabe: Ergänze `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)` in `BasicGameObject`.
Die Methode soll immer `true` zurückgeben.

## Aufgabe
Ergänze in der Klasse `BasicGameObject` die Interface-Deklaration und `@Override`-Annotationen.



## Aufgabe

Ergänze die Methode `boolean checkCollision(IBasicGameObject other)` in `BasicGameObject`. Sie soll `true` zurückgeben, wenn die Hitbox dieses Objekts mit der Hitbox von `other` kollidiert.

\tiny
```{.java .cb-nb line_numbers=false}
// zusätzliche Beispiele für Kollisionstests
var c1 = new BasicGameObject(new V2(2,2), "XY");
var c2 = new BasicGameObject(new V2(3,2), "A");
var c3 = new BasicGameObject(new V2(4,2), "B");
println(c1.checkCollision(c2));
println(c1.checkCollision(c3));
```

**Hinweis:** Nutze `Utils.intersect` 



## Aufgabe

Ergänze die Methode `boolean checkCollision(List<IBasicGameObject>  gameObjects)` in `BasicGameObject`.
Sie prüft ob eine Kollision mit einem anderen Objekt aus `gameObjects` vorliegt.
Die übergebene Liste enthält immer das Objekt selbst.

```{.java .cb-nb line_numbers=false}
var b1 = new BasicGameObject(new V2(3,4), "xy");
var b2 = new BasicGameObject(new V2(3,4), "#");
var b3 = new BasicGameObject(new V2(10,10), "#");
List<IBasicGameObject> allGameObjects = List.of(b1, b2, b3);
println(b1.checkCollision(allGameObjects));
println(b1.checkCollision(List.of(b1)));

```




**Hinweis:** Nutze `checkCollision`



## Aufgabe 
Ergänze die korrekte Implementierung von `isAlive` in `BasicGameObject`.

Erklärung: `isAlive(...)` prüft, ob das Objekt noch Teil des Spiels ist. Es soll true liefern when

- mindestens eine Zelle der `hitBox()` auf dem Spielfeld ist, und
- keine Kollision mit einem anderen Objekt aus `gameObjects` vorliegt (gemeinsame V2 in Hitboxes). Die Liste kann dieses Objekt selbst enthalten; dann gilt `collisionCount > 1` als Hinweis auf Kollision mit einem anderen Objekt.

```{.java .cb-nb line_numbers=false}
var b1 = new BasicGameObject(new V2(3,4), "xy");
var b2 = new BasicGameObject(new V2(3,4), "#");
var b3 = new BasicGameObject(new V2(10,10), "#");
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 3, 4));
```



**Hinweis:** Nutze `Utils.isOnBoard` und die `checkCollision`-Methoden von `BasicGameObject`.

#  MovableGameObject

## Aufgabe 
Ergänze die Methode `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)` in `MovableGameObject`. 
Die Logik soll die gleiche sein wie in `BasicGameObject`, aber die Methode soll die Implementierung von `BasicGameObject` wiederverwenden.


```{.java .cb-nb line_numbers=false}
var b1 = new MovableGameObject(new V2(3,4), "xy");
var b2 = new BasicGameObject(new V2(3,4), "#");
var b3 = new BasicGameObject(new V2(10,10), "#");
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 4, 4));
```

## Aufgabe 

Ergänze die korrekte Interface-Deklaration und `@Override`-Annotationen für das Interface `IBasicGameObject` in der Klasse `MovableGameObject`.



```{.java .cb-nb line_numbers=false}
var b1 = new MovableGameObject(new V2(3,4), "xy");
var b2 = new MovableGameObject(new V2(3,4), "#");
var b3 = new BasicGameObject(new V2(10,10), "#");
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 4, 4));
```






# Alien

## Aufgabe
Ergänze die Methode `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)` in `Alien`. Verwende dabei die Methode der Klasse `BasicGameObject`.

```{.java .cb-nb line_numbers=false}
var b1 = new Alien(new V2(3,4), "xy");
var b2 = new MovableGameObject(new V2(3,4), "#");
var b3 = new BasicGameObject(new V2(10,10), "#");
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 4, 4));
```

## Aufgabe

Ergänze die korrekte Interface-Deklaration und `@Override`-Annotationen für das Interface `IBasicGameObject` in der Klasse `Alien`.



# AlienRocket

## Aufgabe
Ergänze die Methode `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)` in `AlienRocket`. Verwende dabei die Methode der Klasse `BasicGameObject`.

```{.java .cb-nb line_numbers=false}
var b1 = new AlienRocket(new V2(3,4));
var b2 = new MovableGameObject(new V2(3,4), "#");
var b3 = new Alien(new V2(10,10), "#");
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 4, 4));
```



## Aufgabe

Ergänze die korrekte Interface-Deklaration und `@Override`-Annotationen für das Interface `IBasicGameObject` in der Klasse `AlienRocket`.


# Player

## Aufgabe
Ergänze die Methode `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)` in `Player`. Verwende dabei die Methode der Klasse `BasicGameObject`.

```{.java .cb-nb line_numbers=false}
var b1 = new Player(new V2(3,4));
var b2 = new AlienRocket(new V2(3,4));
var b3 = new Alien(new V2(10,10), "#");
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 4, 4));
```


## Aufgabe

Ergänze die korrekte Interface-Deklaration und `@Override`-Annotationen für das Interface `IBasicGameObject` in der Klasse `Player`.



# PlayerRocket

## Aufgabe
Ergänze die Methode `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)` in `PlayerRocket`. Verwende dabei die Methode der Klasse `BasicGameObject`.

```{.java .cb-nb line_numbers=false}
var b2 = new PlayerRocket(new V2(3,4));
var b1 = new Player(new V2(3,4));
var b3 = new AlienRocket(new V2(10,10));
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 4, 4));
```

## Aufgabe

Ergänze die korrekte Interface-Deklaration und `@Override`-Annotationen für das Interface `IBasicGameObject` in der Klasse `PlayerRocket`.




# Plasma

## Aufgabe
Ergänze die Methode `boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height)` in `Plasma`. Verwende dabei die Methode der Klasse `BasicGameObject`.

```{.java .cb-nb line_numbers=false}
var b1 = new Plasma(new V2(5,5), 3);
var b2 = new PlayerRocket(new V2(3,4));
var b3 = new Alien(new V2(10,10), "#");
List<IBasicGameObject>  allGameObjects = List.of(b1, b2, b3);
println(b1.isAlive(allGameObjects, 100, 60));
println(b1.isAlive(List.of(b1), 100, 60));
println(b1.isAlive(List.of(b1), 4, 4));
```




## Aufgabe

Ergänze die korrekte Interface-Deklaration und `@Override`-Annotationen für das Interface `IBasicGameObject` in der Klasse `Plasma`.










# Utils 

## Aufgabe
Ergänze die statische Methode ` List<StringWithLocation> getStringsWithLocation(List<IBasicGameObject> basicGameObjects)` in der Klasse `Utils`. Die Methode soll die `show()`-Listen aller übergebenen `IBasicGameObject` zusammenfügen und als eine Liste zurückgeben.

```{.java .cb-nb line_numbers=false}
var b1 = new BasicGameObject(new V2(3,4), "X");
var m1 = new MovableGameObject(new V2(5,5), "Y");
List<IBasicGameObject> allGameObjects = List.of(b1, m1);
println(Utils.getStringsWithLocation(allGameObjects).size());
```



## Aufgabe
Ergänze die statische Methode `<T extends IBasicGameObject> List<T> removeDeadObjects(List<T> gameObjectsToFilter, List<IBasicGameObject> allGameObjects, int width, int height)` in der Klasse `Utils`. Die Methode soll alle Objekte aus `gameObjectsToFilter` herausfiltern, die laut `isAlive(allGameObjects, width, height)` nicht mehr leben, und die verbleibenden Objekte zurückgeben.

```{.java .cb-nb line_numbers=false}
var a1 = new Alien(new V2(3,4), "A");
var a2 = new Alien(new V2(3,4), "A");
List<IBasicGameObject> allGameObjects = List.of(a1, a2);
List<Alien> aliensToFilter = List.of(a1, a2);
println(Utils.removeDeadObjects(aliensToFilter, allGameObjects, 100, 60).size());
```





# Rocket (Aufgaben)

## Aufgabe
Lege das Interface `Rocket`  an . Das Interface soll `IBasicGameObject` erweitern und die folgenden Methoden deklarieren:

- `Rocket move()`
- `boolean isPlayerRocket()`



## Aufgabe
Ergänze bei den Klassen, die Raketen repräsentieren (`PlayerRocket`, `AlienRocket`, `Plasma`) die Interface-Deklaration `implements Rocket` und setze `@Override` über `move()` und `isPlayerRocket()`.

```{.java .cb-nb line_numbers=false}
var pr = new PlayerRocket(new V2(10,5));
var ar = new AlienRocket(new V2(5,5));
var pl = new Plasma(new V2(5,5), 3);
List<IBasicGameObject> allGameObjects = List.of(pr, ar, pl);
println(pr.isPlayerRocket());
println(ar.isPlayerRocket());
println(pl.isPlayerRocket());
```

---





 

# Utils

## Aufgabe
Ergänze die statische Methode `List<Rocket> move(List<Rocket> rockets)` in der Klasse `Utils`. Die Methode soll jede Rakete in der übergebenen Liste bewegen (durch Aufruf von `move()` auf jedem `Rocket`) und die resultierende Liste zurückgeben.

```{.java .cb-nb line_numbers=false}
var r1 = new PlayerRocket(new V2(10,10));
var r2 = new AlienRocket(new V2(5,5));
List<Rocket> rockets = List.of(r1, r2);
var moved = Utils.move(rockets);
println(moved);
```


# containsNoPlayerRocket

## Aufgabe
Ergänze die statische Methode `boolean containsNoPlayerRocket(List<Rocket> rockets)` in der Klasse `Utils`. Die Methode soll `true` zurückgeben, wenn in der Liste keine `Rocket` existiert, bei der `isPlayerRocket()` `true` ist.

```{.java .cb-nb line_numbers=false}
var r1 = new PlayerRocket(new V2(10,10));
var r2 = new AlienRocket(new V2(5,5));
List<Rocket> rockets1 = List.of(r2);
println(Utils.containsNoPlayerRocket(rockets1)); // true
List<Rocket> rockets2 = List.of(r1, r2);
println(Utils.containsNoPlayerRocket(rockets2)); // false
```


# Model

# Aufgabe

Ergänze in der Klasse `Model` die Eigenscht `rockets` als `List<Rocket>`. Die Methode `restart` von `Model` soll eine leere Liste von Raketen initialisieren.

```{.java .cb-nb line_numbers=false}
var model = new Model(10, 20);
model.restart();
println(model.rockets);
```



# Aufgabe
Ergänze in der Klasse `Model` die Methode `move` so, dass zusätzlich alle Raketen in `rockets` bewegt und die Liste durch die neue Liste ersetzt wird.

