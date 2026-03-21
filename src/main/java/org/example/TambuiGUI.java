package org.example;



import dev.tamboui.backend.jline3.JLineBackend;
import dev.tamboui.buffer.Buffer;
import dev.tamboui.buffer.Cell;
import dev.tamboui.style.Style;
import dev.tamboui.terminal.Terminal;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.List;

import static java.lang.Thread.sleep;

public class TambuiGUI implements AutoCloseable {

    private final int cols;
    private final int rows;
    private final JLineBackend backend;
    private final Terminal<?> terminal;

    public TambuiGUI(int cols, int rows) throws IOException {
        this.cols = cols;
        this.rows = rows;
        this.backend = new JLineBackend();
        this.terminal = new Terminal<>(backend);

        backend.enableRawMode();
        backend.enterAlternateScreen();
        backend.hideCursor();
    }

    public void print(List<StringWithLocation> uiState) throws IOException, InterruptedException {
        terminal.draw(frame -> {
            Buffer buffer = frame.buffer();
            buffer.clear();
            for (var item : uiState) {
                putString(buffer, item.location().x(), item.location().y(), item.string());
            }
        });
        sleep(0);
    }

    public void printString(String s) throws IOException, InterruptedException {
        terminal.draw(frame -> {
            Buffer buffer = frame.buffer();
            buffer.clear();
            putString(buffer, cols / 2, rows / 2, s);
        });
        sleep(0);
    }

    public char getPressedKey() throws IOException {
        int peeked = backend.peek(0); // schaut nach, ob etwas da ist
        if (peeked > 0) {
            return (char) backend.read(0);
        }
        return ' ';
    }

    private void putString(Buffer buffer, int x, int y, String s) {
        for (int i = 0; i < s.length(); i++) {
            buffer.set(x + i, y, new Cell(String.valueOf(s.charAt(i)), Style.EMPTY));
        }
    }

    @Override
    public void close() throws IOException {
        backend.close();
    }
}