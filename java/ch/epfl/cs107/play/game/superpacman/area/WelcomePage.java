package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.Area;
import ch.epfl.cs107.play.game.areagame.AreaBehavior;
import ch.epfl.cs107.play.game.areagame.actor.Background;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.math.DiscreteCoordinates;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;
import ch.epfl.cs107.play.window.Window;

public class WelcomePage implements Graphics {

	private SuperPacman superPacman;

	public WelcomePage(SuperPacman superPacman) {
		this.superPacman = superPacman;

	}

	@Override
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		
		//Main image
		Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width / 2, height / 2));
		ImageGraphics image = new ImageGraphics(ResourcePath.getSprite("superpacman/welcome"), width, height, null,
				anchor);
		image.draw(canvas);
		
		//Arrow
		Vector anchor2 = null;
		if (superPacman.getMode() == SuperPacman.SOLO_MODE)
			anchor2 = canvas.getTransform().getOrigin().sub(new Vector(width / 5, height / 15));
		else
			anchor2 = canvas.getTransform().getOrigin().sub(new Vector(width / 5, height / 8));
		ImageGraphics arrow = new ImageGraphics(ResourcePath.getSprite("superpacman/arrow"), 0.03f, 0.03f, null,
				anchor2);

		arrow.draw(canvas);
	}

}
