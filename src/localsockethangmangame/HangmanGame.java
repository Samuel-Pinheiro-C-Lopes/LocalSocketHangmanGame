package localsockethangmangame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import localsockethangmangame.game.enums.MatchState;
import localsockethangmangame.game.interfaces.Match;
import localsockethangmangame.socket.implementations.MatchProxy;


public class HangmanGame {
	private static void init(final Match match) throws IOException {
		final BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		while(match.getMatchState().equals(MatchState.PLAYING)) {
			System.out.println("The exposed phrase is: " + String.valueOf(match.getExposedPhrase()) + ".");
			System.out.print("Enter with a hint: ");
			line = fromUser.readLine();
			
			if (line.length() != 1)
				continue;
			
			match.hint(line.charAt(0));
		}
		
		if (match.getMatchState().equals(MatchState.LOST)) {
			System.out.println("You lost!");
		} else {
			System.out.println("You won!");
		}
		
		System.out.println("The phrase was: " + String.valueOf(match.getHiddenPhrase()) + ".");
	}
	
	public static void main(String argv[]) throws Exception {
		// init(new SimpleMatch("testando..."));
		init(new MatchProxy("localhost", 7070));
	}
}
