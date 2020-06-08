/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import java.util.Arrays;

import grid.KillerSudokuGrid;
import grid.StdSudokuGrid;
import grid.SudokuGrid;


/**
 * Your advanced solver for Killer Sudoku.
 */
public class KillerAdvancedSolver extends KillerSudokuSolver
{
    // TODO: Add attributes as needed.
	int[][] coverMatrix;
	int gridSize = 0;
	int cageCount = 0;
	int boxSize = 0;
	int cmRowCount;
	int cmColCount;

    public KillerAdvancedSolver() { 
        // TODO: any initialisation you want to implement.
    } // end of KillerAdvancedSolver()
    
    /*
	 * Method to build the cover matrix
	 */
	public void buildCoverMatrix(KillerSudokuGrid grid) {
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
				}
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
				System.out.print(this.coverMatrix[i][j]);
				if ((j + 1) % Math.pow(gridSize, 2) == 0) {
					System.out.print(" ");
				}
			}
			System.out.println("");
		}
	}
	

	/*
	 * Returns the index of coverMatrixindex according to inputs
	 */
	public int getCoverMatrixIndex(int row, int col, int offset) {
		return row * this.gridSize * this.gridSize + col * this.gridSize + offset;
	}
	
    @Override
    public boolean solve(SudokuGrid grid) {
		/* initialising grid size */
		this.gridSize = grid.gridSize;
		this.cageCount = grid.cageCount;
		this.boxSize = (int) Math.sqrt(this.gridSize);
		
		/* Building a cover matrix */
		this.buildCoverMatrix((KillerSudokuGrid) grid);
		
		/*
		 * invoking the dancing links solver
		 */
		KillerDancingLinks killerDancingLinks = new KillerDancingLinks(this.coverMatrix, this.gridSize, grid.cageConstraintsList , grid.validInputs, grid.grid);
		grid.grid = killerDancingLinks.driverFunction();
		
		/*
		 * if it returns a valid grid solution found
		 */
		if(grid.grid.length>0) {
			return true;
		}else {
			for(int i=0;i<grid.gridSize;i++) {
				Arrays.fill(grid.grid[i], -1);
			}
			return false; 
		}

        // placeholder
    } // end of solve()

} // end of class KillerAdvancedSolver
