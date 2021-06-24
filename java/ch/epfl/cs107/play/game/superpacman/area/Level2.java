package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.actor.Gate;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.math.DiscreteCoordinates;

public class Level2 extends SuperPacmanArea {

	public static final DiscreteCoordinates PLAYER_SPAWN_POSITION = new DiscreteCoordinates(15, 29);
	public static final String KEY = "superpacman/Level2";

	
	public Level2(SuperPacman superPacman) {
		super(superPacman);
	}
	
	@Override
	public String getTitle() {
		return KEY;
	}

	@Override
	protected void createArea() {
		//registerActor(new Background(this));
		Key key1 = new Key(this, new DiscreteCoordinates(3, 16));
		registerActor(key1);
		Key key2 = new Key(this, new DiscreteCoordinates(26, 16));
		registerActor(key2);
		Key key3 = new Key(this, new DiscreteCoordinates(2, 8));
		registerActor(key3);
		Key key4 = new Key(this, new DiscreteCoordinates(27, 8));
		registerActor(key4);
		
		Gate gate1 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 14), key1);
		registerActor(gate1);
		gates.add(gate1);
		Gate gate2 = new Gate(this, Orientation.DOWN, new DiscreteCoordinates(5, 12), key1);
		registerActor(gate2);
		gates.add(gate2);
		Gate gate3 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 10), key1);
		registerActor(gate3);
		gates.add(gate3);
		Gate gate4 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(8, 8), key1);
		registerActor(gate4);
		gates.add(gate4);
		
		Gate gate5 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 14), key2);
		registerActor(gate5);
		gates.add(gate5);
		Gate gate6 = new Gate(this, Orientation.DOWN, new DiscreteCoordinates(24, 12), key2);
		registerActor(gate6);
		gates.add(gate6);
		Gate gate7 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 10), key2);
		registerActor(gate7);
		gates.add(gate7);
		Gate gate8 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(21, 8), key2);
		registerActor(gate8);
		gates.add(gate8);
		
		Gate gate9 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(10, 2), key3);
		registerActor(gate9);
		gates.add(gate9);
		Gate gate10 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(19, 2), key3);
		registerActor(gate10);
		gates.add(gate10);
		Gate gate11 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(12, 8), key3);
		registerActor(gate11);
		gates.add(gate11);
		Gate gate12 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(17, 8), key3);
		registerActor(gate12);
		gates.add(gate12);
		
		Gate gate13 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(10, 2), key4);
		registerActor(gate13);
		gates.add(gate13);
		Gate gate14 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(19, 2), key4);
		registerActor(gate14);
		gates.add(gate14);
		Gate gate15 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(12, 8), key4);
		registerActor(gate15);
		gates.add(gate15);
		Gate gate16 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(17, 8), key4);
		registerActor(gate16);
		gates.add(gate16);
		
		Gate gate17 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(14, 3), this);
		registerActor(gate17);
		gates.add(gate17);
		Gate gate18 = new Gate(this, Orientation.RIGHT, new DiscreteCoordinates(15, 3), this);
		registerActor(gate18);
		gates.add(gate18);
		
		super.createArea();
	}

}
