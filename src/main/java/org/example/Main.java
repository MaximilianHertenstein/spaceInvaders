package org.example;

import java.io.IOException;


public class Main {
    static void main() throws IOException, InterruptedException {
   // println(Alien.createAliens());

    var tui = new TUI(50,60);
    var model = new Model(50, 60);
    while (model.gameOngoing()){
        tui.print(model.getUIState());
        var input = tui.getPressedKey();
        model.update(input);
    }
    tui.printString(model.getEndMessage());
    tui.close();
}}
