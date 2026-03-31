package org.example;

import java.util.List;


public record Plasma(MovableGameObject mgo) implements IBasicGameObject, Rocket {


    Plasma(V2 pos, int height){


        String rocket =  Utils.repeat("(((||||||||||)))\n", height);


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


        var mgo = new MovableGameObject(pos,rocket); // "/|\\\n|||\n|||\n|||\n|||"));
        this(mgo);
    }
    @Override
    public V2 pos() {
        return mgo.pos();
    }

    @Override
    public Plasma move() {
        return new Plasma((mgo.move(new V2(0,-3))));
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
