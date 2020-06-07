package solver;

import java.util.*;

public class DancingLinks {

	/*
	 * Global variable of the class
	 */
	int gridSize = 0;
	boolean solutionFound = false;
	int[][] solutionMatrix;
	private ColumnNode header;
	private List<DancingNode> solution;

	/*
	 * Constructor
	 */
	public DancingLinks(int[][] coverMatrix, int size) {
		header = constructDancingLinks(coverMatrix);
		this.gridSize = size;
	}

	public int[][] driverFunction() {
		int returnMatrix[][] = new int[0][];
		solution = new LinkedList<DancingNode>();

		dancingSearch(0);

		if (this.solutionFound) {
			return this.solutionMatrix;
		} else {
			return returnMatrix;
		}
	}

	/*
	 * Converting the cover matrix into dancing links Returns the headerNode
	 */
	private ColumnNode constructDancingLinks(int[][] coverMatrix) {
		int cmRowCount = coverMatrix.length;
		int cmColCount = coverMatrix[0].length;

		ColumnNode headerNode = new ColumnNode("header");
		ArrayList<ColumnNode> columnNodesList = new ArrayList<ColumnNode>();

		/*
		 * Creating new column nodes and adding them to column nodes list
		 */
		for (int i = 0; i < cmColCount; i++) {
			ColumnNode columnNode = new ColumnNode(Integer.toString(i));
			columnNodesList.add(columnNode);

			/* Connecting header node */
			headerNode = (ColumnNode) headerNode.connectRight(columnNode);
		}

		headerNode = headerNode.Right.Column;

		/*
		 * Mapping the 1s in the cover matrix to the dancing link representation
		 */
		for (int i = 0; i < cmRowCount; i++) {
			DancingNode prev = null;
			for (int j = 0; j < cmColCount; j++) {

				/* If 1 is found */
				if (coverMatrix[i][j] == 1) {

					ColumnNode colNode = columnNodesList.get(j);
					DancingNode newNode = new DancingNode(colNode);

					if (prev == null)
						prev = newNode;

					colNode.Up.connectDown(newNode);
					prev = prev.connectRight(newNode);

					colNode.size += 1;
				}
			}
		}

		headerNode.size = cmColCount;

		return headerNode;
	}

	/*
	 * Solution list to sudoku matrix transformation
	 */
	private void reconstructSudokuMatrix() {
		this.solutionMatrix = new int[gridSize][gridSize];

		for (DancingNode dNode : this.solution) {
			DancingNode rcNode = dNode;
			int min = Integer.parseInt(rcNode.Column.name);

			for (DancingNode tmp = dNode.Right; tmp != dNode; tmp = tmp.Right) {
				int tempVal = Integer.parseInt(tmp.Column.name);
				if (tempVal < min) {
					min = tempVal;
					rcNode = tmp;
				}
			}

			/* Calculating position and value */
			int x = Integer.parseInt(rcNode.Column.name);
			int y = Integer.parseInt(rcNode.Right.Column.name);

			int column = x % gridSize;
			int row = x / gridSize;
			int value = (y % gridSize) + 1;

			/* Adding the value to solution matrix */
			this.solutionMatrix[row][column] = value;
		}
	}

	private void dancingSearch(int k) {
		/* If there is nothing remaining in the header */
		if (header.Right == header) {
			solutionFound = true;
			this.reconstructSudokuMatrix();
			return;
		} else {
			ColumnNode columnNode = selectOptimalColumnNode();
			columnNode.cover();

			/* Iterating through the row */
			for (DancingNode row = columnNode.Down; row != columnNode; row = row.Down) {

				/* Adding it to solution list */
				solution.add(row);

				/* Covering columns */
				for (DancingNode j = row.Right; j != row; j = j.Right) {
					j.Column.cover();
				}
				/* Recursive invocation */
				dancingSearch(k + 1);

				/* Backtracking */
				row = solution.remove(solution.size() - 1);
				columnNode = row.Column;

				/* Uncovering */
				for (DancingNode j = row.Left; j != row; j = j.Left) {
					j.Column.uncover();
				}

			}
			columnNode.uncover();
		}
	}

	/*
	 * Selecting the column node with minimum value
	 */
	private ColumnNode selectOptimalColumnNode() {
		int minimum = 999999999;
		ColumnNode minimumColumnNode = null;

		/* Searching for minimum column node */
		for (ColumnNode cNode = (ColumnNode) header.Right; cNode != header; cNode = (ColumnNode) cNode.Right) {
			if (cNode.size < minimum) {
				minimum = cNode.size;
				minimumColumnNode = cNode;
			}
		}

		return minimumColumnNode;
	}

	/*
	 * Method to print the board state
	 */
	private void DisplayDLX() { // diagnostics to have a look at the board state

		for (ColumnNode c = (ColumnNode) header.Right; c != header; c = (ColumnNode) c.Right) {

			for (DancingNode d = c.Down; d != c; d = d.Down) {
				String output = "";
				output += d.Column.name + " --> ";

				for (DancingNode i = d.Right; i != d; i = i.Right) {
					output += i.Column.name + " --> ";
				}
				System.out.println(output);
			}
		}
	}

}
