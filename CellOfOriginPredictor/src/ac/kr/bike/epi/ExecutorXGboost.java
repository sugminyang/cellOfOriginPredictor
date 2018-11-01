package ac.kr.bike.epi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ExecutorXGboost {
	private String shellScript = "./executeXGboost.sh";
	private String intermediateFile;
	
	public void makeShellScript(String rScript) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(shellScript));
//			Rscript ./rscript/xgboost_top20.R -s ./dummy/ -o ./result/
			out.append("rm " + intermediateFile + "\n");
			out.append("Rscript " + rScript + " -s ./dummy/ -o ./result/" + "\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void execute() throws IOException {
		String[] command = {"sh",shellScript};
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
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        //Wait to get exit value
        UtilFunctions.checkExcuteError(process);
	}

	public void clear() throws IOException {
		String[] command = {"rm",shellScript};
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
        while ((line = br.readLine()) != null) {
            System.out.println(line);
        }

        //Wait to get exit value
        UtilFunctions.checkExcuteError(process);
	}

	public void setIntermediateFile(String tempBedfile) {
		intermediateFile = tempBedfile;
	}

}
