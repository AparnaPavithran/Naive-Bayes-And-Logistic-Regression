import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;
import java.util.Map.Entry;

public class NB {
	public static Set<String> allwords = new HashSet<String>();
	public static Set<String> stopwords = new HashSet<String>();
	
	public static TreeMap<String,Integer> spam_file = new TreeMap<String,Integer>();
	public static TreeMap<String,Integer> ham_file = new TreeMap<String,Integer>();
	
	public static HashMap<String,Double> spam_prob = new HashMap<String,Double>();
	public static HashMap<String,Double> ham_prob = new HashMap<String,Double>();
	
	public static void main(String[] args){
		System.out.println("Started");
		//int k=1;
		//if(k==1)
		if(args.length==2)
		{
			String directory_location = args[0];
			String stopword_filter=args[1];
		
		//String directory_location = "/Users/APARNA/Documents/workspace/TEST_NB/src";
		//String stopword_filter="yes";
		
		File spam_loc = new File(directory_location+"/train/spam");
		File stop_loc = new File(directory_location+"/stopwords.txt");
		
		String[] special_symbol = {"!","#","%","^","&","*","(",")", ":",".","{","}", "[","]",">","<","?","/", "*","~", "@"};
		
		//Scanner sc=null;
		File[] files = spam_loc.listFiles(); 
		if (files != null) 
		{ 
			for (File spam : files) 
		{
			//System.out.println("adding spam words"+spam.getName());
			try {
				Scanner sc = new Scanner(spam);
				while(sc.hasNextLine())
				{
					String wordlist= sc.nextLine();
					String[] s=wordlist.split(" ");
					for(int i=0;i<s.length;i++)
					{
						//s[i]=s[i].replaceAll("[^a-zA-Z0-9]+", ""); //all special symbols and numbers are removed
						s[i]=s[i].toLowerCase().trim();
						if(!s[i].isEmpty())
						{							
							//s[i]=s[i].toLowerCase();
								allwords.add(s[i]);
						}
					}
					
				}
				sc.close();
			}
			catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			}
			}	
		}//while(spam.listFiles() != null)
		
		System.out.println("Spam words added to all words");
		
		//add ham words to all words list
		File ham_loc = new File(directory_location+"/train/ham");
		//Scanner sc1=null;
		File[] file_h = ham_loc.listFiles(); 
		if (file_h != null) 
		{ 
			for (File ham : file_h) 
		{
				//System.out.println("adding ham words"+ham.getName());
			try {
				Scanner sc1 = new Scanner(ham);
				while(sc1.hasNextLine())
				{
					String wordlist= sc1.nextLine();
					String[] s=wordlist.split(" ");
					for(int i=0;i<s.length;i++)
					{
						//s[i]=s[i].replaceAll("[^a-zA-Z0-9]+", "");//all special symbols and numbers are removed
						s[i]=s[i].toLowerCase().trim();
						if(!s[i].isEmpty())
						{							
							//s[i]=s[i].toLowerCase();
							allwords.add(s[i]);
						}
					}
					
				}
				sc1.close();
			}
			catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			
			}
		}
			
		}//while(ham.listFiles() != null)
				
		System.out.println("Ham words added to all words");
		//all words are added to word file
		
		//Scanner sc_st=null;
		try {
			Scanner sc_st = new Scanner(stop_loc);
		
		while(sc_st.hasNext()){
			String sw = sc_st.next();
			stopwords.add(sw);
		}
		sc_st.close();
		} catch (FileNotFoundException e) {

			//e.printStackTrace();
		}
		
		
		for(String s1: special_symbol){
			if(allwords.contains(s1) ){
				allwords.remove(s1);
			}
			if(spam_file.containsKey(s1) ){
				spam_file.remove(s1);
			}
			if(ham_file.containsKey(s1) ){
				ham_file.remove(s1);
				
			}
		}
		if(stopword_filter.equals("yes")){

			System.out.println("Removing stop words...");
			for(String str : stopwords){
				if(allwords.contains(str)){
					allwords.remove(str);
				}
			}

		}
		
		
		//add words to spam word and file lists
		//Scanner sc3= null;
		File[] sp_f = spam_loc.listFiles(); 
		if (sp_f != null) 
		{ 
			for (File spam1 : sp_f) 
		{
			
			try {
				Scanner sc3=new Scanner(spam1);
				while(sc3.hasNextLine())
				{
					String wordlist= sc3.nextLine();
					String[] s=wordlist.split(" ");
					
					for(int i=0;i<s.length;i++)
					{
						//System.out.println("s[i] "+s[i]);
						//s[i]=s[i].replaceAll("[^a-zA-Z0-9]+", "");//all special symbols and numbers are removed
						s[i]=s[i].toLowerCase().trim();
						if(!s[i].isEmpty())
						{							
							//s[i]=s[i].toLowerCase();
							//if(allwords.contains(s[i]))
							{
								if(spam_file.containsKey(s[i]))
								{
									int count=spam_file.get(s[i])+1;
									spam_file.put(s[i], count);
									//System.out.println("templist "+temp_list.get(s[i]));
								}
								else
								{
									spam_file.put(s[i], 1);
								}
							}
						}
					}
					
					//System.out.println("spam file has"+temp_list.get(spam1));
					//System.out.println("before "+spam_file.size());
					
					//System.out.println("after "+spam_file.size());
					//System.out.println("spam file has"+spam_file.get(spam1));
				}//for each file by file	
				sc3.close();	
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			}
		}
			
		}
		System.out.println("Spam words added to spam file");
			
		
		//add words to ham word and file lists
		//Scanner sc4= null;
		File[] h_f = ham_loc.listFiles(); 
		if (h_f != null) 
		{ 
			for (File ham1 : h_f) 
		{
			try {
				Scanner sc4=new Scanner(ham1);
				
				while(sc4.hasNextLine())
				{
					String wordlist= sc4.nextLine();
					String[] s=wordlist.split(" ");
					
					for(int i=0;i<s.length;i++)
					{
						//s[i]=s[i].replaceAll("[^a-zA-Z0-9]+", "");//all special symbols and numbers are removed
						s[i]=s[i].toLowerCase().trim();
						if(!s[i].isEmpty())
						{							
							//s[i]=s[i].toLowerCase();
							//if(allwords.contains(s[i]))
							{
								if(ham_file.containsKey(s[i]))
								{
									int count=ham_file.get(s[i])+1;
									ham_file.put(s[i], count);
								}
								else
								{
									ham_file.put(s[i], 1);
								}
							}
						}
					}
				}//for each file by file	
				sc4.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			}
		}
		}
		System.out.println("Ham words added to ham file");
		
		
		
