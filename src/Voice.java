import java.util.List;
import java.util.Random;

public abstract class Voice {
	protected String voiceName = "Unlabeled Voice";
	protected Random random;
	protected VoicedRhythm vr;
	
	public Voice(Random random) {
		this.random = random;
	}
	
	public void generateVoicedRhythm(List<String> chords, Key key, Key nextKey, int switchPoint, Rhythm rhythm, double voiceDensity, boolean beatRepeat, boolean halfTime) {
		this.vr = new VoicedRhythm(this, rhythm, voiceDensity, true, beatRepeat, halfTime); //TODO: randomize the downbeat boolean
	}
	
	abstract public Pattern setOverChords(List<String> chords, Key key, Key nextKey, int switchPoint, boolean silenced);

	abstract protected double[] getChances();

	abstract public String next();
	
	public String toString() {
		return this.voiceName;
	}

	protected abstract Voice copy();
}
