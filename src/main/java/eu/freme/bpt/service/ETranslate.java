package eu.freme.bpt.service;

import eu.freme.bpt.common.Configuration;

import java.io.InputStream;
import java.io.OutputStream;

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
 * Represents the e-translate service
 *
 */
public class ETranslate extends AbstractService {

	public ETranslate(InputStream inputStream, OutputStream outputStream, Configuration configuration) {
		super("e-translate", inputStream, outputStream, configuration);
		parameters.put("source-lang", configuration.getSourceLang());
		parameters.put("target-lang", configuration.getTargetLang());
	}
}
