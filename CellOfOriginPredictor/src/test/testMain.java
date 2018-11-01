package test;

import java.io.IOException;

import javax.swing.text.Utilities;

import ac.kr.bike.epi.ExecuteBedtools;
import ac.kr.bike.epi.ExecutorXGboost;
import ac.kr.bike.epi.ExtractMutationLandscape;
import ac.kr.bike.epi.ReaderVCF;
import ac.kr.bike.epi.UtilFunctions;

///Library/Frameworks/R.framework/Resources 	//r dir
public class testMain {

	public static void main(String[] args) {
		System.out.println("args0: baseFile[1mb], args1: .vcfFile, args2: outputFile");
		String baseFime = args[0];
		String dummy_vcf = args[1];	//"data/dummy/ffe4bb51-e98a-41a7-a4e1-c3970386889c.broad-mutect-v3.20160222.somatic.snv_mnv.vcf";
		String output = args[2];	//"data/dummy/output/tempBed.bed";
		output = UtilFunctions.checkOutPutFile(output);
		
//		System.out.println(args[0] + "\n" + args[1]);
		//vcf to bed.
		ReaderVCF reader = new ReaderVCF(dummy_vcf);
		reader.convert();
		reader.saveBedFormat();
		 
		//bed to mutation in RF or XG.
		String tempBedfile = reader.getOutput();
		
		ExecuteBedtools executor = new ExecuteBedtools(baseFime,tempBedfile);
		
		try {
			executor.exec();
			executor.clear();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//pick mutation 1mbp.
		tempBedfile = executor.getOutput();
		System.out.println(tempBedfile);
		ExtractMutationLandscape extractor = new ExtractMutationLandscape(tempBedfile,output);
		try {
			extractor.extract();
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		//execute r code.
		ExecutorXGboost xgboost = new ExecutorXGboost();
		xgboost.setIntermediateFile(tempBedfile);
		xgboost.makeShellScript();
		try {
			xgboost.execute();
			xgboost.clear();
		} catch (IOException e) {
			System.out.println("[error] xgboost execution error!!");
			e.printStackTrace();
		}
	}

}
