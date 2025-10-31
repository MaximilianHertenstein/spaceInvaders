//package org.example;
//
//public record Rocket(MovableGameObject mgo) implements Movable,IBasicGameObject {
//
//    Rocket(V2 pos){
//        this()
//    }
//
//    @Override
//    public int width() {
//        return mgo.width();
//    }
//
//    @Override
//    public int height() {
//        return mgo.height();
//    }
//
//    @Override
//    public V2 pos() {
//        return mgo.pos();
//    }
//
//    @Override
//    public Movable move(V2 dir) {
//        return new Rocket(mgo.move(dir));
//    }
//}
