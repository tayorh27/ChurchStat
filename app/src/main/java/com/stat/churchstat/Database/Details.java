package com.stat.churchstat.Database;

/**
 * Created by sanniAdewale on 03/01/2017.
 */

public class Details {

    public int id;
    public String no_of_males,no_of_females,no_of_adult,no_of_children,date,category;

    public Details(int id, String no_of_males, String no_of_females, String no_of_adult, String no_of_children,String date, String category){
        this.id = id;
        this.no_of_adult = no_of_adult;
        this.no_of_children = no_of_children;
        this.no_of_females = no_of_females;
        this.no_of_males = no_of_males;
        this.date = date;
        this.category = category;
    }

    public Details(){}
}