		//learn
		learn();
		
		//test
		//int c=test(directory_location);
		
		//////
		File spam_test_loc = new File(directory_location+"/test/spam");
		File ham_test_loc = new File(directory_location+"/test/ham");
		double spam_tot_prob =0.0;
		File[] sp_L = spam_loc.listFiles(); 
		File[] h_L = ham_loc.listFiles(); 
		if (sp_L != null && h_L != null) 
		{ 
			spam_tot_prob = 1.0*(spam_loc.listFiles().length)/(  spam_loc.listFiles().length + ham_loc.listFiles().length ) ;
		}

		double spam_tot_prob_LL = Math.log(spam_tot_prob);
		
		double num_crt_spam =0;
		int sp = 0;
		System.out.println();
		File[] sp_t = spam_test_loc.listFiles(); 
		if (sp_t != null) 
		{ 
		for(File file: spam_test_loc.listFiles()){
			sp = sp +1;
			try {
				if(test(file, spam_tot_prob_LL) == 1){
					num_crt_spam = num_crt_spam + 1.0;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		}
		
		
		//System.out.println();
		double spam_acc = num_crt_spam/sp*100; 
		//System.out.println("Accuracy Spam : "+ spam_acc);
		
		////
		/*
		double num_crt_spam =0;
		int sp = 0;
		System.out.println();
		for(File file: spam_test_loc.listFiles())
		{
			sp = sp +1;
			int c;
			try {
				c = test (directory_location,file,"spam");
			
			if( c == 1){
				num_crt_spam = num_crt_spam + 1.0;
			}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			}
		}
		*/
		if(stopword_filter.equals("yes")){
			System.out.println("Accuracy of Naive Bayes after removing Stop Words:");
		}
		else{
			System.out.println("Accuracy of Naive Bayes with out removing Stop Words: ");
		}
		//System.out.println();
		/*
		double spam_acc = num_crt_spam/sp*100; 
		System.out.println("correct : "+num_crt_spam+" total :"+sp);
		System.out.println("Accuracy Spam : "+ spam_acc);
		
		double num_crt_ham =0;
		int h=0;
		for(File file: ham_test_loc.listFiles()){
			h=h+1;
			int c;
			try {
				c = test (directory_location,file,"ham");
			
			if(c == 0){
				num_crt_ham = num_crt_ham + 1.0;
			}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
			}
		}*////
		double num_crt_ham =0;
		int h=0;
		File[] h_t = ham_test_loc.listFiles(); 
		if (h_t != null) 
		{ 
		for(File file: ham_test_loc.listFiles()){
			h=h+1;
			try {
				if(test(file, spam_tot_prob_LL) == 0){
					num_crt_ham = num_crt_ham + 1.0;
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		}
		//System.out.println();
		double ham_acc = num_crt_ham/h*100; 
		//System.out.println("Accuracy Ham : "+ ham_acc);
		
		double acc=0.0;
		double tot=(double)sp+(double)h;
		acc=(ham_acc*((double)num_crt_ham/tot))+(spam_acc*((double)num_crt_spam/tot));
		//System.out.println();
		System.out.println("Accuracy Naive Base : "+acc);
		System.out.println();
		
		
		////
		//calculate accuracy
		//System.out.println();
		/*double ham_acc = num_crt_ham/h*100; 
		System.out.println("correct : "+num_crt_ham+" total :"+h);
		System.out.println("Accuracy Ham : "+ ham_acc);
		double acc=0.0;
		double tot=(double)sp+(double)h;
		acc=(ham_acc*((double)num_crt_ham/tot))+(spam_acc*((double)num_crt_spam/tot));
		System.out.println();
		System.out.println("Accuracy Naive Base : "+acc);
		System.out.println();
		*/
		}
		else
		{
			System.out.println("Input string not proper");
		}
	}
	
	public static void learn()
	{
		int spam_count=w_count(spam_file);
		int ham_count=w_count(ham_file);
		
		for(String s : allwords)
		{
			if(spam_file.containsKey(s))
			{
				double spam_LH=(spam_file.get(s)+1.0)/(spam_count+allwords.size()+1.0);
				double spam_LL=Math.log(spam_LH);
				spam_prob.put(s, spam_LL);
			}
			else if(ham_file.containsKey(s))
			{
				double ham_LH=(ham_file.get(s)+1.0)/(ham_count+allwords.size()+1.0);
				double ham_LL=Math.log(ham_LH);
				ham_prob.put(s, ham_LL);
			}
		}
	}
	
	public static int w_count(TreeMap<String,Integer> count_file)
	{
		int count=0;
		for(Entry<String, Integer> entry: count_file.entrySet()){
			count = count + entry.getValue();
		}
		
		return count;
	}
	
	public static int test(File file,double spam_tot_prob_LL) throws FileNotFoundException 
	{
		
		/*
		File spam_test_loc = new File(directory_location+"/train/spam");
		File ham_test_loc = new File(directory_location+"/train/ham");
		double spam_tot=spam_test_loc.listFiles().length;
		double ham_tot=ham_test_loc.listFiles().length;
		
		double tot_file_prob_spam = 1.0*(spam_tot)/(spam_tot+ham_tot) ;
		double tot_file_prob_ham = 1.0*(ham_tot)/(spam_tot+ham_tot);

		tot_file_prob_spam = Math.log(tot_file_prob_spam);
		tot_file_prob_ham = Math.log(tot_file_prob_ham);
		
		spam_tot=w_count(spam_file);
		ham_tot=w_count(ham_file);
		
		Scanner scanner= new Scanner(file);
		
		double file_spam_prob = 0.0;
		double file_ham_prob = 0.0;
		
		while(scanner.hasNext()){
			String line = scanner.nextLine();
            	for(String s : line.toLowerCase().split(" ")){
            			
    					if(spam_prob.containsKey(s)){
    						file_spam_prob = file_spam_prob + spam_prob.get(s);
    					}else{

    						file_spam_prob = file_spam_prob + Math.log(1.0 / (allwords.size()+1.0)) ;
    						//file_spam_prob = file_spam_prob + 1.0 / (allwords.size()+1.0) ;

    					}
    					if(ham_prob.containsKey(s)){
    						file_ham_prob = file_ham_prob + ham_prob.get(s);
    					}else{
    						file_ham_prob = file_ham_prob +  Math.log( 1.0 / (allwords.size()+1.0));
    						//file_ham_prob = file_ham_prob +  1.0 / (allwords.size()+1.0);
    					}

    				}	
            
		}
		scanner.close();
		double value=0.0;
		if(type=="spam")
		{
			//value = 
			value=1+(1/(Math.exp(file_ham_prob)*Math.exp(file_ham_prob)));
		}
		else
		{
			value=1+(1/(Math.exp(file_spam_prob)*Math.exp(file_spam_prob)));
		}
		file_spam_prob = file_spam_prob + tot_file_prob_spam;
		file_ham_prob = file_ham_prob + tot_file_prob_ham;

		if(file_spam_prob < file_ham_prob)
			return 1; 
		else
			return 0;
		/*if(type=="spam")
		{
			if(value>1.6)
				return 1;
			else 
				return 0;
		}
		else
		{
			if(value>1.6)
				return 0;
			else 
				return 1;
		}*/
		
		Scanner scanner = new Scanner(file);
		double crnt_spam_prob = 0.0;
		double crnt_ham_prob = 0.0;
		double spam_tot=w_count(spam_file);
		double ham_tot=w_count(ham_file);
		
		while(scanner.hasNext()){
			String line = scanner.nextLine();
            	for(String s : line.toLowerCase().split(" ")){
            			
    					if(spam_prob.containsKey(s)){
    						crnt_spam_prob = crnt_spam_prob + spam_prob.get(s);
    					}else{

    						crnt_spam_prob = crnt_spam_prob + Math.log(1.0 / (spam_tot + allwords.size()+1.0)) ;

    					}
    					if(ham_prob.containsKey(s)){
    						crnt_ham_prob = crnt_ham_prob + ham_prob.get(s);
    					}else{
    						crnt_ham_prob = crnt_ham_prob +  Math.log( 1.0 / (ham_tot + allwords.size()+1.0));
    					}

    				}	
            
		}
		scanner.close();
		crnt_spam_prob = crnt_spam_prob + spam_tot_prob_LL;
		crnt_ham_prob = crnt_ham_prob + spam_tot_prob_LL;

		if(crnt_spam_prob < crnt_ham_prob){
			return 1; 
		}

		else{
			return 0;
		}

		
		
	}
	
}
