package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;

import org.apache.log4j.Logger;

import utd.persistentDataStore.datastoreClient.ClientException;
import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.StreamUtil;

public class DeleteCommand extends ServerCommand{
	private static Logger logger = Logger.getLogger(DeleteCommand.class);
	public void run() throws IOException,ClientException
	{
		try {
			// Read message
			String name=StreamUtil.readLine(inputStream);
			if(FileUtil.directory().contains(name)) {
			FileUtil.deleteData(name);
			
			// Write response
			StreamUtil.writeLine("ok\n", outputStream);
			}else {
				StreamUtil.writeLine("Deleting FAIL: the system doesn't has such file.",outputStream);
			}
			
		
		}catch(Exception ex) {
			logger.error("Exception while processing request. " + ex.getMessage());
			ex.printStackTrace();
			StreamUtil.closeSocket(inputStream);
		}
	}
}

//Failure
//try {
//	// Read message
//	String name=StreamUtil.readLine(inputStream);
//	if(FileUtil.deleteData(name)) {
////		StreamUtil.writeLine("Deleting FAIL: the system doesn't has such file", outputStream);
////		return;
////		throw new ClientException("file with such name doesn't exist");
////	}else {
//	FileUtil.deleteData(name);
//	
//	// Write response
//	StreamUtil.writeLine("ok\n", outputStream);
//	}