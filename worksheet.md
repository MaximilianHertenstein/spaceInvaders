# Space-Invaders worksheet

(new file containing the student's worksheet examples — extracted from the conversation)

(Note: This file contains many java fenced code examples from the worksheet. It's used by the test coverage script.)

<!-- For brevity I include only the example snippets that appeared widely in the worksheet. -->

new V2(3, 2);
new V2(1, -5);

new V2(3, 2).plus(new V2(1, -5));
new V2(3, 4).plus(new V2(-4, -5));

Utils.getXCoordinates(List.of(new V2(3, 4), new V2(7, 9), new V2(3, 5)));
Utils.getXCoordinates(List.of(new V2(-1, 2)));

Utils.getYCoordinates(List.of(new V2(3, 4), new V2(7, 9), new V2(3, 5)));
Utils.getYCoordinates(List.of(new V2(5, 2), new V2(-2, 9)));

Utils.isOnBoard(new V2(3, 4), 10, 8);
Utils.isOnBoard(new V2(-1, 0), 10, 8);
Utils.isOnBoard(new V2(9, 7), 10, 8);

Utils.isOnBoard(List.of(new V2(9,7), new V2(10, 7)), 10, 8);
Utils.isOnBoard(List.of(new V2(-1,7), new V2(0, 7)), 10, 8);
Utils.isOnBoard(List.of(new V2(11,7), new V2(10, 7)), 10, 8);

Utils.intersect(List.of(1, 2), List.of(2, 3, 4));
Utils.intersect(List.of(new V2(1, 1)), List.of(new V2(1, 5)));

var r = new Random();
r.nextInt(5);

Utils.random(List.of(1, 2, 3));
Utils.random(List.of("a", "b", "c"));
Utils.random(List.of());

Utils.charToV2('a');
Utils.charToV2('d');
Utils.charToV2('x');

new StringWithLocation("Hi", new V2(3, 4));
new StringWithLocation("bye", new V2(5, 7));

new BasicGameObject(new V2(3, 4), "xy");
new BasicGameObject(new V2(10, 5), "abc\ndef");

bgo1.show();
bgo2.show();

bgo1.hitBox();
bgo2.hitBox();

new MovableGameObject(bgo);
new MovableGameObject(new V2(3, 4), "xy");
new MovableGameObject(new V2(10, 5), "abc\ndef");

mgo1.pos();
mgo2.show();
mgo2.hitBox();

mgo1.move(new V2(2, 1));

mgo1.touchesLeftBorder();
mgo1.touchesRightBorder(5);

new PlayerRocket(new V2(10, 5));
pr.isPlayerRocket();
pr.pos();
pr.show();
pr.hitBox();
pr.move();

new AlienRocket(new V2(3, 4));
ar.isPlayerRocket();
ar.pos();
ar.show();
ar.hitBox();
ar.move();

new Player(new V2(10, 50));
player.pos();
player.show();
player.hitBox();
player.move(new V2(1,0));
player.reactToBorder(10);
player.moveBounded(new V2(1,0), 10);
player.shoot();

new Alien(new V2(20, 4), "abc\ndef");
alien.pos();
alien.show();
alien.hitBox();
alien.move(new V2(1, 0));
alien.touchesLeftBorder();
alien.touchesRightBorder(8);
alien.isInLastLine(8);
alien.shoot();

new CountDown(5, 3);
new CountDown(7);

c.countDown();
c.finished();

new AlienSwarm(new V2(1,0), List.of(), new CountDown(5));
AlienSwarm.rowToAlienStrings(0);
AlienSwarm.createAliens();
new AlienSwarm();
swarm.noAliensLeft();
swarm.aliensAreInLastLine(7);
swarm.move();
swarm.touchesLeftBorder();
swarm.touchesRightBorder(4);
swarm.computeNextAlienDirection(4);
swarm.reactToBorder(4);
swarm.moveBounded(5);
swarm.countDownFinished();
swarm.getLowestAliens();
swarm.getShootingAlien();

LevelFactory.generateBlock(new V2(0, 0), 3, 2);
LevelFactory.generateBlocks(new V2(0, 0), 3, 2, 3);
LevelFactory.generateBlocks(100, 60);

Utils.repeat("ab", 3);

new Plasma(new V2(5, 3), 3);
plasma.pos();
plasma.show();
plasma.hitBox();
plasma.isPlayerRocket();
plasma.move();

new InvisiblePlasmaCannon(new V2(10, 15));
cannon.shoot();

new Model(100, 60);
model.restart();
model.gameWon();
model.getEndMessage();
model.move('d');
model.update('a');

TUI.print(xs);

Controller controller = new Controller(100, 60);
controller.runGame();

