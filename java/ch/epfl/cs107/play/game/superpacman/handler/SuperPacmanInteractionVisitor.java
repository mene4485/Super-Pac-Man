package ch.epfl.cs107.play.game.superpacman.handler;

import ch.epfl.cs107.play.game.rpg.handler.RPGInteractionVisitor;
import ch.epfl.cs107.play.game.superpacman.actor.Blinky;
import ch.epfl.cs107.play.game.superpacman.actor.Bonus;
import ch.epfl.cs107.play.game.superpacman.actor.Cherry;
import ch.epfl.cs107.play.game.superpacman.actor.Diamond;
import ch.epfl.cs107.play.game.superpacman.actor.Fantome;
import ch.epfl.cs107.play.game.superpacman.actor.Key;
import ch.epfl.cs107.play.game.superpacman.actor.SuperPacmanPlayer;

public interface SuperPacmanInteractionVisitor extends RPGInteractionVisitor{
	
	default void interactWith(SuperPacmanPlayer player){
        // by default the interaction is empty
    }
	
	default void interactWith(Key key){
    }
	
	default void interactWith(Diamond diamond){
    }
	
	default void interactWith(Cherry cherry){
    }
	
	default void interactWith(Bonus bonus){
    }
	
	default void interactWith(Fantome fantome){
    }
	
}
