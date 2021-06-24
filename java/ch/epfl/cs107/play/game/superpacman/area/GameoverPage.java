/*
 * Author :  	Ayman
 * Date:        Dec 11, 2020
 */

package ch.epfl.cs107.play.game.superpacman.area;

import ch.epfl.cs107.play.game.actor.Graphics;
import ch.epfl.cs107.play.game.actor.ImageGraphics;
import ch.epfl.cs107.play.game.areagame.io.ResourcePath;
import ch.epfl.cs107.play.game.superpacman.SuperPacman;
import ch.epfl.cs107.play.math.Vector;
import ch.epfl.cs107.play.window.Canvas;

public class GameoverPage implements Graphics {

	private SuperPacman superPacman;

	public GameoverPage(SuperPacman superPacman) {
		this.superPacman = superPacman;
	}

	@Override
	public void draw(Canvas canvas) {
		float width = canvas.getScaledWidth();
		float height = canvas.getScaledHeight();
		Vector anchor = canvas.getTransform().getOrigin().sub(new Vector(width/2, height/2));
		ImageGraphics image = new ImageGraphics(ResourcePath.getSprite("superpacman/gameover"), width, height, null,
				anchor);
		image.draw(canvas);
	}

}
