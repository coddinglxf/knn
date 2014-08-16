package com.knn;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import com.calculate.edit_distance.Edit_Distance;
import com.dictionary.Dictionary;

public class KNN_Test {

	/**
	 * @param args
	 */
	//double w1=1;
	//double w2=0.1;
	public static void main(String[] args)
	{
//		verbsolve solve=new verbsolve();
//		System.out.println(solve.get_string_by_index(1));;
		int index=1;
//     	KNN_Test test=new KNN_Test();
//		test.init_data(100, 1848);
		while(index<=6)
		{
			double all_pre=0;
			double all_recall=0;
			int k=13;
			int all_sum=1848;
			int init_data=25;
			int all_data=400;
			int test_data=250;
            KNN_Test test=new KNN_Test();
            
			test.init_data(all_data,all_sum);
			test.init(init_data, test_data, all_data);
//				System.out.println(test.all.size());
//				System.out.println(test.test_labeled.size());
//				System.out.println(test.init.size());
//				System.out.println(test.test.size());
			test.knn(k,test);
			double tp=0;//正确时候被分为正确情况
			double fp=0;//错误情况被分为正确情况
			double fn=0;//正确被分为错误情况
			double tn=0;//错误被分为错误的情况
			for(int j=0;j<test.test.size();j++)
			{
				elements e=test.test.get(j);
				boolean ret=test.get_res(test, e, k);
				if(ret==false)
				{
					ret=test.filter_result_false(e);
				}
//				if(ret==true)
//				{
//					ret=test.filter_result_true(e);
//				}
				if(e.flags==true&&ret==true)
				{
					tp++;
				}
				if(e.flags==false&&ret==true)
				{
					fp++;
				}
				if(e.flags==false&&ret==false)
				{
					tn++;
				}
				if(e.flags==true&&ret==false)
				{
					fn++;
				}
			}
			double pre=tp/(tp+fp);
			double recall=tp/(tp+fn);
			all_pre=all_pre+pre;
			all_recall=all_recall+recall;
			System.err.println("tp= "+tp+" fp="+fp+" tn="+tn+" fn="+fn);
			System.err.println(" precious= "+pre+" recall="+recall);
			test.writefile(init_data,test_data,all_data,test.all, tp, fp, fn, test.test);	
			index++;
		}
	}
	
