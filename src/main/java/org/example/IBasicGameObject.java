package org.example;

import java.util.List;

public interface IBasicGameObject {
    List<StringWithLocation> show();

    HitBox hitBox();
}
