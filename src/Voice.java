import java.util.List;

public abstract class Voice {
	protected String voiceName = "Unlabeled Voice";
	
	public Pattern generateVoicedRhythm(List<Integer> chords, Key key, Key nextKey, int switchPoint, Rhythm rhythm, double voiceDensity) {
		return this.setOverChords(chords, key, nextKey, switchPoint, new VoicedRhythm(this, rhythm, voiceDensity, true)); //TODO: randomize the downbeat boolean
	}
	
	protected abstract Pattern setOverChords(List<Integer> chords, Key key, Key nextKey, int switchPoint, VoicedRhythm voicedRhythm);

	protected abstract double[] getChances();

	abstract public String next();
	
	public String toString() {
		return this.voiceName;
	}
}
