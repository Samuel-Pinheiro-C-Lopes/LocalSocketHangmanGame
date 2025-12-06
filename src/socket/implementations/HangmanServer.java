package socket.implementations;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import game.enums.MatchState;
import game.implementations.SimpleMatch;
import game.interfaces.Match;

public class HangmanServer {
	public static void main(String[] args) {
		try (final ServerSocket serverSocket = new ServerSocket(7070)) {
			BufferedReader fromClient;
			ObjectOutputStream toClient;
	
			while(Boolean.TRUE) {
				try (Socket socket = serverSocket.accept()) {
					fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					toClient = new ObjectOutputStream(socket.getOutputStream());
					
					Match match = new SimpleMatch("testando...");
					toClient.writeObject(match);

					String lineRead = fromClient.readLine();
					while (
						isSocketValid(socket).equals(Boolean.TRUE) && 
						isLineReadValid(lineRead).equals(Boolean.TRUE) &&
						isMatchValid(match).equals(Boolean.TRUE)
					) {
						match.hint(lineRead.charAt(0));
						toClient.writeObject(match);
						lineRead = fromClient.readLine();
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	private static Boolean isSocketValid(final Socket socket) {
		return Boolean.valueOf(
			socket.isConnected() == Boolean.TRUE && 
			socket.isClosed() == Boolean.FALSE &&
			socket.isBound() == Boolean.TRUE
		);
	}
	
	private static Boolean isLineReadValid(final String line) {
		return Boolean.valueOf(
			line != null && 
			line.equals("") != Boolean.TRUE
		);
	}
	
	private static Boolean isMatchValid(final Match match) {
		return Boolean.valueOf(
				match.getMatchState().equals(MatchState.PLAYING)
		);
	}
}
