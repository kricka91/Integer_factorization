import java.math.BigInteger;
import java.util.*;
//import java.math.*;


/**
 * Factorizes an integer
 * @author Kristoffer Hallqvist
 */
public class GaussEliminator {
	//Fields:
	//no fields atm
	
	
	//Methods
	/**
	 * Constructor
	 */
	public GaussEliminator() {}

	private void run() {
		Scanner sc = new Scanner(System.in);
		System.err.println("Enter a binary matrix:");
		int rows, columns;
		
		rows = sc.nextInt();
		columns = sc.nextInt();
		BitSet[] matrix = new BitSet[rows];
		int curBit = 0;
		boolean boolCurBit = false;
		
		//System.err.println("nr of columns (BitSet length): " + columns);
		for (int i = 0;i<rows;i++) {
			matrix[i] = new BitSet(columns);
			//System.err.println("Created BitSet of size: " + matrix[i].size());
			for (int j = 0;j<columns;j++) {
				try {
					curBit = sc.nextInt();
					curBit = curBit % 2;
					if (curBit == 0) {
						boolCurBit = false;
					} else {
						boolCurBit = true;
					}
				} catch (Exception e) {
					boolCurBit = false;
				}
				matrix[i].set(j,boolCurBit);
			}
			//System.err.println("length: " + matrix[i].length());
		}
		
		System.err.println("You entered the matrix:");
		printMatrix(matrix, rows, columns);
		
		
		
		/*
		matrix = transpose(matrix, rows, columns);
		System.err.println("After transposition:");
		printMatrix(matrix, columns, rows);
		
		
		matrix = gaussEliminate(matrix, columns, rows);
		System.err.println("After Gauss elimination");
		printMatrix(matrix, columns, rows);
		
		BitSet freeVar = getFreeVariables(matrix, columns, rows);
		System.err.println("Free variables: ");
		printBitSet(freeVar, rows);
		
		BitSet nullspace = null;
		while (true) {
			nullspace = calcNullSpace(matrix, columns, rows, freeVar, nullspace);
			System.err.println("Null space vector: ");
			printBitSet(nullspace, columns);
			if (nullspace.isEmpty()) {
				break;
			}
		}
		//System.err.println("Null space vector: ");
		//printBitSet(nullspace, columns);
		*/
		
		
		matrix = gaussEliminate(matrix, rows, columns);
		System.err.println("After Gauss elimination");
		printMatrix(matrix, rows, columns);
		
		BitSet freeVar = getFreeVariables(matrix, rows, columns);
		System.err.println("Free variables: ");
		printBitSet(freeVar, columns);
		
		BitSet nullspace = null;
		while (true) {
			nullspace = calcNullSpace(matrix, rows, columns, freeVar, nullspace);
			System.err.println("Null space vector: ");
			printBitSet(nullspace, columns);
			if (nullspace.isEmpty()) {
				break;
			}
		}
		//System.err.println("Null space vector: ");
		//printBitSet(nullspace, columns);
		
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) {
		new GaussEliminator().run();
	}
	
	
	public BitSet getFreeVariables(BitSet[] matrix, int r, int c) {
		
		if (c > r) {
			System.err.println("Matrix is wide...");
			return null;
		}
		
		BitSet res = new BitSet(c);
		
		//Warning. We assume that the matrix is "high" or square. Might crash otherwise.
		for(int i = c-1; i >= 0; i--) {	//Lower rows should be all 0 anyway
			
			boolean bitValue = matrix[i].get(i);
			
			if (bitValue) {	//Bound
				//System.err.println("Bound!");
				res.clear(i);
			} else {	//Free
				//System.err.println("Free!");
				res.set(i);
			}
			
		}
		
		return res;
	}
	
	public BitSet[] transpose(final BitSet[] src, int r, int c) {

		BitSet[] res = new BitSet[c];
		for (int i = 0;i<c;i++) {
			res[i] = new BitSet(r);
		}
		
		for (int i = 0;i<c;i++) {
			for (int j = 0;j<r;j++) {
				res[i].set(j, src[j].get(i));
			}
		}
		
		return res;
	}
	
