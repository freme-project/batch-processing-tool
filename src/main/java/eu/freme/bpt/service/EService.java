package eu.freme.bpt.service;

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
 * An enumeration of the services.
 */
public enum EService {
	E_ENTITY("e-entity", "http://api.freme-project.eu/current/e-entity/freme-ner/documents"),
	E_LINK("e-link", "http://api.freme-project.eu/current/e-link/documents"),
	E_PUBLISHING("e-publishing", "http://api.freme-project.eu/current/e-publishing/html"),
	E_TERMINOLOGY("e-terminology", "http://api.freme-project.eu/current/e-terminology/tilde"),
	E_TRANSLATION("e-translation", "http://api.freme-project.eu/current/e-translation/tilde"),
	PIPELINING("pipelining", "http://api.freme-project.eu/current/pipelining/chain")
	;

	private final String name;
	private final String defaultUrl;

	EService(String name, String defaultUrl) {
		this.name = name;
		this.defaultUrl = defaultUrl;
	}

	public String getName() {
		return name;
	}

	public String getDefaultUrl() {
		return defaultUrl;
	}


}
