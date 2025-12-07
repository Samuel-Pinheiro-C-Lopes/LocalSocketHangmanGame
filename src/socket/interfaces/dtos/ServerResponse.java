package socket.interfaces.dtos;

import java.io.Serializable;

import game.interfaces.Match;

public interface ServerResponse extends Serializable {
	Match match();
}
