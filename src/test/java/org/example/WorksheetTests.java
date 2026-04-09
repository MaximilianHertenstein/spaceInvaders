package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class WorksheetTests {

    @Test
    void testV2Plus() {
        var a = new V2(3, 2);
        var b = new V2(1, -5);
        assertEquals(new V2(4, -3), a.plus(b));

        assertEquals(new V2(-1, -1), new V2(3, 4).plus(new V2(-4, -5)));
    }

    @Test
    void testUtilsGetCoordinatesAndCharToV2() {
        var pts = List.of(new V2(3, 4), new V2(7, 9), new V2(3, 5));
        assertEquals(List.of(3, 7, 3), Utils.getXCoordinates(pts));
        assertEquals(List.of(4, 9, 5), Utils.getYCoordinates(pts));

        assertEquals(List.of(-1), Utils.getXCoordinates(List.of(new V2(-1, 2))));

        assertEquals(new V2(-1, 0), Utils.charToV2('a'));
        assertEquals(new V2(1, 0), Utils.charToV2('d'));
        assertEquals(new V2(0, 0), Utils.charToV2('x'));
    }

    @Test
    void testUtilsIsOnBoard() {
        assertTrue(Utils.isOnBoard(new V2(3, 4), 10, 8));
        assertFalse(Utils.isOnBoard(new V2(-1, 0), 10, 8));
        assertTrue(Utils.isOnBoard(new V2(9, 7), 10, 8));

        assertTrue(Utils.isOnBoard(List.of(new V2(9,7), new V2(10, 7)), 10, 8));
        assertTrue(Utils.isOnBoard(List.of(new V2(-1,7), new V2(0, 7)), 10, 8));
        // both points are outside: x == 10 is already out of bounds for width 10
        assertFalse(Utils.isOnBoard(List.of(new V2(11,7), new V2(10, 7)), 10, 8));
    }

    @Test
    void testIntersectAndRandomAndRepeat() {
        assertTrue(Utils.intersect(List.of(1, 2), List.of(2, 3, 4)));
        assertFalse(Utils.intersect(List.of(1, 2), List.of(3, 4)));
        // intersect with V2
        assertFalse(Utils.intersect(List.of(new V2(1,1)), List.of(new V2(1,5))));

        // random should return an element from the list or null for empty
        var r = Utils.random(List.of(1,2,3));
        assertNotNull(r);
        assertTrue(List.of(1,2,3).contains(r));
        assertNull(Utils.random(List.of()));

        assertEquals("ababab", Utils.repeat("ab", 3));
        assertEquals("", Utils.repeat("hi", 0));
    }

    @Test
    void testUtilsAdditionalAndRockets() {
        var b1 = new BasicGameObject(new V2(3,4), "xy");
        // isAlive should be true when only itself is in the list and on board
        assertTrue(b1.isAlive(List.of(b1), 100, 100));

        // getStringsWithLocation
        assertEquals(1, Utils.getStringsWithLocation(List.of(b1)).size());

        // containsNoPlayerRocket
        assertFalse(Utils.containsNoPlayerRocket(List.of(new PlayerRocket(new V2(1,1)))));
        assertTrue(Utils.containsNoPlayerRocket(List.of(new AlienRocket(new V2(1,1)))));

        // move rockets
        var pr = new PlayerRocket(new V2(5,5));
        var moved = Utils.move(List.of(pr));
        assertEquals(1, moved.size());
        assertTrue(moved.get(0).pos().y() < pr.pos().y());

    }

    @Test
    void testStringWithLocationBasicGameObjectAndHitBox() {
        var swl = new StringWithLocation("Hi", new V2(3,4));
        assertEquals("Hi", swl.string());
        assertEquals(new V2(3,4), swl.location());

        var b1 = new BasicGameObject(new V2(3,4), "xy");
        var show1 = b1.show();
        assertEquals(1, show1.size());
        assertEquals("xy", show1.get(0).string());
        assertEquals(new V2(3,4), show1.get(0).location());

        var hit1 = b1.hitBox();
        assertTrue(hit1.contains(new V2(3,4)));
        assertTrue(hit1.contains(new V2(4,4)));

        var b2 = new BasicGameObject(new V2(10,5), "abc\ndef");
        var show2 = b2.show();
        assertEquals(2, show2.size());
        assertEquals("abc", show2.get(0).string());
        assertEquals(new V2(10,5), show2.get(0).location());
        assertEquals("def", show2.get(1).string());
        assertEquals(new V2(10,6), show2.get(1).location());

        var hit2 = b2.hitBox();
        assertTrue(hit2.contains(new V2(10,5)));
        assertTrue(hit2.contains(new V2(12,5)));
        assertTrue(hit2.contains(new V2(11,6)));
    }

    @Test
    void testMovableGameObjectAndBorders() {
        var mgo = new MovableGameObject(new V2(3,4), "xy");
        assertEquals(new V2(3,4), mgo.pos());
        var moved = mgo.move(new V2(2,1));
        assertEquals(new V2(5,5), moved.pos());

        var left = new MovableGameObject(new V2(0,4), "xy");
        assertTrue(left.touchesLeftBorder());
        assertFalse(left.touchesRightBorder(5));

        var right = new MovableGameObject(new V2(3,4), "xy");
        // hitbox spans x=3,4 so width 5 -> rightmost index 4 -> touchesRight true
        assertTrue(right.touchesRightBorder(5));
        assertFalse(right.touchesRightBorder(6));
    }

    @Test
    void testPlayerAndRocketsAndAlienRockets() {
        var player = new Player(new V2(10,5));
        assertEquals(new V2(10,5), player.pos());
        var pr = player.shoot();
        assertEquals(new V2(10,3), pr.pos());
        assertTrue(pr.isPlayerRocket());

        var ar = new AlienRocket(new V2(3,4));
        assertEquals(new V2(3,4), ar.pos());
        assertFalse(ar.isPlayerRocket());
        var ar2 = ar.move();
        assertEquals(new V2(3,5), ar2.pos());
    }

    @Test
    void testAlienAndIsInLastLineAndShoot() {
        var alien = new Alien(new V2(5,6), "abc\ndef");
        assertEquals(new V2(5,6), alien.pos());
        assertFalse(alien.isInLastLine(9));
        assertTrue(alien.isInLastLine(8));
        var rocket = alien.shoot();
        assertEquals(new V2(5,8), rocket.pos());
    }

    @Test
    void testCountDown() {
        var c = new CountDown(3,2);
        c = c.countDown();
        assertEquals(1, c.current());
        c = c.countDown();
        assertEquals(0, c.current());
        c = c.countDown();
        assertEquals(3, c.current());
        assertTrue(new CountDown(4,0).finished());
        assertFalse(new CountDown(4,1).finished());
    }

    @Test
    void testAlienSwarmBasics() {
        var swarm = new AlienSwarm();
        assertEquals(30, swarm.aliens().size());
        assertFalse(swarm.noAliensLeft());
        assertFalse(swarm.aliensAreInLastLine(100));
        var moved = swarm.move();
        assertEquals(swarm.aliensDirection(), moved.aliensDirection());
        assertFalse(moved.aliens().isEmpty());

        // getLowestAliens should return a list (not null)
        var lowest = swarm.getLowestAliens();
        assertNotNull(lowest);
        // countdown finished false initially
        assertFalse(swarm.countDownFinished());
    }

    @Test
    void testLevelFactoryAndPlasmaCannon() {
        var blocks = LevelFactory.generateBlock(new V2(0,0), 3,2);
        assertEquals(6, blocks.size());
        var many = LevelFactory.generateBlocks(new V2(0,0), 3,2,3);
        assertEquals(3 * 3 * 2, many.size());

        var plasma = new Plasma(new V2(5,10), 3);
        assertEquals(new V2(5,10), plasma.pos());
        var moved = plasma.move();
        assertEquals(new V2(5,7), moved.pos());

        var cannon = new InvisiblePlasmaCannon(new V2(10,15));
        var p = cannon.shoot();
        assertEquals(new V2(10,0), p.pos());
    }

    @Test
    void testModelRestartAndMove() {
        var model = new Model(100,60);
        model.restart();
        assertNotNull(model.player);
        assertNotNull(model.alienSwarm);
        assertNotNull(model.blocks);
        assertFalse(model.blocks.isEmpty());

        model.move('d');
        // player should have moved (or at least still exist)
        assertNotNull(model.player.pos());
    }

    @Test
    void testPlayerMovementAndBorders() {
        var player = new Player(new V2(0,5));
        // touching left border
        assertTrue(player.hitBox().stream().anyMatch(v -> v.x() == 0));
        // reactToBorder when at left should move right
        var movedRight = player.move(new V2(-1,0)).reactToBorder(10);
        // ensure still a Player
        assertNotNull(movedRight);

        // test moveBounded doesn't throw and returns Player
        var bounded = new Player(new V2(9,5)).moveBounded(new V2(1,0), 10);
        assertNotNull(bounded);
    }

    @Test
    void testAlienSpecificMethods() {
        var a = new Alien(new V2(0,5), "x");
        assertTrue(a.touchesLeftBorder());
        assertFalse(a.touchesRightBorder(100));
        var moved = a.move(new V2(1,0));
        assertEquals(new V2(1,5), moved.pos());
    }

    @Test
    void testAlienSwarmDirectionLogic() {
        // moving right and touching right border -> next direction down
        var swarm = new AlienSwarm(new V2(1,0), List.of(new Alien(new V2(3,5), "x")), new CountDown(1,0));
        var next = swarm.computeNextAlienDirection(4);
        assertEquals(new V2(0,1), next);

        // moving down and touching right -> left
        var swarm2 = new AlienSwarm(new V2(0,1), List.of(new Alien(new V2(3,5), "x")), new CountDown(1,0));
        var next2 = swarm2.computeNextAlienDirection(4);
        assertEquals(new V2(-1,0), next2);
    }

    @Test
    void testLevelFactoryWidthHeight() {
        var blocks = LevelFactory.generateBlocks(20, 12);
        // should return a list (possibly empty) but not null
        assertNotNull(blocks);
    }

    @Test
    void testPlasmaAndCollision() {
        var plasma = new Plasma(new V2(5,5), 2);
        assertTrue(plasma.isPlayerRocket());
        assertNotNull(plasma.show());
        assertNotNull(plasma.hitBox());

        // collision: two objects on same cell -> isAlive false
        var bb1 = new BasicGameObject(new V2(2,2), "x");
        var bb2 = new BasicGameObject(new V2(2,2), "y");
        assertFalse(bb1.isAlive(List.of(bb1, bb2), 10, 10));
    }

    @Test
    void testMissingWorksheetExamples() throws NoSuchMethodException {


        // AlienSwarm.rowToAlienStrings(0)
        assertEquals("{OO}\n/VV\\", AlienSwarm.rowToAlienStrings(0));

        // AlienSwarm.createAliens()
        assertEquals(30, AlienSwarm.createAliens().size());

        // swarm.getShootingAlien() -> when countdown finished should return an Alien
        var swarm = new AlienSwarm(new V2(1,0), AlienSwarm.createAliens(), new CountDown(1,0));
        assertNotNull(swarm.getShootingAlien());
        // when not finished -> null
        var swarm2 = new AlienSwarm(new V2(1,0), AlienSwarm.createAliens(), new CountDown(1,1));
        assertNull(swarm2.getShootingAlien());

        // TUI.print(xs) — only check that method exists (avoids terminal dependency)
        assertDoesNotThrow(() -> TUI.class.getMethod("print", java.util.List.class));
    }
}

