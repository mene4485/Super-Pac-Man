package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Audio;
import ch.epfl.cs107.play.window.Canvas;

//Class that will extends every object that are collectable, which means that when SuperPacMan passes through, 
//they disappear and can have an effect on the SuperPacman and/or his score  
public class CollectableAreaEntity extends AreaEntity {

	protected int points = 0;

	public CollectableAreaEntity(Area area, Orientation orientation, DiscreteCoordinates position) {
		super(area, orientation, position);
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return false;
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
		// TODO Auto-generated method stub
	}

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
	}

	@Override
	public void draw(Canvas canvas) {
		// TODO Auto-generated method stub
	}

	@Override
	public void bip(Audio audio) {
		// TODO Auto-generated method stub
	}

	public int getPoints() {
		return points;
	}
	
}
