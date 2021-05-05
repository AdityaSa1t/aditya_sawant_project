package api;

import com.cs540.rbatch.api.RBatchProcessor;
import com.cs540.rbatch.driver.TestCallback;
import com.cs540.rbatch.driver.TestObj;
import com.cs540.rbatch.utility.RedisClientUtil;
import org.junit.jupiter.api.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RBatchProcessorTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStreamCaptor));
    }

    @Test
    public void addOneObject() throws NoSuchMethodException, ClassNotFoundException {
        TestObj t = new TestObj(1, "Test-1");
        RBatchProcessor.enqueue("qA1", t, new TestCallback());
        assert RedisClientUtil.getList("qA1").contains(t);
        RedisClientUtil.clearList("qA1");
    }

    @Test
    public void checkIfCallbackIsCalled() throws NoSuchMethodException, ClassNotFoundException {
        TestObj t1 = new TestObj(1, "Test-1");
        TestObj t2 = new TestObj(2, "Test-2");
        RBatchProcessor.enqueue("qA1", t1, new TestCallback());
        RBatchProcessor.enqueue("qA1", t2, new TestCallback());
        Assertions.assertEquals("[Test{id=1, message='Test-1'}, Test{id=2, message='Test-2'}]",
                outputStreamCaptor.toString().trim());
    }

    @Test
    public void addObjectToListThatDoesNotExist() {
        Assertions.assertThrows(IllegalArgumentException.class, ()->{
            TestObj t = new TestObj(1, "Test-1");
            RBatchProcessor.enqueue("qA11", t, new TestCallback());
        });
    }

    @Test
    public void addObjectToListWithWrongCallback() {
        Assertions.assertThrows(NoSuchMethodException.class, ()->{
            TestObj t = new TestObj(1, "Test-1");
            RBatchProcessor.enqueue("qC1", t, new TestCallback());
        });
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

}
