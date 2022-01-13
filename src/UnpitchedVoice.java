import java.util.Random;

abstract class UnpitchedVoice extends Voice{
	
	public UnpitchedVoice(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		super(key, chords, rhythm, density, isLead, random);
	}

	@Override
	public Pattern getPatternInKey() {
		return pattern;
	}
}
