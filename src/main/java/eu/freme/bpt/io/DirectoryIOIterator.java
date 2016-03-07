package eu.freme.bpt.io;

import eu.freme.bpt.common.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

/**
 * Copyright (C) 2016 Agro-Know, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
 * Institut für Angewandte Informatik e. V. an der Universität Leipzig,
 * Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Iterates over the files in the given directory represented as an InputStream per file.
 * Does NOT process directories recursively.
 *
 */
public class DirectoryIOIterator extends AbstractIOIterator {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Iterator<File> fileIterator;
	private final File outputDir;
	private final Format outFormat;

	/**
	 * Creates a DirectoryIOIterator for a given directory.
	 * @param inputDirectory    The directory to process. Each file in the directory is iterated and an InputStream for
	 *                          these files are returned upon calling next().
	 */
	public DirectoryIOIterator(final File inputDirectory, final File outputDirectory, final Format outFormat) {
		File[] inputFiles = inputDirectory.listFiles();
		List<File> files = inputFiles == null ? Collections.emptyList() : Arrays.asList(inputFiles);
		fileIterator = files.iterator();
		outputDir = outputDirectory;
		this.outFormat = outFormat;
	}

	@Override
	public boolean hasNext() {
		return fileIterator.hasNext();
	}

	@Override
	public IO next() {
		File inFile = fileIterator.next();
		File outFile = getOutputFile(outputDir, inFile, outFormat);

		try {
			InputStream inputStream = new BufferedInputStream(new FileInputStream(inFile));
			OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(outFile));
			return new IO(inputStream, outputStream);
		} catch (FileNotFoundException e) {
			logger.error("Could not create input stream from file {}.", inFile, e);
			throw new NoSuchElementException(e.getMessage());
		}
	}
}
