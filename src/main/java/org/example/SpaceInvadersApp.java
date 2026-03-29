package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.geometry.VPos;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class GUI extends GameApplication {

    private static int COLS;
    private static int ROWS;
    private static final int CHAR_SIZE = 24;

    private Model model;
    private char currentKey = ' ';

    public static void start(int cols, int rows) {
        COLS = cols;
        ROWS = rows;
        GameApplication.launch(GUI.class, new String[] {});
    }

    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(COLS * CHAR_SIZE);
        settings.setHeight(ROWS * CHAR_SIZE);
        settings.setTitle("Space Invaders");
    }

    @Override
    protected void initGame() {
        model = new Model(COLS, ROWS);
        FXGL.getGameScene().setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.A,  () -> currentKey = 'a');
        FXGL.onKey(KeyCode.D,  () -> currentKey = 'd');
        FXGL.onKey(KeyCode.L,  () -> currentKey = 'l');
        FXGL.onKey(KeyCode.K,  () -> currentKey = 'k');
        FXGL.onKeyDown(KeyCode.SPACE, () -> model = new Model(COLS, ROWS));
        FXGL.onKeyDown(KeyCode.Q, () -> FXGL.getGameController().exit());
    }

    @Override
    protected void onUpdate(double tpf) {
        if (model.gameOngoing()) {
            model.update(currentKey);
            currentKey = ' ';
        }
        render();
    }

    private void render() {
        var scene = FXGL.getGameScene();
        scene.clearGameViews();
        scene.clearUINodes();

        if (!model.gameOngoing()) {
            String msg = model.getEndMessage() + "\nLeertaste: Neustart\nQ: Beenden";
            drawText(msg, 0, ROWS * CHAR_SIZE / 2.0, CHAR_SIZE * 4);
            return;
        }

        for (var item : model.getUIState()) {
            drawText(item.string(), item.location().x() * CHAR_SIZE, item.location().y() * CHAR_SIZE, CHAR_SIZE);
        }
    }


    private void drawText(String s, double x, double y, int charSize) {
        var t = makeText(s, x, y, charSize);
        FXGL.getGameScene().addUINode(t);
    }

    private static Text makeText(String s, double x, double y, int charSize) {
        var t = new Text(s);
        t.setFill(Color.WHITE);
        // Use a slightly smaller font than CHAR_SIZE so descenders don't get clipped
        t.setFont(Font.font(charSize));
        t.setTextOrigin(VPos.TOP);
        // Use visual bounds so descenders are taken into account when positioning
        t.setBoundsType(TextBoundsType.VISUAL);
        t.setX(x);
        // Center the text vertically inside the CHAR_SIZE cell so descenders are visible
        t.setY(y);
        return t;
    }
}