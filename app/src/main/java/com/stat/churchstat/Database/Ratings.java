package com.stat.churchstat.Database;

/**
 * Created by sanniAdewale on 10/02/2017.
 */

public class Ratings {

    public int id;
    public String cat, star, comment, improves, date;

    public Ratings(int id, String cat, String star, String comment, String improves, String date) {
        this.id = id;
        this.cat = cat;
        this.star = star;
        this.comment = comment;
        this.improves = improves;
        this.date = date;
    }

    public Ratings(){

    }
}
