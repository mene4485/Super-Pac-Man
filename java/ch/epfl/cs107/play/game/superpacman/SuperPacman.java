package ch.epfl.cs107.play.game.superpacman;

import ch.epfl.cs107.play.game.areagame.actor.Orientation;
import ch.epfl.cs107.play.game.rpg.RPG;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;
import ch.epfl.cs107.play.game.superpacman.area.GameoverPage;
import ch.epfl.cs107.play.game.superpacman.area.Level0;
import ch.epfl.cs107.play.game.superpacman.area.Level1;
import ch.epfl.cs107.play.game.superpacman.area.Level2;
import ch.epfl.cs107.play.game.superpacman.area.PausePage;
import ch.epfl.cs107.play.game.superpacman.area.SuperPacmanArea;
import ch.epfl.cs107.play.game.superpacman.area.WelcomePage;
import ch.epfl.cs107.play.game.superpacman.area.WinPage;
import ch.epfl.cs107.play.io.FileSystem;
import ch.epfl.cs107.play.window.Keyboard;
import ch.epfl.cs107.play.window.Window;

public class SuperPacman extends RPG {

	private final int WELCOME_STATE = 0;
	private final int PLAY_STATE = 1;
	private final int PAUSE_STATE = 2;
	private final int GAMEOVER_STATE = 3;
	private final int WIN_STATE = 4;

	public static final int SOLO_MODE = 1;
	public static final int MULTI_MODE = 2;

	private int state;
	private int mode;

	private WelcomePage welcomePage;
	private PausePage pausePage;
	private GameoverPage gameoverPage;
	private SuperPacmanPlayer player;
	private WinPage winPage;

	public SuperPacman() {
		state = WELCOME_STATE;
		mode = SOLO_MODE;
	}

	@Override
	public String getTitle() {
		return "Super Pac-Man";
	}

	private void createAreas() {
		addArea(new Level0(this));
		addArea(new Level1(this));
		addArea(new Level2(this));
	}

	private void resetPlayer() {
		player = new SuperPacmanPlayer(getCurrentArea(), Orientation.RIGHT, Level0.PLAYER_SPAWN_POSITION);
		initPlayer(player);
	}

	@Override
	public boolean begin(Window window, FileSystem fileSystem) {
		if (super.begin(window, fileSystem)) {
			createAreas();
			setCurrentArea(Level0.KEY, true);
			resetPlayer();
			pausePage = new PausePage(this);
			gameoverPage = new GameoverPage(this);
			welcomePage = new WelcomePage(this);
			winPage = new WinPage(this);
			return true;
		}
		return false;
	}

	@Override
	public void update(float deltaTime) {
		processKeyboard();
		if (state == WELCOME_STATE) {
			welcomePage.draw(getWindow());
		} else if (state == PAUSE_STATE) {
			pausePage.draw(getWindow());
		} else if (((SuperPacmanPlayer) getPlayer()).getStatus().isDead()) {
			gameoverPage.draw(getWindow());
			state = GAMEOVER_STATE;
		} else if (state == WIN_STATE) {
			winPage.draw(getWindow());
		} 
		else {
			super.update(deltaTime);
		}
	}

	public void processKeyboard() {
		Keyboard keyboard = getCurrentArea().getKeyboard();
		if (state == WELCOME_STATE) {
			if (keyboard.get(Keyboard.DOWN).isDown()) {
				mode = MULTI_MODE;
			}
			if (keyboard.get(Keyboard.UP).isDown()) {
				mode = SOLO_MODE;
			}
			if (keyboard.get(Keyboard.ENTER).isDown()) {
				state = PLAY_STATE;
			}
		} else if (state == PLAY_STATE) {
			if (keyboard.get(Keyboard.P).isDown()) {
				state = PAUSE_STATE;
			}
			if (((SuperPacmanArea) (player.getOwnerArea())).isOn() && player.getOwnerArea() instanceof Level2) {
				state = WIN_STATE;
			}
		} else if (state == PAUSE_STATE) {
			if (keyboard.get(Keyboard.C).isDown()) {
				state = PLAY_STATE;
			}
		} else if (state == GAMEOVER_STATE) {
			if (keyboard.get(Keyboard.S).isDown()) {
				state = PLAY_STATE;
				setCurrentArea(Level0.KEY, true);
				SuperPacmanArea.setCAMERA_SCALE_FACTOR(20.f);
				resetPlayer();
			}
		} else if (state == WIN_STATE) {
			if (keyboard.get(Keyboard.R).isDown()) {
				state = PLAY_STATE;
				setCurrentArea(Level0.KEY, true);
				SuperPacmanArea.setCAMERA_SCALE_FACTOR(20.f);
				resetPlayer();
				}
		}

	}

	public SuperPacmanPlayer getPlayer() {
		return (SuperPacmanPlayer) super.getPlayer();
	}

	public int getMode() {
		return mode;
	}

}
