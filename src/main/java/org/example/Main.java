package org.example;

import java.io.IOException;
import java.util.List;

import static java.lang.IO.println;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main() throws IOException, InterruptedException {
    println(Alien.createAliens());

    var tui = new TUI(40,27);
    var model = new Model();
    while (true){
    println(model.getUIState());
    tui.print(model.getUIState());
    model.update(' ');
    }
}}
