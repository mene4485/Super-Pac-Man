package ch.epfl.cs107.play.game.superpacman.actor;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public class Bonus extends CollectableAreaEntity {

	private final static int SPEED = 6;

	private Animation animation;
	
	private int invulnerabilityTime = 10;

	public Bonus(Area area, DiscreteCoordinates position) {
		super(area, Orientation.UP, position);
		Sprite sprite[] = RPGSprite.extractSprites("superpacman/coin", 4, 1, 1, this, 16, 16);
		animation = new Animation(SPEED / 2, sprite);
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void update(float deltaTime) {
		animation.update(deltaTime);
	}

	@Override
	public void draw(Canvas canvas) {
		if (animation != null)
			animation.draw(canvas);
	}
	
	public int getInvulnerabilityTime() {
		return invulnerabilityTime;
	}
	
}
