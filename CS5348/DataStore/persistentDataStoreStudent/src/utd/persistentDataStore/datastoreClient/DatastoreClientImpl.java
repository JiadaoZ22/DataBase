package utd.persistentDataStore.datastoreClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.StreamUtil;

public class DatastoreClientImpl implements DatastoreClient
{
	private static Logger logger = Logger.getLogger(DatastoreClientImpl.class);

	private InetAddress address;
	private int port;

	public DatastoreClientImpl(InetAddress address, int port)
	{
		this.address = address;
		this.port = port;
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#write(java.lang.String, byte[])
	 */
	@Override
    public void write(String name, byte[] data) throws ClientException, ConnectionException
	{
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Executing WRITE Operation");
			StreamUtil.writeLine("write", outputStream);
			StreamUtil.writeLine(name, outputStream);
			StreamUtil.writeLine(Integer.toString(data.length), outputStream);
			StreamUtil.writeData(data, outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			logger.debug("Response " + result);
			logger.debug("Closing Socket");
			socket.close();
			return;
		}
		catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
		//socket closes before N bytes is read from client
		catch(Exception ex) {
	        	throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#read(java.lang.String)
	 */
	@Override
    public byte[] read(String name) throws ClientException, ConnectionException
	{
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Executing READ Operation");
			StreamUtil.writeLine("read", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			String size = StreamUtil.readLine(inputStream);
			byte[] content = StreamUtil.readData(Integer.parseInt(size),inputStream);
			logger.debug("Response " + result);
			logger.debug("Response " + size);
			logger.debug("Response " + content);
			logger.debug("Closing Socket");
			socket.close();
			return content;
		}
		catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
		catch(Exception ex) {
	        	throw new ClientException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#delete(java.lang.String)
	 */
	@Override
    public void delete(String name) throws ClientException, ConnectionException
	{
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Executing DELETE Operation");
			StreamUtil.writeLine("delete", outputStream);
			StreamUtil.writeLine(name, outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			if(result.equalsIgnoreCase("ok")) {
				logger.debug( "Response " + result);
				logger.debug("Closing Socket");
				socket.close();
			}else {
				socket.close();
				throw new ClientException("Deleting FAIL: the system doesn't has such file.");
			}
		}catch (IOException ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
	}

	/* (non-Javadoc)
	 * @see utd.persistentDataStore.datastoreClient.DatastoreClient#directory()
	 */
	@Override
    public List<String> directory() throws ClientException, ConnectionException
	{
		logger.debug("Executing Directory Operation");
		try {
			logger.debug("Opening Socket");
			Socket socket = new Socket();
			SocketAddress saddr = new InetSocketAddress(address, port);
			socket.connect(saddr);
			InputStream inputStream = socket.getInputStream();
			OutputStream outputStream = socket.getOutputStream();
			
			logger.debug("Executing LIST Operation");
			StreamUtil.writeLine("Directory", outputStream);
			
			logger.debug("Reading Response");
			String result = StreamUtil.readLine(inputStream);
			String nameNumber = StreamUtil.readLine(inputStream);
			List<String> directory=new ArrayList<String>(0);
			logger.debug("Response " + result);
			logger.debug("Response " + nameNumber);
			if(!nameNumber.equalsIgnoreCase("")) {
				int i=Integer.parseUnsignedInt(nameNumber);
				while(i!=0) {
					String message=StreamUtil.readLine(inputStream);
					logger.debug("Response " + message);
					directory.add(message);
					i--;
				}
				logger.debug("Closing Socket");
				socket.close();
			}else {
				socket.close();
				//throw new ClientException("The system is empty,please add some files first.");
			}
			return directory;
		}

		catch (Exception ex) {
			throw new ConnectionException(ex.getMessage(), ex);
		}
	}

}

