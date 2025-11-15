package org.example;

import java.io.IOException;

public class Controller {
    TUI tui;// = new TUI(100,60);
    Model model;// = new Model(100, 60);

    public Controller(int cols, int rows) throws IOException {
        tui = new TUI(cols,rows);
        model = new Model(cols, rows);
    }

    void runGame() throws IOException, InterruptedException {
        char input  = ' ';
        while (model.gameOngoing() && input != 'q'){
            tui.print(model.getUIState());
            input = tui.getPressedKey();
            model.update(input);
        }
        tui.printString(model.getEndMessage());
        tui.close();
    }
}
