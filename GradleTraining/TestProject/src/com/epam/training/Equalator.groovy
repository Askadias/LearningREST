package com.epam.training

/**
 * Created by Uladzislau_Prykhodzk on 9/12/14.
 */
class Equalator {
    public <T> int num(List<T> persons, T person, Closure cl) {
        int result = 0;
        persons.each {
            if (cl(it, person)) result++;
        }
        return result;
    }
}