	//Prev denotes the solution given by the previous call to this function
	//Free denotes the free variables, calculated by getFreeVariables();
	public BitSet calcNullSpace(BitSet[] matrix, int r, int c, final BitSet free, final BitSet prev) {
		
		if (free == null || free.isEmpty()) {	//Return zero-vector
			BitSet ret = new BitSet(c);
			ret.clear();
			return ret;
		}
		
		BitSet variables;
		
		int lastRow;
		BitSet lr;

		if (prev == null || prev.isEmpty()) {
			lastRow = c-1;
			variables = new BitSet(c);
			variables.set(0,c);
		} else {
			lr = (BitSet)prev.clone();
			lr.and(free);
			//lastRow = lr.length()-1;
			lastRow = lr.nextSetBit(0);
			
			/*
			if (lastRow == -1) {
				System.err.println("lastRow is negative!");
				System.err.println("prev is: " + prev.toString());
				printBitSet(prev, prev.size());
				System.err.println("prev length is " + prev.length());
				System.err.println("lr is " + lr.toString());
				printBitSet(lr, lr.size());
				System.err.println("lr length is " + lr.length());
				
				System.err.println("Bits 0 and 1 of lr are:");
				System.err.println("" + lr.get(0));
				System.err.println("" + lr.get(1));
				
				System.err.println("free is " + free.toString());
				printBitSet(free, free.size());
				System.err.println("free length is " + free.length());
			}
			*/
			if (lastRow == -1) {
				lastRow++;
			}
			
			variables = (BitSet)prev.clone();
			
			//if (lastRow > 0) {
				variables.set(0,lastRow);
				variables.clear(lastRow);
			//}
			
			//System.err.println("variables is now:");
			//printBitSet(variables, c);
			lastRow--;
		}
		
		BitSet bsTmp = new BitSet(c);
		//System.err.println("lastRow is: " + lastRow);
		
		//Warning. We assume that the matrix is "high" or square. Might crash otherwise.
		for(int i = lastRow; i >= 0; i--) {	//Lower rows should be all 0 anyway
			
			bsTmp = (BitSet)matrix[i].clone();
			//Set the variables
			bsTmp.and(variables);
			
			if (!free.get(i)) {	//Bound
				//System.err.println("Bound!" + " i is " + i);
				//Determine what we need to set it to
				int bitVal = bsTmp.cardinality();
				//System.err.println("bitVal is: " + bitVal);
				bitVal = (bitVal+1) % 2;
				
				if (bitVal == 0) {
					variables.clear(i);
				} else {
					variables.set(i);
				}

			} else {	//Free, set to 1 as default for now
				//System.err.println("Free!");
				variables.set(i);
			}
			
		}
		
		return variables;
	}
	
	public BitSet[] gaussEliminate(final BitSet[] src, int r, int c) {
		BitSet[] res;
		res = (BitSet[])src.clone();	//Use this if you don't want to modify input
		//res = src;	//Use this if you want to modify the input instead
		BitSet tmp;
		int cNoBit = 0;
		int x = -1;
		for (int j = 0;j<c;j++) {
			x = -1;
			for (int i = j-cNoBit;i<r;i++) {
				if (res[i].get(j) == true) {
					if (x < 0) {
						x = i;
					} else {
						res[i].xor(res[x]);
					}
				}
			}
			
			if (x > -1) {
				tmp = (BitSet)res[j-cNoBit].clone();
				res[j-cNoBit] = (BitSet)res[x].clone();
				res[x] = (BitSet)tmp.clone();
			} else {
				cNoBit++;
			}
		}
		
		
		//Rearrange for a nicer diagonal
		
		for (int j = c-1;j>=0;j--) {
			if (res[j].get(j) == true) {	//No rearranging needed.
				break;
			}
			
			if (res[j].isEmpty()) {	//0-row. Just continue
				continue;
			}
			
			int bitPos = res[j].nextSetBit(0);
			
			tmp = (BitSet)res[j].clone();
			res[j] = new BitSet(c);
			res[j].clear();
			res[bitPos] = (BitSet)tmp.clone();
			
		}
		
		
		
		return res;
	}
	
	public void printMatrix(BitSet[] matrix, int r, int c) {
		for (int i = 0;i<r;i++) {
			printBitSet(matrix[i], c);
		}
	}
	
	public void printBitSet(BitSet bs, int c) {
		if (bs != null) {
			for (int j = 0;j<c;j++) {
				System.err.print(" " + (bs.get(j) == true ? 1 : 0));
			}
			System.err.println();
		} else {
			System.err.println("null");
		}
	}
}





