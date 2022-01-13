import java.util.Random;

abstract class PitchedVoice extends Voice{
	
	protected Pattern patternOverChords;
	
	public PitchedVoice(Key key, Pattern chords, Pattern rhythm, Double density, boolean isLead, Random random) {
		super(key, chords, rhythm, density, isLead, random);
		patternOverChords = new Pattern(this.random);
	}

	@Override
	public Pattern getPatternInKey() {
		Pattern result = new Pattern(this.random);
		String[] patternArray = this.pattern.getPatternArray();
		boolean ignoreNext = false;
		for(String note : patternArray) {
			if(ignoreNext) {
				ignoreNext = false;
				result.add(note);
				continue;
			}
			try {
				int noteAsNum = Integer.parseInt(note);
				result.add(this.key.transposeToKey(noteAsNum));
			}catch (NumberFormatException e) {
				result.add(note);
				if(note.equals("/") || note.equals("*")) {
					ignoreNext = true;
				}
			}
		}
		return result;
	}
}
