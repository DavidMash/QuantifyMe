import java.util.Random;

public interface Voice {
	public void generatePattern(Random random);
	
	public Pattern getPatternInKey(int key, int mode);
}
