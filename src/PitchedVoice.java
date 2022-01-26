import java.util.List;

abstract class PitchedVoice extends Voice{
	@Override
	protected Pattern setOverChords(List<Integer> chords, Key key, Key nextKey, int switchPoint, VoicedRhythm voicedRhythm) {
		return voicedRhythm.getOverChords(chords, key, nextKey, switchPoint);
	}
}
