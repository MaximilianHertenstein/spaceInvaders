package org.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BasicGameObjectTest {


    @Test
    void testCheckCollision() {
        var go1 = new BasicGameObject(new V2(3, 7), List.of("#"));
        var go2 = new BasicGameObject(new V2(2, 7), List.of("|", "|"));
        assertFalse(go1.checkCollision(go2));
    }
    @Test

    void testCheckCollision2() {
        var go1 = new BasicGameObject(new V2(3, 7), List.of("#"));
        var go2 = new BasicGameObject(new V2(3, 7), List.of("|", "|"));
        assertTrue(go1.checkCollision(go2));
    }

    @Test
    void testCheckCollision3() {
        var go1 = new BasicGameObject(new V2(3, 7), List.of("##", "##"));
        var go2 = new BasicGameObject(new V2(5, 7), List.of("##", "##"));
        assertFalse(go1.checkCollision(go2));
    }


}