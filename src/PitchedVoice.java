import java.util.Random;

abstract class PitchedVoice extends Voice{
	
	protected Pattern patternOverChords;
	
	public PitchedVoice(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		super(key, chords, rhythm, density, isLead, random);
		patternOverChords = new Pattern(this.random);
	}

	@Override
	public Pattern getPatternInKey() {
		return this.pattern.getPatternInKey(chords, key);
	}
}
