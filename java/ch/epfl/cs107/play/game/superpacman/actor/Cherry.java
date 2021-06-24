package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Cherry extends CollectableAreaEntity {

	private Sprite sprite;

	public Cherry(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
		sprite = new RPGSprite("superpacman/cherry", 1, 1, this);
		points = 200;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		if (sprite != null)
			sprite.draw(canvas);
	}

}
