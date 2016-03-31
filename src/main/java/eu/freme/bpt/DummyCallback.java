package eu.freme.bpt;

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
public class DummyCallback implements Callback {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());


	@Override
	public void onTaskComplete(File inputFile, File outputFile) {
		logger.debug("onTaskComplete: {}, {}", inputFile, outputFile);
	}

	@Override
	public void onTaskFails(File inputFile, File outputFile, final String reason) {
		logger.debug("onTaskFails: {}, {}. reason: {}", inputFile, outputFile, reason);
	}

	@Override
	public void onBatchComplete() {
		logger.debug("onBatchComplete");
	}
}
