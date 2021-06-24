package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.AreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.signal.logic.Logic;
import ch.epfl.cs107.play.window.Canvas;

public class Gate extends AreaEntity {

	private Sprite sprite;
	private Logic signal;

	public Gate(Area area, Orientation orientation, DiscreteCoordinates position, Logic signal) {
		super(area, orientation, position);
		if (orientation == Orientation.UP || orientation == Orientation.DOWN) //The region (0,0) will be extracted if the barrier is oriented vertically (DOWN or UP) and (0,64) otherwise
			sprite = new RPGSprite("superpacman/gate", 1, 1, this, new RegionOfInterest(0, 0, 64, 64));
		else
			sprite = new RPGSprite("superpacman/gate", 1, 1, this, new RegionOfInterest(0, 64, 64, 64));
		this.signal = signal;
	}

	@Override
	public void draw(Canvas canvas) {
		if (sprite != null && !signal.isOn())
			sprite.draw(canvas);
	}


	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return false;
	}

	@Override
	public boolean isViewInteractable() {
		return false;
	}
	
	@Override
	public void update(float deltaTime) {
		super.update(deltaTime);
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
	}
		
	public boolean isOpen() {
		return signal.isOn();
	}

	protected void setSignal(Logic signal){
		this.signal = signal;
	}

}
