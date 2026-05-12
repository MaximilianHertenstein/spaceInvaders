package org.example;

import com.almasb.fxgl.app.GameApplication;

import java.io.IOException;
import java.util.List;

import static java.lang.IO.println;

public class Main {
    // Korrekte main-Methode für das Jar-Manifest und lokale Ausführung
    public static void main() throws IOException, InterruptedException {

//        var aliens = List.of(new Alien(new V2(2,38), "a"));
//        var m2 = new Model(60,40, new V2(5,4), aliens, List.of(), List.of());
//        println(m2.gameLost());
//        var model = new Model(40,40);
//        println(model.playerIsAlive());
//        SpaceInvadersApp.start(100, 60);
        //var tui = new TUI(20, 7);
        //tui.print(List.of(new StringWithLocation("Hello", new V2(5, 3)),new StringWithLocation("World", new V2(9, 5))));
        GameApplication.launch(SpaceInvadersApp.class, new String[]{});
   //var controller = new Controller(100, 60);
   //controller.runGame();
//////
//
//        var alien = new Alien(new V2(5, 6), "abc\ndef");
//        println(alien.isInLastLine(9));
//
//        var x = new SuperRocket(new V2(10,10));
//        println(x.show());

    // Weitere Tests / Debug-Code können hier auskommentiert liegen bleiben
}}
