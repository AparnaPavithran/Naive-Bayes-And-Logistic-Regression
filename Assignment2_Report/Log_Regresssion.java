import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

public class Log_Regresssion {

	public static Set<String> allwords = new HashSet<String>();
	public static Set<String> allfiles = new HashSet<String>();
	public static Set<String> stopwords = new HashSet<String>();
	public static HashMap<String,Double> word_weight = new HashMap<String,Double>();
	
	public static HashMap<String,HashMap<String,Integer>> spam_file = new HashMap<String,HashMap<String,Integer>>();
	public static Set<String> spamfiles = new HashSet<String>();
	
	public static HashMap<String,HashMap<String,Integer>> ham_file = new HashMap<String,HashMap<String,Integer>>();
	public static Set<String> hamfiles = new HashSet<String>();
	
	public static void main(String[] args){
		
		if(args.length==5)
		{
			System.out.println("Started");
			
			String directory_location = args[0];
			String stopword_filter=args[1];
			double learning_rate = Double.parseDouble(args[2]);
			double lambda_constant = Double.parseDouble(args[3]);
			int number_of_iterations = Integer.parseInt(args[4]);
			
			//String directory_location = "/Users/APARNA/Documents/workspace/TEST_NB/src";
			//String stopword_filter="yes";
			//double learning_rate=0.1;
			//double lambda_constant = .89;
			//int number_of_iterations = 1;
			
			//add spam words to all words list
			//String spam_loc=directory_location+"/train/spam";
			File spam_loc = new File(directory_location+"/train/spam");
			File stop_loc = new File(directory_location+"/stopwords.txt");
			Scanner sc=null;
			for(File spam : spam_loc.listFiles())
			{
				//System.out.println("adding spam words"+spam.getName());
				try {
					sc = new Scanner(spam);
					while(sc.hasNextLine())
					{
						String wordlist= sc.nextLine();
						String[] s=wordlist.split(" ");
						for(int i=0;i<s.length;i++)
						{
							s[i]=s[i].replaceAll("[^a-zA-Z]+", "");
							if(!s[i].isEmpty())
							{							
								s[i]=s[i].toLowerCase();
									allwords.add(s[i]);
							}
						}
						
					}
				}
				catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
				}
				
			}//while(spam.listFiles() != null)
			sc.close();
			System.out.println("Spam words added to all words");
			//add ham words to all words list
			File ham_loc = new File(directory_location+"/train/ham");
			Scanner sc1=null;
			for(File ham : ham_loc.listFiles())
			{
					//System.out.println("adding ham words"+ham.getName());
				try {
					sc1 = new Scanner(ham);
					while(sc1.hasNextLine())
					{
						String wordlist= sc1.nextLine();
						String[] s=wordlist.split(" ");
						for(int i=0;i<s.length;i++)
						{
							s[i]=s[i].replaceAll("[^a-zA-Z]+", "");
							if(!s[i].isEmpty())
							{							
								s[i]=s[i].toLowerCase();
									allwords.add(s[i]);
							}
						}
						
					}
				}
				catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				
				}
				
			}//while(ham.listFiles() != null)
			sc1.close();		System.out.println("Ham words added to all words");
			//all words are added to word file
			
			Scanner sc_st=null;
			try {
				sc_st = new Scanner(stop_loc);
			} catch (FileNotFoundException e) {

				//e.printStackTrace();
			}
			while(sc_st.hasNext()){
				String sw = sc_st.next();
				stopwords.add(sw);
			}
			sc_st.close();
			
			if(stopword_filter.equals("yes")){

				System.out.println("Removing stop words...");
				for(String str : stopwords){
					if(allwords.contains(str)){
						allwords.remove(str);
					}
				}

			}
			
