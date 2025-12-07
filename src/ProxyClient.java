
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;

public class ProxyClient {
	public static void main(String argv[]) throws Exception {
		BufferedReader fromUser = new BufferedReader(new InputStreamReader(System.in));
	
	    try (Socket socketCliente = new Socket("localhost", 7070)) {
	        final DataOutputStream toServer = new DataOutputStream(socketCliente.getOutputStream());
	        final BufferedReader fromServer = new BufferedReader(new InputStreamReader(socketCliente.getInputStream()));
	        System.out.println("client pre");
	        final String clientText = fromUser.readLine();
	        System.out.println("client mid " + clientText);
	        toServer.writeBytes(clientText + System.lineSeparator());
	        toServer.flush();
	        
	        final String serverText = fromServer.readLine();
	        System.out.println("client post " + serverText);
	        System.out.println("Do Servidor: " + serverText);
	    }
	}

}