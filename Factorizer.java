import java.math.BigInteger;
import java.util.*;
//import java.math.*;


/**
 * Factorizes an integer
 * @author Kristoffer Hallqvist, Dmitrij Lioubartsev
 */
public class Factorizer {
	//Fields:
	private ArrayList<Integer> smallPrimes;	//Hard coded primes
	
	//private final static int MAX_B = 500;
	//private final static int MAX_M = 500;
	
	/* from http://primes.utm.edu/lists/small/1000.txt*/
	int[] primes =  {2,  3,   5,      7  ,   11    , 13    , 17   ,  19  ,   23  ,   29 ,
		     31  ,   37   ,  41   ,  43  ,   47   ,  53  ,   59 ,    61 ,    67  ,   71 ,
		     73   ,  79  ,   83   ,  89   ,  97  ,  101   , 103  ,  107   , 109  ,  113 ,
		    127  ,  131  ,  137  ,  139 ,   149  ,  151 ,  157  ,  163   , 167  ,  173 ,
		    179   , 181  ,  191  ,  193  ,  197 ,   199 ,   211  ,  223 ,   227  ,  229 ,
		    233   , 239 ,   241 ,   251  ,  257  , 263 ,  269   , 271  ,  277  ,  281 ,
		    283   , 293   , 307  ,  311 ,   313  ,  317  ,  331 ,   337  ,  347  ,  349 ,
		    353   , 359 ,   367 ,   373 ,   379  ,  383  ,  389   , 397 ,   401  ,  409 ,
		    419   , 421 };
	
	private int B;
	private int M;
	
	//Methods
	/**
	 * Constructor
	 */
	public Factorizer() {
		smallPrimes = new ArrayList<Integer>();
		smallPrimes.add(2);
		smallPrimes.add(3);
		smallPrimes.add(5);
		smallPrimes.add(7);
		smallPrimes.add(11);
		smallPrimes.add(13);
		smallPrimes.add(17);
		smallPrimes.add(19);
		smallPrimes.add(23);
		smallPrimes.add(29);
		smallPrimes.add(31);
		smallPrimes.add(37);
		smallPrimes.add(41);
		smallPrimes.add(43);
		smallPrimes.add(47);
		smallPrimes.add(53);
		smallPrimes.add(59);
		smallPrimes.add(61);
		smallPrimes.add(67);
		smallPrimes.add(71);
		smallPrimes.add(73);
		smallPrimes.add(79);
		smallPrimes.add(83);
		smallPrimes.add(89);
		smallPrimes.add(97);
		smallPrimes.add(101);
		smallPrimes.add(103);
		//Add more primes if you wish.
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) {
		//System.err.println("It works!");
		//System.err.println("Input a number");
		Factorizer factorizer = new Factorizer();
		BigInteger input;
		
		if (args.length > 0) {
			try {
				input = new BigInteger(args[0]);
				
				ArrayList<BigInteger> result = factorizer.factorize(input);
				for (int i = 0;i<result.size();i++) {
					System.out.println(result.get(i));
				}
				System.out.println();
			} catch (Exception e) {
				System.err.println("Invalid number format.");
			}
			return;
		}

		Scanner sc = new Scanner(System.in);
		
		while (sc.hasNextLine()) {
			try {
				input = new BigInteger(sc.nextLine());
			} catch (Exception e) {
				System.err.println("Invalid number format.");
				return;
			}
			System.out.println("Square root is: " + factorizer.longSqrt(input));
			
			ArrayList<BigInteger> result = factorizer.factorize(input);
			for (int i = 0;i<result.size();i++) {
				System.out.println(result.get(i));
			}
			System.out.println();
		}
	}
	
	/**
	 * Factorize an integer using an appropriate method
	 */
	private ArrayList<BigInteger> factorize(BigInteger input) {
		//TODO
		//Choose the appropriate method
		
		//Some input checking, although Kattis
		//doesn't give us number smaller than 2
		if (BigInteger.ONE.equals(input.max(BigInteger.ONE))) {
			System.err.println("Input is smaller than 2...");
			System.exit(0);
		}
		
		return factorizeNaive(input);
	}
	