			HashMap<String, Integer> temp_list_spam_train = new HashMap<String, Integer>();
			HashMap<String, Integer> temp_list_ham_train = new HashMap<String, Integer>();
			HashMap<String, Integer> temp_list_spam_test = new HashMap<String, Integer>();
			HashMap<String, Integer> temp_list_ham_test = new HashMap<String, Integer>();
			//add words to spam word and file lists
			Scanner sc3= null;
			for(File spam1 : spam_loc.listFiles())
			{
				try {
					sc3=new Scanner(spam1);
					spamfiles.add(spam1.getName());
					allfiles.add(spam1.getName());
					
					while(sc3.hasNextLine())
					{
						String wordlist= sc3.nextLine();
						String[] s=wordlist.split(" ");
						
						for(int i=0;i<s.length;i++)
						{
							//System.out.println("s[i] "+s[i]);
							s[i]=s[i].replaceAll("[^a-zA-Z]+", "");
							if(!s[i].isEmpty())
							{							
								s[i]=s[i].toLowerCase();
								if(allwords.contains(s[i]))
								{
									if(temp_list_spam_train.containsKey(s[i]))
									{
										int count=temp_list_spam_train.get(s[i])+1;
										temp_list_spam_train.put(s[i], count);
										//System.out.println("templist "+temp_list.get(s[i]));
									}
									else
									{
										temp_list_spam_train.put(s[i], 1);
									}
								}
							}
						}
						
						//System.out.println("spam file has"+temp_list.get(spam1));
						//System.out.println("before "+spam_file.size());
						
						//System.out.println("after "+spam_file.size());
						//System.out.println("spam file has"+spam_file.get(spam1));
					}//for each file by file	
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				}
				spam_file.put(spam1.getName(), temp_list_spam_train);
			}
			System.out.println("Spam words added to spam file");
			sc3.close();	
			//temp_list.clear();
			
			
			/*int c1=0;
			Iterator<Entry<String, HashMap<String, Integer>>> parent = spam_file.entrySet().iterator();
			while (parent.hasNext()) 
			{
			    Entry<String, HashMap<String, Integer>> parentPair = parent.next();
			    System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());
			    if(parentPair.getValue().containsKey("http"))
			    {
			    	System.out.println("test ");
			    	c1=parentPair.getValue().get("http");
			    }
			}
			*/
			
			//add words to ham word and file lists
			Scanner sc4= null;
			for(File ham1 : ham_loc.listFiles())
			{
				try {
					sc4=new Scanner(ham1);
					hamfiles.add(ham1.getName());
					allfiles.add(ham1.getName());
					
					while(sc4.hasNextLine())
					{
						String wordlist= sc4.nextLine();
						String[] s=wordlist.split(" ");
						
						for(int i=0;i<s.length;i++)
						{
							s[i]=s[i].replaceAll("[^a-zA-Z]+", "");
							if(!s[i].isEmpty())
							{							
								s[i]=s[i].toLowerCase();
								if(allwords.contains(s[i]))
								{
									if(temp_list_ham_train.containsKey(s[i]))
									{
										int count=temp_list_ham_train.get(s[i])+1;
										temp_list_ham_train.put(s[i], count);
									}
									else
									{
										temp_list_ham_train.put(s[i], 1);
									}
								}
							}
						}
						
						
					}//for each file by file
					
					
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
				}
				ham_file.put(ham1.getName(), temp_list_ham_train);
			}
			System.out.println("Ham words added to ham file");
			sc4.close();
			//temp_list.clear();
			//words are added to spam n ham files n made a key,value pair set
			
			//learn the data set
			learn(number_of_iterations,learning_rate,lambda_constant);
			System.out.println("Learning done");
			//test the data set
			int spam_count = 0,ham_count=0 ;
			int num_spam_test_items = 0;
			int num_ham_test_items = 0;
			
