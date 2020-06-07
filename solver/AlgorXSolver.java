/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */
package solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import grid.StdSudokuGrid;
import grid.SudokuGrid;

/**
 * Algorithm X solver for standard Sudoku.
 */
public class AlgorXSolver extends StdSudokuSolver {

	int[][] coverMatrix;
	int cmRowCount;
	int cmColCount;
	int gridSize = 0;
	int boxSize = 0;

	ArrayList<Integer> cmActiveRows = new ArrayList<Integer>();
	ArrayList<Integer> cmActiveCols = new ArrayList<Integer>();

	ArrayList<Integer> partialSolution = new ArrayList<Integer>();

	public AlgorXSolver() {
		// TODO: any initialisation you want to implement.
	} // end of AlgorXSolver()

	/*
	 * Returns the index of coverMatrixindex according to inputs
	 */
	public int coverMatrixIndex(int row, int col, int offset) {
		return row * this.gridSize * this.gridSize + col * this.gridSize + offset;
	}

	/*
	 * Cover the input row, the columns with one in the input row and the rows of
	 * the previous columns with one in the cover matrix
	 * 
	 * returns an array off size 2 of arrayLists. returnArr[0] = removedRows,
	 * returnArr[1] = * removedCols
	 * 
	 */
	public ArrayList<Integer>[] coverRowColRow(int rowIndex) {
		ArrayList<Integer>[] returnArr = new ArrayList[2];
		ArrayList<Integer> removedRows = new ArrayList<Integer>();
		ArrayList<Integer> removedCols = new ArrayList<Integer>();
		removedRows.add(rowIndex);

		Iterator<Integer> colItr = this.cmActiveCols.iterator();
		
		while (colItr.hasNext()) {
			int j = (Integer) colItr.next();

			if (this.coverMatrix[rowIndex][j] == 1) {
				removedCols.add(j);
				colItr.remove();
				Iterator<Integer> rowItr = this.cmActiveRows.iterator();
				while (rowItr.hasNext()) {
					int i = (Integer) rowItr.next();
					if (this.coverMatrix[i][j] == 1) {
						removedRows.add(i);
						rowItr.remove();
					}
				}
			}
		}
		returnArr[0] = removedRows;
		returnArr[1] = removedCols;
		return returnArr;
	}

	/*
	 * Method to uncover a previous cover operation
	 */
	public void uncoverRowColRow(ArrayList<Integer>[] rowColToUncover) {
		ArrayList<Integer> removedRows = rowColToUncover[0];
		ArrayList<Integer> removedCols = rowColToUncover[1];

		this.cmActiveRows.addAll(removedRows);
		this.cmActiveCols.addAll(removedCols);
	}

