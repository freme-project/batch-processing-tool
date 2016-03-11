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
public class ETerminology extends AbstractService {

    public ETerminology(InputStream inputStream, OutputStream outputStream, Configuration configuration) {
        super("e-terminology", inputStream, outputStream, configuration);
        parameters.put("source-lang", configuration.getSourceLang());
        parameters.put("target-lang", configuration.getTargetLang());
        parameters.put("mode", configuration.getMode());
        
        if (configuration.getCollection() != null) {
            parameters.put("collection", configuration.getCollection());
        }
        
        if (configuration.getKey() != null) {
            parameters.put("key", configuration.getKey());
        }
        
        if (configuration.getDomain() != null) {
            parameters.put("domain", configuration.getDomain());
        }
    }

    public static void addOptions(Options options) {
        Option sourceLang = Option.builder("s").longOpt("source-lang").hasArg().argName("LANGUAGE").desc("The source language of the input document(s)").build();
        Option targetLang = Option.builder("t").longOpt("target-lang").hasArg().argName("LANGUAGE").desc("The target language of the output document(s)").build();
        Option collection = Option.builder("c").longOpt("collection").hasArg().argName("ID").desc("Collection ID from https://term.tilde.com portal").required(false).build();
        Option domain = Option.builder("d").longOpt("domain").hasArg().argName("DOMAIN").desc("It filters out by domain proposed terms").required(false).build();
        Option key = Option.builder("k").longOpt("key").hasArg().argName("KEY").desc("A private key to access private and not publicly available translation systems.").required(false).build();
        Option mode = Option.builder().longOpt("mode").hasArg().argName("MODE").desc("Whether the result must contain full terminology information or only term annotations with references to the full information").required(false).build();
        options.addOption(collection).addOption(key).addOption(mode).addOption(domain).addOption(sourceLang).addOption(targetLang);
    }

    public static String getDefaultValue(String option) {
        switch (option) {
            case "mode":
                return "full";
            case "source-lang":
                return "en";
            case "target-lang":
                return "de";
            default:
                return null;
        }
    }
}
