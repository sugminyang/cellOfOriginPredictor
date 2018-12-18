package test;

import java.io.IOException;

import ac.kr.bike.epi.EpimarkerRank;

public class EpimarkerRankTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("[LENGTH:" +args.length +"]"+ args[0] + ", " + args[1] +", " + args[2] + ", " + args[3]);
		if(args.length != 4)	{	
			System.out.println("ERROR: Invalid parameters.");
			System.exit(1);
		}
//		String path = "C:\\Users\\dean\\Downloads\\[1] project\\2017\\BMC_journal\\xgboost\\result\\top20\\";
		String path = args[0];
		String mutation = args[1];
		int top = Integer.parseInt(args[2]);
		String initJar = args[3];
		
		EpimarkerRank rank = new EpimarkerRank();
		
		try {
//			for(String mutation: rank.recursiveFileRead(path))	{
//				System.out.println("=======================" + mutation + "==========================");
				rank.setMutation(mutation);
				
				if(top == 0)	{	//last item
					rank.top1(path,top);
				}
				else	{
					rank.readFile(path,top,initJar);
					rank.write(path, top,initJar);	//file and top x(number) e.g)top 20
				}
				
				//System.out.println(rank.avgEvalLog(path));
				rank.clearVector();
//			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
