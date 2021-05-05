package api;

import com.cs540.rbatch.api.RBatchProcessor;
import com.cs540.rbatch.driver.TestCallback;
import com.cs540.rbatch.driver.TestObj;
import com.cs540.rbatch.utility.RedisClientUtil;
import org.junit.Test;


public class RBatchProcessorTest {

    @Test
    public void addOneObject() throws NoSuchMethodException, ClassNotFoundException {
        TestObj t = new TestObj(1, "Test-1");
        RBatchProcessor.enqueue("qA1", t, new TestCallback());
        assert RedisClientUtil.getList("qA1").contains(t);
        RedisClientUtil.clearList("qA1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void addOneObjectToListThatDoesNotExist() throws NoSuchMethodException, ClassNotFoundException {
        TestObj t = new TestObj(1, "Test-1");
        RBatchProcessor.enqueue("qA11", t, new TestCallback());
    }
}
