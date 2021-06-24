package ch.epfl.cs107.play.game.superpacman.area;

import java.util.LinkedList;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Window;

public abstract class SuperPacmanArea extends Area implements Logic { // implemeting Logic because a SuperPacmanArea
																		// should become a signal that is activated when
																		// all diamonds are collected.

	public static float CAMERA_SCALE_FACTOR = 20.f;

	protected List<Gate> gates;
	private SuperPacmanBehavior behavior;

	private SuperPacman superPacman;
	private SuperPacmanPlayer player;

	public SuperPacmanArea(SuperPacman superPacman) {
		this.superPacman = superPacman;
		gates = new LinkedList<>();
	}

	/**
	 * Create the area by adding it all actors called by begin method Note it set
	 * the Behavior as needed !
	 */
	protected void createArea() {
		behavior.registerActors(this);
	}

	@Override
	public final float getCameraScaleFactor() {
		return SuperPacmanArea.CAMERA_SCALE_FACTOR;
	}
	
	public static void setCAMERA_SCALE_FACTOR(float cAMERA_SCALE_FACTOR) {
		CAMERA_SCALE_FACTOR = cAMERA_SCALE_FACTOR;
	}

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		if (super.begin(window, fileSystem)) {
			// Set the behavior map
			behavior = new SuperPacmanBehavior(window, getTitle(), superPacman);
			setBehavior(behavior);
			createArea();
			return true;
		}
		return false;
	}

	@Override
	public boolean isOn() {
		return behavior.getNbDiamonds() == player.diamondsEaten;
	}

	@Override
	public boolean isOff() {
		return behavior.getNbDiamonds() != player.diamondsEaten;
	}

	@Override
	public float getIntensity() {
		return 1;
	}

	public Gate getGate(int x, int y) {
		for (int i = 0; i < gates.size(); i++) {
			if (gates.get(i).getCurrentCells().contains(new DiscreteCoordinates(x, y))) {
				return gates.get(i);
			}
		}
		return null;
	}

	public SuperPacmanBehavior getBehavior() {
		return behavior;
	}

	public SuperPacman getAreaGame() {
		return superPacman;
	}

}
