import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;
public class MazeSolver implements IMazeSolver {
	private static final int TRUE_WALL = Integer.MAX_VALUE;
	private static final int EMPTY_SPACE = 0;
	private PriorityQueue<RoomNode> priorityQueue;
	private int[][] distanceTo;
	boolean[][] visited;
	boolean solved = false;
	private static final List<Function<Room, Integer>> WALL_FUNCTIONS = Arrays.asList(
			Room::getNorthWall,
			Room::getEastWall,
			Room::getWestWall,
			Room::getSouthWall
	);
	private static final int[][] DELTAS = new int[][] {
			{ -1, 0 }, // North
			{ 0, 1 }, // East
			{ 0, -1 }, // West
			{ 1, 0 } // South
	};

	private Maze maze;

	public class RoomNode implements Comparable<RoomNode> {

		public int fear;
		public int row;
		public int col;

		public RoomNode(int row, int col, int fear) {
			this.row = row;
			this.col = col;
			this.fear = fear;
		}

		@Override
		public int compareTo(RoomNode o) {
			if (this.fear < o.fear) {
				return -1;
			} else if (this.fear > o.fear) {
				return 1;
			} else {
				return 0;
			}
		}

		@Override
		public String toString() {
			String str = "";
			str = String.format("%s %s %s  ", row, col, fear);
			return str;
		}
	}

	public MazeSolver() {
		this.priorityQueue = new PriorityQueue<>();
	}

	@Override
	public void initialize(Maze maze) {
		this.maze = maze;
		this.distanceTo = new int[maze.getRows()][maze.getColumns()];
		this.visited = new boolean[maze.getRows()][maze.getColumns()];
	}

	public void relax(RoomNode from, RoomNode to, int weight) {
		if (distanceTo[from.row][from.col] + weight < distanceTo[to.row][to.col]) {
			distanceTo[to.row][to.col] = distanceTo[from.row][from.col] + weight;
			RoomNode bruh = new RoomNode(to.row, to.col, from.fear + weight);
			priorityQueue.add(bruh);
		}
	}

	@Override
	public Integer pathSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		if (maze == null) {
			throw new Exception("Oh no! You cannot call me without initializing the maze!");
		}

		if (startRow < 0 || startCol < 0 || startRow >= maze.getRows() || startCol >= maze.getColumns() ||
				endRow < 0 || endCol < 0 || endRow >= maze.getRows() || endCol >= maze.getColumns()) {
			throw new IllegalArgumentException("Invalid start/end coordinate");
		}
		solved = false;

		RoomNode startNode = new RoomNode(startRow, startCol, 0);
		priorityQueue.add(startNode);
		for (int i = 0; i < maze.getRows(); i++) {
			for (int j = 0; j < maze.getColumns(); j++) {
				distanceTo[i][j] = Integer.MAX_VALUE;
			}
		}
		distanceTo[startRow][startCol] = 0;

		while (!priorityQueue.isEmpty()) {
			for (int i = 0; i < distanceTo.length; i++) {
				System.out.println(Arrays.toString(distanceTo[i]));
			}
			System.out.print(priorityQueue.peek().toString());
			RoomNode focusRoom = priorityQueue.poll();
			Room focus = maze.getRoom(focusRoom.row, focusRoom.col);

			if (focusRoom.row == endRow && focusRoom.col == endCol) {
				return distanceTo[focusRoom.row][focusRoom.col];
			}
			//check north
			if (!(focus.getNorthWall() == Integer.MAX_VALUE)) {
				int northFear = focus.getNorthWall() == 0 ? 1 : focus.getNorthWall();
				RoomNode northNode = new RoomNode(focusRoom.row + DELTAS[0][0], focusRoom.col + DELTAS[0][1], 0);
				relax(focusRoom, northNode, northFear);
				if (northNode.row == endRow && northNode.col == endCol) {
					return distanceTo[northNode.row][northNode.col];
				}
			}

			if (!(focus.getEastWall() == Integer.MAX_VALUE)) {
				int eastFear = focus.getEastWall() == 0 ? 1 : focus.getEastWall();
				RoomNode eastNode = new RoomNode(focusRoom.row + DELTAS[1][0], focusRoom.col + DELTAS[1][1], 0);
				relax(focusRoom, eastNode, eastFear);
				if (eastNode.row == endRow && eastNode.col == endCol) {
					return distanceTo[eastNode.row][eastNode.col];
				}
			}

			if (!(focus.getWestWall() == Integer.MAX_VALUE)) {
				int westFear = focus.getWestWall() == 0 ? 1 : focus.getWestWall();
				RoomNode westNode = new RoomNode(focusRoom.row + DELTAS[2][0], focusRoom.col + DELTAS[2][1], 0);
				relax(focusRoom, westNode, westFear);
				if (westNode.row == endRow && westNode.col == endCol) {
					return distanceTo[westNode.row][westNode.col];
				}
			}

			if (!(focus.getSouthWall() == Integer.MAX_VALUE)) {
				int southFear = focus.getSouthWall() == 0 ? 1 : focus.getSouthWall();
				RoomNode southNode = new RoomNode(focusRoom.row + DELTAS[3][0], focusRoom.col + DELTAS[3][1], 0);
				relax(focusRoom, southNode, southFear);
				if (southNode.row == endRow && southNode.col == endCol) {
					return distanceTo[southNode.row][southNode.col];
				}
			}





		}

		if (distanceTo[endRow][endCol] == Integer.MAX_VALUE) {
			return null;
		} else {
			return distanceTo[endRow][endCol];
		}


	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol) throws Exception {
		// TODO: Find minimum fear level given new rules.
		return null;
	}

	@Override
	public Integer bonusSearch(int startRow, int startCol, int endRow, int endCol, int sRow, int sCol) throws Exception {
		// TODO: Find minimum fear level given new rules and special room.
		return null;
	}

	public static void main(String[] args) {
		try {
			Maze maze = Maze.readMaze("maze-empty.txt");
			IMazeSolver solver = new MazeSolver();
			solver.initialize(maze);

			System.out.println(solver.pathSearch(0, 0, 2,3));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
