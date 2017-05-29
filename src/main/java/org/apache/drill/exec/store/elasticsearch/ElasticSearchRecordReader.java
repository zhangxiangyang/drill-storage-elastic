/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.drill.exec.store.elasticsearch;

import com.google.common.collect.Sets;
import org.apache.drill.common.exceptions.ExecutionSetupException;
import org.apache.drill.common.expression.SchemaPath;
import org.apache.drill.exec.ExecConstants;
import org.apache.drill.exec.ops.FragmentContext;
import org.apache.drill.exec.ops.OperatorContext;
import org.apache.drill.exec.physical.impl.OutputMutator;
import org.apache.drill.exec.store.AbstractRecordReader;
import org.apache.drill.exec.vector.complex.impl.VectorContainerWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//TODO
public class ElasticSearchRecordReader extends AbstractRecordReader {

    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchRecordReader.class);
    private final ElasticSearchStoragePlugin plugin;
    private final FragmentContext fragmentContext;
    private final boolean unionEnabled;
    private Set<String> fields;
    private OperatorContext operatorContext;
    private VectorContainerWriter writer;

    public ElasticSearchRecordReader(ElasticSearchScanSpec elasticSearchScanSpec, List<SchemaPath> columns,
                                     FragmentContext context, ElasticSearchStoragePlugin elasticSearchStoragePlugin) {
        //TODO
        this.fields = new HashSet<>();
        this.plugin = elasticSearchStoragePlugin;
        this.fragmentContext = context;
        setColumns(columns);
        //TODO: What does this mean?
        this.unionEnabled = fragmentContext.getOptions().getOption(ExecConstants.ENABLE_UNION_TYPE);
        logger.debug("TODO constructor");
    }

    @Override
    protected Collection<SchemaPath> transformColumns(Collection<SchemaPath> projectedColumns) {
        Set<SchemaPath> transformed = Sets.newLinkedHashSet();
        //TODO: See if we can only poll for selected columns
        if (!isStarQuery()) {
            for (SchemaPath column : projectedColumns) {
                String fieldName = column.getRootSegment().getPath();
                transformed.add(column);
                this.fields.add(fieldName);
            }
        } else {
            transformed.add(AbstractRecordReader.STAR_COLUMN);
        }
        return transformed;
    }

    @Override
    public void setup(OperatorContext context, OutputMutator output) throws ExecutionSetupException {
        this.operatorContext = context;
        this.writer = new VectorContainerWriter(output,this.unionEnabled);
    }

    @Override
    public int next() {
        return 0;
    }

    @Override
    public void close() throws Exception {

    }
}
