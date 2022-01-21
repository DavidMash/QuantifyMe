import java.util.List;

abstract class PitchedVoice extends Voice{
	@Override
	protected Pattern setOverChords(List<Integer> chords, VoicedRhythm voicedRhythm) {
		return voicedRhythm.getOverChords(chords);
	}
}
