package eu.freme.bpt.io;

import eu.freme.bpt.common.Format;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

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
 */
public abstract class AbstractIOIterator implements IOIterator {
	protected final Logger logger = LoggerFactory.getLogger(this.getClass());

	protected AbstractIOIterator() {
		logger.debug("Created {}", this.getClass());
	}

	protected File getOutputFile(final File outputDir, final File inputFile, final Format outFormat) {
		String outFileName = inputFile.getName();
		int pointIndex = outFileName.lastIndexOf('.');
		if (pointIndex > 0) {
			outFileName = outFileName.substring(0, pointIndex);
		}
		outFileName += "." + outFormat.getFileExtension();
		return new File(outputDir, outFileName);
	}
}
