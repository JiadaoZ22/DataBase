package utd.persistentDataStore.simpleSocket.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import org.apache.log4j.Logger;

import utd.persistentDataStore.datastoreClient.ConnectionException;
import utd.persistentDataStore.utils.StreamUtil;

public class ExampleClient
{
	private static Logger logger = Logger.getLogger(ExampleClient.class);
	
	private InetAddress address;
	private int port;

	public ExampleClient(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/**
	 * Sends the given string to the server which will echo it back
	 */
	public String echo(String message) throws ConnectionException
	{
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");
			StreamUtil.writeLine("echo", outputStream);
			StreamUtil.writeLine(message, outputStream);
			StreamUtil.writeLine("2222"+message, outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			String result2 = StreamUtil.readLine(inputStream);
			String result3 = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result+result2+result3);
			
			return result+result2;
		}
		catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
	}
		
	/**
	 * Sends the given string to the server which will echo it back
	 */
	public String reverse(String message) throws ConnectionException
	{
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Writing Message");
			StreamUtil.writeLine("reverse", outputStream);
			StreamUtil.writeLine(message, outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
			
			return result;
		}
		catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
	}

}
