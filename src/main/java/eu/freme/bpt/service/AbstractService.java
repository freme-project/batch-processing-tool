package eu.freme.bpt.service;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import eu.freme.bpt.common.Configuration;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
 * Some common logic for e-services.
 *
 */
public abstract class AbstractService implements Service {
	private static Logger logger = LoggerFactory.getLogger(AbstractService.class);

	private final InputStream inputStream;
	private final OutputStream outputStream;
	private final String endpoint;

	private final Map<String, String> headers;
	protected final Map<String, Object> parameters;

	public AbstractService(final String serviceName, final InputStream inputStream, final OutputStream outputStream, final Configuration configuration) {
		this.endpoint = configuration.getEndpoint(serviceName);
		this.inputStream = inputStream;
		this.outputStream = outputStream;
		headers = new HashMap<>();
		headers.put("accept", configuration.getOutFormat().getMimeType());
		headers.put("content-type", configuration.getInFormat().getMimeType());
		parameters = new HashMap<>(3);
	}

	@Override
	public Boolean call() {
		boolean success = false;
		Unirest.setTimeouts(30000, 300000);
		try {
			byte[] input = IOUtils.toByteArray(inputStream);
			HttpResponse<InputStream> response = Unirest.post(endpoint).headers(headers).queryString(parameters).body(input).asBinary();
			if (response.getStatus() == 200) {
				logger.debug("Request alright.");
				InputStream responseInput = response.getBody();
				try {
					IOUtils.copy(responseInput, outputStream);
					success = true;
				} catch (IOException e) {
					logger.error("Error while writing response.", e);
				} finally {
					try {
						responseInput.close();
					} catch (IOException e) {
						// not important :)
					}
				}
			} else {
				String body = IOUtils.toString(response.getBody());
				logger.error("Error response from service {}: Status {}: {} - {}", endpoint, response.getStatus(), response.getStatusText(), body);
			}
		} catch (Exception e) {
			logger.error("Request to {} failed." + endpoint, e);
		} finally {
			try {
				inputStream.close();
				outputStream.close();
			} catch (IOException e) {
				// not important
			}
		}
		return success;
	}
}
