package proxy.implementations;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import game.enums.MatchState;
import game.interfaces.Match;

public class MatchProxy implements Match {
	private final String host;
	private final Integer port;
	private Socket socket;
	private Match match;
	
	public MatchProxy(String host, Integer port) throws UnknownHostException, IOException, ClassNotFoundException {
		this.host = host;
		this.port = Integer.valueOf(port);
		this.restartMatch();
	}
	
	@Override
	public char[] getCurrentState() {
		if (match == null)
			return null;
		
		return match.getCurrentState();
	}
	
	@Override
	public MatchState getMatchState() {
		if (this.match == null)
			return null;
		
		return this.match.getMatchState();
	}

	@Override
	public void hint(Character c) {
		try {
			if (!this.match.getMatchState().equals(MatchState.PLAYING))
				this.restartMatch();
				
			this.writeCharToServer(c);
			this.match = this.getMatchFromServer();			
		} catch (Exception e) {
			this.match = null;
			e.printStackTrace();
		}
	}
	
	private void writeCharToServer(final Character c) throws IOException {
		 final DataOutputStream toServer = new DataOutputStream(this.socket.getOutputStream());
		 
		 toServer.writeChar(c);
		 toServer.flush();
	}
	
	private Match getMatchFromServer() throws IOException, ClassNotFoundException {
		ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
		
		final Object response = ois.readObject();
		
		if (response == null || !(response instanceof Match))
			throw new IllegalArgumentException("Response wasn't of type Match or was null.");
		
		return (Match) response;
	}
	
	private void restartMatch() throws UnknownHostException, IOException, ClassNotFoundException {
		if (this.socket != null)
			this.socket.close();
		
		this.socket = new Socket(this.host, this.port);
		this.match = getMatchFromServer();
	}
}
