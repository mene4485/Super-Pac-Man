package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanBehavior;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Keyboard;

public class Pinky extends Fantome {

	private final int MIN_AFRAID_DISTANCE = 5;
	private final int MAX_RANDOM_ATTEMPT = 200;
	private Orientation orientation;

	private int currentAttempt = 0;

	public Pinky(Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates refuge) {
		super(area, orientation, position, refuge, "superpacman/ghost.pinky");
		orientation = Orientation.DOWN;
		if (isPlayer2()) {
			getOwnerArea().setViewCandidate(null);
		}
	}
	

	@Override
	public Orientation getNextOrientation() {
		//In the case where the game mode is multiplayer
		if (isPlayer2()) {
			return orientation;
		}
		
		// In the case where the game mode is solo
		if (!isDisplacementOccurs() && path != null && !path.isEmpty()) {
			return path.poll();
		}
		return null;
	}

	/**
	 * In the case where the game mode is multiplayer
	 * */
	@Override
	public void update(float deltaTime) {
		if (isPlayer2()) {
			getOwnerArea().setViewCandidate(null);
			SuperPacmanArea.setCAMERA_SCALE_FACTOR(30.f);
			setMoveFrame(4);
			Keyboard keyboard = getOwnerArea().getKeyboard();
			moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.A));
			moveOrientate(Orientation.UP, keyboard.get(Keyboard.W));
			moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.D));
			moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.S));
		}
		super.update(deltaTime);
	}

	private void moveOrientate(Orientation orientation, Button b) {
		if (b.isDown()) {
			this.orientation = orientation;
		}
	}

	
	/**
	 * In the case where the game mode is solo
	 * */
	@Override
	protected void calculatePath() {
		DiscreteCoordinates targetCell = null;
		List<DiscreteCoordinates> targets = new LinkedList<DiscreteCoordinates>();
		boolean change = false;

		SuperPacmanBehavior behavior = ((SuperPacmanArea) getOwnerArea()).getBehavior();

		targets = behavior.getFieldCells(getCurrentMainCellCoordinates().x, getCurrentMainCellCoordinates().y,
				Math.max(behavior.getWidth(), behavior.getHeight()));

		if (isAfraid()) {
			if (player != null && currentAttempt < MAX_RANDOM_ATTEMPT) {
				change = true;
				targets = behavior.getRunAwayCells(player.getCurrentCells().get(0).x, player.getCurrentCells().get(0).y,
						MIN_AFRAID_DISTANCE);
				currentAttempt++;
			}
		} else {
			if (player != null) {
				change = true;
				targets = behavior.getFieldCells(player.getCurrentCells().get(0).x, player.getCurrentCells().get(0).y,
						1);
			}
		}

		if (change || path == null || (path != null && path.isEmpty())) {
			if (targets.size() > 0) {
				Random rand = new Random();
				targetCell = targets.get(rand.nextInt(targets.size()));
			}
			if (targetCell != null) {
				path = behavior.getAreaGraph().shortestPath(getCurrentMainCellCoordinates(), targetCell);
			}
		}
	}

}
