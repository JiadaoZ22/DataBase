package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;


import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.StreamUtil;

public class ReadCommand extends ServerCommand {
	private static Logger logger = Logger.getLogger(ReadCommand.class);
	public void run() throws IOException
	{
		try {
			// Read message
			String name=StreamUtil.readLine(inputStream);
			if(!FileUtil.directory().contains(name)) {
				StreamUtil.writeLine("Reading FAIL: the system doesn't contain such file", outputStream);
				return;
			}
			byte[] data=FileUtil.readData(name);
			
			// Write response
			StreamUtil.writeLine("ok\n", outputStream);
			StreamUtil.writeLine(Integer.toString(data.length), outputStream);
			StreamUtil.writeData(data, outputStream);
			
		}catch(Exception ex) {
			logger.error("Exception while processing request. " + ex.getMessage());
			ex.printStackTrace();
			StreamUtil.closeSocket(inputStream);
		}
	}
}


