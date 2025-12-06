import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class ProxyServer {

	public static void main(String[] args) {
		try (final ServerSocket serverSocket = new ServerSocket(7070)) {
			BufferedReader fromClient;
			DataOutputStream toClient;
	
			String clientText;
			String clientTextUpperCase;
	
			while(Boolean.TRUE) {
				try (Socket socket = serverSocket.accept()) {
					fromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					toClient = new DataOutputStream(socket.getOutputStream());
	
					clientText = fromClient.readLine();
					clientTextUpperCase = clientText.toUpperCase() + "\\n";
	
					toClient.writeBytes(clientTextUpperCase);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
}