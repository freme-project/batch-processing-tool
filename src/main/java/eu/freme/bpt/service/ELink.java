package eu.freme.bpt.service;

import eu.freme.bpt.common.Configuration;

import java.io.InputStream;
import java.io.OutputStream;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Copyright (C) 2016 Agroknow, Deutsches Forschungszentrum für Künstliche
 * Intelligenz, iMinds, Institut für Angewandte Informatik e. V. an der
 * Universität Leipzig, Istituto Superiore Mario Boella, Tilde, Vistatec, WRIPL
 * (http://freme-project.eu)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 *
 * Represents the e-translate service
 *
 */
public class ELink extends AbstractService {

    public ELink(InputStream inputStream, OutputStream outputStream, Configuration configuration) {
        super("e-link", inputStream, outputStream, configuration);
        parameters.put("templateid", configuration.getTemplateID());
    }

    public static void addOptions(Options options) {
        Option templateID = Option.builder().longOpt("templateid").hasArg().argName("ID").desc("This parameter sets the ID of the template to be used.").required(true).build();
        options.addOption(templateID);
    }

    public static String getDefaultValue(String option) {
        return null;
    }
}
