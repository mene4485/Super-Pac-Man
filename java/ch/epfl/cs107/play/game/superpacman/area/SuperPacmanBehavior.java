package ch.epfl.cs107.play.game.superpacman.area;

import java.util.LinkedList;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.AreaGraph;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Blinky;
import ch.epfl.cs107.play.game.superpacman.actor.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Fantome;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Inky;
import ch.epfl.cs107.play.game.superpacman.actor.Pinky;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.actor.Wall;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Window;

public class SuperPacmanBehavior extends AreaBehavior {

	public enum SuperPacmanCellType {

		NONE(0), // never used as real content
		WALL(-16777216), // black
		FREE_WITH_DIAMOND(-1), // white
		FREE_WITH_BLINKY(-65536), // red
		FREE_WITH_PINKY(-157237), // pink
		FREE_WITH_INKY(-16724737), // cyan
		FREE_WITH_CHERRY(-36752), // light red
		FREE_WITH_BONUS(-16478723), // light blue
		FREE_EMPTY(-6118750); // sort of gray

		final int type;

		SuperPacmanCellType(int type) {
			this.type = type;
		}

		public static SuperPacmanCellType toType(int type) {
			for (SuperPacmanCellType ict : SuperPacmanCellType.values()) {
				if (ict.type == type)
					return ict;
			}
			// When you add a new color, you can print the int value here before
			// assign it to a type
			return NONE;
		}
	}

	/**
	 * Cell adapted to the Super PacMan game
	 */
	public class SuperPacmanCell extends AreaBehavior.Cell {

		/// Type of the cell following the enum
		private final SuperPacmanCellType type;
		int x, y; // position of the SuperPacman

		/**
		 * Default SuperPacmanCell Constructor
		 * 
		 * @param x    (int): x coordinate of the cell
		 * @param y    (int): y coordinate of the cell
		 * @param type (EnigmeCellType), not null
		 */
		public SuperPacmanCell(int x, int y, SuperPacmanCellType type) {
			super(x, y);
			this.x = x;
			this.y = y;
			this.type = type;
		}

		@Override
		public boolean takeCellSpace() {
			return false;
		}

		@Override
		protected boolean canLeave(Interactable entity) {
			return true;
		}

		@Override
		protected boolean canEnter(Interactable entity) {
			if (entity instanceof SuperPacmanPlayer && this.type == SuperPacmanCellType.WALL) { // If SuperPacman
																								// encounters a wall,
																								// canEnter return false
																								// so it can't pass
																								// through the wall
				return false;
			}

			if (entity instanceof SuperPacmanPlayer) {
				SuperPacmanArea area = (SuperPacmanArea) ((SuperPacmanPlayer) entity).getOwnerArea(); // getting the
																										// area
																										// (level0,1
																										// etc. ) where
																										// the
																										// SuperPacman
																										// is
				Gate gate = area.getGate(x, y); // each area has a list of all the gates contained in them
				if (gate != null && !gate.isOpen())// checking if there is an (open) gate at the SuperPacman coordinates
													// to let him pass or not
					return false;
			}
			
			if (entity instanceof Fantome && this.type == SuperPacmanCellType.WALL) {
				return false;
			}
			
			if (entity instanceof Fantome) {
				SuperPacmanArea area = (SuperPacmanArea) ((Fantome) entity).getOwnerArea(); // getting the
																										// area
																										// (level0,1
																										// etc. ) where
																										// the
																										// SuperPacman
																										// is
				Gate gate = area.getGate(x, y); // each area has a list of all the gates contained in them
				if (gate != null && !gate.isOpen())// checking if there is an (open) gate at the SuperPacman coordinates
													// to let him pass or not
					return false;
			}
			
			

			return true; // Otherwise return true
		}

		@Override
		public boolean isCellInteractable() {
			return true;
		}

		@Override
		public boolean isViewInteractable() {
			return false;
		}

		@Override
		public void acceptInteraction(AreaInteractionVisitor v) {
		}

	}

	private int nbDiamondsCreated;
	private AreaGraph areaGraph;
	private SuperPacman superPacman;

