package ac.kr.bike.epi;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

public class ReaderVCF {
	private String vcfFile = "";
	private String output = "./temp.bed";
	private HashMap<String,Vector<String>>	variantsByChr;

	public ReaderVCF(String dummy_vcf) {
		vcfFile = dummy_vcf;
		variantsByChr = new 	HashMap<>();
	}

	public void convert() {
		try {
			BufferedReader in = new  BufferedReader(new FileReader(vcfFile));
			String line;

			while((line=in.readLine()) != null)	
			{
				if(line.charAt(0) == '#')	continue;	//option value skip.

				//#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO	FORMAT	PCSI_0326_Ly_R-PCSI_0326_Pa_P_526
				String[] items = line.split("\t");
				String chr = items[0];
				if(chr.equalsIgnoreCase("x") || chr.equalsIgnoreCase("y"))	continue;	//sex chromosome skip.

				//all lines related somatic mutations.
				//				System.out.println(line);

				/*	
				 * convert format for bedtools library
				 * chr1	stpos	edpos	
				 */
				String variantPos = items[1];

				Vector<String> variant;
				if(variantsByChr.containsKey(chr))	{
					variant = variantsByChr.get(chr);

				}
				else	{	//creat point mutation vector.
					variant = new Vector<>();
				}

				//add chr - variants.
				variant.addElement(variantPos);
				variantsByChr.put(chr, variant);		
			}
 
			in.close();
		} catch (FileNotFoundException e) {
			System.out.println("[FileNotFoundException] location of vcf file is not valid.");
		} catch (IOException e) {
			System.out.println("[IOException] file read error.");
		}

		System.out.println("have finished converting process..");
	}

	public void saveBedFormat() {
		if(variantsByChr == null || variantsByChr.size() == 0)	{
			System.out.println("[convert error] not be exactly completed in convert processing");
			return ;
		}

		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(output));
			for(String chr : variantsByChr.keySet())	{	//by chromosome
				for(String variantPos : variantsByChr.get(chr))	{	//variants
					out.write("chr" + chr + "\t" + variantPos + "\t" + variantPos + "\n");
				}
			}
			out.close();

		} catch (IOException e) {
			System.out.println("[IOException] output path or filename is not valid");
			e.printStackTrace();
		}
		System.out.println("have finished writing bed file..");

	}

	public String getOutput() {
		return output;
	}


}
