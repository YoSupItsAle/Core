package org.aledev.core;

public class Int {

    private int amount;

    public void increaseAmount(int amount){
        this.amount+=amount;
    }

    public void decreaseAmount(int amount){
        this.amount-=amount;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setAmount(int amount){
        this.amount = amount;
    }


}
