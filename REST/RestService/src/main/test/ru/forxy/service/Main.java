package ru.forxy.service;

class A {
    public int d;
    public String s;
}

public class Main {

    public static void main(String[] args) {
        A a = new A();
        a.s = "aaa";
        a.d = 1;
        setS(a);
        System.out.println(a.s + a.d);
    }

    private static void setS(A a) {
        a = new A();
        a.d = 2;
        a.s = "bbb";
    }
}
