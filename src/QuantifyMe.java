import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class QuantifyMe {
	static final int CHORD = 0;
	static final int ARPEGGIO = 1;
	
	static final int TOTAL_VOICE_OPTIONS = 2;
	static final double SILENT_THRESHOLD = 0.3;
	static final double LEADER_THRESHOLD = 0.9;
	static final int MAX_VOICES = 10;
	static final Random random = new Random(); 
	
	public static void main(String[] args) throws IOException {
	    System.out.println("Setting up output file...");
	    
		File file = new File("generatedPattern.txt");
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    
	    System.out.println("Generating voices...");
	    
	    Pattern chords = new Pattern("0 5 1 4"); //we need random chord progressions
	    int numberOfVoices = random.nextInt(MAX_VOICES);
	    String[] voices = new String[numberOfVoices];
	    
	    Double[] densities = new Double[numberOfVoices];
	    for(int i = 0; i < numberOfVoices; i++) {
	    	int voiceIndex = random.nextInt(numberOfVoices);
	    	while(densities[voiceIndex] != null) {
	    		voiceIndex = (voiceIndex + 1) % numberOfVoices;
	    	}
	    	densities[voiceIndex] = (Double) (i / (double) numberOfVoices);
	    }
	    
	    System.out.println("Generating patterns...");
	    
	    for(int i = 0; i < numberOfVoices; i++) {
	    	double density = densities[i];
	    	//if(density < SILENT_THRESHOLD) continue;
	    	
	    	boolean isLead = density >= LEADER_THRESHOLD;
	    	int voiceSelection = random.nextInt(TOTAL_VOICE_OPTIONS);
	    	if(voiceSelection == CHORD) {
		    	voices[i] = new Chord(chords, density, isLead);
	    	}else if(voiceSelection == ARPEGGIO) {
		    	voices[i] = new Arpeggio(chords, density, isLead);
	    	}
	    	
	    	Pattern pattern = Voice.getPatternInKey(,);

		    writer.append(pattern.inKey().toString());
		    writer.append("%");
	    }
	    
	    System.out.println("Adding note of voice order in file...");
	    
	    writer.append("Voices in order:");
	    for(int i = 0; i < numberOfVoices; i++) {
	    	writer.append("\n"+i+") "+voices[i]);
	    }
	    
	    System.out.println("Saving to File: "+file.getAbsolutePath());
	    writer.close();
	    System.out.println("Done!");
	}
	
	
	//make the voices into classes
	public static Pattern arpeggio(Pattern chords, Double density, boolean leader) throws IOException {
		Voice
		Pattern pattern = new Pattern("0 2 4 6");
	    pattern.repeatOverChords(chords);
	    return pattern;
	}
	
	public static Pattern chord(Pattern chords, Double density, boolean leader) throws IOException {
		Pattern pattern = new Pattern("[ 0 , 2 , 4 , 6 ]");
	    pattern.repeatOverChords(chords);
	    return pattern;
	}
}
