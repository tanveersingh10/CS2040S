import java.util.ArrayList;
public class MazeSolverWithPower implements IMazeSolverWithPower {
	private static final int NORTH = 0, SOUTH = 1, EAST = 2, WEST = 3;
	public Maze maze;
	public boolean visited[][][];
	ArrayList<Integer> numSteps;
	RoomNode endNode;
	public boolean[][] visitedLocation; //this one only tracks if the room has been visited and ignores the
										//number of superpowers. to help with numReachable

	public class RoomNode {
		int superpowers;
		int row;
		int col;
		RoomNode parent;
		public RoomNode(int row, int col, int superpowers) {
			this.superpowers = superpowers;
			this.row = row;
			this.col = col;
			this.parent = null;
		}
	}

	private static int[][] DELTAS = new int[][] {
		{ -1, 0 }, // North
		{ 1, 0 }, // South
		{ 0, 1 }, // East
		{ 0, -1 } // West
	};

	public MazeSolverWithPower() {
		this.numSteps = new ArrayList<>();
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		return pathSearch(startRow, startCol, endRow, endCol, 0);
	}

	@Override
	public Integer numReachable(int k) throws Exception {
		if (k > numSteps.size() - 1 || k < 0) {
			return 0;
		}
		return numSteps.get(k);
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow,
							  int endCol, int superpowers) throws Exception {


		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}

		boolean solved = false;
		Integer resultDistance = null;

