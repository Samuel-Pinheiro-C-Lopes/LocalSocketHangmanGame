package localsockethangmangame.socket.interfaces.dtos;

import java.io.Serializable;

import localsockethangmangame.game.interfaces.Match;

public interface ServerResponse extends Serializable {
	Match match();
}
