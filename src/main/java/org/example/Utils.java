package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    private static boolean isOnBoard(V2 v, int width, int height) {
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

    static <T> T random(List<T> xs) {
        if (xs.isEmpty()) return null;
        var random = new Random();
        var index = random.nextInt(xs.size());
        return xs.get(index);
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

    public static List<Shooting> getShootingObjects(List<Rocket> rockets, Alien shootingAlien, Player player, char pressedKey) {
        var shootingObjects = new ArrayList<Shooting>();
        if (shootingAlien != null) {
            shootingObjects.add(shootingAlien);
        }
        var playerCanShoot = containsNoPlayerRocket(rockets);
        if (pressedKey == 'k' && playerCanShoot) {
            shootingObjects.add(player);
        }
        if (pressedKey == 'l' && playerCanShoot) {
            shootingObjects.add(new InvisibleRocketLauncher(player.pos()));
        }
        return shootingObjects;
    }

    public static List<Rocket> getNewRockets(List<Shooting> shootingObjects) {
        var acc = new ArrayList<Rocket>();
        for (var shootingObject : shootingObjects) {
            acc.add(shootingObject.shoot());
        }
        return acc;
    }

    public static List<Rocket> getNewRockets(List<Rocket> rockets, Alien shootingAlien, Player player, char key) {
        var shootingObjects = getShootingObjects(rockets, shootingAlien, player, key);
        return getNewRockets(shootingObjects);

    }
}
