package AIGenetic;
import java.util.Random;
//The class aiga
public class aiga 
{
	static aiga_out ao = new aiga_out("log.ini");
	//create the population and fitness score matrices
	public static int[][] p, ps; 
	public static int[] scores, scoress;
	//A random number generation method will be used so declare the object here
	public static Random rand = null;
	//and the solution we want
	public static int[] result = {1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1,0,1};
	//Main
	/*It handles the main functions of the algorithm.
	 *It is the skeleton of the entire program and calls the rest of the functions where they are needed */
	public static void main(String[] args)
	{//Some prints
		ao.write_msg("This is an implementation of a genetic algorithm", true);
		ao.write_msg("The algorithm is trying to produce the following bit sequence: \n"+result[0]+","+result[1]+","+result[2]+","+result[3]+","+result[4]+","+result[5]+","+result[6]+","+result[7]+","+result[8]+","+result[9]+","+result[10]+","+result[11]+","+result[12]+","+result[13]+","+result[14]+","+result[15]+","+result[16]+","+result[17]+","+result[18]+","+result[19]+","+result[20]+","+result[21]+","+result[22],true);
		ao.write_msg("The first population will now be produced", true);
		//p will hold the first population
		p = new int[20][23];
		//using the function fgen_init, we create 20 random bit sequences
		for(int i = 0; i <= 19; i++) 
		{
			ao.write_msg("Generating p["+i+"]",true);
			p[i] = fgen_init((76777+(301*i)));
			ao.write_msg("p["+i+"]:", false);
			fchr_wr(p[i]);
		}
		ao.write_msg("Generation of first population complete",true);		
		ao.write_msg("The fitness scores for the first population will now be calculated",true);
		//initialize the score matrices
		scores = new int[20];
		scoress = new int[20];
		int min = 23;//will be used as control in the main loop
		int min_index = -1;
		//the following loop will call the ffunction to calculate the scores for each chromosome
		for(int i = 0; i <= 19; i++)
		{
			ao.write_msg("Calculating score for: p["+i+"]",true);
			scores[i] = ffunction(i);
			//next we calculate the minimum score in case the solution comes early
			if (scores[i] < min) 
			{
				min = scores[i];
				min_index = i;
			}
		}	
		ao.write_msg("Calculation of the scores is complete",true);
		ao.write_msg("The main loop is about to begin...",true);
		//declare all needed variables
		double[] fp = null, fp_hat = null;
		int[] sel = new int[2], chr1, chr2;
		int[][] offspring = null; 
		int index = 0;
		int round = 1;
		ps = new int[20][23];
		double[] fsp = new double[20];
		/*the while loop will terminate when a solution is found.
		 *If the solution is already among the first population, the while loop will be skipped*/
		while(min!=0)
		{
			ao.write_msg("Round "+round,true);
			ao.write_msg("Calculation of the selection probabilities will now begin",true);
			/*Using fprob, calculate the selection probability of each chromosome.
			 *Since the best score is 0, the lower the selection probability is,
			 *the higher the chance will be for a chromosome to be selected.*/
			fp = fprob();
			//fsp is an array in which every element is the sum of alla elements up ti itself
			fsp[0]=fp[0];
			for(int i = 1; i <= 19; i++) fsp[i] = fp[i]+fsp[i-1];
			ao.write_msg("Calculation of the selection probabilities is complete",true);
			ao.write_msg("Selection of the 10 best chromosomes will now begin",true);
			index = 0;
			fp_hat = new double[20];
			for(int i = 0; i <= 19; i++) fp_hat[i]=fp[i];
			for(int i = 0; i <= 9; i++)
			{
				//find the best (lowest selection probability) chromosome
				index = fmin(fp_hat);
				ao.write_msg("Minimum at "+index,true);
				//Move the chromosome to ps
				for(int j = 0; j <= 22; j++)
				{
					ps[i][j] = p[index][j];
				}				
				ao.write_msg("Moved p["+index+"] to ps["+i+"]",true);
				//Then increase its selection probability so that the algorithm cannot choose it again
				fp_hat[index]=1;				
			}
			/*At this point, ps contains 10 chromosomes - the ones with the 10 lowest selection probabilities*/
			ao.write_msg("The selection of the 10 chromosomes that will remain is complete",true);
			ao.write_msg("The selection of the 10 parents will now begin",true);	
			//Now choose 5 pairs for crossover
			for(int i = 10; i <= 19; i+=2)//i is incremented by two since we save two chromosomes every round of the loop
			{
				ao.write_msg("Selecting parents for the chromosomes "+i+" and "+(i+1),true);
				//select two chromosomes
				for(int j = 0; j <= 19; j++) fp_hat[j]=fp[j];//this resets the probability matrix
				sel = frand_sel(fsp,(88883+(int)((10^(i-10))*3.3))+(int)((10^(round^2))*9.7));
				chr1 = p[sel[0]];//parent 1
				chr2 = p[sel[1]];//parent 2
				ao.write_msg("The selection of the parents is complete",true);
				ao.write_msg("The crossover for chromosomes "+i+" and "+(i+1)+" will begin",true);
				ao.write_msg("Parent 1: ",false);
				fchr_wr(chr1);
				ao.write_msg("Parent 2: ", false);
				fchr_wr(chr2);
				//crossover time	
				offspring = fcrossover(chr1, chr2, 33737+(int)((10^(i-10))*7.7)+(int)(round*(13^(round+4))));
				ps[i]=offspring[0];//place the chromosomes in the ps array
				ps[i+1]=offspring[1];
				ao.write_msg("The crossover for chromosomes "+i+" and "+(i+1)+" is complete",true);
				ao.write_msg("Offspring 1: ", false);
				fchr_wr(ps[i]);
				ao.write_msg("Offspring 2", false);
				fchr_wr(ps[i+1]);
			}
			ao.write_msg("The mutation will now begin",true);
			fmutate(93467+(int)((10^round)*3.7));
			ao.write_msg("Mutation is complete",true);
			ao.write_msg("The array p will now be overwritten with the elements of the array ps",true);
			for(int i = 0; i <= 19; i++)
			{
				for(int j = 0; j <= 22; j++)
				{
					p[i][j]=ps[i][j];
				}
			}
			ao.write_msg("The array p has been overwritten",true);
			ao.write_msg("New population: ", true);
			for(int i = 0; i <= 19; i++)
			{
				ao.write_msg("p["+i+"]: ", false);
				fchr_wr(p[i]);
			}
			ao.write_msg("The calculation of the new scores will now begin",true);
			min = 23;
			min_index = -1;
			for(int i = 0; i <= 19; i++)
			{
				ao.write_msg("Calculating score for: p["+i+"]",true);
				scores[i] = ffunction(i);
				//next we calculate the minimum score in case the solution comes early
				if (scores[i] < min) 
				{
					min = scores[i];
					min_index = i;
				}
			}	
			ao.write_msg("Calculation of the new scores is complete",true);
			ao.write_msg("New minimum is: "+min,true);
			ao.write_msg("Round "+round+" is complete",true);
			round++;
		}
		ao.write_msg("The solution was achieved after "+(round-1)+" rounds.",true);
		ao.write_msg("Solution index: "+min_index,true);
		ao.exit();
		System.exit(0);
	}
	//fgen_init
	/*This function uses a seed to create a random number sequence and, based on that,
	 *creates a pseudo random bit sequence to be used as a chromosome */
	public static int[] fgen_init(int seed)
	{
		ao.write_msg("fgen_init called; seed = "+seed,true);
		//create an instance of the random generator using the seed
		rand = new Random(seed);
		//chr will hold the new chromosome
		int[] chr = new int[23];
		//And now, the loop that will create the sequence
		for(int i = 0; i <= 22; i++) 
		{
			/*Rand will yield an integer between 0 and 9;
			 * That integer mod2 is 1 for odd numbers or a 0 for even ones;
			 * That integer + 1 mod2 is the same, but vice versa. */
			chr[i] = (rand.nextInt(10)+1)%2;
		}
		//return the chromosome
		ao.write_msg("Back to main...",true);
		return chr;
	}
	//ffunction
	/*This function is the fitness function of the algorithm. It counts the number of genes
	 *a chromosome has that are different from those of the solition 
	 *and returns it to the main function. */
	public static int ffunction(int index)
	{
		ao.write_msg("ffunction called: index = "+index,true);
		int score = 0;//temporary variable for the score
		for (int i = 0; i <= 22; i++)
		{
			 /*Performing a subtraction between each gene of the chromosome and the result
			  *will give us an estimate of the difference between them. If the genes are equal,
			  *the result will be 0. If the total score of a chromosome is 0, 
			  *the algorithm has found the solution */
			ao.write_msg("Calculating score for gene number: "+i,true);
			 if((result[i] - p[index][i]) != 0) score += 1;
		}
		ao.write_msg("Total score is: "+score+"\nBack to main...",true);
		return score;
	}
	//fprob
	/*This function computes the fitness probability.
	 *For each chromosome, the value of score[i]/sum(scores) and an array with that value is returned.*/
	public static double[] fprob()
	{
		ao.write_msg("fprob called - no arguments",true);
		int sum = 0;
		double[] fp = new double[20];
		//first, calculate the sum of the scores
		for(int i = 0; i <= 19; i++) sum+=scores[i];
		ao.write_msg("Scores sum = "+sum,true);
		//next calculate the selection probability for each chromosome
		for(int i = 0; i <= 19; i++) 
		{
			fp[i]=(double)scores[i]/sum;
			ao.write_msg("Chromosome "+i+"; selection probability: "+fp[i],true);			
		}
		ao.write_msg("Back to main...",true);
		return fp;
	}
	//fmin
	/*This function finds the minimum in the double[] argument*/
	public static int fmin(double[] array)
	{
		int index = 0;
		double min = 1.1;
		ao.write_msg("fmin called - array argument length: "+array.length,true);
		for(int i = 0; i < array.length; i++)
		{
			ao.write_msg("Processing array["+i+"] = "+array[i],true);
			if(array[i] < min) 
			{
				ao.write_msg("Found lesser than "+min+"; array["+i+"];",true);
				min = array[i];
				index = i;
			}
		}
		ao.write_msg("Found minimum at "+index,true);
		ao.write_msg("Back to main...",true);
		return index;
	}
	//frand_sel
	/*This functions chooses two chromosomes as parents for a crossover.
	 *Two random numbers [0,99] are picked and the function checks in which 
	 *field of the fsp array each number belongs. The index of the field is 
	 *then the index to the parent in the array p of main */
	public static int[] frand_sel(double[] ffp, int seed)
	{
		ao.write_msg("frand_sel called - arguments array length: "+ffp.length+"; seed: "+seed+";",true);
		int[] sel = {-1, -1};//here the indices of the two parents will be saved
		rand = new Random(seed);//initialize the generator
		int ch1 = rand.nextInt(100);//pick two random numbers between 0 and 99
		int ch2 = rand.nextInt(100);
		ao.write_msg("Random numbers: "+ch1+", "+ch2,true);
		for(int i = 0; i < ffp.length; i++)
		{//first, turn p into a % chance			
			ao.write_msg("Processing ffp["+i+"] = "+ffp[i],true);
			//if one of the numbers belong in in an element, set that i as the index of the parent
			if((ch1 <= ffp[i]*100)&&(sel[0]==-1)) {sel[0]=i; ao.write_msg("Parent 1 found: p["+i+"]",true);}
			if((ch2 <= ffp[i]*100)&&(sel[1]==-1)) {sel[1]=i; ao.write_msg("Parent 2 found: p["+i+"]",true);}			
		}		
		//if both numbers belong in the same area, change the second index
		if(sel[0]==sel[1]) 
		{
			sel[1]=(int)(sel[0]+rand.nextInt(10))%19;
			ao.write_msg("Same parent chosen twice; Changing sel[1] to "+sel[1],true);
		}
		ao.write_msg("Back to main... returning: "+sel[0]+" and "+sel[1],true);
		//return the choices
		return sel;
	}
	//fcrossover
	/*This function crosses over two chromosomes. The crossover point is decided randomly.*/
	public static int[][] fcrossover(int[] chr1, int[] chr2, int seed)
	{
		ao.write_msg("fcrossover called; seed = "+seed,true);
		int[][] offs = new int[2][23];
		rand = new Random(seed);//the random number generator
		int croff = rand.nextInt(23);//one random integer [0,22]
		if(croff<=2) ++croff;//if the offset point is less than 3 		
		else if(croff>=20) --croff;//or more than 20, add or subtract 1 respectively
		ao.write_msg("Crossover point is: "+croff,true);
		for(int i = 0; i <= 22; i++)
		{
			if(i < croff)//copy the genes up to the cross point
			{
				offs[0][i]=chr1[i];//chr1 -> offspring1
				offs[1][i]=chr2[i];//chr2 -> offspring2
			}
			else
			{//and after the cross point
				offs[1][i]=chr1[i];//chr1 -> offspring2
				offs[0][i]=chr2[i];//chr2 -> offspring1
			}
		}
		ao.write_msg("Back to main...",true);
		return offs;
	}
	//fmutate
	/*This function handles the mutation (10% of population).
	 *The function performs all changes directly on the global ps array*/
	public static void fmutate(int seed)
	{
		ao.write_msg("fmutate called; seed = "+seed,true);
		rand = new Random(seed);
		int[] sel = new int[2];
		sel[0]=rand.nextInt(20);
		sel[1]=rand.nextInt(20);
		if(sel[0]==sel[1]) sel[1]=(int)(sel[0]+rand.nextInt(10))%20;
		ao.write_msg("Mutation will be performed on chromosomes "+sel[0]+" and "+sel[1],true);
		int index = rand.nextInt(23);//the random gene of the chromosome
		ps[sel[0]][index] = ((ps[sel[0]][index])+1)%2;
		ao.write_msg("Chromosome: "+sel[0]+"; gene changed from "+((ps[sel[0]][index])+1)%2+" to "+ps[sel[0]][index],true);
		index = rand.nextInt(23);
		ps[sel[1]][index] = ((ps[sel[1]][index])+1)%2;
		ao.write_msg("Chromosome: "+sel[1]+"; gene changed from "+((ps[sel[1]][index])+1)%2+" to "+ps[sel[1]][index],true);
		ao.write_msg("Back to main...",true);
	}
	//fchr_wr
	/*This function prints the chromosome array argument*/
	public static void fchr_wr(int[] chr)
	{
		for(int i = 0; i <= 22; i++) ao.write_msg(""+chr[i], false);
		ao.write_msg("", true);
	}
}
