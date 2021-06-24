package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level1 extends SuperPacmanArea {

	public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 6);
	public static final String KEY = "superpacman/Level1";
	
	public Level1(SuperPacman superPacman) {
		super(superPacman);
	}
	
	@Override
	public String getTitle() {
		return KEY;
	}
	
	@Override
	protected void createArea() {
		//registerActor(new Background(this));
		Door door = new Door(Level2.KEY, Level2.PLAYER_SPAWN_POSITION, Logic.TRUE, this, Orientation.DOWN, new DiscreteCoordinates(14, 0), new DiscreteCoordinates(15, 0));
		registerActor(door);
		
		Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3), this);
		registerActor(gate1);
		gates.add(gate1);
		
		Gate gate2 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3), this);
		registerActor(gate2);
		gates.add(gate2);
		
		setViewCandidate(null);
		super.createArea();
	}
	
}
