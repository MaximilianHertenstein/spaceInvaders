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

Erklärung: `isAlive(...)` prüft, ob das Objekt noch Teil des Spiels ist. Es soll true liefern wenn

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




