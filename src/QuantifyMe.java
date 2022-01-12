import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuantifyMe {
	static final int KICK = 0;
	static final int SNARE = 1;
	static final int HIHAT = 2;
	static final int CHORD = 3;
	static final int ARPEGGIO = 4;
	static final int BASS = 5;
	static final int SUB_BASS = 6;
	
	
	static final int TOTAL_VOICE_OPTIONS = 7;
	static final double SILENT_THRESHOLD = 0.3;
	static final double LEADER_THRESHOLD = 0.9;
	static final int MAX_VOICES = 7;
	static final Random random = new Random(); 
	
	public static void main(String[] args) throws IOException {
		boolean debug = false;
		if(args.length > 0 && args[0].equals("debug")) debug = true;
		if(debug) System.out.println("Starting in debug mode...");
	    System.out.println("Setting up output file...");
	    
		File file = new File("generatedPattern.txt");
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    
	    System.out.println("Generating chords, rhythms, and voices...");
	    
	    Pattern chords = new Pattern("0 5 1 4", random); //we need random chord progressions
	    int numberOfVoices = random.nextInt(MAX_VOICES);
	    Voice[] voices = new Voice[numberOfVoices];
	    
	    int numberOfRhythms = random.nextInt(MAX_VOICES) + 1;
	    List<Pattern> rhythms = new ArrayList<>(numberOfRhythms);
	    for(int i = 0; i < numberOfRhythms; i++) {
	    	Pattern rhythm = new Pattern(random);
	    	rhythm.generateRhythm();
	    	if(debug) System.out.println("Generated Rhythm: "+rhythm);
	    	rhythms.add(rhythm);
	    }
	    
	    //Key key = new Key(random);
	    Key key = new Key(0, Key.MODE.MAJOR, random);
	    
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
	    	if(voiceSelection == KICK) {
		    	voices[i] = new Chord(key, chords, chords, density, isLead, random);
	    	} else if(voiceSelection == SNARE) {
		    	voices[i] = new Arpeggio(key, chords, chords, density, isLead, random);
	    	} else if(voiceSelection == HIHAT) {
		    	voices[i] = new Arpeggio(key, chords, chords, density, isLead, random);
	    	} else if(voiceSelection == CHORD) {
		    	voices[i] = new Chord(key, chords, chords, density, isLead, random);
	    	} else if(voiceSelection == ARPEGGIO) {
		    	voices[i] = new Arpeggio(key, chords, chords, density, isLead, random);
	    	} else if(voiceSelection == BASS) {
		    	voices[i] = new Arpeggio(key, chords, chords, density, isLead, random);
	    	} else {
	    		voices[i] = new Chord(key, chords, chords, density, isLead, random);
	    	}
	    	
	    	voices[i].generatePattern();
	    	Pattern pattern = voices[i].getPatternInKey();

		    writer.append(pattern.toString());
		    writer.append("%");
	    }
	    
	    System.out.println("Adding note of voice order in file...");
	    
	    writer.append("\nVoices in order:");
	    for(int i = 0; i < numberOfVoices; i++) {
	    	writer.append("\n"+i+") "+voices[i]);
	    }
	    writer.append("\nin the key of "+key+".");
	    
	    System.out.println("Saving to File: "+file.getAbsolutePath());
	    writer.close();
	    System.out.println("Done!");
	}
}
