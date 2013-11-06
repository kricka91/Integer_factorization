import java.math.BigInteger;
import java.util.*;
//import java.math.*;


/*
 * Factorizes an integer
 * @author Kristoffer Hallqvist, Dmitrij Lioubartsev
 */
public class Factorizer {

	/*
	 * Main method
	 */
	public static void main(String[] args) {
		System.out.println("It works!");
		Factorizer factorizer = new Factorizer();
		Scanner sc = new Scanner(System.in);
		BigInteger input;
		
		while (sc.hasNextLine()) {
			input = new BigInteger(sc.nextLine());
			ArrayList<BigInteger> result = factorizer.factorize(input);
			
			for (int i = 0;i<result.size();i++) {
				System.out.println(result.get(i));
			}
			System.out.println();
		}
	}
	
	/*
	 * Factorize an integer using an appropriate method
	 */
	private ArrayList<BigInteger> factorize(BigInteger input) {
		return factorizeNaive();
		
		//TODO
		//Choose the appropriate method
	}
	
	/*
	 * Factorize an integer using the quadratic sieve method
	 */
	private ArrayList<BigInteger> factorizeQS(BigInteger input) {
		//TODO
	}
	
	/*
	 * Factorize an integer using Pollard's rho method
	 */
	private ArrayList<BigInteger> factorizePolRho(BigInteger input) {
		//TODO maybe
	}
	
	/*
	 * Factorize an integer using naive trial division
	 */
	private ArrayList<BigInteger> factorizeNaive(BigInteger input) {
		//TODO
		
		ArrayList<BigInteger> result = new ArrayList<BigInteger>();
		//TODO
		
		//result.add(new BigInteger("2"));
		BigInteger sq = 0;
		BigInteger[] dar;
		
		for (BigInteger i = new BigInteger("2");;i++) {
			dar = input.divideAndRemainder(i);
		}
		
		
		
		return result;
	}
}





