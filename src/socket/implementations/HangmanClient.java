package socket.implementations;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import game.enums.MatchState;
import game.interfaces.Match;
import proxy.implementations.MatchProxy;

public class HangmanClient {
	private static void init(final Match match) throws IOException {
		final BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
		String line;
		
		while(match.getMatchState().equals(MatchState.PLAYING)) {
			System.out.println("The exposed phrase is: " + match.getCurrentState().toString() + ".");
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
		
		System.out.println("The phrase was: " + match.getCurrentState().toString() + ".");
	}
	
	public static void main(String argv[]) throws Exception {
		init(new MatchProxy("localhost", 7070));
	}
}
