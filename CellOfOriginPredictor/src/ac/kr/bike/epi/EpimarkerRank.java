package ac.kr.bike.epi;



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class EpimarkerRank {
	public Integer threshold = 100000;
	public Vector<Map<String,Integer>> vec = new Vector<>();
	public String mutation;
	

	public String getMutation() {
		return mutation;
	}

	public void setMutation(String mutation) {
		this.mutation = mutation;
	}

	public void setThreshold(int val)	{
		threshold = val;
	}
	
	public void clearVector()	{
		vec.clear();
	}
	
	public Vector<Map<String,Integer>> readFile(String fileName,int top,String init) throws IOException	{
		BufferedReader br = null;
		try
		{
			if(init.equalsIgnoreCase("t"))	{
				br = new BufferedReader(new FileReader(fileName+"rank_"+mutation+".txt"));
			}
			else	{
				br = new BufferedReader(new FileReader(fileName+"rank_"+mutation+"_"+(top+1)+".txt"));
			}
		}
		catch(FileNotFoundException e)	{
			br = new BufferedReader(new FileReader(fileName+"rank_"+mutation+".txt"));
		}
		
		String line = "";
		boolean flag = true;
		int a= 0;
		while((line=br.readLine())!=null)	{
			String[] RankArray = line.trim().split("  ");
//			System.out.println(RankArray.length);
//			System.out.println(line);
			
			//vector has or hasn't item in RankArray element.
			for(int i =0; i < RankArray.length;i++)	{
				Map<String,Integer> item = new HashMap<String,Integer>();
				String key = RankArray[i];
				item.put(key, new Integer(i+1));
				
				if(flag)	{	//initial items setting(once).
					vec.add(item);
				}
				else	{
					if(!isContains(key))	{	//false means newItem.
						item.replace(key, new Integer(item.get(key) + threshold));
						vec.add(item);
					}
					else	{	//exist item.
						addRank(item);
					}
				}
			}
			
//			System.out.println(checkUsageItem(RankArray).size() + ": \n" + checkUsageItem(RankArray));
			//not used item in vector
			for(String notUsedItem: checkUsageItem(RankArray))	{
				updateNotUsedItem(notUsedItem);
			}
			
			flag = false;
			System.out.println();
		}
		
//		System.out.println(vec.size() + "\n" + vec);
		
		br.close();
		
		return vec;
	}
	
	private void updateNotUsedItem(String notUsedItem) {
		// TODO Auto-generated method stub
		for(Map<String,Integer> item : vec)	{
			if(item.containsKey(notUsedItem))	{
				int newVal = item.get(notUsedItem) + threshold;
				vec.remove(item);
				int index = sortIndex(notUsedItem,newVal);	
				item.replace(notUsedItem, newVal);
				
				vec.insertElementAt(item, index);
				break;
			}
		}
	}

	private Vector<String> checkUsageItem(String[] rankArray) {
		// TODO Auto-generated method stub
		Vector<String> notUsedItem = new Vector<>();
		for(Map<String,Integer> item : vec)	{
			boolean used = false;
			
			for(String key : rankArray)	{
				if(item.containsKey(key))	{
					used = true;
					break;
				}
			}
			
			if(!used)	{	//does not used. (add threshold)
				Iterator itr = item.keySet().iterator();
				String key = "";
				while(itr.hasNext())	{
					key = (String)itr.next();
				}
				
				if(key.length() == 0)	{
					System.out.println("error");
					System.exit(1);
				}
				
				notUsedItem.add(key);
			}
		}
		return notUsedItem;
	}

	public boolean isContains(String newKey)	{
		for(Map<String,Integer> item : vec)	{
			if(item.containsKey(newKey))	{
				return true;
			}
		}
		
		return false;
	}
	
	public void addRank(Map<String,Integer> item)	{
		Iterator itr = item.keySet().iterator();
		String key = "";
		while(itr.hasNext())	{
			key = (String)itr.next();
		}
		
		if(key.length() == 0)	{
			System.out.println("error");
			System.exit(1);
		}
		
		for(Map<String,Integer> it : vec)	{
			
			if(it.containsKey(key))	{
				Integer newVal = item.get(key) + it.get(key);
				vec.remove(it);
				int index = sortIndex(key,newVal);	
				item.replace(key, newVal);
//				it.replace(key, item.get(key) + it.get(key));	//accumulation
				
				vec.insertElementAt(item, index);
				break;
			}
		}
	}

	public void write(String path, int top,String init) throws IOException {
		// TODO Auto-generated method stub
		BufferedWriter wr2 = null;
		BufferedWriter wr = null;
//		BufferedWriter wr3 = null;
		
		if(init.equalsIgnoreCase("T"))	{
			wr = new BufferedWriter(new FileWriter(path+"output_"+mutation+".txt"));
			wr2 = new BufferedWriter(new FileWriter(path+"output_"+mutation+"_extract.txt",true));
//			wr3 = new BufferedWriter(new FileWriter(path+"output_"+mutation+"1_extract.txt",true));
			
			wr2.write("All Features100"+"\t"+"-"+"\t");

			Vector<Double> lastEval = avgEvalLogLast(path+"evalLog_"+mutation+".txt");
			wr2.write("Tr: ("+lastEval.get(0)+")" + ", Ts: ("+lastEval.get(1)+")");
//			wr2.write(avgEvalLogLast(path+"evalLog_"+mutation+".txt")+"");
			wr2.newLine();
			
//			wr3.write("All Features1"+"\t"+"-"+"\t");
//
//			wr3.write(avgEvalLogOne(path+"evalLog_"+mutation+".txt")+"");
//			wr3.newLine();
			
			wr.write("name");wr.newLine();
			for(int i = 0; i < vec.size();i++)	{
				if(i+1 > top)	{
					break;
				}
				
				Map<String,Integer> item = vec.get(i);
				//wr.write(item.toString());
				for(String key:item.keySet())
					wr.write(key);

				wr.newLine();

			}

			wr.close();
			wr2.close();
//			wr3.close();
		}
		else	{
			wr = new BufferedWriter(new FileWriter(path+"output_"+mutation+".txt"));
			wr.write("name");wr.newLine();
			for(int i = 0; i < vec.size();i++)	{
				if(i+1 > top)	{
					if(top < 10)	{
						try
						{
							wr2 = new BufferedWriter(new FileWriter(path+"output_"+mutation+"_extract.txt",true));
//							wr3 = new BufferedWriter(new FileWriter(path+"output_"+mutation+"1_extract.txt",true));

							Map<String,Integer> item = vec.get(i);
							for(String key:item.keySet())	{
								wr2.write((top+1)+"\t"+key+"\t");
							}
							
							Vector<Double> lastEval = avgEvalLogLast(path+"evalLog_"+mutation+"_"+(top+1)+".txt");
							wr2.write("Tr: ("+lastEval.get(0)+")" + ", Ts: ("+lastEval.get(1)+")");
//							wr2.write(avgEvalLogLast(path+"evalLog_"+mutation+"_"+(top+1)+".txt")+"");
							wr2.newLine();

							wr2.close();
							
//							Map<String,Integer> item2 = vec.get(i);
//							for(String key:item2.keySet())	{
//								wr3.write((top+1)+"\t"+key+"\t");
//							}

//							wr3.write(avgEvalLogOne(path+"evalLog_"+mutation+"_"+(top+1)+".txt")+"");
//							wr3.newLine();
//
//							wr3.close();							
						}catch(FileNotFoundException e)	{
							e.printStackTrace();
						}
						//break;		//TODO skip break.
					}
				}
				Map<String,Integer> item = vec.get(i);
				//wr.write(item.toString());
				for(String key:item.keySet())
					wr.write(key);

				wr.newLine();

			}

			wr.close();
		}
		
	}

	public int sortIndex(String key, int newVal)	{
		for(int i =0 ; i< vec.size(); i++)	{
			Map<String,Integer> item = vec.get(i);
			
			if(item.get(key) != null)	continue;	//skip
			for(int val: item.values())	{
				if(val > newVal)	{
					return i;
				}
			}
		}
		
		return vec.size();
	}
	
	public static List<String> recursiveFileRead(String sDirectoryPath) throws IOException
    {
         File dirFile = new File(sDirectoryPath);
         File[] fileList = dirFile.listFiles();
         List<String> fileName = new Vector<>();
         
         if(fileList == null)
              return null;
         
         for(File tempFile : fileList)     {
              if(tempFile.isFile())     {
                   String tempFileName = tempFile.getName();
                   
                   if(tempFileName.contains("rank"))	{
                       int commaIndex = tempFileName.indexOf(".txt");
                       int underbarIndex = tempFileName.indexOf("_")+1;
                       String mutationName = tempFileName.substring(underbarIndex,commaIndex);
                       
                       System.out.println(mutationName);
                	   fileName.add(mutationName);
                   }
              }
         }
         
         return fileName;
    }
	
	public Vector<Double> avgEvalLogLast(String fileName) throws IOException	{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		double avg2 = 0.;
		double avg  = 0.;
		int count = 0;
		String prev = "";
		boolean start_flag = false;
		
		while((line=br.readLine())!=null)	{
			
			if(line.trim().contains("iter") && start_flag)	{
				if(prev.contains("iter"))	continue;
				System.out.println(prev);
				String[] evalLogArray = prev.trim().split(" ");
				//System.out.println(prev + "\t" + evalLogArray.length);
//				System.out.println(evalLogArray.length);
				if(evalLogArray[evalLogArray.length-1].equals("1"))
				{
					evalLogArray[evalLogArray.length-1] = "1.000000";
				}
				else if(evalLogArray[evalLogArray.length-1].equals("0"))
				{
					evalLogArray[evalLogArray.length-1] = "0.000000";
				}
				
				Vector<String> validLog = new Vector<String>();
				for(String part : evalLogArray)	{
					if(part.contains("."))	{
						validLog.add(part);
					}
				}
				
				avg += Double.parseDouble(validLog.get(0));
				avg2 += Double.parseDouble(validLog.get(1));
				count++;
			}
			else	{
				prev = line;
				start_flag = true;
			}
		}
		
		//last set
		/********************************************************/
		System.out.println(prev);
		String[] evalLogArray = prev.trim().split(" ");
		//System.out.println(prev + "\t" + evalLogArray.length);
		
		if(evalLogArray[evalLogArray.length-1].equals("1"))
		{
			evalLogArray[evalLogArray.length-1] = "1.000000";
		}
		else if(evalLogArray[evalLogArray.length-1].equals("0"))
		{
			evalLogArray[evalLogArray.length-1] = "0.000000";
		}
		
		Vector<String> validLog = new Vector<String>();
		for(String part : evalLogArray)	{
			if(part.contains("."))	{
				validLog.add(part);
			}
		}
		
		avg += Double.parseDouble(validLog.get(0));
		avg2 += Double.parseDouble(validLog.get(1));
		count++;
		/********************************************************/
		
		System.out.println("count: "+ count+ ", avg: "+avg/count + ", avg2: "+avg2/count);
		Vector<Double> dVec = new Vector<Double>();
		dVec.add(avg/count);
		dVec.add(avg2/count);
		
		br.close();
		return dVec;
	}
	
	/*public Vector<Double> avgEvalLogOne(String fileName) throws IOException	{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		double avg = 0.;
		double avg2 = 0.;
		int count = 0;
		int iter = 1;

		while((line=br.readLine())!=null)	{

			if(line.contains("1:    1"))	{
				System.out.println(line);
				String[] evalLogArray = line.trim().split(" ");
				
				Vector<String> validLog = new Vector<String>();
				for(String part : evalLogArray)	{
					if(part.contains("0."))	{
						validLog.add(part);
					}
				}
				
				avg += Double.parseDouble(validLog.get(0));
				avg2 += Double.parseDouble(validLog.get(1));
				count++;
			}
		
		}

		System.out.println("count: "+ count+ ", avg: "+avg/count + ", avg2: "+avg2/count);

		Vector<Double> dVec = new Vector<Double>();
		dVec.add(avg/count);
		dVec.add(avg2/count);
		
		br.close();
		return dVec;
	}*/
	
	/*public double avgEvalLog(String fileName) throws IOException	{
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		String line = "";
		double avg = 0.;
		int count = 0;
		int iter = 1;
		
		while((line=br.readLine())!=null)	{
		
			if(line.contains("iter"))	{
				iter = 1;
				continue;
			}
			String splitTerm = "";
			if(iter / 10 >= 10)	{
				splitTerm = iter+":  "+iter;
			}
			else if(iter / 10 < 1)	{
				splitTerm = iter+":    "+iter;
			}
			else if(iter / 10 < 10)	{
				splitTerm = iter+":   "+iter;
			}
			else	{
				System.out.println("error splitTerm" + line);
			}
				
			String[] evalLogArray = line.trim().split(splitTerm);
			System.out.println(splitTerm);
//			System.out.println(line + "\t" + evalLogArray.length);
			String[] validLog = evalLogArray[1].trim().split(" ");
			System.out.println(validLog[0] +", " + validLog[1] );
			avg += Double.parseDouble(validLog[0]);
			count++;
			iter++;			
		}
		
		System.out.println("count: "+ count+ ", avg: "+avg/count);
		
		br.close();
		return avg/count;
	}*/
	
	public void remove() {
		// TODO Auto-generated method stub
		System.out.println(vec.size());
		vec.remove(vec.size()-1);
		System.out.println(vec.size());
	}

	public void top1(String path,int top) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(path+"output_"+mutation+".txt"));
		String line = "";
		boolean flag = true;
		int a= 0;
		String lastOne = "";
		while((line=br.readLine())!= null)	{
			if(line.trim().length() != 0)	{
				lastOne = line.trim();
			}
		}
		
		System.out.println(lastOne);
		
		try
		{
			BufferedWriter wr2 = new BufferedWriter(new FileWriter(path+"output_"+mutation+"_extract.txt",true));
//			BufferedWriter wr3 = new BufferedWriter(new FileWriter(path+"output_"+mutation+"1_extract.txt",true));
			Vector<Double> lastEval = avgEvalLogLast(path+"evalLog_"+mutation+"_"+(top+1)+".txt");
			
//			Vector<Double> OneEval = avgEvalLogOne(path+"evalLog_"+mutation+"_"+(top+1)+".txt");
			
			wr2.write("1\t"+lastOne+"\tTr: ("+lastEval.get(0)+")" + ", Ts: ("+lastEval.get(1)+")");
			wr2.newLine();
			
			wr2.close();
			
//			wr3.write("1\t"+lastOne+"\tTr: ("+OneEval.get(0)+")" + ", Ts: ("+OneEval.get(1)+")");
//			wr3.newLine();
//			
//			wr3.close();
		}catch(FileNotFoundException e)	{
			e.printStackTrace();
		}
	}

}
