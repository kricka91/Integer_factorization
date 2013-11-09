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
		gaussEliminate(matrix, rows, columns);
		System.err.println("After Gauss elimination");
		printMatrix(matrix, rows, columns);
	}

	/**
	 * Main method
	 */
	public static void main(String[] args) {
		new GaussEliminator().run();
	}
	
	private BitSet[] calcNullSpace(BitSet[] matrix) {
		return null;
	}
	
	private BitSet[] gaussEliminate(BitSet[] matrix, int r, int c) {
		int[] originalOrder = new int[r];
		for (int i = 0;i<r;i++) {
			originalOrder[i] = i;
		}
		
		BitSet tmp;
		int itmp;
		int cNoBit = 0;
		int x = -1;
		for (int j = 0;j<c;j++) {
			
			printMatrix(matrix, r, c);
			
			x = -1;
			for (int i = j-cNoBit;i<r;i++) {
				if (matrix[i].get(j) == true) {
					if (x < 0) {
						x = i;
					} else {
						System.err.println("xoring " + i + " with " + x);
						matrix[i].xor(matrix[x]);
					}
				}
			}
			
			if (x > -1) {
				System.err.println("x and j are: " + x + " and " + j);
				tmp = (BitSet)matrix[j-cNoBit].clone();
				matrix[j-cNoBit] = (BitSet)matrix[x].clone();
				matrix[x] = (BitSet)tmp.clone();
				itmp = originalOrder[j-cNoBit];
				originalOrder[j-cNoBit] = originalOrder[x];
				originalOrder[x] = itmp;
			} else {
				cNoBit++;
			}
		}
		
		for (int i = 0;i<r;i++) {
			System.err.print("" + originalOrder[i] + "; ");
		}
		System.err.println();
		
		return null;
	}
	
	private void printMatrix(BitSet[] matrix, int r, int c) {
		for (int i = 0;i<r;i++) {
			printBitSet(matrix[i], c);
			System.err.println();
		}
	}
	
	private void printBitSet(BitSet bs, int c) {
		for (int j = 0;j<c;j++) {
			System.err.print(bs.get(j) == true ? 1 : 0);
		}
	}
}





