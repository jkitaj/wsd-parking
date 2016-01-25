package pl.pw.wsd.wsdparking;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class SaveStats {
	public static void saveToFile(String line) {
		Path file = Paths.get(Constants.NAME_OF_FILE_WITH_STATS);
		try {
			Files.write(file, Arrays.asList(line), Charset.forName("UTF-8"), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearStat(){
		Path file = Paths.get(Constants.NAME_OF_FILE_WITH_STATS);
		try {
			Files.deleteIfExists(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
