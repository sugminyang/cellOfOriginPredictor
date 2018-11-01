package ac.kr.bike.epi;

public class UtilFunctions {
	public static String checkOutPutFile(String file)	{
		if(file.contains("."))	{
			int index = file.lastIndexOf(".");
			
			return file.substring(0, index) + ".csv";
		} 
		else	{
			return file + ".csv";
		}
	}
	
	public static void checkExcuteError(Process process)	{
		//Wait to get exit value
        int exitValue = -999;
        try {
            exitValue = process.waitFor();
            
        } catch (InterruptedException e) {
        	System.out.println("\n\nExit Value is " + exitValue);
            e.printStackTrace();
        }	
	}
}
