package localsockethangmangame.game.implementations;

import java.io.Serializable;

import localsockethangmangame.game.enums.MatchState;
import localsockethangmangame.game.interfaces.Match;

public class SimpleMatch implements Match, Serializable {
	private static final long serialVersionUID = 1L;
	private final char[] hiddenPhrase;
	private final char[] exposedPhrase;
	private MatchState matchState;
	private int maxCharMatches;
	private int charMatches;
	private int lives;
	
	public SimpleMatch(String hiddenPhrase) {
		super();
		this.matchState = MatchState.PLAYING;
		this.hiddenPhrase = hiddenPhrase.toCharArray();
		this.exposedPhrase = new char[this.hiddenPhrase.length];
		this.prepareExposedPhrase(this.hiddenPhrase, this.exposedPhrase);
		this.charMatches = 0;
		this.lives = 3;
		this.maxCharMatches = hiddenPhrase.replace(" ", "").length();
	}

	@Override
	public char[] getExposedPhrase() { return this.exposedPhrase; }
	@Override
	public char[] getHiddenPhrase() { return this.hiddenPhrase; }
	@Override
	public MatchState getMatchState() { return this.matchState; }
	@Override
	public void hint(Character c) {
		if (!this.matchState.equals(MatchState.PLAYING))
			return;
		
		this.updateExposedPhraseBasedOnHint(c, hiddenPhrase, exposedPhrase);
		
		if (this.lives < 1)
			this.matchState = MatchState.LOST;
		else if (maxCharMatches == charMatches)
			this.matchState = MatchState.WON;
	}
	private void prepareExposedPhrase(char[] hiddenPhrase, char[] exposedPhrase) {
		char c;
		
		for (int i = 0; i < hiddenPhrase.length; i++) {
			c = hiddenPhrase[i];
			
			if (c == ' ') exposedPhrase[i] = ' ';
			else exposedPhrase[i] = '_';
		}
	}
	private void updateExposedPhraseBasedOnHint(
		char hint, 
		char[] hiddenPhrase, 
		char[] exposedPhrase
	) {
		int currentCharMatches = this.charMatches;
		char c;
		
		for (int i = 0; i < hiddenPhrase.length; i++) {
			c = hiddenPhrase[i];
			
			if (c == ' ' || hint != c)
				continue;
			
			this.exposedPhrase[i] = c;
			this.charMatches += 1;
		}
		
		if (currentCharMatches == this.charMatches)
			this.lives -= 1;
	}
}