	/**
	 * Default Tuto2Behavior Constructor
	 * 
	 * @param window (Window), not null
	 * @param name   (String): Name of the Behavior, not null
	 */
	public SuperPacmanBehavior(Window window, String name, SuperPacman superPacman) {
		super(window, name);
		int height = getHeight();
		int width = getWidth();
		this.superPacman = superPacman;
		areaGraph = new AreaGraph();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				SuperPacmanCellType cellType = SuperPacmanCellType.toType(getRGB(height - 1 - y, x));
				setCell(x, y, new SuperPacmanCell(x, y, cellType));
			}
		}

		// adding a node to aeragraph everywhere where there isn't a wall
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				SuperPacmanCellType type = ((SuperPacmanCell) getCell(x, y)).type;
				if (type != SuperPacmanCellType.WALL) {
					areaGraph.addNode(new DiscreteCoordinates(x, y), hasLeftEdge(x, y), hasUpEdge(x, y),
							hasRightEdge(x, y), hasDownEdge(x, y));
				}
			}
		}

	}

	private boolean hasDownEdge(int x, int y) {
		return y > 0 && ((SuperPacmanCell) getCell(x, y - 1)).type != SuperPacmanCellType.WALL;
	}

	private boolean hasUpEdge(int x, int y) {
		return y < getHeight() - 1 && ((SuperPacmanCell) getCell(x, y + 1)).type != SuperPacmanCellType.WALL;
	}

	private boolean hasRightEdge(int x, int y) {
		return x < getWidth() - 1 && ((SuperPacmanCell) getCell(x + 1, y)).type != SuperPacmanCellType.WALL;
	}

	private boolean hasLeftEdge(int x, int y) {
		return x > 0 && ((SuperPacmanCell) getCell(x - 1, y)).type != SuperPacmanCellType.WALL;
	}

	protected void registerActors(Area area) {
		int height = getHeight();
		int width = getWidth();
		int c = 0;
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				SuperPacmanCell cell = (SuperPacmanCell) getCell(x, y);
				if (cell.type == SuperPacmanCellType.WALL) {
					Wall wall = new Wall(area, new DiscreteCoordinates(x, y), getWallAround(x, y));
					area.registerActor(wall);
				} else if (cell.type == SuperPacmanCellType.FREE_WITH_DIAMOND) {
					Diamond diamond = new Diamond(area, new DiscreteCoordinates(x, y));
					area.registerActor(diamond);
					this.nbDiamondsCreated += 1;
					System.out.println(nbDiamondsCreated);
				} else if (cell.type == SuperPacmanCellType.FREE_WITH_BONUS) {
					Bonus bonus = new Bonus(area, new DiscreteCoordinates(x, y));
					area.registerActor(bonus);
				} else if (cell.type == SuperPacmanCellType.FREE_WITH_CHERRY) {
					Cherry cherry = new Cherry(area, new DiscreteCoordinates(x, y));
					area.registerActor(cherry);
				} else if (cell.type == SuperPacmanCellType.FREE_WITH_BLINKY) {
					Blinky blinky = new Blinky(area, Orientation.DOWN, new DiscreteCoordinates(x, y),
							new DiscreteCoordinates(x, y));
					area.registerActor(blinky);
				} else if (cell.type == SuperPacmanCellType.FREE_WITH_INKY) {
					Inky inky = new Inky(area, Orientation.UP, new DiscreteCoordinates(x, y),
							new DiscreteCoordinates(x, y));
					area.registerActor(inky);
				} else if (cell.type == SuperPacmanCellType.FREE_WITH_PINKY) {
					Pinky pinky = new Pinky(area, Orientation.DOWN, new DiscreteCoordinates(x, y),
							new DiscreteCoordinates(x, y));
					if (superPacman.getMode() == SuperPacman.MULTI_MODE && c == 0) {
						pinky.setPlayer2(true);
						c++; // introducing a variable c to be sure pinky is initialized only once if it's a
								// second player
					}
					area.registerActor(pinky);
				} else if (cell.type == SuperPacmanCellType.FREE_EMPTY) {

				}
			}
		}
	}

	// method to check if the neighborhood around its position contains a wall or
	// not
	public boolean[][] getWallAround(int x, int y) {
		boolean[][] Table = new boolean[3][3];
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				if ((x + j >= 0) && (y + i >= 0) && (x + j < this.getWidth()) && (y + i < this.getHeight())) {
					if (SuperPacmanCellType.WALL == ((SuperPacmanCell) getCell(x + j, y + i)).type)
						Table[j + 1][-i + 1] = true;
					else
						Table[j + 1][-i + 1] = false;
				} else
					Table[j + 1][-i + 1] = false;
			}
		}
		return Table;
	}

	public int getWidth() {
		return super.getWidth();
	}

	public int getHeight() {
		return super.getHeight();
	}

	public AreaGraph getAreaGraph() {
		return areaGraph;
	}

	// Get cells where the ghost can potentially go
	public List<DiscreteCoordinates> getFieldCells(int x, int y, int viewField) {
		List<DiscreteCoordinates> cells = new LinkedList<DiscreteCoordinates>();
		for (int i = x - viewField; i <= x + viewField; i++) {
			for (int j = y - viewField; j <= y + viewField; j++) {
				if (i >= 0 && i < getWidth() && j >= 0 && j < getHeight()) {
					if (((SuperPacmanCell) getCell(i, j)).type != SuperPacmanCellType.WALL) {
						cells.add(new DiscreteCoordinates(i, j));
					}
				}
			}
		}
		return cells;
	}

	public List<DiscreteCoordinates> getRunAwayCells(int x, int y, int minAfraidDistance) {
		List<DiscreteCoordinates> cells = new LinkedList<DiscreteCoordinates>();
		for (int i = 0; i < getWidth(); i++) {
			for (int j = 0; j < getHeight(); j++) {
				if ((i <= x - minAfraidDistance || i >= x + minAfraidDistance)
						&& (j <= y - minAfraidDistance || j >= y + minAfraidDistance)) {
					if (((SuperPacmanCell) getCell(i, j)).type != SuperPacmanCellType.WALL) {
						cells.add(new DiscreteCoordinates(i, j));
					}
				}
			}
		}
		return cells;
	}
	
	public int getNbDiamonds() {
		return this.nbDiamondsCreated;
	}

}
