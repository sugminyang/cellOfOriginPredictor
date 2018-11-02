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

	public static String getOutputName(String output) {
		if(output.contains("/"))	{
			int ind_slash = output.lastIndexOf("/");
			int ind_dot = output.lastIndexOf(".");
			
			return output.substring(ind_slash+1, ind_dot);
		}
		else	{
			if(output.indexOf(".") != 0) {
				int ind_dot = output.lastIndexOf(".");
				return output.substring(0, ind_dot);
			}
			else	{
				int ind_dot = output.lastIndexOf(".");
				return output.substring(1, ind_dot);
			}
		}
	}
}
