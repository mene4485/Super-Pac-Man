package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.signal.logic.Logic;

public class Level0 extends SuperPacmanArea {

	// We make this attributes static so it's easy to get them from another class
	// level to code the door passes.
	public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(10, 1);
	public static final String KEY = "superpacman/Level0";

	public Level0(SuperPacman superPacman) {
		super(superPacman);
	}

	@Override
	public String getTitle() {
		return KEY;
	}

	@Override
	protected void createArea() {
		// registerActor(new Background(this));
		Door door = new Door(Level1.KEY, Level1.PLAYER_SPAWN_POSITION, Logic.TRUE, this, Orientation.UP,
				new DiscreteCoordinates(5, 9), new DiscreteCoordinates(6, 9));
		registerActor(door);

		Key key = new Key(this, new DiscreteCoordinates(3, 4));
		registerActor(key);

		Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(5, 8), key);
		gates.add(gate1);
		registerActor(gate1); // gates is a list in SuperPacmanArea that gathers all the gates in all the
								// levels (to have all the coordinates where there is a gate )

		Gate gate2 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(6, 8), key);
		registerActor(gate2);
		gates.add(gate2);

		super.createArea();
	}

}