	/**
	 * Factorize an integer using the quadratic sieve method
	 */
	private ArrayList<BigInteger> factorizeQS(BigInteger input) {
		//TODO
		B = 500;
		M = 1000000; //1 million
		long sqrtn = longSqrt(input);
		ArrayList<ArrayList<Integer>> factorBase = getLegendrePrimes(input,B);
		ArrayList<QVectorElement> finalQs = findQs(factorBase,M,sqrtn,input);
		BitSet[] Qarray = new BitSet[finalQs.size()];
		for(int i = 0; i < finalQs.size(); i++) {
			Qarray[i] = finalQs.get(i).getBitSet();
		}
		
		GaussEliminator ge = new GaussEliminator();
		BitSet[] sol = ge.gaussEliminate(Qarray, Qarray.length, factorBase.size()+1);
		
		
		
		
		
		return null;
	}
	
	/**
	 * Factorize an integer using Pollard's rho method
	 */
	private ArrayList<BigInteger> factorizePolRho(BigInteger input) {
		//TODO maybe
		return null;
	}
	
	/**
	 * Factorize an integer using naive trial division
	 */
	private ArrayList<BigInteger> factorizeNaive(BigInteger input) {
		//TODO
		ArrayList<BigInteger> result = new ArrayList<BigInteger>();
		BigInteger[] dar;
		BigInteger curNum = input;
		long sq = longSqrt(input);
		long l = 0;
		
		for (int i = 0; l <= sq;) {
			if (i < smallPrimes.size()) {
				l = smallPrimes.get(i);
				i++;
			} else {
				l+=2;
			}
			
			while (true) {
				dar = curNum.divideAndRemainder(BigInteger.valueOf(l));
				if (dar[1].equals(BigInteger.ZERO)) {
					//Divisible
					result.add(BigInteger.valueOf(l));
					curNum = dar[0];
				} else {
					break;
				}
			}
			
			if (curNum.equals(BigInteger.ONE)) {
				break;
			}
			sq = longSqrt(curNum);
		}
		if (!curNum.equals(BigInteger.ONE)) {
			result.add(curNum);
		}

		return result;
	}
	
	/**
	 * @return The integer square root (BigInteger) of the input value
	 */
	private BigInteger bigSqrt(BigInteger n) {
		BigInteger min = BigInteger.ONE;
		BigInteger max = n;
		
		
		while(max.subtract(min).compareTo(BigInteger.ONE) == 1) {
			BigInteger c = max.add(min).shiftRight(1);
			BigInteger pow = c.pow(2);
			int comp = pow.compareTo(n);
			if(comp == 0) {
				//they are equal
				//found perfect square
				return c;
			} else if(comp == 1) {
				//c^2 is greater than n
				max = c.subtract(BigInteger.ONE);
			} else {
				min = c;
			}
		}
		
		//now we have a situation where max-min == 1 
		if(max.pow(2).compareTo(n) < 1) {
			return max;
		} else {
			return min;
		}
		//return BigInteger.valueOf(longSqrt(n));
	}
	
	/**
	 * @return The integer square root (long) of the input value
	 * 
	 * Uses a binary search to find it. Complexity is O( b*log(n)*O(p(b,2)) ) where
	 * n = size of integer to root
	 * b = bits in n (== log(n))
	 * O(p(b,2)) is the complexity of BigInteger.pow(2), a biginteger with b bits.
	 */
	private long longSqrt(BigInteger n) {
		/*
		int numBits = n.bitLength();
		int sqrtNumBits = numBits/2;	//Eller nåt sånt
		BigInteger max = BigInteger.ONE.shiftLeft(sqrtNumBits+1);
		BigInteger min = max.shiftRight(1);
		System.err.println("Min and max are: " + min.toString() + " & " + max.toString());
		System.err.println("Numbits and sqrtNumBits are: " + numBits + " & " + sqrtNumBits);
		*/
		return bigSqrt(n).longValue();
		
		//double d = n.doubleValue();
		//return (long)Math.sqrt(d);
		//TODO Don't know if this is accurate enough
	}
	
	/**
	 * Get an arraylist of arraylists. The inner arraylists' first element
	 * is a prime p less than B which fulfill Legendre(n,p) == 1. All such primes have their
	 * own inner arraylist. What follows is up to two different integers which are the solutions
	 * to that equation.
	 * @param n
	 * @param B
	 * @return
	 */
	public ArrayList<ArrayList<Integer>> getLegendrePrimes(BigInteger n, int B) {
		ArrayList<ArrayList<Integer>> factorBase = new ArrayList<ArrayList<Integer>>();
		for(int i = 0; i < primes.length; i++) {
			int p = primes[i];
			if(p > B) {
				return factorBase;
			} 
			int ni = (int) n.mod(BigInteger.valueOf(p)).longValue();
			ArrayList<Integer> psols = new ArrayList<Integer>();
			int numFound = 0;
			for(int j = 1; j < p; j++) {
				int tmp = (j*j) % p;
				if(tmp == ni) {
					if(psols.isEmpty())
						psols.add(p);
					
					psols.add(j); //j is solution
					numFound++;
					//factorBase.add();
					//break;
					if(numFound == 2) {
						break; //can only be two solutions.
					}
				}
					
			}
			
			if(!psols.isEmpty()) {
				factorBase.add(psols);
			}
			
		}
		
		return factorBase;
	}
	
