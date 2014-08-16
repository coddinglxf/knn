package com.result.tidy;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class ResultTidy {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ResultTidy tidy=new ResultTidy();
		tidy.solve();
		ArrayList<ele>list=tidy.list;
		double pre=0;
		double recall=0;
		for(ele e:list)
		{
			System.out.println(e.pre+" "+e.recall);
			pre=pre+e.pre;
			recall=recall+e.recall;
		}
		System.out.println(pre/list.size());
		System.out.println(recall/list.size());
		
		pre=pre/list.size();
		recall=recall/list.size();
		System.out.println(2*pre*recall/(pre+recall));
		System.out.println(list.size());
	}
	ArrayList<ele>list=new ArrayList<ele>();
	public void solve()
	{
		BufferedReader br=null;
		int index=1;
		int flags=1;
		System.out.println(flags);
		try 
		{
			br=new BufferedReader(new FileReader("f://cross_val//15//cross_valiton_with_k_13.txt"));
			String line=br.readLine();
			while(line!=null)
			{
				if(index%9==7)
				{			
					String pre=line.trim();
					//System.out.println(pre+" "+flags);
					line=br.readLine();
					String recall=line.trim();
					//System.out.println(recall+" "+flags);
					flags++;
					index++;
					if(!pre.endsWith("NaN"))
					{
						double p=Double.parseDouble(pre.split("=")[1]);
						double r=Double.parseDouble(recall.split("=")[1]);
						ele e=new ele();
						e.pre=p;
						e.recall=r;
						this.list.add(e);
					}
				}
				line=br.readLine();
				index++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try
			{
				br.close();
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
class ele
{
	double pre;
	double recall;
}