package org.example;

import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.graphics.TextGraphics;
import com.googlecode.lanterna.screen.TerminalScreen;
import com.googlecode.lanterna.terminal.DefaultTerminalFactory;
import com.googlecode.lanterna.terminal.swing.SwingTerminalFontConfiguration;

import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

public class TUI {
    private final int cols;
    private final int rows;
    private final TerminalScreen screen;
    private final TextGraphics textGraphics;


    public TUI(int cols, int rows) throws IOException {
        this.cols = cols;
        this.rows = rows;
        this.screen = new DefaultTerminalFactory().setPreferTerminalEmulator(true).setInitialTerminalSize(new TerminalSize(cols + 2, rows + 2))
                .setTerminalEmulatorFontConfiguration(
                        SwingTerminalFontConfiguration.getDefaultOfSize(15)
                ).createScreen();

        screen.startScreen();
        screen.setCursorPosition(null);
        textGraphics = screen.newTextGraphics();
    }
    // private val screen = DefaultTerminalFactory().setInitialTerminalSize(TerminalSize(columns + 2, rows + 2)).createScreen()


    public void print(List<StringWithLocation> uiState) throws IOException, InterruptedException {
        screen.clear();
        for (var x : uiState) {
            textGraphics.putString(x.location().x(), x.location().y(), x.string());
        }

        screen.refresh();
        sleep(10);
    }

    public void close() throws IOException {
        screen.close();
    }

    public void printString(String s) throws IOException, InterruptedException {
        screen.clear();
        textGraphics.putString(cols / 2, rows / 2, s);
        screen.refresh();
        sleep(10000);
    }

    public char getPressedKey() throws IOException {
        var input = screen.pollInput();
        if (input != null && input.getCharacter() != null) {
            return input.getCharacter();
        }
        return ' ';
    }
}