	public ArrayList<QVectorElement> findQs(ArrayList<ArrayList<Integer>> psols, long M, long sqrtn, BigInteger n) {
		//initialize BitSet arraylist
		ArrayList<QVectorElement> sols = new ArrayList<QVectorElement>();
		final int EXTRA_Qs = 10;
		
		//for(int i = 0; i < psols.size() + EXTRA_Qs;i++) {
			//sols.add(new BitSet(psols.size()+1)); //initialized to all zeros.
		//}
		
		long interSizeHalf = 2500;
		
		//first interval
		BigInteger inter_min = BigInteger.valueOf(sqrtn-interSizeHalf);
		BigInteger inter_max = BigInteger.valueOf(sqrtn+interSizeHalf);
		
		ArrayList<QVectorElement> QVector = new ArrayList<QVectorElement>();
		for(BigInteger i = inter_min; i.compareTo(inter_max) <= 0; i = i.add(BigInteger.ONE)) {
			QVector.add(new QVectorElement(psols,i,n));
		}
		
		
		for(int i = 0; i < psols.size(); i++) {
			ArrayList<Integer> psol = psols.get(i);
			int p = psol.get(0);
			BigInteger pbi = BigInteger.valueOf(p);
			for(int j = 1; j < psol.size();j++) {
				BigInteger x = BigInteger.valueOf(psol.get(j));
				//now we have our x - put it at the beginning of the interval.
				int comp = x.compareTo(inter_min);
				if(comp == -1) {
					//basic idea: k*p + r = inter_min   => k*p = inter_min-r
					
					// r = inter_min % p
					BigInteger r = inter_min.mod(pbi);
					
					// k*p = inter_min-r
					// k*p will always be smaller than or equal to inter_min
					// however, smallest possible x is k*p+x
					BigInteger mbi = inter_min.subtract(r).add(x); 
					
					//this may already be in the interval
					//if it isnt, add p to it.
					if(mbi.compareTo(inter_min) == -1) { //if still not enough
						x = mbi.add(pbi);
					} else {
						x = mbi;
					}
				} else if(comp == 0){
					//do nothing, x is already there!
				} else {
					//lower bound is smaller than current x
					//similar thoughts as above. but now we seek x - k*p
					//TODO
				}
				
				while(x.compareTo(inter_max) <= 0) {
					QVectorElement qve = QVector.get((int) x.subtract(inter_min).longValue());
					if(qve.divideQWith(i)) {
						sols.add(qve);
						if(sols.size() == psols.size()+EXTRA_Qs) {
							//RETURN!!!!!
							return sols;
						}
					}
					
					x = x.add(pbi); //go to next x.
				}
				
				
			}
		}
		//couldnt find adequate Q-vector
		
		return null;
	}
	
	/**
	 * Return the factors from the given solution.
	 * @param factored a bitset of the Qs to use in the calculations.
	 * @param qVector A vector of all Qs of which the remainder Q is 1.
	 * @param n The number to factor.
	 * @return
	 */
	public BigInteger[] getFactors(BitSet factored, ArrayList<QVectorElement> qVector, BigInteger n) {
		
		//ArrayList<QVectorElement> relevant = new ArrayList<QVectorElement>();
		BigInteger x = BigInteger.ONE;
		BigInteger y = BigInteger.ONE;
		for(int i = 0; i < qVector.size(); i++) {
			if(factored.get(i)) {
				QVectorElement qve = qVector.get(i);
				x = x.multiply(qve.getX()).mod(n);
				y = y.multiply(qve.getOriginalQ()).mod(n); //keep the numbers "small"
			}
		}
		
		y = bigSqrt(y);
		
		BigInteger[] factors = new BigInteger[2];
		factors[0] = x.subtract(y).gcd(n);
		factors[1] = x.add(y).gcd(n);
		
		return factors;
	}
	
	
	
}





