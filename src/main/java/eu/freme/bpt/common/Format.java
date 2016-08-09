package eu.freme.bpt.common;

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
 * Some info in some mime-types
 *
 */
public enum Format {

	// formats officially supported by the FREME services
	text("txt", "text/plain"),
	turtle("ttl", "text/turtle"),
	n3("n3", "text/n3"),
	n_triples("nt", "application/n-triples"),
	json_ld("json", "application/ld+json"),
	rdf_xml("xml", "application/rdf+xml"),
	xliffXml("xml", "application/x-xliff+xml"),
	html("html", "text/html"),
	odt("odt", "application/x-openoffice"),
	xml("xml", "text/xml"),

	// formats needed by e-Publishing
	zip("zip", "application/zip"),
	epub3("epub3", "application/epub+zip")
	;

	private final String fileExtension;
	private final String mimeType;		// TODO: to mime type object of the HTTP library to be used ?


	Format(String fileExtension, String mimeType) {
		this.fileExtension = fileExtension;
		this.mimeType = mimeType;
	}

	public String getFileExtension() {
		return fileExtension;
	}

	public String getMimeType() {
		return mimeType;
	}
}
