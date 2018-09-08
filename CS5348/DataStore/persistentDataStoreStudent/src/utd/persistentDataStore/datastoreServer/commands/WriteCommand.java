package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.StreamUtil;

public class WriteCommand extends ServerCommand{
	private static Logger logger = Logger.getLogger(WriteCommand.class);
	public void run() throws IOException
	{
		try {
			String response;
			// Read message
			String name=StreamUtil.readLine(inputStream);
			Integer length=Integer.parseInt(StreamUtil.readLine(inputStream));
			byte[] data = StreamUtil.readData(length,inputStream);
			if(data.length!=length) {
				StreamUtil.writeLine("Writing FAIL: the socket closes before N bytes is read from the client", outputStream);
				return;
			}				
			FileUtil.writeData(name, data);
			
			// Write response
			StreamUtil.writeLine("ok\n", outputStream);
		}catch(Exception ex) {
			logger.error("Exception while processing request. " + ex.getMessage());
			ex.printStackTrace();
			StreamUtil.closeSocket(inputStream);
		}
	}
}
