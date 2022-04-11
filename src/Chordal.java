import java.util.Random;

public class Chordal extends PitchedVoice{
	private static final double RANDOMIZE_CHORD_CHANCE = 0.1;
	private static double[] chances;
	private static String[] chordStyles = {"[ 0 ]",
											"[ 0 , 2 ]",
											"[ 0 , 4 ]",
											"[ 0 , 2 , 4 ]",
											"[ 0 , 2 , 4 , 6 ]",
											"[ 0 , 2 , 4 , 6 , 8 ]",
											"[ 0 , 2 , 6 , 8 ]",
											"[ 0 , 2 , 6 ]",
											"[ -3 , 0 ]"
	};
	
	private String chord;
	
	public Chordal(Random random) {
		super(random);
		this.random = random;
		this.voiceName = "Chord";
		this.chord = chordStyles[this.random.nextInt(chordStyles.length)];
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[11];
			chances[VoicedRhythm.FILL] = 0.3;
			chances[VoicedRhythm.DOWNBEAT] = 0.5;
			chances[VoicedRhythm.EVERY_BEAT] = 0.3;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0.5;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0.1;
			chances[VoicedRhythm.EVERY_NOTE] = 0;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0;
			chances[VoicedRhythm.OFFBEAT] = 0.4;
			chances[VoicedRhythm.PEDAL] = 0.7;
			chances[VoicedRhythm.P_STOP_BEAT] = 0.1;
			chances[VoicedRhythm.P_STOP_HALF] = 0.3;
		}
		return chances;
	}


	@Override
	public String next() {
		if (this.random.nextDouble() < RANDOMIZE_CHORD_CHANCE) {
			return chordStyles[this.random.nextInt(chordStyles.length)];
		}
		return chord;
	}

	@Override
	protected Voice copy() {
		return new Chordal(this.random);
	}
}
