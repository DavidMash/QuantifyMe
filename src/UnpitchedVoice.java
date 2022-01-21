import java.util.List;

abstract class UnpitchedVoice extends Voice{
	@Override
	protected Pattern setOverChords(List<Integer> chords, VoicedRhythm voicedRhythm) {
		return voicedRhythm.getOverChordsNoTransposing(chords);
	}
}
