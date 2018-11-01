package ac.kr.bike.epi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ExtractMutationLandscape {
	private String output;
	private String tempFile;
	
	public ExtractMutationLandscape(String tempFile, String output) {
		this.output = output;
		this.tempFile = tempFile; 
	} 

	public void extract() throws IOException {
//		awk '{print $5}' test.bed
		// TODO SORTING !!!!!! DICTIONARY.. CHECK CHECK!!
//		sorting dictionary . chr1 -> c11 ....chr9
		String[] command = {"awk","{print $5}",tempFile};
        ProcessBuilder probuilder = new ProcessBuilder( command );
        
        probuilder.directory(new File("./"));
		Process process = probuilder.start();
		
		//Read out dir output
        InputStream is = process.getInputStream();
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        String line;
        System.out.printf("Output of running %s is:\n",
                Arrays.toString(command));
        BufferedWriter out = new BufferedWriter(new FileWriter(output));
        out.append("mutation\n");
        while ((line = br.readLine()) != null) {
            out.append(line+"\n");
        }
        out.close();

        //Wait to get exit value
        int exitValue = -999;
        try {
            exitValue = process.waitFor();
            
        } catch (InterruptedException e) {
        	System.out.println("\n\nExit Value is " + exitValue);
            e.printStackTrace();
        }
        
        System.out.println(this.getClass() + "..done...");
	}

}
