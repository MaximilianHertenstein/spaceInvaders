package org.example;

public record CountDown(int start,
    int current){

    public CountDown(int start) {
        this(start, start);
    }


    public CountDown countDown(){
        if (current > 0) {
            return new CountDown(start, current -1);
        }
        else {
            return new CountDown(start);
        }

    }

    public boolean finished(){
        return this.current == 0;
    }
}
