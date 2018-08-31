package edu.buffalo.cse.ladangol;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Solution {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			BufferedReader brstop = new BufferedReader(new FileReader("Data/stopwords_english.txt"));
			BufferedReader br = new BufferedReader(new FileReader("Data/trainingdata.txt"));
			int linenum = Integer.parseInt(br.readLine());
			Set<String> stopwords = new HashSet<String>();
			String line;
			HashMap<String, Integer> labels = new HashMap<String, Integer>();
		    labels.put("justinbieber", 1);
		    labels.put("BillGates", 2);
		    labels.put("google", 3);
		    labels.put("KingJames", 4);
		    labels.put("BarackObama", 5);
			HashMap<String, Integer> words = new HashMap<String, Integer>();
			HashSet<String> wordList = new HashSet<String>();	
			HashMap<String, Integer> justinfreq = new HashMap<String, Integer>();
			HashMap<String, Integer> googlefreq = new HashMap<String, Integer>();
			HashMap<String, Integer> kingjamesfreq = new HashMap<String, Integer>();
			HashMap<String, Integer> barakfreq = new HashMap<String, Integer>();
			HashMap<String, Integer> billgaesfreq = new HashMap<String, Integer>();
			while((line= brstop.readLine()) != null){
				stopwords.add(line);
			}
			int numgoogle=0, numbill =0, numjustin =0, numbarak=0, numKing =0;
			while((line = br.readLine())!=null){
				//String[] splitedLine = line.split("\\s");
				String[] result = line.split(" ", 2);
				String label = result[0];
				String wordsstr = result[1];
				Iterator<String> stw = stopwords.iterator();
				String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
				wordsstr = wordsstr.replaceAll(urlPattern, "");
				wordsstr= wordsstr.replaceAll("[^\\w-]+", " ");
				//wordsstr = wordsstr.replaceAll("[\\+\\.\\^:,\\{\\}]","");
				while(stw.hasNext()){
					wordsstr = wordsstr.replaceAll("\\s"+stw.next()+"\\s", " ");
				}
				
				wordsstr = wordsstr.trim();
				String[] wordsarr = wordsstr.split(" ");
				
				switch(label){
					case "justinbieber":
						numjustin ++;
						for(int i=0; i< wordsarr.length; i++){
							String key = wordsarr[i].toLowerCase();
							if(justinfreq.containsKey(key)){
								justinfreq.put(key, justinfreq.get(key)+1);
							}
							else{
								justinfreq.put(key, 1);
							}
							wordList.add(wordsarr[i]);
						}
						break;
					case "BillGates":
						numbill++;
						for(int i=0; i< wordsarr.length; i++){
							String key = wordsarr[i].toLowerCase();
							if(billgaesfreq.containsKey(key)){
								billgaesfreq.put(key,billgaesfreq.get(key)+1);
							}
							else{
								billgaesfreq.put(key, 1);
							}
							wordList.add(wordsarr[i]);
						}
						break;
					case "google":
						numgoogle++;
						for(int i=0; i< wordsarr.length; i++){
							String key = wordsarr[i].toLowerCase();
							if(googlefreq.containsKey(key)){
								googlefreq.put(key, googlefreq.get(key)+1);
							}
							else{
								googlefreq.put(key, 1);
							}
							wordList.add(wordsarr[i]);
						}
						break;
					case "KingJames":
						numKing++;
						for(int i=0; i< wordsarr.length; i++){
							String key = wordsarr[i].toLowerCase();
							if(kingjamesfreq.containsKey(key)){
								kingjamesfreq.put(key, kingjamesfreq.get(key)+1);
							}
							else{
								kingjamesfreq.put(key, 1);
							}
							wordList.add(wordsarr[i]);
						}
						break;
					case "BarackObama":
						numbarak++;
						for(int i=0; i< wordsarr.length; i++){
							String key = wordsarr[i].toLowerCase();
							if(barakfreq.containsKey(key)){
								barakfreq.put(key, barakfreq.get(key)+1);
							}
							else{
								barakfreq.put(key, 1);
							}
							wordList.add(wordsarr[i]);
						}
				}
				
			}
			//Size of each hashMap is the number of tweets in each class
			//linenum is the number of all tweets
			
			//prior probability of each class
			double googleprb = numgoogle/(double)linenum;
			double billprob = numbill/(double)linenum;
			double barakprob = numbarak/(double)linenum;
			double kingprob = numKing/(double)linenum;
			double justinprob = numjustin/(double)linenum;
			

					
			//Test
			System.out.println("Give me a test sentense");
			Scanner scanner = new Scanner(System. in); 
			String input= scanner.nextLine();
			String urlPattern = "((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)";
			input = input.replaceAll(urlPattern, "");
			//input= input.replaceAll("[\\+\\.\\^:,\\{\\}]","");
			//input= input.replaceAll("[^a-zA-Z]", " ");
			input = input.replaceAll("[^\\w-]+", " ");
			
			Iterator<String> stw = stopwords.iterator();
			
			while(stw.hasNext()){
				input = input.replaceAll("\\s"+stw.next()+"\\s", " ");
			}
			
			input = input.trim();
			String[] wordsinput = input.split(" ");
			double[] whichtweet = new double[5];
			whichtweet[0] = FindLikelihood(wordsinput, googlefreq, numgoogle, googleprb, wordList.size());
			whichtweet[1] = FindLikelihood(wordsinput, justinfreq, numjustin, justinprob, wordList.size());
			whichtweet[2] = FindLikelihood(wordsinput, billgaesfreq, numbill, billprob, wordList.size());
			whichtweet[3] = FindLikelihood(wordsinput, kingjamesfreq, numKing, kingprob, wordList.size());
			whichtweet[4] = FindLikelihood(wordsinput, barakfreq, numbarak, barakprob, wordList.size());
			
			//find max
			double max= whichtweet[0];
			int index =0;
			for(int i=1; i< whichtweet.length;i++){
				if(whichtweet[i] > max){
					max=whichtweet[i];
					index =i;
				}
			}
			switch(index){
			case 0:
				System.out.println("@google");
				break;
			case 1:
				System.out.println("@justinebiber");
				break;
			case 2:
				System.out.println("@BillGates");
				break;
			case 3:
				System.out.println("@KingJames");
				break;
			case 4:
				System.out.println("@BarakObama");
				break;
			}
			//double[] whichtweet = new double[5];
			
			
			//////////////////////////////

			
			
			
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	private static double FindLikelihood(String[] wordsinput, HashMap<String, Integer> googlefreq, int numgoogle, double googleprb, int wordsize) {
		double[] likelihood = new double[wordsinput.length];
		double size = numgoogle;
		for(int i=0; i< wordsinput.length; i++){
			String key = wordsinput[i].toLowerCase();
			double temp=0;
			if(googlefreq.containsKey(key)){
				temp = googlefreq.get(key);
			}
			temp++;
			temp = temp/(size+wordsize);
			likelihood[i] = Math.log(temp)/Math.log(2);
		}
		double sum =0;
		for(int i=0; i< likelihood.length; i++){
			sum += likelihood[i];
		}
		return Math.log(googleprb)/Math.log(2) + sum; 
	}

}
