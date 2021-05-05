package api;

import com.cs540.rbatch.api.RBatchProcessor;
import com.cs540.rbatch.driver.TestCallback;
import com.cs540.rbatch.driver.TestObj;
import com.cs540.rbatch.utility.RedisClientUtil;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RBatchProcessorTest {

    @Test
    @DisplayName("Adds an object to a list")
    public void addOneObject() throws NoSuchMethodException, ClassNotFoundException {
        TestObj t = new TestObj(1, "Test-1");
        RBatchProcessor.enqueue("qA1", t, new TestCallback());
        assert RedisClientUtil.getList("qA1").contains(t);
        RedisClientUtil.clearList("qA1");
    }

    @Test(expected = IllegalArgumentException.class)
    @DisplayName("Adds an object to a list that does not exist")
    public void addOneObjectToListThatDoesNotExist() throws NoSuchMethodException, ClassNotFoundException {
        TestObj t = new TestObj(1, "Test-1");
        RBatchProcessor.enqueue("qA11", t, new TestCallback());
    }
}
