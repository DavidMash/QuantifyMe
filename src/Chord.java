import java.util.Random;

public class Chord extends PitchedVoice{

	public Chord(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		super(key, chords, rhythm, density, isLead, random);
	}

	@Override
	public void generatePattern() {
		// TODO: add more patterns
		// TODO: make this respect the rhythm!
		this.pattern = new Pattern("[ 0 , 2 , 4 , 6 ]", random);
	}

	@Override
	public String toString() {
		return "Chord";
	}

}