	ArrayList<elements>init=new ArrayList<elements>();
	ArrayList<elements>test_labeled=new ArrayList<elements>();
	ArrayList<elements>all=new ArrayList<elements>();
	ArrayList<elements>test=new ArrayList<elements>();
	HashSet<Integer>hash=new HashSet<Integer>();
	int correct=0;
	int sum=0;
	public boolean filter_result_false(elements e)
	{
		//ArrayList<>;
		boolean flags=false;
		ArrayList<Integer>term_index=e.terms_index;
		if(e.hash_verb.size()>0)
		{
			Iterator<Integer>it=e.hash_verb.keySet().iterator();
			while(it.hasNext())
			{
				int verb_index=it.next();
				int dic_index=e.hash_verb.get(verb_index);
				verbsolve solve=new verbsolve();
				String verbs=solve.get_string_by_index(dic_index);
				boolean exisit=solve.is_in_key_verbs_dic(verbs);
				if(exisit==true)//判断是在这几个动词情况。开始判断满足在词典中
				{
					int sentence=verb_index%100000;
					int parserindex=verb_index/100000;
					int gene1=term_index.get(0);
					int gene2=term_index.get(term_index.size()-1);
					if(gene1<sentence&&sentence<gene2)//在句子中的位置介于实体之间
					{
						if(Math.abs(parserindex)<=5&&Math.abs(term_index.size()-1-parserindex)<=5)
						{
							flags=true;
							break;
						}
					}
					if(gene2<sentence&&sentence<gene1)//在句子中的位置介于实体之间
					{
						if(Math.abs(parserindex)<=5&&Math.abs(term_index.size()-1-parserindex)<=5)
						{
							flags=true;
							break;
						}
					}
				}
			}
		}
		return flags;
	}
	public boolean filter_result_true(elements e)
	{
		boolean flags=true;
		if(e.terms_index.size()>3)
		{
//			ArrayList<Integer>list=e.terms_index;			
			ArrayList<Integer>verbs_list=e.verbs_list;
			if(verbs_list.size()==0)//长度大于三的时候还是不存在动词之间的连接，这种时候，判断正确的情况仍然是错误的
			{
				return false;
			}
			
			//开始判断动词是不是在两个基因实体之间。
//			int i=0;
//			for( i=0;i<verbs_list.size();i++)
//			{
//				int gene_1_index=list.get(0);
//				int verbs_index=verbs_list.get(i);
//				int gene_2_index=list.get(list.size()-1);
//				if((gene_1_index<verbs_index&&verbs_index<gene_2_index))
//				{
//					if(Math.abs(gene_1_index-verbs_index)<=4&&Math.abs(gene_2_index-verbs_index)<=4)
//					{
//						//flags=true;
//						break;
//					}	
//				}
//				
//				if((gene_2_index<verbs_index&&verbs_index<gene_1_index))
//				{
//					if(Math.abs(gene_1_index-verbs_index)<=4&&Math.abs(gene_2_index-verbs_index)<=4)
//					{
//						//flags=true;
//						break;
//					}	
//				}
//			}
//			if(i==verbs_list.size())
//			{
//				return false;
//			}
		}
		return flags;
	}
	public void writefile(int init_data,int test_data,int all_sum,ArrayList<elements>all,
			double tp,double fp,double fn,ArrayList<elements>test)
	{
		BufferedWriter bw=null;
		try
		{
			bw=new BufferedWriter(new OutputStreamWriter(new FileOutputStream("f://cross_val//cross_valiton_with_k_13.txt",true)));
			double all_rate=get_rate(all);
			double test_rate=get_rate(test);
			bw.write("all_true_rate ="+all_rate+"\r\n");
			bw.write("test_true_rate ="+test_rate+"\r\n");
			bw.write("sum_of_init_data="+init_data+"\r\n");
			bw.write("sum_of_test_data="+test_data+"\r\n");
			bw.write("sum_of_all="+all_sum+"\r\n");
			bw.write("tp="+tp+" "+"fp="+fp+" "+"fn="+fn+"\r\n");
			double pre=tp/(tp+fp);
			double recall=tp/(tp+fn);
			bw.write("precious="+pre+"\r\n");
			bw.write("recall="+recall+"\r\n");
			bw.write("\r\n");
		
		} catch (Exception e) {
			e.printStackTrace();
		}finally
		{
			try 
			{
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	public double get_rate(ArrayList<elements>all)
	{
		double index=0;
		for(int i=0;i<all.size();i++)
		{
			if(all.get(i).flags==true)
			{
				index++;
			}
		}
		return index/(double)all.size();
	}
	public boolean get_res(KNN_Test test,elements e,int k)
	{
		ArrayList<Integer>top_k_list=get_top_k(k, e,test);
		for(int i=0;i<test.init.size();i++)
		{
			test.init.get(i).travel_flags=false;
		}
		return test.decide_true_false(top_k_list).istrue;
	}
	public void init(int labeld,int test,int all_sum)
	{
		int sum=labeld+test;
		while(this.hash.size()<sum)
		{
			this.hash.add((int)(Math.random()*all_sum));
		}
		Iterator<Integer>it=this.hash.iterator();
		int flags=1;
		while(it.hasNext())
		{
			int index=it.next();
			if(flags<=labeld)
			{
				this.init.add(this.all.get(index));
			}
			else
			{
				this.test.add(this.all.get(index));
			}
			flags++;
		}
		
		for(int i=0;i<this.all.size();i++)
		{
			if(!this.hash.contains(i))
			{
				this.test_labeled.add(all.get(i));
			}
		}
	}
	public void init_data(int maxmun,int all)
	{
		BufferedReader br=null;
		ArrayList<elements>data=new ArrayList<elements>();
		int index=0;
		System.out.println(index);
		try
		{
			br=new BufferedReader(new FileReader("bal_with_index.txt"));
			String line=br.readLine();
			while(line!=null)
			{
				String temp[]=line.trim().split(" ");
				double d[]=new double[temp.length-1];
				ArrayList<Integer>verbs_list=new ArrayList<Integer>();
				ArrayList<Integer>terms_list=new ArrayList<Integer>();
				HashMap<Integer, Integer>hash_verb=new HashMap<Integer, Integer>();
				for(int i=0;i<temp.length-1;i++)
				{				
					if(temp[i].contains("@"))
					{
						String t[]=temp[i].split("@");
						d[i]=Double.parseDouble(t[0]);
						if(t[1].startsWith("V"))
						{
							verbs_list.add(Integer.parseInt(t[0]));//加入词语的词典索引部分
							hash_verb.put(i/2*100000+Integer.parseInt(t[2]),Integer.parseInt(t[0]));
						}
						terms_list.add(Integer.parseInt(t[2]));
						
					}
					else
					{
						d[i]=Double.parseDouble(temp[i]);
					}
					
				}
				elements e=new elements();
				e.val=d;
				e.hash_verb=hash_verb;
				e.terms_index=terms_list;
				e.verbs_list=verbs_list;
				e.flags=temp[temp.length-1].equals("true")? true:false;
//				System.out.println(line);
//				for(int i=0;i<e.val.length;i++)
//				{
//					System.out.print(e.val[i]+" ");
//				}System.out.println();
//				System.out.println(e.verbs_list);
//				System.out.println(e.terms_index);
//				System.out.println(e.hash_verb);
//				System.out.println(".................");
				data.add(e);
				index++;
				line=br.readLine();
			}
			HashSet<Integer>hash=new HashSet<Integer>();
			while(hash.size()<maxmun)
			{
				hash.add((int)(Math.random()*all));
			}
			Iterator<Integer>it=hash.iterator();
			while(it.hasNext())
			{
				int key=it.next();
				this.all.add(data.get(key));
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
	public double knn(int k,KNN_Test test)
	{
		while(this.test_labeled.size()>0)
		{
			ele_most most=get_most(k,test);
			int index=most.index;
			elements e=this.test_labeled.get(index);
			if(most.istrue==e.flags)
			{
				correct++;
			}
			
			sum++;
			elements temp=new elements();//注意这里面一定要重新定义一个变量，不然后面的变量删除会对结果产生影响
			temp.flags=most.istrue;
			temp.travel_flags=false;
			temp.val=e.val;
			this.init.add(temp);
			this.test_labeled.remove(index);
			System.out.println("init length= "+this.init.size()+" test length="+this.test_labeled.size()+
					" correct="+this.correct+" sum="+this.sum+" pre="+(double)correct/(double)sum);
		}
		return (double)correct/(double)sum;
	}
	public ArrayList<Integer> get_top_k(int k,elements e,KNN_Test test)
	{
		ArrayList<Integer>ret=new ArrayList<Integer>();
		double max=-1;
		int index=-1;
		for(int i=1;i<=k;i++)
		{
			for(int j=0;j<this.init.size();j++)
			{
				if(this.init.get(j).travel_flags==false)
				{
					elements temp_e=this.init.get(j);
					Edit_Distance distance=new Edit_Distance();
					int dis=distance.edit_distance(temp_e.val, e.val);
					double d=(double)dis/(double)Math.max(init.get(j).val.length, e.val.length);
					d=Math.pow(Math.E, -d);
					//同时可以计算动词对于整个的影响。
					//double cosin=calculate_cosin(temp_e.val, e.val);
					
					//d=test.w1*d+test.w2*cosin;
					if(d>=max)
					{
						max=d;
						index=j;
					}
				}
			}
			this.init.get(index).travel_flags=true;
			ret.add(index);
		}
		return ret;
	}	
	public ele_most get_most(int k,KNN_Test test)
	{
		ele_most most=new ele_most();
		double max=-1;
		int index=-1;
		double s_true=0;
		double s_false=0;
		//boolean flags=false;
		for(int i=0;i<this.test_labeled.size();i++)
		{
			elements e=this.test_labeled.get(i);
			ArrayList<Integer>top_k_list=get_top_k(k, e,test);
			for(int x=0;x<this.init.size();x++)
			{
				this.init.get(x).travel_flags=false;
			}
			ele_most most_temp=decide_true_false(top_k_list);
			double sum=most_temp.sum_false+most_temp.sum_true;
			double max_value=Math.max((double)most_temp.sum_false/sum, (double)most_temp.sum_true/sum);
			//System.out.println(most.sum_false+" "+most.sum_true);
			if(max_value>max)
			{
				max=max_value;
				s_false=most_temp.sum_false;
				s_true=most_temp.sum_true;
				index=i;
			}
		}
		most.istrue=s_true>s_false? true:false;
		most.sum_false=s_false;
		most.sum_true=s_true;
		most.index=index;
		System.out.println("true="+most.sum_true+" false="+most.sum_false+" max= "+max+" index="+index+" most.istrue="+most.istrue);
		return most;
	}	
	public ele_most decide_true_false(ArrayList<Integer>top_k_list)
	{
		ele_most most=new ele_most();
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
		most.istrue=sum_true>sum_false? true:false;
		most.sum_false=(double)sum_false;
		most.sum_true=(double)sum_true;
		return most;
	}
	public double calculate_cosin(double v1[],double v2[])
	{
		double index=0;
		for(int i=0;i<v1.length;i++){
			for(int j=0;j<v2.length;j++)
			{
				if(v1[i]==v2[j])
				{
					index++;
				}
			}
		}
		return index/(v1.length*v2.length);
	}
}
class elements
{
	double val[];
	boolean flags;
	boolean travel_flags=false;
	ArrayList<Integer>verbs_list=new ArrayList<Integer>();
	ArrayList<Integer>terms_index=new ArrayList<Integer>();
	HashMap<Integer, Integer>hash_verb=new HashMap<Integer, Integer>();//第一个是index，后面对应字典索引。
	public elements() {
		// TODO Auto-generated constructor stub
	}
	public elements(double val[],boolean flags)
	{
		this.val=val;
		this.flags=flags;
	}
}

class ele_most
{
	int index=-1;
	double sum_true=0;
	double sum_false=0;
	boolean istrue=false;
}

class verbsolve
{
	public String get_string_by_index(int index)
	{
		BufferedReader br=null;
		String line="";
		int i=1;
		try
		{
			br=new BufferedReader(new FileReader("dictionary.txt"));
			line=br.readLine();
			while(i<index)
			{
				line=br.readLine();
				i++;
			}
		} catch (Exception e) {
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
		return line.trim().split(" ")[0];
	}
	public boolean is_in_key_verbs_dic(String verbs)
	{	
		for(int i=0;i<Dictionary.key_interaction_verbs.length;i++)
		{
			String temp=Dictionary.key_interaction_verbs[i];
			if(verbs.equals(temp))
			{
				return true;
			}
			if(temp.contains(verbs)&&verbs.length()>=4)
			{
				return true;
			}
			if(verbs.contains(temp))
			{
				return true;
			}
		}
		return false;
	}
}