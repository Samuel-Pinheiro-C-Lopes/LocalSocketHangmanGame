package game.interfaces;

import game.enums.MatchState;

public interface Match {
	char[] getExposedPhrase();
	char[] getHiddenPhrase();
	MatchState getMatchState();
	void hint(Character c);
}
