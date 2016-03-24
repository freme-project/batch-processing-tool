package eu.freme.bpt;

import eu.freme.bpt.config.BPTProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * <p>Object to use as an interface to the tool.
 * <p>
 * <p>Copyright 2016 Data Science Lab (Ghent University - iMinds)</p>
 *
 * @author Gerald Haesendonck
 */
public class BPT {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	private BPTProperties properties = new BPTProperties();
	private File input = null;

	/**
	 * Load properties from a given file.
	 * @param propertiesFile	The file to read the properties from.
	 * @return					A BPT object with the properties set.
	 * @throws IOException		The properties file is not found, or something goes wrong reading the file.
	 */
	public BPT loadProperties(final String propertiesFile) throws IOException {
		properties.load(propertiesFile);
		logger.debug(properties.toString());
		return this;
	}

	// TODO: asynchonous mode
	//public BPT registerCallback() {	}

	/**
	 * Set a file or directory as input.
	 * @param fileOrDirectory	The path to a file or directory.
	 * @return					A BPT object with the input path set.
	 * @throws FileNotFoundException	The input file or directory is not found.
	 */
	public BPT setInput(final String fileOrDirectory) throws FileNotFoundException {
		input = new File(fileOrDirectory);
		if (!input.exists()) {
			throw new FileNotFoundException("File or directory " + fileOrDirectory + " not found!");
		}
		logger.debug("Input file or directory {} set!", input);
		return this;
	}

}
