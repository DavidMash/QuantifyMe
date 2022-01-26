public class Chord{
	static private final int[][] CHORDS = {
		{0, 4, 7},
		{0, 3, 7},
		{0, 4, 7, 10},
		{0, 4, 7, 11},
		{0, 3, 7, 10}
	};
	static private final int MAJOR = 0;
	static private final int MINOR = 1;
	static private final int DOMINANT_7TH = 2;
	static private final int MAJOR_7TH = 3;
	static private final int MINOR_7TH = 4;
	
	private int note;
	private int chord;
	
	public String toString() {
		String chordSymbol = "";
		if (chord == MAJOR) {
			
		} else if (chord == MINOR) {
			
		} else if (chord == DOMINANT_7TH) {
			
		} else if (chord == MAJOR_7TH) {
			
		}  else if (chord == MINOR_7TH) {
			
		}
		return (note + chordSymbol);
	}
}
