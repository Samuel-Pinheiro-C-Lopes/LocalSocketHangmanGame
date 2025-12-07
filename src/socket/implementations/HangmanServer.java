package socket.implementations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import game.enums.MatchState;
import game.implementations.SimpleMatch;
import game.interfaces.Match;
import socket.implementations.dtos.ServerResponseDTO;
import socket.interfaces.dtos.ClientRequest;

public class HangmanServer {
	public static void main(String[] args) throws ClassNotFoundException {
		try (final ServerSocket serverSocket = new ServerSocket(7070)) {
			ObjectInputStream fromClient;
			ObjectOutputStream toClient;
	
			while(Boolean.TRUE) {
				try (Socket socket = serverSocket.accept()) {
					final Match match = new SimpleMatch("testando...");
					fromClient = new ObjectInputStream(socket.getInputStream());
					toClient = new ObjectOutputStream(socket.getOutputStream());
					toClient.flush();
					
					toClient.writeObject(new ServerResponseDTO(match));

					Object request = fromClient.readObject();
					
					while (
						isSocketValid(socket).equals(Boolean.TRUE) && 
						isRequestValid(request).equals(Boolean.TRUE) &&
						isMatchValid(match).equals(Boolean.TRUE)
					) {
						final ClientRequest validRequest = (ClientRequest) request; 
						
						if (validRequest.c() == null)
							break;
						
						match.hint(validRequest.c());
						toClient.reset();
						toClient.writeObject(new ServerResponseDTO(match));
						request = fromClient.readObject();
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
	
	private static Boolean isRequestValid(final Object obj) {
		return Boolean.valueOf(
			obj != null && 
			(obj instanceof ClientRequest) == Boolean.TRUE
		);
	}
	
	private static Boolean isMatchValid(final Match match) {
		return Boolean.valueOf(
				match.getMatchState().equals(MatchState.PLAYING)
		);
	}
}
