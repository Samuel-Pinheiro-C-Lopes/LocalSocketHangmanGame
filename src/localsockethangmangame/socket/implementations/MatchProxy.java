package localsockethangmangame.socket.implementations;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import localsockethangmangame.game.enums.MatchState;
import localsockethangmangame.game.interfaces.Match;
import localsockethangmangame.socket.implementations.dtos.ClientRequestDTO;
import localsockethangmangame.socket.interfaces.dtos.ClientRequest;
import localsockethangmangame.socket.interfaces.dtos.ServerResponse;

public class MatchProxy implements Match {
	private final String host;
	private final Integer port;
	private ObjectOutputStream toServer;
	private ObjectInputStream fromServer;
	private Socket socket;
	private Match match;
	
	public MatchProxy(
		String host, 
		Integer port
	) throws UnknownHostException, IOException, ClassNotFoundException {
		this.port = Integer.valueOf(port);
		this.host = host;
		this.restartMatch();
	}
	
	@Override
	public char[] getExposedPhrase() { return match.getExposedPhrase(); }
	
	@Override
	public char[] getHiddenPhrase() { return match.getHiddenPhrase(); }
	
	@Override
	public MatchState getMatchState() { return this.match.getMatchState(); }
	
	@Override
	public void hint(Character c) {
		try {
			if (!this.match.getMatchState().equals(MatchState.PLAYING))
				this.restartMatch();
				
			this.writeCharToServer(c);
			this.match = this.getMatchFromServer();
			
			if (!this.match.getMatchState().equals(MatchState.PLAYING)) 
				this.endMatch();
		} catch (Exception e) {
			this.match = null;
			e.printStackTrace();
		}
	}
	
	private Match getMatchFromServer() throws IOException, ClassNotFoundException {		
		final Object response = this.fromServer.readObject();
		
		if (response == null || !(response instanceof ServerResponse))
			throw new IllegalArgumentException("Response wasn't of type ServerResponse or was null.");
		
		return ((ServerResponse) response).match();
	}
	
	private void restartMatch() throws UnknownHostException, IOException, ClassNotFoundException {
		if (this.socket != null)
			this.socket.close();
		
		this.socket = new Socket(this.host, this.port);
		this.toServer = new ObjectOutputStream(this.socket.getOutputStream());
		this.fromServer = new ObjectInputStream(this.socket.getInputStream());
		toServer.flush();
		this.match = getMatchFromServer();
	}
	
	private void writeCharToServer(final Character c) throws IOException {
		 final ClientRequest request = new ClientRequestDTO(c);
		 
		 this.toServer.writeObject(request);
	}
	
	private void endMatch() throws IOException {
		this.writeCharToServer(null);
		this.socket.close();
	}
}



