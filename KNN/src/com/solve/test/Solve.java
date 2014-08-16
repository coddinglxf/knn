package com.solve.test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class Solve {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception
	{
		ArrayList<String>true_list=new ArrayList<String>();
		HashSet<String>hash_true=new HashSet<String>();
		HashSet<String>hash_false=new HashSet<String>();
		ArrayList<String>false_list=new ArrayList<String>();
		BufferedReader br=new BufferedReader(new FileReader("F:\\File_Temp\\num_with_index_tidy.txt"));
		BufferedWriter bw_true=new BufferedWriter(new FileWriter("f://true.txt"));
		BufferedWriter bw_false=new BufferedWriter(new FileWriter("f://false.txt"));
		BufferedWriter bw=new BufferedWriter(new FileWriter("f://bal.txt"));
		String line=br.readLine().trim();
		while(line!=null)
		{		
			line=line.trim();
			if(line.endsWith("false"))
			{
				System.out.println("false");
				bw_false.write(line+"\r\n");
				hash_false.add(line);
				false_list.add(line);
			}
			else
			{
				System.out.println("true");
				bw_true.write(line+"\r\n");
				hash_true.add(line);
				true_list.add(line);
			}
		
			line=br.readLine();
		}
		bw_false.close();
		bw_true.close();
		br.close();
		
		//int index=0;
		System.out.println(true_list.size());
		System.err.println(false_list.size());
		
		Iterator<String>true_it=hash_true.iterator();
		Iterator<String>false_it=hash_false.iterator();
		
		while(true_it.hasNext())
		{
			bw.write(true_it.next()+"\r\n");
			bw.write(false_it.next()+"\r\n");
		}
	//	for(int i=0;i<true_list.size();i++)
//		{
//			bw.write(true_list.get(i)+"\r\n");
	//		bw.write(false_list.get(i)+"\r\n");
//			bw.write(false_list.get(index)+"\r\n");
//			index++;
//			bw.write(false_list.get(index)+"\r\n");
//			index++;
//			bw.write(false_list.get(index)+"\r\n");
//			index++;
//		}
		bw.close();
//		BufferedWriter bw=new BufferedWriter(new FileWriter("f://bal.txt"));
//		BufferedReader br_true=new BufferedReader(new FileReader("f://true.txt"));
//		BufferedReader br_false=new BufferedReader(new FileReader("f://false.txt"));
	}

}
