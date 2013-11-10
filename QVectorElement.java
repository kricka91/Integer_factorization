import java.math.BigInteger;
import java.util.BitSet;
import java.util.ArrayList;

public class QVectorElement {

	private BitSet rep;
	private final BigInteger x;
	private BigInteger OriginalQ;
	private BigInteger Q;
	private final BigInteger n;
	private final ArrayList<ArrayList<Integer>> factorBase;
	private boolean Qcalc; //if we have calculated Q
	
	public QVectorElement(ArrayList<ArrayList<Integer>> factorBase, BigInteger x, BigInteger n) {
		//rep = new BitSet(factorBase.size()+1);
		this.factorBase = factorBase;
		this.x = x;
		this.n = n;
		Qcalc = false;
	}
	
	/**
	 * Index should be the index in the factorBase arraylist of arraylists. That does not include -1!
	 * Meaning 2 would always be index 0, because its the first prime.
	 * @param index
	 * @return true if Q is 1 now.
	 */
	public boolean divideQWith(int index) {
		if(!Qcalc) {
			Q = x.multiply(x).subtract(n);
			OriginalQ = Q;
			rep = new BitSet(factorBase.size()+1);
			Qcalc = true;
			if(Q.compareTo(BigInteger.ZERO) < 0) {
				Q = Q.negate();
				rep.set(0);
			}
		}
		int bitindex = index+1;
		
		int numDivs = 0;
		boolean all = false;
		while(!all) {
			BigInteger[] tmp = Q.divideAndRemainder(BigInteger.valueOf(factorBase.get(index).get(0)));
			if(tmp[1].equals(BigInteger.ZERO)) {
				Q = tmp[0];
				numDivs++;
			} else {
				all = true;
			}
		}
		
		if(numDivs == 0) {
			//for debugging purposes
			System.err.println("Q is divisible by p they said. There will be no remainder they said");
		}
		
		if(numDivs%2 == 1) {
			rep.set(bitindex);
		}
		
		if(Q.equals(BigInteger.ONE)) {
			return true;
		} else {
			return false;
		}
	}
	
	public BigInteger getX() {
		return x;
	}
	
	public BigInteger getOriginalQ() {
		return OriginalQ;
	}
	
	public BitSet getBitSet() {
		return rep;
	}
	
	
}
