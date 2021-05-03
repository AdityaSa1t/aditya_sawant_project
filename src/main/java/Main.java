import com.cs540.rbatch.api.RBatchProcessor;

import java.lang.reflect.InvocationTargetException;

public class Main {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        TestObj t1 = new TestObj(1, "test");
        TestObj t2 = new TestObj(2, "test");
        TestObj t3 = new TestObj(3, "test");
        TestObj t4 = new TestObj(4, "test");
        TestCallback t = new TestCallback();
        String qName = "q10";
        RBatchProcessor.enqueue(qName, t1, t);
        RBatchProcessor.enqueue(qName, t2, t);
        RBatchProcessor.enqueue(qName, t3, t);
        RBatchProcessor.enqueue(qName, t4, t);
    }
}
