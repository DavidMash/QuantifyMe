import java.util.List;

abstract class UnpitchedVoice extends Voice{
	@Override
	protected Pattern setOverChords(List<Integer> chords, Key key, Key nextKey, int switchPoint, VoicedRhythm voicedRhythm) {
		return voicedRhythm.getOverChordsNoTransposing(chords);
	}
}
