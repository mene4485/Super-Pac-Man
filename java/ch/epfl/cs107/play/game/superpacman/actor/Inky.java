package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Inky extends Fantome {

	private final int MAX_DISTANCE_WHEN_SCARED = 5;
	private final int MAX_DISTANCE_WHEN_NOT_SCARED = 10;

	public Inky(Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates refuge) {
		super(area, orientation, position, refuge, "superpacman/ghost.inky");
	}

	@Override
	public Orientation getNextOrientation() {
		if (!isDisplacementOccurs() && path != null && !path.isEmpty())
			return path.poll();
		return null;
	}

	@Override
	protected void calculatePath() {
		DiscreteCoordinates targetCell = null;
		List<DiscreteCoordinates> targets = new LinkedList<DiscreteCoordinates>();
		boolean change = false;

		targets = ((SuperPacmanArea) getOwnerArea()).getBehavior().getFieldCells(refuge.x, refuge.y,
				MAX_DISTANCE_WHEN_NOT_SCARED);

		//This whole block is to get the targets
		if (isAfraid()) { //change the target if he is afraid 
			change = true;
			targets = ((SuperPacmanArea) getOwnerArea()).getBehavior().getFieldCells(refuge.x, refuge.y,
					MAX_DISTANCE_WHEN_SCARED); //the player becomes the target
		} else {
			if (player != null) {
				change = true;
				targets = ((SuperPacmanArea) getOwnerArea()).getBehavior()
						.getFieldCells(player.getCurrentCells().get(0).x, player.getCurrentCells().get(0).y, 1);
			}
		}

		//This whole block is to get the shortest path
		if (change || path == null || (path != null && path.isEmpty())) {
			if (targets.size() > 0) {
				Random rand = new Random();
				targetCell = targets.get(rand.nextInt(targets.size()));
			}
			if (targetCell != null) {
				path = ((SuperPacmanArea) getOwnerArea()).getBehavior().getAreaGraph()
						.shortestPath(getCurrentMainCellCoordinates(), targetCell);
			}
		}
	}

}
