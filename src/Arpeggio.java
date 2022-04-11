import java.util.List;
import java.util.Random;

public class Arpeggio extends PitchedVoice{
	private static double[] chances;
	
	private static final String[][] arps = {
			{"0", "2"},
			{"0", "5"},
			{"0", "2", "4"},
			{"0", "2", "4", "6"},
			{"0", "2", "4", "7"},
	};
	private static final int UP = 0;
	private static final int UP_DOWN = 1;
	private static final int DOWN = 2;
	private static final int DOWN_UP = 3;
	private static final int OPTIONS = 4;
	
	private int arpSelection;
	private int arpMovement;
	private int curr = -1;
	private boolean direction = false;
	
	public Arpeggio(Random random) {
		super(random);
		this.voiceName = "Arpeggio";
		this.arpSelection = this.random.nextInt(arps.length);
		this.arpMovement = this.random.nextInt(OPTIONS);
		
	}

	@Override
	protected double[] getChances() {
		if(chances == null) {
			chances = new double[11];
			chances[VoicedRhythm.FILL] = 0.3;
			chances[VoicedRhythm.DOWNBEAT] = 0.1;
			chances[VoicedRhythm.EVERY_BEAT] = 0.2;
			chances[VoicedRhythm.EVERY_OTHER_BEAT] = 0.2;
			chances[VoicedRhythm.EVERY_SECOND_BEAT] = 0.3;
			chances[VoicedRhythm.EVERY_NOTE] = 0.85;
			chances[VoicedRhythm.EVERY_OTHER_NOTE] = 0.2;
			chances[VoicedRhythm.OFFBEAT] = 0.6;
			chances[VoicedRhythm.PEDAL] = 0.4;
			chances[VoicedRhythm.P_STOP_BEAT] = 0;
			chances[VoicedRhythm.P_STOP_HALF] = 0;
		}
		return chances;
	}
	
	@Override
	public String next() {
		if (curr < 0) {
			if (arpMovement < 2) {
				this.curr = 0;
				this.direction = true;
			} else {
				this.curr = arps[arpSelection].length - 1;
				this.direction = false;
			}
		} else {
			if (arpMovement == UP) {
				curr = (curr + 1) % arps[arpSelection].length;
			} else if (arpMovement == DOWN) {
				curr = (curr == 0)? arps[arpSelection].length - 1: curr - 1;
			} else if (arpMovement == UP_DOWN || arpMovement == DOWN_UP) {
				curr = (direction)? curr + 1: curr - 1;
			}
			if (curr >= arps[arpSelection].length - 1 || curr <= 0) {
				direction = !direction;
			}
		}
		return arps[arpSelection][curr];
	}

	@Override
	protected Voice copy() {
		return new Arpeggio(this.random);
	}
}
