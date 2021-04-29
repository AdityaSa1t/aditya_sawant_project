import java.io.Serializable;
import java.util.List;

public class TestObj implements Serializable {

    private int id;
    private String message;

    public TestObj(int id, String msg){
        this.id = id;
        this.message = msg;
    }

    public void print(List<TestObj> tests){
        System.out.println(tests);
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", message='" + message + '\'' +
                '}';
    }
}
