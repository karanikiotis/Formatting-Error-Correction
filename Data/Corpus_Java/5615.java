/*
 * Copyright (C) 2016 QAware GmbH
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package de.qaware.chronix.solr.ingestion;

import de.qaware.chronix.solr.ingestion.format.GraphiteFormatParser;

/**
 * Handler to ingest the Graphite line format.
 */
public class GraphiteIngestionHandler extends AbstractIngestionHandler {
    /**
     * Constructor.
     */
    public GraphiteIngestionHandler() {
        super(new GraphiteFormatParser());
    }

    @Override
    public String getDescription() {
        return "The Chronix Graphite ingestion handler.";
    }
}
