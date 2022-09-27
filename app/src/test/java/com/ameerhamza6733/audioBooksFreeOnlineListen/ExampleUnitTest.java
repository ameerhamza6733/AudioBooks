package com.ameerhamza6733.audioBooksFreeOnlineListen;

import org.junit.Test;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void main() throws Exception {
        new Hamza();
    }
}

class Name {

    Name() {
        System.out.println("What is my name?");
    }
}

class Hamza extends Name {

    {
        System.out.println("My Name is Hamza rafique");
    }
    static {
        System.out.println("I am looking for job");
    }
    public Hamza() {
        System.out.println("My Name is Hamza");
    }
}
