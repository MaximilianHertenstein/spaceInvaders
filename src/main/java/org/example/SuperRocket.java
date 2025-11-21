package org.example;

import java.util.List;

public record SuperRocket(Rocket playerRocket) implements IBasicGameObject, Rocket {


    SuperRocket(V2 pos){


        var s =
                     """
                     #**
                   :#****-
                  #*.....**
                 #:........*
                ::...........
                :.... : .....
               :.. #****** ...
               :..##*******...
              ::.. ###**** ....
              :.....##### .....
              :................
              ::...............
              ::.....***.......
             *::.....***.......*
            ***::....***......***
          *****::...#***#.....*****
         ****###::..=***+....###****
         **#    :::..***.....    #**
         **      ::::#*#::::      **
         *#       ***#*#***       #*
         *#        :==*==:        #*
                   :=====:
                   ::===::
                   :::=:::
                    :::::
                     :::
         """;


        var playerRocket = new PlayerRocket(new MovableGameObject(pos,s)); // "/|\\\n|||\n|||\n|||\n|||"));
        this(playerRocket);
    }
    @Override
    public V2 pos() {
        return playerRocket.pos();
    }

    @Override
    public Rocket move() {
        return new SuperRocket((playerRocket.move()));
    }

    @Override
    public boolean isPlayerRocket() {
        return true;
    }



    @Override
    public List<StringWithLocation> show() {
        return playerRocket.show();
    }

    @Override
    public List<V2> hitBox() {
        return playerRocket.hitBox();
    }


    // the super rocket is always alive. it flyes to the edge of the board.
    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return Utils.isOnBoard(hitBox(), width, height);
    }
}
