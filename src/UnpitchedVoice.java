import java.util.List;
import java.util.Random;

abstract class UnpitchedVoice extends Voice{
	
	public UnpitchedVoice(Random random) {
		super(random);
	}
	
	@Override
	public Pattern setOverChords(List<String> chords, Key key, Key nextKey, int switchPoint, boolean silenced) {
		return this.vr.getOverChordsNoTransposing(chords, silenced);
	}
}
