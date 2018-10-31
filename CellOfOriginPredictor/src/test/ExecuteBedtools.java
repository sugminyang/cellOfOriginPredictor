package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class ExecuteBedtools {

	private String bedfile;
	private String output;
	
	public ExecuteBedtools(String bedfile) {
		this.bedfile = bedfile;
	}

	public void exec() throws IOException {
		//bedtools intersect -a 1mb_paper.bed -b with_chr.vcf -c > DO22417.bed
		
		String[] command = {"bedtools", "intersect", "-a", "data/1mb_paper.bed" ,"-b",bedfile,"-c", ">",output};
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
        try {
            int exitValue = process.waitFor();
            System.out.println("\n\nExit Value is " + exitValue);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
	}

	public void setoutput(String output) {
		this.output = output;
	}

	
}
