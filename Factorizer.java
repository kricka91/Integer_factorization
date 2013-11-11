import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;
//import java.math.*;


/**
 * Factorizes an integer
 * @author Kristoffer Hallqvist, Dmitrij Lioubartsev
 */
public class Factorizer {
	//Fields:
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
	
	private BigInteger threshHold = new BigInteger("100000");
	
	//Methods
	/**
	 * Constructor
	 */
	public Factorizer() {
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
			//System.out.println("Square root is: " + factorizer.longSqrt(input));
			//System.out.println("Cube root is: " + factorizer.longMRoot(input,4));

			
			ArrayList<BigInteger> result = factorizer.factorize(input);
			if(result == null) {
				System.out.println("fail");
			} else {
				for (int i = 0;i<result.size();i++) {
					System.out.println(result.get(i));
				}
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
		System.err.println("calling factorize");
		
		//Some input checking, although Kattis
		//doesn't give us numbers smaller than 2
		if (BigInteger.ONE.equals(input.max(BigInteger.ONE))) {
			System.err.println(input);
			System.err.println("Input is smaller than 2...");
			System.exit(0);
		}
		//BigInteger threshHold = BigInteger.valueOf(1000);
		//factorizeQS(input);
		
		//System.out.println(bigMRoot(input,15));
		ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
		
		final int certainty = 10; //TODO parameter here
		
		
		if(input.isProbablePrime(certainty)) {
			factors.add(input);
			return factors;
		}
		
		
		//first, remove small factors by trial division.
		BigInteger rem = input;
		
		
		for(int p : primes) {
			boolean divAll = false;
			while(!divAll) {
				BigInteger pbi = BigInteger.valueOf(p);
				BigInteger[] divr = rem.divideAndRemainder(pbi);
				if(divr[1].equals(BigInteger.ZERO)) {
					//found low factor!
					rem = divr[0];
					factors.add(pbi);
				} else {
					divAll = true;
				}
			}

		}
		
		if(rem.equals(BigInteger.ONE))
			return factors;
		
		if(rem.isProbablePrime(certainty)) {
			factors.add(rem);
			return factors;
		}

		//check if remainder is perfect square
		BigDecimal bd = new BigDecimal(rem);
		final int maxExponent = 30;
		boolean gotAll = false;
		int exponent = -1;
		for(int i = 0; i < primes.length; i++) {
			int p = primes[i];
			if(p > maxExponent) {
				break;
			}
			BigInteger root = bigMRoot(rem,p);
			BigInteger tmp = root.pow(p);
			if(tmp.equals(rem)) {
				//found perfect root!
				exponent = p;
				rem = root;
				break;
			}
		}
			
		if(exponent != -1) {
			//System.err.println("Found exponent: " + exponent);
			ArrayList<BigInteger> remFactorized = factorize(rem);
			if(remFactorized == null) {
				return null;
			} else {
				for(int i = 0; i < exponent; i++) {
					factors.addAll(remFactorized);
				}
				return factors;
			}
		}
			
		if(rem.equals(BigInteger.ONE))
			return factors;
		
		
		//Check which algorithm is appropriate
		if(rem.compareTo(threshHold) <= 0) {
			ArrayList<BigInteger> tmp = factorizeNaive(rem);
			if(tmp != null)
				factors.addAll(tmp);
			else
				return null;
		} else {
			//if(rem.compareTo(new BigInteger("1180591620717411303424")) == 1) { //2^70
			
			//if(rem.compareTo(new BigInteger("1208925819614629174706176")) == 1) { //2^80
			if(rem.compareTo(new BigInteger("151115727451828646838272")) == 1) { //2^77
			//if(rem.compareTo(new BigInteger("302231454903657293676544")) == 1) { //2^78
			
			
				return null;
			}
			
			
			
			System.err.println("Calling factorizeQS with parameter: " + rem);
			//ArrayList<BigInteger> tmp = factorizeQS(rem);
			ArrayList<BigInteger> tmp = factorizePolRho(rem);
			if(tmp != null) {
				factors.addAll(tmp);
				
				System.err.println("Found " + tmp.size() + " factors:");
				for (int i = 0;i<tmp.size();i++) {
					System.err.println("" + tmp.get(i));
				}
				
			} else {
				System.err.println("FactorizeQS returned null");
				return null;
			}
		}
		
		
		return factors;
		//return factorizeNaive(input);
	}
	
	/**
	 * Factorize an integer using the quadratic sieve method
	 */
	private ArrayList<BigInteger> factorizeQS(BigInteger input) {
		//TODO
		B = 500;
		//B = max 422?
		M = 1000000; //1 million
		
		ArrayList<BigInteger> factors = new ArrayList<BigInteger>();
		
		BigInteger sqrtn = bigSqrt(input);
		ArrayList<ArrayList<Integer>> factorBase = getLegendrePrimes(input,B);
		System.err.println("FactorBase: " + factorBase.size());
		System.err.println(factorBase);
		System.err.println("searching for Qs...");
		ArrayList<QVectorElement> finalQs = findQs(factorBase,M,sqrtn,input);
		System.err.println("found " + finalQs.size() + " Qs");
		for(int i = 0; i < finalQs.size(); i++) {
			//System.err.print(finalQs.get(i).getOriginalQ() + " ");
			
		}
		System.err.println();
		BitSet[] Qarray = new BitSet[finalQs.size()];
		for(int i = 0; i < finalQs.size(); i++) {
			Qarray[i] = finalQs.get(i).getBitSet();
		}
		//System.err.println("Found All Qs!");
		
		GaussEliminator ge = new GaussEliminator();
		
		int r = Qarray.length;
		int c = factorBase.size()+1;
		System.err.println("r: " + r + ", c:" + c);
		ge.printMatrix(Qarray, r, c);
		//Qarray = ge.gaussEliminate(Qarray, r, c);
		System.err.println("-------------");
		//ge.printMatrix(Qarray, r, c);
		final BitSet free = ge.getFreeVariables(Qarray, r, c);
		System.err.println("free:");
		ge.printBitSet(free, c);
		System.err.println("-------------");
		boolean foundFactors = false;
		BitSet sol = null;
		int maxIters = 50;
		int iter = 0;
		while(!foundFactors) {
			
			if(iter == maxIters)
				break;
			//System.err.println("Matrix: ");
			//System.err.println(Qarray);
			//System.err.println("r: " + r + ", c: " + c);
			//System.err.println("free: " + ge.printBitSet(free, c));
			//ge.printBitSet(free, c);
			/*if(sol!= null)
				ge.printBitSet(sol, c);
				//System.err.println("sol: " + sol);
			else
				System.err.println("sol is null");
			*/
			//System.err.println("1");
			sol = ge.calcNullSpace(Qarray, r, c, free, sol);
			//ge.printBitSet(sol, c);
			
			if(!isNullSpace(Qarray, sol)) {
				//System.err.println("NOT NULL SPACE");
			} else {
				//System.err.println("IN NULL SPACE");
			}
			
			//System.err.println("2");
			if(sol.isEmpty()) 
				break;
			BigInteger[] possibleFactors = getFactors(sol,finalQs,input);
			
			
			
			//if(possibleFactors[0].equals(BigInteger.ONE) && possibleFactors[0].equals(BigInteger.ONE)) {
				//System.err.println("BOTH ARE ONE");
			//}
			
			
			//check results
			if(possibleFactors[0].compareTo(BigInteger.ONE) == 1 && !possibleFactors[0].equals(input)) {
				//wooooh, found factor :)
				System.err.println("FOUND FACTORS");
				System.err.println(possibleFactors[0] + " " + possibleFactors[1]);
				foundFactors = true;
				BigInteger rem = input.divide(possibleFactors[0]);
				
				if(!rem.equals(BigInteger.ONE))
				if(possibleFactors[1].equals(rem)) {
					//input == possiblefactors[0]*possibleFactors[1]
					//System.err.println("Before recursive call; possibleFactors are:");
					//System.err.println(possibleFactors[0]);
					//System.err.println(possibleFactors[1]);
					ArrayList<BigInteger> factorsOf0 = factorize(possibleFactors[0]);
					ArrayList<BigInteger> factorsOf1 = factorize(possibleFactors[1]);
					factors.addAll(factorsOf0);
					factors.addAll(factorsOf1);
					return factors;
				} else {
					//input == possibleFactors[0]*rem
					
					//TODO possible optimization here by checking possibleFactors[1]
					ArrayList<BigInteger> factorsOf0 = factorize(possibleFactors[0]);
					ArrayList<BigInteger> factorsOfRem = factorize(rem);
					if(factorsOf0 == null || factorsOfRem == null)
						return null;
					factors.addAll(factorsOf0);
					factors.addAll(factorsOfRem);
					return factors;
				}
			}
			
			/*
			if(possibleFactors[1].compareTo(BigInteger.ONE) == 1) {
				foundFactors = true;
				BigInteger rem = input.divide(possibleFactors[1]);
				ArrayList<BigInteger> factorsOf1 = factorize(possibleFactors[1]);
				ArrayList<BigInteger> factorsOfRem = factorize(rem);
				
				if(factorsOf1 == null || factorsOfRem == null)
					return null;
				
				factors.addAll(factorsOf1);
				factors.addAll(factorsOfRem);
				return factors;
			}
			*/
			
			iter++;
			//public BigInteger[] getFactors(BitSet factored, ArrayList<QVectorElement> qVector, BigInteger n) {
				
		}
		
		if(foundFactors)
			return factors;
		else
			return null; //couldnt factorize
		
		//System.err.println("factors in factor base: " + factorBase.size()); //TODO
		//System.err.println("Final Q size: " + finalQs.size());
		



		
	}
	
	/**
	 * Factorize an integer using Pollard's rho method
	 */
	private ArrayList<BigInteger> factorizePolRho(BigInteger n) {
		BigInteger x = BigInteger.valueOf(2);
		BigInteger y = BigInteger.valueOf(2);
		BigInteger d = BigInteger.valueOf(1);
		BigInteger z = BigInteger.ONE;
		
		int i = 0;
		while(d.equals(BigInteger.ONE)) {
			x = rhoF(x,n);
			y = rhoF(rhoF(y,n),n);
			//d = x.subtract(y).abs().gcd(n);
			z = z.multiply(x.subtract(y).abs()).mod(n);
			i++;
			if(i == 100) {
				d = z.gcd(n);
				i = 0;
				z = BigInteger.ONE;
			}
		}
		
		if(d.equals(n)) {
			return null;
		} else {
			//n == d*rem
			ArrayList<BigInteger> res = new ArrayList<BigInteger>();
			BigInteger rem = n.divide(d);
			ArrayList<BigInteger> r1 = factorize(d);
			ArrayList<BigInteger> r2 = factorize(rem);
			if(r1 == null || r2 == null) {
				return null;
			}
			res.addAll(r1);
			res.addAll(r2);
			return res;
		}
		
		//return null;
	}
	
	private BigInteger rhoF(BigInteger x, BigInteger n) {
		return x.pow(2).subtract(BigInteger.ONE).mod(n);
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
		for (int i = primes[primes.length-1]; l <= sq;) {
		//for (int i =0; l <= sq;) {
			if (i < primes.length) {
				l = primes[i];
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
	 * @return The integer m-th root (BigInteger) of the input value
	 */
	private BigInteger bigMRoot(BigInteger n, int m) {
		//BigInteger min = BigInteger.ONE;
		//BigInteger max = n;
		
		
		int numBits = n.bitLength();
		int sqrtNumBits = (numBits-1)/m+1;	//Detta bör fungera
		BigInteger max = BigInteger.ONE.shiftLeft(sqrtNumBits);
		BigInteger min = max.shiftRight(1);
		//System.err.println("Min and max are: " + min.toString() + " & " + max.toString());
		//System.err.println("Numbits and sqrtNumBits are: " + numBits + " & " + sqrtNumBits);
		
		
		while(max.subtract(min).compareTo(BigInteger.ONE) == 1) {
			BigInteger c = max.add(min).shiftRight(1);
			BigInteger pow = c.pow(m);
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
		if(max.pow(m).compareTo(n) < 1) {
			return max;
		} else {
			return min;
		}
		//return BigInteger.valueOf(longSqrt(n));
	}
	
	/**
	 * @return The integer square root (BigInteger) of the input value
	 */
	private BigInteger bigSqrt(BigInteger n) {
		return bigMRoot(n, 2);
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
		return bigSqrt(n).longValue();
		
		//double d = n.doubleValue();
		//return (long)Math.sqrt(d);
		//TODO Don't know if this is accurate enough
	}
	
	/**
	 * @return The integer m-th root (long) of the input value
	 */
	private long longMRoot(BigInteger n, int m) {
		return bigMRoot(n,m).longValue();
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
	
	public ArrayList<QVectorElement> findQs(ArrayList<ArrayList<Integer>> psols, 
												long M, BigInteger sqrtn, BigInteger n) {
		//initialize BitSet arraylist
		ArrayList<QVectorElement> sols = new ArrayList<QVectorElement>();
		//final int EXTRA_Qs = 10;
		final int goal = psols.size()+20; //TODO parameters are here!!
		//final int goal = 431;
		final int maxIntervals = 30;
		final BigInteger interSize = BigInteger.valueOf(500);
		
		BigInteger lowEnd = sqrtn;
		BigInteger highStart = sqrtn.add(BigInteger.ONE); 
		
		//first interval
		int numItersDone = 0;
		while(numItersDone < maxIntervals && sols.size() < goal) {
			//low end
			BigInteger interMin = lowEnd.subtract(interSize);
			BigInteger interMax = lowEnd;
			//search interval
			sols = searchQInInterval(sols,psols,interMin,interMax,n,goal);
			
			if(sols.size() >= goal) {
				return sols;
			}
			//not at our goal yet!
			//update low_end
			lowEnd = interMin.subtract(BigInteger.ONE);
			
			//search high interval now
			interMin = highStart;
			interMax = highStart.add(interSize);
			
			sols = searchQInInterval(sols,psols,interMin,interMax,n,goal);
			//update highStart
			highStart = interMax.add(BigInteger.ONE);
		}
		
		
		
		//BigInteger inter_min = BigInteger.valueOf(sqrtn-interSizeHalf);
		//BigInteger inter_max = BigInteger.valueOf(sqrtn+interSizeHalf);
		
		return sols;
	}
	
	private ArrayList<QVectorElement> searchQInInterval(
			ArrayList<QVectorElement> sols, ArrayList<ArrayList<Integer>> psols,
			BigInteger inter_min, BigInteger inter_max, BigInteger n, int goal) {
		
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
					//IDEA: move interval to the other side instead.
					BigInteger new_i_min = x.add(x.subtract(inter_min));
					//now we can do the same!
					BigInteger r = new_i_min.mod(pbi);
					BigInteger tmp = new_i_min.subtract(r);
					BigInteger mbi = tmp.add(x); 
					
					BigInteger dist;
					if(mbi.compareTo(new_i_min) == 1) { //if still not enough
						dist = tmp.subtract(pbi);
					} else {
						dist = tmp;
					}
					x = x.subtract(dist);
				}
				
				while(x.compareTo(inter_max) <= 0) {
					QVectorElement qve = QVector.get((int) x.subtract(inter_min).longValue());
					if(qve.divideQWith(i)) {
						sols.add(qve);
						if(sols.size() == goal) {
							//RETURN!!!!!
							return sols;
						}
					}
					
					x = x.add(pbi); //go to next x.
				}
				
				
			}
		}
		return sols;
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
		factors[0] = x.subtract(y).abs().gcd(n);
		factors[1] = x.add(y).gcd(n);
		
		return factors;
	}
	
	public boolean isNullSpace(BitSet[] matrix, final BitSet sol) {
		for(BitSet bs : matrix) {
			BitSet bsc = (BitSet) bs.clone();
			bsc.and(sol);
			if(bsc.cardinality() % 2 != 0)
				return false;
		}
		return true;
	}
	
	public boolean controlQ(QVectorElement q,ArrayList<ArrayList<Integer>> factorBase) {
		if(q.getOriginalQ().compareTo(BigInteger.ZERO) == -1) {
			if(!q.getBitSet().get(0)) {
				return false;
			}
		} else {
			if(q.getBitSet().get(0)) {
				return false;
			}
		}
		
		BigInteger r = BigInteger.ONE;
		for(int i = 0; i < factorBase.size(); i++) {
			int bi = i+1;
			if(q.getBitSet().get(bi)) {
				r = r.multiply(BigInteger.valueOf(factorBase.get(i).get(0)));
			}
		}
		
		if(!r.equals(q.getOriginalQ())) {
			return false;
		}
		return true;
	}
	
	
	
}





