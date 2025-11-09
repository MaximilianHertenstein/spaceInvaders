package org.example;

public record InvisibleRocketLauncher(V2 pos) implements Shooting{

    @Override
    public Rocket shoot() {
        return new SuperRocket(pos().plus(new V2(-10,-25)));
    }
}
