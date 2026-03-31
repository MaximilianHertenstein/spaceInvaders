package org.example;

public record PlasmaShooter(V2 pos) implements Shooting{

    @Override
    public Rocket shoot() {
        return new Plasma(pos().plus(new V2(0,-50)));
    }
}
