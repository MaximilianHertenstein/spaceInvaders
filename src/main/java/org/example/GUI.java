package org.example;

import com.almasb.fxgl.app.scene.GameScene;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.dsl.components.Effect;
import javafx.geometry.VPos;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GUI {

    public static final Color NEON_GREEN = Color.web("#39FF14");

    private final GameScene scene;
    private final Font smallFont;
    private final DropShadow neonEffect;

    public GUI(int size) {
        var fontPath = GUI.class.getResourceAsStream("/DepartureMono-Regular.otf");
        smallFont = Font.loadFont(fontPath, size);
        scene = FXGL.getGameScene();

        var glow = new Glow(0.8);
        var shadow = new DropShadow();
        shadow.setColor(NEON_GREEN);
        shadow.setRadius(14);
        shadow.setSpread(0.35);
        shadow.setInput(glow);
        neonEffect = shadow;
    }

    public void clearScreen() {
        scene.clearUINodes();
    }

    public void renderGame(List<StringWithLocation> uiState) {
        for (var item : uiState) {
            drawText(item.string(), item.location().x(), item.location().y());
        }
    }

    public void renderEndScreen(String endMessage) {
        String msg = endMessage + "\nLeertaste: Neustart\nQ: Beenden";
        drawText(msg, 5, 5);
    }

    private void drawText(String s, double x, double y) {
        double charSize = smallFont.getSize();

        var text = new Text(x * charSize, y * charSize, s);
        text.setFill(NEON_GREEN);
        text.setFont(smallFont);
        text.setTextOrigin(VPos.TOP);
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setEffect(neonEffect);

        scene.addUINode(text);
    }
}