package test;

import java.io.IOException;

import ac.kr.bike.epi.ReaderVCF;

public class testMain {

	public static void main(String[] args) {
		String dummy_vcf = args[0];	//"data/dummy/ffe4bb51-e98a-41a7-a4e1-c3970386889c.broad-mutect-v3.20160222.somatic.snv_mnv.vcf";
		String output = args[1];	//"data/dummy/output/tempBed.bed";
		System.out.println(args[0] + "\n" + args[1]);
		//vcf to bed.
		ReaderVCF reader = new ReaderVCF(dummy_vcf);
		reader.convert();
		reader.saveBedFormat();
		
		//bed to mutation in RF or XG.
//		String tempBedfile = reader.getOutput();
//		ExecuteBedtools executor = new ExecuteBedtools(tempBedfile);
//		executor.setoutput(output);
//		try {
//			executor.exec();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
