package utility;

import com.cs540.rbatch.utility.RedisClientUtil;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RedisClientUtilTest {
    private static String random = "someRandomList";
    private static String nonExistentList = "q1w2e3r4t5y6u7i8o90op";

    @Test
    @Order(1)
    public void createRedisList(){
        RedisClientUtil.getList(random).add("test");
        assert RedisClientUtil.keyExists(random);
    }

    @Test
    @Order(2)
    public void validateListSearch(){
        assert !RedisClientUtil.keyExists(nonExistentList) && RedisClientUtil.keyExists(random);
    }

    @Test
    @Order(2)
    public void verifyCount(){
        assert RedisClientUtil.getListSize(nonExistentList) == 0 && RedisClientUtil.getListSize(random) == 1;
    }

    @Test
    @Order(3)
    public void clearList(){
        RedisClientUtil.clearList(random);
        assert RedisClientUtil.getList(random).size() == 0;
    }
}
