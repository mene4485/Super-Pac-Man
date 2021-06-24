package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RandomGenerator;

public class Blinky extends Fantome{

	public Blinky(Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates refuge) {
		super(area, orientation, position, refuge,"superpacman/ghost.blinky");
	}
	
	public Orientation getNextOrientation(){
		int randomInt = RandomGenerator.getInstance().nextInt(4);
		return Orientation.fromInt(randomInt); //returns orientation randomly chosen by a number between 0 and 3
	}

	@Override
	protected void calculatePath() {
		//does nothing because blinky moves by himself randomly
	}

}
