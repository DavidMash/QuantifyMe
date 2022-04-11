import java.util.List;
import java.util.Random;

abstract class PitchedVoice extends Voice{
	
	public PitchedVoice(Random random) {
		super(random);
	}
	
	@Override
	public Pattern setOverChords(List<String> chords, Key key, Key nextKey, int switchPoint, boolean silenced) {
		return this.vr.getOverChords(chords, key, nextKey, switchPoint, silenced);
	}
}
