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
public class EEntity extends AbstractService {

    public EEntity(InputStream inputStream, OutputStream outputStream, Configuration configuration) {
        super("e-entity", inputStream, outputStream, configuration);
        parameters.put("language", configuration.getLanguage());
        parameters.put("dataset", configuration.getDataset());
        parameters.put("mode", configuration.getMode());
    }

    public static void addOptions(Options options) {
        Option language = Option.builder("l").longOpt("language").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").required(false).build();
        Option dataset = Option.builder().longOpt("dataset").hasArg().argName("DATASET").desc("The dataset used for entity linking which includes a list of entites and associated labels.").required(false).build();
        Option mode = Option.builder().longOpt("mode").hasArg().argName("MODE").desc("This parameter allows to produce only partly results of named entity recognition.").required(false).build();
        options.addOption(language).addOption(dataset).addOption(mode);
    }

    public static String getDefaultValue(String option) {
        switch (option) {
            case "mode":
                return "all";
            case "language":
                return "en";
            case "dataset":
                return "dbpedia";
            default:
                return null;
        }
    }
}
