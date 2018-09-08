package utd.persistentDataStore.simpleSocket.client;

import java.net.InetAddress;
import java.net.UnknownHostException;

import utd.persistentDataStore.datastoreClient.ConnectionException;

public class ClientApplication
{
	static public final int port = 10023;

	private void runApp() throws UnknownHostException, ConnectionException
	{
		byte byteAddr[] = { 127, 0, 0, 1 };
		InetAddress address = InetAddress.getByAddress(byteAddr);
		ExampleClient client = new ExampleClient(address, port);
		
		String message = "Now is the time for all good men";
		System.out.println("Calling Echo with " + message );
		String received = client.echo(message);
		System.out.println("Received " + received);
		
		System.out.println("\nCalling Reverse with " + message );
		received = client.reverse(message);
		System.out.println("Received " + received);
		System.out.println("Finished");
	}

	public static void main(String args[])
	{
		try {
			ClientApplication ca = new ClientApplication();
			ca.runApp();
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
