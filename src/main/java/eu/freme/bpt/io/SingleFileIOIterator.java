package eu.freme.bpt.io;

import eu.freme.bpt.common.Format;

import java.io.*;

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
 * Does not really iterate, just returns the InputStream created from the given file. Always allows one iteration.
 *
 */
public class SingleFileIOIterator extends AbstractIOIterator {
	private boolean hasNext = true;
	private final InputStream in;
	private final OutputStream out;

	/**
	 * Creates a SingleFileIOIterator from the given file.
	 * @param inputFile    The file to process.
	 * @param outputDir	   The directory to write the output. The output file is a file in that directory with the same name as the input file.
	 * @throws FileNotFoundException	The given file is not found.
	 */
	public SingleFileIOIterator(final File inputFile, final File outputDir, final Format outFormat) throws FileNotFoundException {
		in = new BufferedInputStream(new FileInputStream(inputFile));
		File outputFile = getOutputFile(outputDir, inputFile, outFormat);
		out = new BufferedOutputStream(new FileOutputStream(outputFile));
	}

	/**
	 * Creates a SingleFileIOIterator from the given file. Output is written to standard out
	 * @param inputFile    The file to process.
	 * @throws FileNotFoundException	The given file is not found.
	 */
	public SingleFileIOIterator(final File inputFile) throws FileNotFoundException {
		in = new BufferedInputStream(new FileInputStream(inputFile));
		out = System.out;
	}

	@Override
	public boolean hasNext() {
		return hasNext;
	}

	@Override
	public IO next() {
		hasNext = false;
		return new IO(in, out);
	}
}
