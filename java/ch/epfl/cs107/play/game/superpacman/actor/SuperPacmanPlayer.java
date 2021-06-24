package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.List;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Door;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Button;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Keyboard;

public class SuperPacmanPlayer extends Player {

	private final static int SPEED = 5;

	private Sprite[][] sprites;
	private Animation[] animations;
	private Animation currentAnimation;// creating currentAnimation, that will change when the sprite moves and the
										// draw method draws this exact current animation
	private float invulnerabilityTime;
	private float recreatingTime = 0; 

	private SuperPacmanPlayerStatusGUI status;

	private final SuperPacmanPlayerHandler handler;
	private Orientation desiredOrientation;
	
	public static int diamondsEaten;

	private class SuperPacmanPlayerHandler implements SuperPacmanInteractionVisitor {

		@Override
		public void interactWith(Door door) {
			setIsPassingADoor(door);
		}

		@Override
		public void interactWith(Diamond diamond) {
			SuperPacmanPlayer.this.collect(diamond);// gets the points
			getOwnerArea().unregisterActor(diamond);// so it disappears from the area
			diamondsEaten += 1;
			System.out.println("One more eaten : " + diamondsEaten);
		}

		@Override
		public void interactWith(Bonus bonus) {
			SuperPacmanPlayer.this.invulnerabilityTime += bonus.getInvulnerabilityTime(); //starts timer of invulnerability of SuperPacman
			SuperPacmanPlayer.this.collect(bonus);// gets the points
			getOwnerArea().unregisterActor(bonus); // so it disappears from the area
		}

		@Override
		public void interactWith(Cherry cherry) {
			SuperPacmanPlayer.this.collect(cherry);// gets the points
			getOwnerArea().unregisterActor(cherry);// so it disappears from the area
		}

		@Override
		public void interactWith(Key key) {
			SuperPacmanPlayer.this.collect(key);
			getOwnerArea().unregisterActor(key);// so it disappears from the area
		}
		
		@Override
		public void interactWith(Fantome fantome) {
			if (isVulnerable()) {
				if (recreatingTime == 0) {
					SuperPacmanPlayer.this.dead();
					fantome.setPlayer(null);
					recreatingTime = 2; //2 seconds of invulnearibility
				}
			} else {
				if (fantome.getIntouchableTime() == 0) {
					fantome.respawn();
					status.addScore(Fantome.GHOST_SCORE);
				}
			}
		}
	
	}

	// Path graphicPath;

	/**
	 * Default SuperPacmanPlayer Constructor
	 * 
	 * @param area        (Area), not null
	 * @param orientation (Orientation): orientation of the pacman when the game
	 *                    starts, not null
	 * @param coordinates (DiscreteCoordinates) : coordinates of the pacman when the
	 *                    game starts, not null
	 */
	public SuperPacmanPlayer(Area area, Orientation orientation, DiscreteCoordinates coordinates) {
		super(area, orientation, coordinates);
		handler = new SuperPacmanPlayerHandler();
		sprites = RPGSprite.extractSprites("superpacman/pacman", 4, 1, 1, this, 64, 64,
				new Orientation[] { Orientation.DOWN, Orientation.LEFT, Orientation.UP, Orientation.RIGHT });
		animations = Animation.createAnimations(2, sprites);
		currentAnimation = animations[Orientation.UP.ordinal()]; // set first current orientation to UP
		setStatus(new SuperPacmanPlayerStatusGUI());
		desiredOrientation = orientation;
	}

	public void dead() {
		SuperPacmanPlayer.this.resetMotion();
		// Substract one life
		status.takeLife();

		// Go to initial position
		getOwnerArea().leaveAreaCells(SuperPacmanPlayer.this, SuperPacmanPlayer.this.getEnteredCells());
		DiscreteCoordinates position = null;
		if (getOwnerArea() instanceof Level0)
			position = Level0.PLAYER_SPAWN_POSITION;
		else if (getOwnerArea() instanceof Level1)
			position = Level1.PLAYER_SPAWN_POSITION;
		else if (getOwnerArea() instanceof Level2)
			position = Level2.PLAYER_SPAWN_POSITION;
		this.setCurrentPosition(position.toVector());
	}

	public SuperPacmanPlayerStatusGUI getStatus() {
		return status;
	}

	public void setStatus(SuperPacmanPlayerStatusGUI status) {
		this.status = status;
	}

	@Override
	public List<DiscreteCoordinates> getCurrentCells() {
		return Collections.singletonList(getCurrentMainCellCoordinates());
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		return null;
	}

	@Override
	public boolean wantsCellInteraction() {
		return true;
	}

	@Override
	public boolean wantsViewInteraction() {
		return false;
	}

	@Override
	public void interactWith(Interactable other) {
		other.acceptInteraction(handler);
	}

	@Override
	public boolean takeCellSpace() {
		return true;
	}

	@Override
	public boolean isCellInteractable() {
		return true;
	}

	@Override
	public boolean isViewInteractable() {
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas); // we draw only the current animation
		status.draw(canvas); // drawing the Score and lives
	}

	@Override
	public void update(float deltaTime) {
		Keyboard keyboard = getOwnerArea().getKeyboard();
		moveOrientate(Orientation.LEFT, keyboard.get(Keyboard.LEFT));
		moveOrientate(Orientation.UP, keyboard.get(Keyboard.UP));
		moveOrientate(Orientation.RIGHT, keyboard.get(Keyboard.RIGHT));
		moveOrientate(Orientation.DOWN, keyboard.get(Keyboard.DOWN));

		if (!isDisplacementOccurs()) {
			List<DiscreteCoordinates> coords = Collections
					.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()));
			if (getOwnerArea().canEnterAreaCells(this, coords)) {
				orientate(desiredOrientation);
				resetMotion();
			}
			move(SPEED);
		}

		updateAnimation(deltaTime);

		invulnerabilityTime -= deltaTime;
		if (invulnerabilityTime < 0)
			invulnerabilityTime = 0;

		recreatingTime -= deltaTime;
		if (recreatingTime < 0)
			recreatingTime = 0;

		super.update(deltaTime);
	}

	/**
	 * Orientate this player in the given orientation if the given button is down
	 * 
	 * @param orientation (Orientation): given orientation, not null
	 * @param b           (Button): button corresponding to the given orientation,
	 *                    not null
	 */

	private void moveOrientate(Orientation orientation, Button b) {
		if (b.isDown()) {
			desiredOrientation = orientation;
		}
	}

	private void updateAnimation(float deltaTime) {// method that deals with the update of animations
		if (isDisplacementOccurs()) {
			int currentOrientation = getOrientation().ordinal();
			currentAnimation = animations[currentOrientation]; // getting the currentOrientation
			currentAnimation.update(deltaTime); // the methods "update" of Animation changes and updates the image
		} else {
			currentAnimation.reset(); // when the pacman stops, the animations is reset to the first animation of the
										// array Animations, so it's original picture
		}
	}

	public void collect(CollectableAreaEntity collectableAreaEntity) {
		status.addScore(collectableAreaEntity.getPoints()); // methods that add the points collected by picking the
															// objects
	}

	@Override
	public Area getOwnerArea() {
		return super.getOwnerArea();
	}

	public boolean isVulnerable() {
		return invulnerabilityTime == 0;
	}
	
	public void enterArea(Area area, DiscreteCoordinates position) {
		super.enterArea(area, position);
		diamondsEaten = 0;
	}

}
