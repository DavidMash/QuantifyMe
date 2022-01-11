import java.util.HashMap;
import java.util.Map;

public class Scale {
	
	public static final Map<Integer, Integer> major = new HashMap<>();
	
	public static Map<Integer, Integer> major() {
		if(major.isEmpty()) {
			major.put(-6, -10);
			major.put(-5, -8);
			major.put(-4, -7);
			major.put(-3, -5);
			major.put(-2, -3);
			major.put(-1, -1);
			major.put(0, 0);
			major.put(1, 2);
			major.put(2, 4);
			major.put(3, 5);
			major.put(4, 7);
			major.put(5, 9);
			major.put(6, 11);
		}
		return major;
	}
}
