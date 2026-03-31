package org.example;

import java.util.ArrayList;
import java.util.List;

public class LevelFactory {
    public static List<BasicGameObject> generateBlock(V2 pos, int cols, int rows) {
        var acc = new ArrayList<BasicGameObject>();
        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < cols; x++) {
                acc.add(new BasicGameObject(pos.plus(new V2(x, y)), "#"));
            }
        }
        return acc;
    }

    public static List<BasicGameObject> generateBlocks(V2 startPos, int cols, int rows, int count) {
        var acc = new ArrayList<BasicGameObject>();
        var pos = startPos;
        for (int i = 0; i < count; i++) {

            acc.addAll(generateBlock(pos, cols, rows));
            pos = pos.plus(new V2(cols + 2, 0));
        }
        return acc;
    }


    public static List<BasicGameObject> generateBlocks(int width, int height) {
        int blockCols = 4;
        int blockGap = 2;
        int blockRows = 3;
        int blockCount = width / (blockCols + blockGap);
        return generateBlocks(new V2(1, 3 * height / 4), blockCols, blockRows, blockCount);
    }


}
