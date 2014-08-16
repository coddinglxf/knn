package com;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import com.calculate.edit_distance.Edit_Distance;

public class KNN_Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{
		KNN_Test solution=new KNN_Test();
		solution.init_data(500);
		solution.KNN(13);
	}
	ArrayList<elements>init=new ArrayList<elements>();
	ArrayList<elements>test_labeled=new ArrayList<elements>();
	int correct;
	public void init_data(int initial)
	{
		BufferedReader br=null;
		int index=0;
		try
		{
			br=new BufferedReader(new FileReader("bal.txt"));
			String line=br.readLine();
			while(line!=null)
			{
				String temp[]=line.split(" ");
				double d[]=new double[temp.length-1];
				for(int i=0;i<temp.length-1;i++)
				{
					d[i]=Double.parseDouble(temp[i]);
				}
				if(index<initial)
				{
					elements e=new elements();
					e.val=d;
					e.flags=temp[temp.length-1].equals("true")? true:false;
					this.init.add(e);
				}
				else
				{
					elements e=new elements();
					e.val=d;
					e.flags=temp[temp.length-1].equals("true")? true:false;
					this.test_labeled.add(e);
				}
				index++;
				line=br.readLine();
			}
		} catch (Exception e) 
		{
			e.printStackTrace();
		}finally
		{
			try
			{
				br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void KNN(int k)//设置k临近个数
	{
		/* 对于每个测试样本，
		 * 找到k个相互临近的个数
		 * 通过投票的方法
		 * 然后重新分配这个样本的情况，
		 * 该样本当作没有标记样本
		 * 最后这个样本需要加入到这个数据集合中*/
		for(int i=0;i<this.test_labeled.size();i++)
		{
			elements e=this.test_labeled.get(i);
			ArrayList<Integer>top_k_list=new ArrayList<Integer>();
			boolean flags=decide_true_false(top_k_list);
			System.out.println(flags);
			if(flags==e.flags)
			{
				correct++;
			}
			
			elements tempe=new elements();
			tempe.travel_flags=false;
			tempe.flags=flags;
			tempe.val=e.val;
			this.init.add(tempe);
			for(int j=0;j<this.init.size();j++)
			{
				this.init.get(j).flags=false;
			}
			System.out.println("init size= "+this.init.size()+" correct="+correct);
		}
		System.out.println("correct= "+correct+" all="+test_labeled.size());
		System.out.println("precious= "+(double)correct/(double)test_labeled.size());
	}

	public ArrayList<Integer> get_top_k(int k,elements e)
	{
		ArrayList<Integer>ret=new ArrayList<Integer>();
		for(int i=1;i<=k;i++)
		{
			double max=Double.MIN_VALUE;
			int index=-1;
			for(int j=0;j<this.init.size();j++)
			{
				if(init.get(j).travel_flags==false)
				{
					Edit_Distance distance=new Edit_Distance();
					int dis=distance.edit_distance(init.get(j).val, e.val);
					double d=(double)dis/(double)Math.max(init.get(j).val.length, e.val.length);
					if(d>=max)
					{
						max=d;
						index=j;
					}
				}
			}//设置init中index的travel_flags标志为true，表示已经访问过了
			init.get(index).travel_flags=true;
			ret.add(index);
		}
		return ret;
	}

	public boolean decide_true_false(ArrayList<Integer>top_k_list)
	{
		
		int sum_true=0,sum_false=0;
		for(int i=0;i<top_k_list.size();i++)
		{
			int index=top_k_list.get(i);
			if(this.init.get(index).flags==true)
			{
				sum_true++;
			}
			else
			{
				sum_false++;
			}
		}
		return sum_true>sum_false? true:false;
	}
}
class elements
{
	double val[];
	boolean flags;
	boolean travel_flags=false;
	public elements() {
		// TODO Auto-generated constructor stub
	}
	public elements(double val[],boolean flags)
	{
		this.val=val;
		this.flags=flags;
	}
}
