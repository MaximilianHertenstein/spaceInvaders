package org.example;

import java.util.List;

public record SuperRocket(MovableGameObject mgo) implements IBasicGameObject, Rocket {


    SuperRocket(V2 pos){



        String rocket = "";
        for (int i = 0; i <50; i++) {
            rocket += "(((||||||||||)))\n";
        }

//        var s =
//                     """
//                     #**
//                   :#****-
//                  #*.....**
//                 #:........*
//                ::...........
//                :.... : .....
//               :.. #****** ...
//               :..##*******...
//              ::.. ###**** ....
//              :.....##### .....
//              :................
//              ::...............
//              ::.....***.......
//             *::.....***.......*
//            ***::....***......***
//          *****::...#***#.....*****
//         ****###::..=***+....###****
//         **#    :::..***.....    #**
//         **      ::::#*#::::      **
//         *#       ***#*#***       #*
//         *#        :==*==:        #*
//                   :=====:
//                   ::===::
//                   :::=:::
//                    :::::
//                     :::
//         """;


        var playerRocket = new MovableGameObject(pos,rocket); // "/|\\\n|||\n|||\n|||\n|||"));
        this(playerRocket);
    }
    @Override
    public V2 pos() {
        return mgo.pos();
    }

    @Override
    public Rocket move() {
        return new SuperRocket((mgo.move(new V2(0,-3))));
    }

    @Override
    public boolean isPlayerRocket() {
        return true;
    }



    @Override
    public List<StringWithLocation> show() {
        return mgo.show();
    }

    @Override
    public List<V2> hitBox() {
        return mgo.hitBox();
    }


    // the super rocket is always alive. it flyes to the edge of the board.
    @Override
    public boolean isAlive(List<IBasicGameObject> gameObjects, int width, int height) {
        return Utils.isOnBoard(hitBox(), width, height);
    }
}
