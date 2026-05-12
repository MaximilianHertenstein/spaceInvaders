package org.example;

import java.util.ArrayList;
import java.util.List;

public class RocketFactory {
    public static List<Shooting> getShootingObjects(List<Rocket> rockets, Alien shootingAlien, Player player, char pressedKey) {
        var shootingObjects = new ArrayList<Shooting>();
        if (shootingAlien != null) {
            shootingObjects.add(shootingAlien);
        }
        var playerCanShoot = Utils.containsNoPlayerRocket(rockets);
        if (pressedKey == 'k' && playerCanShoot) {
            shootingObjects.add(player);
        }
        if (pressedKey == 'l' && playerCanShoot) {
            shootingObjects.add(new InvisiblePlasmaCannon(player.pos()));
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

    public static List<Rocket> addNewRockets(List<Rocket> rockets, Alien shootingAlien, Player player, char key) {
        var currentRockets = new ArrayList<Rocket>(rockets);
        var newRockets = getNewRockets(rockets, shootingAlien, player, key);
        currentRockets.addAll(newRockets);
        return currentRockets;

    }


}
