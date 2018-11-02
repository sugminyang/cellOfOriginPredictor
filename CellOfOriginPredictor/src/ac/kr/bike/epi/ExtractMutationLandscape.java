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
        String outputName = UtilFunctions.getOutputName(output);
        out.append(outputName + "\n");
        while ((line = br.readLine()) != null) {
            out.append(line+"\n");
        }
        out.close();

        //Wait to get exit value
        UtilFunctions.checkExcuteError(process);
        
        System.out.println(this.getClass() + "..done...");
	}

}
