package game.interfaces;

import game.enums.MatchState;

public interface Match {
	char[] getCurrentState();
	MatchState getMatchState();
	void hint(Character c);
}
