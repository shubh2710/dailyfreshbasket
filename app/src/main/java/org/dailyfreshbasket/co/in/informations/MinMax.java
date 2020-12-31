package org.dailyfreshbasket.co.in.informations;

import java.io.Serializable;

/**
 * Created by shubham on 3/29/2017.
 */
public class MinMax implements Serializable {
    private int min;
    private int max;
    private int listNumber;
    private boolean isapply;
    public MinMax(int min,int max,int listNumber,boolean isapply){
        this.min=min;
        this.max=max;
        this.listNumber=listNumber;
        this.isapply=isapply;
    }
    public int getListNumber(){
        return listNumber;
    }
    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }
    public boolean getisapply(){
        return isapply;
    }

}