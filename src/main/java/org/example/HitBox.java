package org.example;

public record HitBox(V2 pos, int width, int height) {
    boolean intersects(HitBox other){
        boolean intersectsX = pos.x() > other.pos().x() && pos.x() < other.pos().x() + other.width() || other.pos().x() > pos.x() && other.pos().x() < pos.x() + width();
        boolean intersectsY = pos.y() > other.pos().y() && pos.y() < other.pos().y() + other.height() || other.pos().y() > pos.y() && other.pos().y() < pos.y() + height();
        return intersectsX && intersectsY;
    }
}
