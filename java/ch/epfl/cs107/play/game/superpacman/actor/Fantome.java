package ch.epfl.cs107.play.game.superpacman.actor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.actor.Animation;
import ch.epfl.cs107.play.game.areagame.actor.Interactable;
import ch.epfl.cs107.play.game.areagame.actor.Interactor;
import ch.epfl.cs107.play.game.areagame.actor.MovableAreaEntity;
import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.areagame.actor.Path;
import ch.epfl.cs107.play.game.areagame.actor.Sprite;
import ch.epfl.cs107.play.game.areagame.handler.AreaInteractionVisitor;
import ch.epfl.cs107.play.game.rpg.actor.Player;
import ch.epfl.cs107.play.game.rpg.actor.RPGSprite;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.handler.SuperPacmanInteractionVisitor;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.window.Canvas;

public abstract class Fantome extends MovableAreaEntity implements Interactable, Interactor {

	private final int SPEED = 5;
	private int MOVE_FRAME = 10;
	public static int GHOST_SCORE = 500;
	public static int VIEW_FIELD = 5;

	private float intouchableTime = 0;

	protected boolean player2;

	protected DiscreteCoordinates refuge;
	protected Player player;

	private Sprite[][] afraidSprites;
	private Animation[] afraidAnimations;
	private Sprite[][] sprites;
	private Animation[] animations;
	private Animation currentAnimation;
	protected Orientation desiredOrientation;

	private boolean afraid;

	protected Queue<Orientation> path;

	public Fantome(Area area, Orientation orientation, DiscreteCoordinates position, DiscreteCoordinates refuge,
			String spriteName) {
		super(area, orientation, position);

		this.refuge = refuge;
		player = null;

		// Initialiser l'animation par d�faut
		sprites = RPGSprite.extractSprites(spriteName, 2, 1, 1, this, 16, 16,
				new Orientation[] { Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT });
		animations = Animation.createAnimations(SPEED / 2, sprites);
		currentAnimation = animations[Orientation.UP.ordinal()];

		// Initialiser l'animation de peur
		afraidSprites = RPGSprite.extractSprites("superpacman/ghost.afraid", 2, 1, 1, this, 16, 16,
				new Orientation[] { Orientation.UP, Orientation.RIGHT, Orientation.DOWN, Orientation.LEFT });
		afraidAnimations = Animation.createAnimations(SPEED / 2, afraidSprites);

		desiredOrientation = Orientation.LEFT;

		init();

	}

	protected abstract void calculatePath();// to be defined by each type of ghost

	// getter to know if the ghost is afraid or not
	public boolean isAfraid() {
		return afraid;
	}

	protected void setAfraid(boolean afraid) {
		this.afraid = afraid;
		calculatePath();
	}

	protected void setPlayer(Player player) {
		this.player = player;
		calculatePath();
	}

	// Initialiser la position du fantome
	public boolean init() {
		intouchableTime = 2;
		setPlayer(null);
		abortCurrentMove();
		resetMotion();
		Area area = getOwnerArea();
		area.leaveAreaCells(this, getCurrentCells());
		this.setCurrentPosition(refuge.toVector());
		return true;
	}
	
	public boolean respawn() {
		intouchableTime = 2;
		setPlayer(null);
		abortCurrentMove();
		resetMotion();
		Area area = getOwnerArea();
		area.leaveAreaCells(this, getEnteredCells());
		this.setCurrentPosition(refuge.toVector());
		return true;
	}

	@Override
	public void draw(Canvas canvas) {
		currentAnimation.draw(canvas);
		// To draw the path
		/*
		 * if (path != null & !isDisplacementOccurs()) { Path graphicPath = new
		 * Path(this.getPosition(), new LinkedList<Orientation>(path));
		 * graphicPath.draw(canvas); }
		 */
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
		return true;
	}

	@Override
	public void acceptInteraction(AreaInteractionVisitor v) {
		((SuperPacmanInteractionVisitor) v).interactWith(this);
	}

	@Override
	public void update(float deltaTime) {
		calculatePath();
		desiredOrientation = getNextOrientation();
		if (!isDisplacementOccurs() && desiredOrientation != null) {
			List<DiscreteCoordinates> coords = Collections
					.singletonList(getCurrentMainCellCoordinates().jump(desiredOrientation.toVector()));
			if (getOwnerArea().canEnterAreaCells(this, coords)) {
				orientate(desiredOrientation);
				resetMotion();
			}
			move(MOVE_FRAME);
		}
		checkVulnerability();
		updateAnimation(deltaTime);

		/* Time to be intouchable */
		intouchableTime -= deltaTime;
		if (intouchableTime < 0)
			intouchableTime = 0;

		super.update(deltaTime);
	}

	private void checkVulnerability() {
		SuperPacmanArea area = (SuperPacmanArea) getOwnerArea();
		SuperPacman superPacman = area.getAreaGame();
		boolean newAfraidState = !((SuperPacmanPlayer) superPacman.getPlayer()).isVulnerable();// to know if the
																								// invulnerabilityTime
																								// is finished

		// if invulnerabilityTime is not finished, then newAfraidState = true and if
		// isAfraid is false so afraid becomes true and if isAfraid is true, nothing
		// happens
		// if the invulnerabilityTime is finished, then newAfraidState = false and
		// isAfraid is true so afraid becomes false
		if (newAfraidState != isAfraid()) {
			setAfraid(newAfraidState);
		}
	}

	private void updateAnimation(float deltaTime) {
		if (isDisplacementOccurs()) {
			int currentOrientation = getOrientation().ordinal();
			if (isAfraid()) {
				currentAnimation = afraidAnimations[0];
			} else {
				currentAnimation = animations[currentOrientation];
			}
			currentAnimation.update(deltaTime);
		} else {
			currentAnimation.reset();
		}
	}

	@Override
	public boolean wantsCellInteraction() {
		return false;
	}

	@Override
	public boolean wantsViewInteraction() {
		return true;
	}

	@Override
	public List<DiscreteCoordinates> getFieldOfViewCells() {
		List<DiscreteCoordinates> field = new LinkedList<>();
		field = ((SuperPacmanArea) getOwnerArea()).getBehavior().getFieldCells(getCurrentMainCellCoordinates().x,
				getCurrentMainCellCoordinates().y, VIEW_FIELD);
		return field;
	}

	@Override
	public void interactWith(Interactable other) {
		if (other instanceof Player) {
			setPlayer((SuperPacmanPlayer) other);
		}
	}

	public float getIntouchableTime() {
		return intouchableTime;
	}

	public boolean isPlayer2() {
		return player2;
	}

	public void setPlayer2(boolean player2) {
		this.player2 = player2;
	}
	
	public Area getOwnerArea() {
		return super.getOwnerArea();
	}
	
	public void setMoveFrame(int moveframe) {
		this.MOVE_FRAME = moveframe;
	}

	public abstract Orientation getNextOrientation();

}
