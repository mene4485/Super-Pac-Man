package ch.epfl.cs107.play.game.superpacman.actor;

import java.awt.Color;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.actor.TextGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.math.RegionOfInterest;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class SuperPacmanPlayerStatusGUI implements Graphics {

	private int score = 0;
	private final int MAX_VIES = 5;
	private int vies = 5;

	public SuperPacmanPlayerStatusGUI() {
	}

	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
		for (int i = 1; i <= MAX_VIES; i++) {
			int n = i - 1;
			int m = i <= vies ? 0 : 64;
			ImageGraphics life = new ImageGraphics(ResourcePath.getSprite("superpacman/lifeDisplay"), 1.f, 1.f,
					new RegionOfInterest(m, 0, 64, 64), anchor.add(new Vector(n, height - 1.5f)), 1, 1);
			life.draw(canvas);
		}
		TextGraphics scoreText = new TextGraphics("Score : " + score, 1, Color.YELLOW, Color.BLACK, 0, false, false,
				anchor.add(new Vector(MAX_VIES + 1, height - 1.375f)));
		scoreText.draw(canvas);

	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public void addScore(int score) {
		this.score += score;
	}

	public void takeLife() {
		vies--;
	}

	public boolean isDead() {
		return vies == 0;
	}

	public void reset() {
		vies = MAX_VIES;
		score = 0;
	}
	

}
