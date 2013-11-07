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
			//System.out.println(factorizer.longSqrt(input));
			
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
		double d = n.doubleValue();
		return BigInteger.valueOf((long)Math.sqrt(d));
		//TODO Don't know if this is accurate enough
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
		BigInteger min = BigInteger.ONE;
		BigInteger max = n;
		
		while(max.subtract(min).compareTo(BigInteger.ONE) == 1) {
			BigInteger c = max.add(min).shiftRight(1);
			BigInteger pow = c.pow(2);
			int comp = pow.compareTo(n);
			if(comp == 0) {
				//they are equal
				//found perfect square
				return c.longValue();
			} else if(comp == 1) {
				//c^2 is greater than n
				max = c.subtract(BigInteger.ONE);
			} else {
				min = c;
			}
		}
		
		//now we have a situation where max-min == 1 
		if(max.pow(2).compareTo(n) < 1) {
			return max.longValue();
		} else {
			return min.longValue();
		}
		
		//double d = n.doubleValue();
		//return (long)Math.sqrt(d);
		//TODO Don't know if this is accurate enough
	}
	
}





