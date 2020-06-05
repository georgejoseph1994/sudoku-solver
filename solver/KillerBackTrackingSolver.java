/*
 * @author Jeffrey Chan & Minyi Li, RMIT 2020
 */

package solver;

import grid.KillerSudokuGrid;
import grid.SudokuGrid;


/**
 * Backtracking solver for Killer Sudoku.
 */
public class KillerBackTrackingSolver extends KillerSudokuSolver
{
    // TODO: Add attributes as needed.

    public KillerBackTrackingSolver() {
        // TODO: any initialisation you want to implement. 
    } // end of KillerBackTrackingSolver()

    
    public boolean fastValidate(SudokuGrid grid, int i, int j) {
    	int boxSize = (int) (Math.sqrt(grid.gridSize));
    	if(grid.validateRow(i) && grid.validateColumn(j) && grid.validateBox((boxSize*(i/boxSize))+ (j/boxSize)) && ((KillerSudokuGrid)grid).fastValidateCage(i,j)) {
    		
    		return true;
    	}else{
    		return false;
    	}
    }
    
    @Override
    public boolean solve(SudokuGrid grid) {
    	for (int i = 0; i < grid.gridSize; i++) {
			for (int j = 0; j < grid.gridSize; j++) {

				if (grid.grid[i][j] == -1) {
					int sizeOfValidInputs = grid.validInputs.length;
					for (int k = 0; k < sizeOfValidInputs; k++) {
						grid.grid[i][j] = grid.validInputs[k];
						
						/* Checking if valid grid */
						if (fastValidate(grid, i, j) == true) {
//							System.out.println(grid);
							if (!solve(grid)) {
								grid.grid[i][j] = -1;
							} else
								break;
						} else {
							grid.grid[i][j] = -1;
						}
					}
					if (grid.grid[i][j] == -1) {
						return false;
					}
				}

			}
		}
		return true;
	} // end of solve()

} // end of class KillerBackTrackingSolver()
