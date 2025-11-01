package org.example;

public class CountDown {
    final int start;
    int current;

    public CountDown(int start) {
        this.start = start;
        this.current = start;
    }

    public void reset(){
        this.current = start;
    }

    public void countDown(){
        this.current -= 1;
    }

    public boolean finished(){
        return this.current == 0;
    }
}
