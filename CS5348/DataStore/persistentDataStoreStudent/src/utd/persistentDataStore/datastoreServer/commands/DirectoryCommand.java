package utd.persistentDataStore.datastoreServer.commands;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import utd.persistentDataStore.utils.FileUtil;
import utd.persistentDataStore.utils.StreamUtil;

public class DirectoryCommand extends ServerCommand {
	private static Logger logger = Logger.getLogger(DirectoryCommand.class);
	public void run() throws IOException
	{
		try {
			// Read message
			List<String> directory=FileUtil.directory();

			// Write response
			StreamUtil.writeLine("ok\n", outputStream);
//			if(FileUtil.directory().isEmpty()) {
//				StreamUtil.writeLine("0", outputStream);
//				return;
//			}
			StreamUtil.writeLine(Integer.toString(directory.size()), outputStream);
//			Iterator<String> dr=directory.iterator();
//			while(dr.hasNext()) {
//				StreamUtil.writeLine(name, outputStream);
//			}
			for(String name:directory) {
				StreamUtil.writeLine(name, outputStream);
			}
			
		}catch(Exception ex) {
			logger.error("Exception while processing request. " + ex.getMessage());
			ex.printStackTrace();
			StreamUtil.closeSocket(inputStream);
		}
	}
}
