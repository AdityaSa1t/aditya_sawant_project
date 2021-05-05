package utility;

import com.cs540.rbatch.utility.ConfigUtil;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class ConfigUtilTest {

    @Test
    public void checkListConfig(){
        Map<String, Object> config = ConfigUtil.getListConfig();
        assert config.size()==3;
        assert config.containsKey("qA1");
        assert config.containsKey("qB1");
        assert config.containsKey("qC1");
    }

    @Test
    public void checkListSize(){
        assert ConfigUtil.getListSize("qA1") == 2;
    }

    @Test
    public void checkCallback(){
        assert ConfigUtil.getListCallback("qA1").equals("print");
    }

    @Test
    public void checkTypeOfObjectsInList(){
        assert ConfigUtil.getListClazz("qA1").equals("com.cs540.rbatch.driver.TestObj");
    }
}
