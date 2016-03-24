package eu.freme.bpt.config;

import eu.freme.bpt.util.FailurePolicy;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Copyright (C) 2016 Agroknow, Deutsches Forschungszentrum für Künstliche
 * Intelligenz, iMinds, Institut für Angewandte Informatik e. V. an der
 * Universität Leipzig, Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL
 * (http://freme-project.eu)
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * <p>
 * Holds the properties of the tool.
 */
public class BPTProperties {
	private Properties properties = new Properties();

	public void load(final String propertiesFile) throws IOException {
		try (InputStream propertiesStream = new FileInputStream(propertiesFile)) {
			properties.load(propertiesStream);
		}
	}

	public int getThreads() {
		return Integer.parseInt(properties.getProperty("threads"), 1);
	}

	public FailurePolicy.Strategy getFailureStrategy() {
		String failureStr = properties.getProperty("failure", "best-effort");
		return FailurePolicy.Strategy.valueOf(failureStr);
	}

	public String getPrefix() {
		return properties.getProperty("prefix", "http://freme-project.eu/");
	}

	public String getEEntity() {
		return properties.getProperty("e-entity", "http://api.freme-project.eu/current/e-entity/freme-ner/documents");
	}

	public String getELink() {
		return properties.getProperty("e-link", "http://api.freme-project.eu/current/e-link/documents");
	}

	public String getEPublishing() {
		return properties.getProperty("e-publishing", "http://api.freme-project.eu/current/e-publishing/html");
	}

	public String getETerminology() {
		return properties.getProperty("e-terminology", "http://api.freme-project.eu/current/e-terminology/tilde");
	}

	public String getETranslation() {
		return properties.getProperty("e-translation", "http://api.freme-project.eu/current/e-translation/tilde");
	}

	public String getPipelining() {
		return properties.getProperty("pipelining", "http://api.freme-project.eu/current/pipelining/chain");
	}
}
