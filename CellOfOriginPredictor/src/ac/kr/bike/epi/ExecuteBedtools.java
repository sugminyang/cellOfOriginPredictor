package ac.kr.bike.epi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ExecuteBedtools {

	private String bedfile;
	private String baseFile;
	private String intermediateFile = "./resultOfBedtools.bed";
	private String shellScript = "./execBedtools.sh";
	
	public ExecuteBedtools(String baseFile, String bedfile) {
		this.baseFile = baseFile;
		this.bedfile = bedfile;
	}

	public void exec() throws IOException {
		//bedtools intersect -a 1mb_paper.bed -b with_chr.vcf -c > DO22417.bed
		makeShellScript(baseFile,bedfile);
		
		
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

	private void makeShellScript(String baseFile, String tempFile) {
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(shellScript));
			out.append("bedtools intersect -a " + baseFile +" -b " + tempFile + " -c > " + intermediateFile + "\n");
			out.append("rm " + tempFile +"\n");
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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

	public String getOutput() {
		return intermediateFile;
	}

	
}
