import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class QuantifyMe {
	static final Random random = new Random(); 
	
	public static BufferedReader keyboard;
	public static boolean debug = false;
	public static boolean choose = false;
	
	public static void main(String[] args) throws IOException {
		if(args.length > 0 && args[0].equals("debug")) { 
			debug = true;
			System.out.println("Starting in debug mode...");
		}
		
		if(args.length > 1 && args[1].equals("choose")) {
			choose = true;
			System.out.println("with choosing on...");
		}
		
		keyboard = new BufferedReader (new InputStreamReader(System.in));
		
	    System.out.println("Setting up output file...");
	    
		File file = new File("generatedPattern.txt");
	    BufferedWriter writer = new BufferedWriter(new FileWriter(file));
	    
	    Arrangement arrangement = new Arrangement(random);
	    writer.append(arrangement.toString());
	    
	    System.out.println("Adding note of voice order in file...");
	    
	    writer.append("\nVoices in order:");
	    int voiceNum = 1;
	    for (Voice voice : arrangement.getVoices()) {
	    	writer.append("\n" + (voiceNum++) + ") " + voice);
	    }
	    writer.append("\nin the key of "+arrangement.getKey()+".");
	    
	    System.out.println("Saving to File: "+file.getAbsolutePath());
	    writer.close();
	    System.out.println("Done!");
	}
	
	
}
