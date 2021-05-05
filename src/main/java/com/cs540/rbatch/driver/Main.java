package com.cs540.rbatch.driver;

import com.cs540.rbatch.api.RBatchProcessor;

public class Main {

    public static void main(String[] args){
        TestObj t1 = new TestObj(1, "test");
        TestObj t2 = new TestObj(2, "test");
        TestObj t3 = new TestObj(3, "test");
        TestObj t4 = new TestObj(4, "test");
        TestCallback t = new TestCallback();
        String qName1 = "qA1";
        String qName2 = "qB1";
        try {
            RBatchProcessor.enqueue(qName1, t1, t);
            RBatchProcessor.enqueue(qName2, t2, t);
            RBatchProcessor.enqueue(qName1, t3, t);
            RBatchProcessor.enqueue(qName2, t4, t);
            RBatchProcessor.enqueue(qName1, t1, t);
            RBatchProcessor.enqueue(qName2, t2, t);
            RBatchProcessor.enqueue(qName1, t3, t);
            RBatchProcessor.enqueue(qName2, t4, t);
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        }

    }
}