		this.visited = new boolean[maze.getRows()][maze.getColumns()][superpowers + 1];
		this.visitedLocation = new boolean[maze.getRows()][maze.getColumns()];


		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				this.visitedLocation[i][j] = false;
				for (int k = 0; k <= superpowers; k++) {
					this.visited[i][j][k] = false;
					maze.getRoom(i, j).onPath = false;
				}
			}
		}
		ArrayList<RoomNode> frontier = new ArrayList<>();
		maze.getRoom(startRow, startCol).onPath = true;
		this.numSteps = new ArrayList<>();
		//ArrayList<RoomNode> stepsCounter = new ArrayList<>();

		RoomNode startNode = new RoomNode(startRow, startCol, superpowers);
		visited[startRow][startCol][superpowers] = true;
		visitedLocation[startRow][startCol] = true;
		frontier.add(startNode);
		//stepsCounter.add(startNode);
		int distanceCount = 0;
		int numDistance = 1;

		while (!frontier.isEmpty()) {
			ArrayList<RoomNode> nextFrontier = new ArrayList<>();
 			numSteps.add(distanceCount, numDistance);
			 numDistance = 0;
			for (RoomNode room: frontier) {
				if (room.row == endRow && room.col == endCol && !solved) {
					solved = true;
					resultDistance = distanceCount;
					endNode = room;
					maze.getRoom(room.row, room.col).onPath = true;
				}


				//check north
				if (!maze.getRoom(room.row, room.col).hasNorthWall() &&
						!visited[room.row - 1][room.col][room.superpowers]) {
					RoomNode northNode = new RoomNode(room.row + DELTAS[0][0], room.col + DELTAS[0][1], room.superpowers);
					nextFrontier.add(northNode);
					visited[northNode.row][northNode.col][northNode.superpowers] = true;
					northNode.parent = room;
					if (!visitedLocation[northNode.row][northNode.col]) {
						visitedLocation[northNode.row][northNode.col] = true;
						numDistance++;
					}

 				} else if (maze.getRoom(room.row, room.col).hasNorthWall()
						&& (room.row - 1) >= 0 && room.superpowers > 0 && !visited[room.row - 1][room.col][room.superpowers - 1]) {
					RoomNode northNode = new RoomNode(room.row + DELTAS[0][0], room.col + DELTAS[0][1], room.superpowers - 1);
					nextFrontier.add(northNode);
					visited[northNode.row][northNode.col][northNode.superpowers] = true;
					northNode.parent = room;
					if (!visitedLocation[northNode.row][northNode.col]) {
						visitedLocation[northNode.row][northNode.col] = true;
						numDistance++;
					}
				}

				//check south
				if (!maze.getRoom(room.row, room.col).hasSouthWall() &&
						!visited[room.row + 1][room.col][room.superpowers]) {
					RoomNode southNode = new RoomNode(room.row + DELTAS[1][0], room.col + DELTAS[1][1], room.superpowers);
					nextFrontier.add(southNode);
					visited[southNode.row][southNode.col][southNode.superpowers] = true;
					southNode.parent = room;
					if (!visitedLocation[southNode.row][southNode.col]) {
						visitedLocation[southNode.row][southNode.col] = true;
						numDistance++;
					}
				} else if (maze.getRoom(room.row, room.col).hasSouthWall()
						&& (room.row + 1 <= maze.getRows() - 1) && room.superpowers > 0 && !visited[room.row + 1][room.col][room.superpowers - 1]) {
					RoomNode southNode = new RoomNode(room.row + DELTAS[1][0], room.col + DELTAS[1][1], room.superpowers - 1);
					nextFrontier.add(southNode);
					visited[southNode.row][southNode.col][southNode.superpowers] = true;
					southNode.parent = room;
					if (!visitedLocation[southNode.row][southNode.col]) {
						visitedLocation[southNode.row][southNode.col] = true;
						numDistance++;
					}
				}

				//check east
				if (!maze.getRoom(room.row, room.col).hasEastWall() &&
						!visited[room.row][room.col + 1][room.superpowers]) {
					RoomNode eastNode = new RoomNode(room.row + DELTAS[2][0], room.col + DELTAS[2][1], room.superpowers);
					nextFrontier.add(eastNode);
					visited[eastNode.row][eastNode.col][eastNode.superpowers] = true;
					eastNode.parent = room;
					if (!visitedLocation[eastNode.row][eastNode.col]) {
						visitedLocation[eastNode.row][eastNode.col] = true;
						numDistance++;
					}
				} else if (maze.getRoom(room.row, room.col).hasEastWall()
						&& (room.col + 1 <= maze.getColumns() - 1) && room.superpowers > 0 && !visited[room.row][room.col + 1][room.superpowers - 1]) {
					RoomNode eastNode = new RoomNode(room.row + DELTAS[2][0], room.col + DELTAS[2][1], room.superpowers - 1);
					nextFrontier.add(eastNode);
					visited[eastNode.row][eastNode.col][eastNode.superpowers] = true;
					eastNode.parent = room;
					if (!visitedLocation[eastNode.row][eastNode.col]) {
						visitedLocation[eastNode.row][eastNode.col] = true;
						numDistance++;
					}
				}

				if (!maze.getRoom(room.row, room.col).hasWestWall() &&
						!visited[room.row][room.col - 1][room.superpowers]) {
					RoomNode westNode = new RoomNode(room.row + DELTAS[3][0], room.col + DELTAS[3][1], room.superpowers);
					nextFrontier.add(westNode);
					visited[westNode.row][westNode.col][westNode.superpowers] = true;
					westNode.parent = room;
					if (!visitedLocation[westNode.row][westNode.col]) {
						visitedLocation[westNode.row][westNode.col] = true;
						numDistance++;
					}
				} else if (maze.getRoom(room.row, room.col).hasWestWall()
						&& (room.col - 1 >= 0) && room.superpowers > 0 && !visited[room.row][room.col - 1][room.superpowers - 1]) {
					RoomNode westNode = new RoomNode(room.row + DELTAS[3][0], room.col + DELTAS[3][1], room.superpowers - 1);
					nextFrontier.add(westNode);
					visited[westNode.row][westNode.col][westNode.superpowers] = true;
					westNode.parent = room;
					if (!visitedLocation[westNode.row][westNode.col]) {
						visitedLocation[westNode.row][westNode.col] = true;
						numDistance++;
					}
				}
			}
			distanceCount++;
			frontier = nextFrontier;
		}

		if (solved) {
			while (endNode.parent != null) {
				RoomNode parent = endNode.parent;
				maze.getRoom(parent.row, parent.col).onPath = true;
				endNode = parent;
			}
		}
		return resultDistance;
	}


	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-sample.txt");
			IMazeSolverWithPower solver = new MazeSolverWithPower();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 3, 3, 2));
			MazePrinter.printMaze(maze);	


			for (int i = 0; i <= 9; ++i) {
				System.out.println("Steps " + i + " Rooms: " + solver.numReachable(i));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
