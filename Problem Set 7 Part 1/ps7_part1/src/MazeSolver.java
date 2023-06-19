import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MazeSolver implements IMazeSolver {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	public Maze maze;
	public int startRow, startCol, endRow, endCol;
	public boolean[][] visited;
	ArrayList<RoomNode> frontier;
	boolean solved;
	RoomNode endNode;
	HashMap<Integer, Integer> numberOfSteps;

	private static int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 1, 0 }, // South
			{ 0, 1 }, // East
			{ 0, -1 } // West
	};
	Integer resultDistance;

	public class RoomNode {
		int distance;
		int row;
		int col;
		RoomNode parent;
		public RoomNode(int distance, int row, int col) {
			this.distance = distance;
			this.row = row;
			this.col = col;
			this.parent = null;
		}
	}

	public MazeSolver() {
		// TODO: Initialize variables
		this.maze = null;
		this.frontier = new ArrayList<>();
		this.solved = false;
		this.resultDistance = null;
		this.endNode = null;

	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		numberOfSteps = new HashMap<>();
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		for (int i = 0; i < maze.getRows(); ++i) {
			for (int j = 0; j < maze.getColumns(); ++j) {
				this.visited[i][j] = false;
				maze.getRoom(i, j).onPath = false;
			}
		}

		this.startRow = startRow;
		this.startCol = startCol;
		this.endRow = endRow;
		this.endCol = endCol;

		visited[startRow][startCol] = true;

		this.frontier.add(new RoomNode(0, startRow, startCol));
		int distanceCount = 0;
		while (!frontier.isEmpty()) {
			numberOfSteps.put(distanceCount, frontier.size());
			ArrayList<RoomNode> nextFrontier = new ArrayList<>();
			for (RoomNode room : frontier) {
				int currRow = room.row;
				int currCol = room.col;
				if (currRow == this.endRow && currCol == this.endCol) {
					this.solved = true;
					resultDistance = distanceCount;
					endNode = room;
					maze.getRoom(room.row, room.col).onPath = true;
				}

				//check north
				RoomNode northNode = new RoomNode(distanceCount, currRow + DELTAS[0][0], currCol + DELTAS[0][1]);
				if (!maze.getRoom(currRow, currCol).hasNorthWall()
						&& !visited[northNode.row][northNode.col]) {
					visited[northNode.row][northNode.col] = true;
					northNode.parent = room;
					nextFrontier.add(northNode);
				}

				// south
				RoomNode southNode = new RoomNode(distanceCount, currRow + DELTAS[1][0], currCol + DELTAS[1][1]);
				if (!maze.getRoom(currRow, currCol).hasSouthWall()
						&& !visited[southNode.row][southNode.col]) {
					nextFrontier.add(southNode);
					southNode.parent = room;
					visited[southNode.row][southNode.col] = true;
				}

				//east
				RoomNode eastNode = new RoomNode(distanceCount, currRow + DELTAS[2][0], currCol + DELTAS[2][1]);
				if (!maze.getRoom(currRow, currCol).hasEastWall()
						&& !visited[eastNode.row][eastNode.col]) {
					nextFrontier.add(eastNode);
					eastNode.parent = room;
					visited[eastNode.row][eastNode.col] = true;
				}

				//west
				RoomNode westNode = new RoomNode(distanceCount, currRow + DELTAS[3][0], currCol + DELTAS[3][1]);
				if (!maze.getRoom(currRow, currCol).hasWestWall()
						&& !visited[westNode.row][westNode.col]) {
					nextFrontier.add(westNode);
					westNode.parent = room;
					visited[westNode.row][westNode.col] = true;
				}
			}
			frontier = nextFrontier;
			distanceCount++;


		}
		if (solved) {
			while (endNode.parent != null) {
				RoomNode parent = endNode.parent;
				maze.getRoom(parent.row, parent.col).onPath = true;
				endNode = parent;
			}
		}
		Integer temp = resultDistance;
		resultDistance = 0;
		return temp;
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k > numberOfSteps.size() - 1 || k < 0) {
			return 0;
		}
		return numberOfSteps.get(k);
	}

	public static void main(String[] args) {
		// Do remember to remove any references to ImprovedMazePrinter before submitting
		// your code!
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 3, 2));
			MazePrinter.printMaze(maze);

			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