			File spam_loc_test = new File(directory_location+"/test/spam");
			Scanner sc5=null;
			for(File spam_test : spam_loc_test.listFiles())
			{
				num_spam_test_items = num_spam_test_items+1;
				
				try {
					sc5=new Scanner(spam_test);
					
					while(sc5.hasNext())
					{
						String wordlist= sc5.nextLine();
						String[] s=wordlist.split(" ");
						
						for(int i=0;i<s.length;i++)
						{
							s[i]=s[i].replaceAll("[^a-zA-Z]+", "");
							if(!s[i].isEmpty())
							{							
								s[i]=s[i].toLowerCase();
								if(allwords.contains(s[i]))
								{
									if(temp_list_spam_test.containsKey(s[i]))
									{
										int count=temp_list_spam_test.get(s[i])+1;
										temp_list_spam_test.put(s[i], count);
									}
									else
									{
										temp_list_spam_test.put(s[i], 1);
									}
								}
							}
						}
						
						//spam_file.put(spam_test.getName(), temp_list_spam_test);
					}
					double test_sum = 0;
					for(Entry<String, Integer> test :temp_list_spam_test.entrySet()){
						if(word_weight.containsKey(test.getKey())){
							test_sum = test_sum +( (word_weight.get(test.getKey())* test.getValue()));
						}
					}
					test_sum = test_sum+0.1;
					if(test_sum>=0)
					{
						spam_count++;
					}
			
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
					}
				temp_list_spam_test.clear();
			}
			//temp_list.clear();
			
			File ham_loc_test = new File(directory_location+"/test/ham");
			Scanner sc6=null;
			for(File ham_test : ham_loc_test.listFiles())
			{
				num_ham_test_items = num_ham_test_items+1;
				try {
					sc6=new Scanner(ham_test);
					while(sc6.hasNext())
					{
						String wordlist= sc6.nextLine();
						String[] s=wordlist.split(" ");
						
						for(int i=0;i<s.length;i++)
						{
							s[i]=s[i].replaceAll("[^a-zA-Z]+", "");
							if(!s[i].isEmpty())
							{							
								s[i]=s[i].toLowerCase();
								if(allwords.contains(s[i]))
								{
									if(temp_list_ham_test.containsKey(s[i]))
									{
										int count=temp_list_ham_test.get(s[i])+1;
										temp_list_ham_test.put(s[i], count);
									}
									else
									{
										temp_list_ham_test.put(s[i], 1);
									}
								}
							}
						}
						
						//ham_file.put(ham_test.getName(), temp_list_ham_test);
					}
					double test_sum = 0;
					for(Entry<String, Integer> test :temp_list_ham_test.entrySet()){
						if(word_weight.containsKey(test.getKey())){
							double check=(word_weight.get(test.getKey()));
							test_sum = test_sum + ((word_weight.get(test.getKey())* test.getValue()));
						}
					}
					test_sum = test_sum+0.1;
					if(test_sum<0)
					{
						ham_count++;
					}
			
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
					}
				temp_list_ham_test.clear();
			}
			System.out.println("Testing done");
			
