package  org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class GUI extends GameApplication {

    private static int COLS;
    private static int ROWS;
    private static final int CHAR_SIZE = 15;

    private Model model;
    private char currentKey = ' ';

    public static void start(int cols, int rows, String[] args) {
        COLS = cols;
        ROWS = rows;
        GameApplication.launch(GUI.class, args);
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
    }

    @Override
    protected void initInput() {
        FXGL.onKey(KeyCode.LEFT,  () -> currentKey = 'l');
        FXGL.onKey(KeyCode.RIGHT, () -> currentKey = 'r');
        FXGL.onKey(KeyCode.SPACE, () -> currentKey = ' ');
        FXGL.onKeyDown(KeyCode.R, () -> model = new Model(COLS, ROWS));
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
        FXGL.getGameScene().clearGameViews();

        if (!model.gameOngoing()) {
            drawText(model.getEndMessage() + "  —  R: Neustart  Q: Beenden",
                    Color.YELLOW, CHAR_SIZE * 2, 50, ROWS * CHAR_SIZE / 2);
            return;
        }

        for (var item : model.getUIState()) {
            drawText(item.string(), Color.WHITE, CHAR_SIZE,
                    item.location().x() * CHAR_SIZE,
                    item.location().y() * CHAR_SIZE);
        }
    }

    private void drawText(String s, Color color, int size, double x, double y) {
        var t = new Text(s);
        t.setFill(color);
        t.setFont(Font.font("Monospaced", size));
        t.setX(x);
        t.setY(y);
        FXGL.getGameScene().addUINode(t);
    }
}