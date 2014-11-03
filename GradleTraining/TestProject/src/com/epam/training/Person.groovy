package com.epam.training

import groovy.transform.ToString

/**
 * Created by Uladzislau_Prykhodzk on 9/12/14.
 */
@ToString
class Person {
    String name
    int age

    boolean asBoolean() {
        age < 120
    }
}
