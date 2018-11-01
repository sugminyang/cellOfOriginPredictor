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
}