	@Override
	public boolean solve(SudokuGrid grid) {

		/* initialising grid size */
		this.gridSize = grid.gridSize;
		this.boxSize = (int) Math.sqrt(this.gridSize);

		/* Building a cover matrix */
		this.buildCoverMatrix((StdSudokuGrid) grid);

		/* Covering rows corresponding to the input matrix */
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				int valueInCell = grid.grid[row][col];

				if (valueInCell != -1) {
					for (int n = 0; n < grid.validInputs.length; n++) {
						if (valueInCell == grid.validInputs[n]) {
							this.coverRowColRow(this.coverMatrixIndex(row, col, n));
						}
					}
				}

			}
		}

		/* Invoking the recursive function to solve the cover matrix */
		if (this.recursiveAlgoXSolve()) {
			int rowIndex, colIndex, value;

			/*
			 * Rebuilding the matrix from partial solution array. Partial solution contains
			 * the rows which qualified as a solution from cover matrix
			 */
			for (int i = 0; i < this.partialSolution.size(); i++) {
				int x = this.partialSolution.get(i);
				rowIndex = x / (this.gridSize * this.gridSize);
				colIndex = (x / this.gridSize) % this.gridSize;
				value = grid.validInputs[x % this.gridSize];
				grid.grid[rowIndex][colIndex] = value;
			}
			return true;
		}
		return false;

	}

	/**
	 * 
	 * Recursive Algorithm X solver returns true if the solution is found the
	 * partial solution will be in this.partialSolution
	 */
	public boolean recursiveAlgoXSolve() {

		/* Checking if constraint is met */
		if (this.cmActiveCols.size() == 0 && this.cmActiveRows.size() == 0) {
			return true;
		} else if (this.cmActiveCols.size() == 0 || this.cmActiveRows.size() == 0) {
			return false;
		} else {

			int choosenColumn = this.cmActiveCols.get(0);
			int selectedRow = -1;
			ArrayList<Integer> cmActiveRowsCopy = new ArrayList<Integer>();
			cmActiveRowsCopy.addAll(cmActiveRows);

			for (int i1 = 0; i1 < cmActiveRowsCopy.size(); i1++) {

				if (this.coverMatrix[cmActiveRowsCopy.get(i1)][choosenColumn] == 1) {

					selectedRow = cmActiveRowsCopy.get(i1);
					this.partialSolution.add(selectedRow);
					this.cmActiveRows.remove(Integer.valueOf(selectedRow));

					ArrayList<Integer>[] returnArr = this.coverRowColRow(selectedRow);

					/* Recursive call */
					if (this.recursiveAlgoXSolve()) {
						return true;
					} else {
						this.partialSolution.remove(new Integer(selectedRow));
						this.uncoverRowColRow(returnArr);
					}
				}
			}

		}
		return false;
	}

	/*
	 * Method to build the cover matrix
	 */
	public void buildCoverMatrix(StdSudokuGrid grid) {
		cmRowCount = (int) Math.pow(grid.gridSize, 3);
		cmColCount = (int) Math.pow(grid.gridSize, 2) * 4;
		coverMatrix = new int[cmRowCount][cmColCount];

		int coverIndex = 0;
		coverIndex = this.fillCellConstraints(coverIndex);
		coverIndex = this.fillRowConstraints(coverIndex);
		coverIndex = this.fillColConstraints(coverIndex);
		this.fillBoxConstraints(coverIndex);
	}

	/*
	 * Fill the 1s corresponding to cell constraints in the cover matrix
	 */
	public int fillCellConstraints(int coverIndex) {
		for (int row = 0; row < gridSize; row++) {
			for (int col = 0; col < gridSize; col++) {
				for (int i = 0; i < gridSize; i++) {
					int coverMatrixRowindex = row * gridSize * gridSize + col * gridSize + i;
					this.coverMatrix[coverMatrixRowindex][coverIndex] = 1;
					this.cmActiveRows.add(coverMatrixRowindex);
				}
				this.cmActiveCols.add(coverIndex);
				coverIndex++;
			}
		}
		return coverIndex;
	}

	/*
	 * Fill the 1s corresponding to row constraints in the cover matrix
	 */
	public int fillRowConstraints(int coverIndex) {
		for (int row = 0; row < gridSize; row++) {
			for (int i = 0; i < gridSize; i++) {
				for (int col = 0; col < gridSize; col++) {
					int coverMatrixRowindex = row * gridSize * gridSize + col * gridSize + i;
					this.coverMatrix[coverMatrixRowindex][coverIndex] = 1;
				}
				this.cmActiveCols.add(coverIndex);
				coverIndex++;
			}
		}
		return coverIndex;
	}

	/*
	 * Fill the 1s corresponding to column constraints in the cover matrix
	 */
	public int fillColConstraints(int coverIndex) {
		for (int col = 0; col < gridSize; col++) {
			for (int i = 0; i < gridSize; i++) {
				for (int row = 0; row < gridSize; row++) {
					int coverMatrixRowindex = row * gridSize * gridSize + col * gridSize + i;
					this.coverMatrix[coverMatrixRowindex][coverIndex] = 1;
				}
				this.cmActiveCols.add(coverIndex);
				coverIndex++;
			}
		}
		return coverIndex;
	}

	/*
	 * Fill the 1s corresponding to box constraints in the cover matrix
	 */
	public int fillBoxConstraints(int coverIndex) {
		for (int row = 0; row < gridSize; row += boxSize) {
			for (int col = 0; col < gridSize; col += boxSize) {

				for (int i = 0; i < gridSize; i++) {
					for (int rowOffset = 0; rowOffset < boxSize; rowOffset++) {
						for (int colOffset = 0; colOffset < boxSize; colOffset++) {
							int coverMatrixRowindex = ((row + rowOffset) * gridSize * gridSize)
									+ ((col + colOffset) * gridSize) + i;
							this.coverMatrix[coverMatrixRowindex][coverIndex] = 1;
						}
					}
					this.cmActiveCols.add(coverIndex);
					coverIndex++;
				}
			}
		}
		return coverIndex;
	}

	/*
	 * Display the cover matrix on the console with space separation for each
	 * constraints
	 */
	public void displayCoverMatrix() {
		for (int i = 0; i < cmRowCount; i++) {
			for (int j = 0; j < cmColCount; j++) {
				if (this.cmActiveRows.contains(i) && this.cmActiveCols.contains(j)) {
					System.out.print(this.coverMatrix[i][j]);
				} else {
					System.out.print(" ");
				}
				if ((j + 1) % Math.pow(gridSize, 2) == 0) {
					System.out.print(" ");
				}
			}
			System.out.println("");
		}
	}

}
