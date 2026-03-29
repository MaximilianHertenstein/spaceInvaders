package org.example;

import com.almasb.fxgl.app.GameApplication;
import com.almasb.fxgl.app.GameSettings;
import com.almasb.fxgl.dsl.FXGL;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;
//import javafx.scene.input.KeyCode;
//import javafx.scene.paint.Color;


public class SpaceInvadersApp extends GameApplication {

    private static final int COLS = 100;
    private static final int ROWS = 60;
    private static final int CHAR_SIZE = 24;

    private  GUI gui;
    private Model model;
    private char currentKey = ' ';
    private double acc;


    @Override
    protected void initSettings(GameSettings settings) {
        settings.setWidth(COLS * CHAR_SIZE);
        settings.setHeight(ROWS * CHAR_SIZE);
        settings.setTitle("Space Invaders");

    }

    @Override
    protected void initGame() {
        model = new Model(COLS, ROWS);
        gui = new GUI(CHAR_SIZE);
        FXGL.getGameScene().setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void initInput() {
        // simple mapping of movement/shoot keys
        FXGL.onKey(KeyCode.A, () -> currentKey = 'a');
        FXGL.onKey(KeyCode.D, () -> currentKey = 'd');
        FXGL.onKey(KeyCode.L, () -> currentKey = 'l');
        FXGL.onKey(KeyCode.K, () -> currentKey = 'k');
        FXGL.onKeyDown(KeyCode.SPACE,  () ->model.restart());
        FXGL.onKeyDown(KeyCode.Q, FXGL.getGameController()::exit);
    }

    private void render() {
        gui.clearScreen();
        if (model.gameOngoing()) {
            gui.renderGame(model.getUIState());

        } else {
            gui.renderEndScreen(model.getEndMessage());
        }
    }

    @Override
    protected void onUpdate(double tpf) {
        acc += tpf;

        if (acc > 0.08) {
            model.update(currentKey);
            currentKey = ' ';
            acc = 0;
            render();
        }

    }


}