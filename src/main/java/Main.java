import com.cs514.rbatch.utility.RedisClientUtil;
import org.redisson.api.RList;
import org.redisson.api.RedissonClient;

public class Main {

    public static void main(String[] args) {
        RedissonClient redissonClient = RedisClientUtil.redissonClient;
        RList<Object> q1 = redissonClient.getList("q1");
        q1.add("Test");
    }
}
