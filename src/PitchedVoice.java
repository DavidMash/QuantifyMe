import java.util.Random;

abstract class PitchedVoice extends Voice{
	
	public PitchedVoice(Key key, Pattern chords, Double density, boolean isLead, Random random) {
		super(key, chords, density, isLead, random);
	}
	
	public void repeatOverChords() {
		
	}

	@Override
	void setPatternOverChords() {
		String[] roots = this.chords.toString().split(" ");
		String[] oldPattern = this.pattern.toString().split(" ");
		for(String root : roots) {
			int rootAsNum = Integer.parseInt(root);
			boolean ignoreNext = false;
			for(String note : oldPattern) {
				if(ignoreNext) {
					ignoreNext = false;
					this.patternOverChords.add(note);
					continue;
				}
				try {
					int noteAsNum = Integer.parseInt(note);
					this.patternOverChords.add(rootAsNum+noteAsNum);
				}catch (NumberFormatException e) {
					this.patternOverChords.add(note);
					if(note.equals("/") || note.equals("*")) {
						ignoreNext = true;
					}
				}
			}
		}
		this.patternOverChords.cutTimeBy(roots.length);
	}

	@Override
	public Pattern getPatternInKey() {
		Pattern result = new Pattern();
		String[] patternArray = this.patternOverChords.toString().split(" ");
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
