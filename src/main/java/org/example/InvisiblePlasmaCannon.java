package org.example;

public record InvisiblePlasmaCannon(V2 pos) implements Shooting{

    @Override
    public Rocket shoot() {
        var plasmaHeight =  pos.y();
        return new Plasma(  pos().plus(new V2(0,-plasmaHeight)), plasmaHeight);
    }
}
