package eu.freme.bpt.io;

import eu.freme.bpt.common.Configuration;
import eu.freme.bpt.common.Format;

import java.io.File;
import java.io.IOException;

/**
 * Copyright (C) 2016 Agroknow, Deutsches Forschungszentrum für Künstliche Intelligenz, iMinds,
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
public class IteratorFactory {

	public static IOIterator create(final Configuration configuration) throws IOException, IOCombinationNotPossibleException {
		Format outFormat = configuration.getOutFormat();
		File outputDir = configuration.getOutputDir();
		File inputFile = configuration.getInputFile();

		if (outputDir != null) {
			if (!outputDir.exists()) {
				if (!outputDir.mkdirs()) {
					throw new IOException("Could not create output directory " + outputDir);
				}
			} else {
				if (outputDir.isFile()) {
					throw new IOException("The given output is a file. Shoul be a directory: " + outputDir);
				}
			}
		}
		if (inputFile != null) {
			if (inputFile.isFile()) {
				if (outputDir != null) {
					return new SingleFileIOIterator(inputFile, outputDir, outFormat);
				} else {
					return new SingleFileIOIterator(inputFile);
				}
			} else {
				if (outputDir != null) {
					return new DirectoryIOIterator(inputFile, outputDir, outFormat);
				} else {
					throw new IOCombinationNotPossibleException("If the input is a directory, the output should be a directory as well." +
							" Check the -od option!");
				}
			}
		} else {
			if (outputDir != null) {
				return new StandardIOIterator(outputDir, outFormat);
			} else {
				return new StandardIOIterator();
			}
		}
	}

}
