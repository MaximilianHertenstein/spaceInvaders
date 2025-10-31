package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicGameObjectTest {


    @Test
    void testCheckCollision() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(1,9), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }
    @Test
    void testCheckCollision2() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(2,9), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }
    @Test
    void testCheckCollision3() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(3,9), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }

    @Test
    void testCheckCollision4() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(4,9), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }

    @Test
    void testCheckCollision5() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(5,9), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }

    @Test
    void testCheckCollision6() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(6,9), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }

    // right
    @Test
    void testCheckCollision7() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(5,8), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }

    @Test
    void testCheckCollision8() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(5,7), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }


    @Test
    void testCheckCollision9() {
        var go1 = new BasicGameObject(new V2(2,7), 3, 2, List.of(""));
        var go2 = new BasicGameObject(new V2(5,6), 30, 20, List.of(""));
        assertFalse(go1.checkCollision(go2));
    }



}