			//calculate accuracy
			double spam_acc = ( (double)spam_count / (double)num_spam_test_items)*100;
			double ham_acc = ( (double)ham_count / (double)num_ham_test_items)*100;
			double acc=0.0;
			double tot=(double)num_spam_test_items+(double)num_ham_test_items;
			acc=(ham_acc*((double)num_ham_test_items/tot))+(spam_acc*((double)num_spam_test_items/tot));
			//System.out.println("Sp tot "+num_spam_test_items+"count "+spam_count);
			//System.out.println("H tot "+num_ham_test_items+"count "+ham_count);
			System.out.println("Accuracy Logistic Regression : "+acc);
			System.out.println();
			
		}//if(args.length==5)
		else
		{
			System.out.println("Input String missing... Exiting...");
		}
		
	}
	
	public static void learn(int number_of_iterations, double learning_rate,double lambda_constant) 
	{
		//Random r= new Random();
		//Set<String> word;
		double flag;
		int count=0;
		double w,delta_weight=0,sig_error=0,w0=0.1;
		for(String word : allwords)
		{	
			w=Math.random()*2-(-1);
			word_weight.put(word, w);
		}
		System.out.println("initialized weight");
		for(int i=0;i<number_of_iterations;i++)
		{
			System.out.println("iteration value "+i);
			System.out.println("allwords for loop starts");
			for(String word : allwords)
			{
				//System.out.println("allfiles for loop starts for word : "+word);
				try{
				for(String file : allfiles)
				{
					delta_weight=0;
					try{
					if(spamfiles.contains(file))
						flag=1;
					else
						flag=0;
					//System.out.println("flag "+flag);
					count=word_count(file,word);
					sig_error=helper(file,word);
					if(sig_error>50){
						sig_error= 1.0;
					}
					else if(sig_error<-50){

						sig_error= 0.0;
					}
					else{
						sig_error= (1.0 /(1.0+ Math.exp(-sig_error)));
					}
					//System.out.println("sigma :"+word+" "+sig_error);
					//sig_error=(flag-sig_error);
					double sig_flag=(flag-sig_error);
					//System.out.println("sigma flag :"+word+" "+sig_error);
					
					//System.out.println("Count : "+count);
					delta_weight=delta_weight+count*sig_flag;
					//delta_weight=delta_weight+sig_error;
					//System.out.println("delta_weight :"+word+" "+delta_weight);
					}catch(Exception e){
					}
				}
				//System.out.println("All files done for one word");
				double new_wt = word_weight.get(word) + learning_rate*delta_weight -(learning_rate*lambda_constant*word_weight.get(word));
				
					//new_wt= (1.0 /(1.0+ Math.exp(-new_wt)));
				//if(new_wt<0)	
				//System.out.println("new wt :"+word+" "+new_wt);
				word_weight.put(word, new_wt);
				}catch(Exception e){
				}
				//delta_weight=0;
				
			}
			System.out.println("All words done");
				
			}
	}
	
	public static double helper(String file, String word)
	{
		
		if(spamfiles.contains(file))
		{
			double sig_error=0.1;
			try{
			Iterator<Entry<String, HashMap<String, Integer>>> parent = spam_file.entrySet().iterator();
			while (parent.hasNext()) 
			{
			    Entry<String, HashMap<String, Integer>> parentPair = parent.next();
			    //System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());
			   // if(parentPair.getValue().containsKey(word))
			    {
			    	sig_error = sig_error +((word_weight.get(parentPair.getKey()))*(parentPair.getValue().get(word)));
			    }
			}
			}
			catch(Exception E){
				//System.out.println( E);
				
			}
			return sig_error;
		}
		else
		{
			double sig_error=0.1;
			try
			{
			Iterator<Entry<String, HashMap<String, Integer>>> parent = ham_file.entrySet().iterator();
			while (parent.hasNext()) 
			{
			    Entry<String, HashMap<String, Integer>> parentPair = parent.next();
			    //System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());
			    //if(parentPair.getValue().containsKey(word))
			    {
			    	sig_error = sig_error +((word_weight.get(parentPair.getKey()))*(parentPair.getValue().get(word)));
			    }
			}
			}
			catch(Exception e){
				//System.out.println(e);
			}
			return sig_error;
		}
	}
	
	public static int word_count(String file,String word)
	{
		int count =0;
		if(spamfiles.contains(file))
		{count =0;
			try{
			Iterator<Entry<String, HashMap<String, Integer>>> parent = spam_file.entrySet().iterator();
			while (parent.hasNext()) 
			{
			    Entry<String, HashMap<String, Integer>> parentPair = parent.next();
			    //System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());
			    if(parentPair.getValue().containsKey(word))
			    {
			    	count=parentPair.getValue().get(word);
			    }
			}
			} catch (Exception e) {
			}
			
		}
		else if(hamfiles.contains(file))
		{//System.out.println("ham file has");
			count =0;
			try {
			Iterator<Entry<String, HashMap<String, Integer>>> parent = ham_file.entrySet().iterator();
			while (parent.hasNext()) 
			{
			    Entry<String, HashMap<String, Integer>> parentPair = parent.next();
			    //System.out.println("parentPair.getKey() :   " + parentPair.getKey() + " parentPair.getValue()  :  " + parentPair.getValue());
			    if(parentPair.getValue().containsKey(word))
			    {
			    	count=parentPair.getValue().get(word);
			    }
			}//System.out.println("ham loop ends ");
			} catch (Exception e) {
			}
			
		}
		return count;
				
			
	}
}
