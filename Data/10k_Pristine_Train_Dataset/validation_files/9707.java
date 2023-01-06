/*
 * Copyright 2012 NGDATA nv
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.lilyproject.lilyservertestfw.launcher;

import java.io.File;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.lilyproject.hadooptestfw.HBaseTestingUtilityFactory;
import org.lilyproject.lilyservertestfw.TemplateDir;

public class HadoopLauncherService implements LauncherService {
    private org.lilyproject.hadooptestfw.fork.HBaseTestingUtility hbaseTestUtility;
    private Configuration conf;
    private File testHome;
    private boolean clearData;
    private Option disableMROption;
    private boolean disableMapReduce = false;

    private Log log = LogFactory.getLog(getClass());

    @Override
    public void addOptions(List<Option> options) {
        disableMROption = OptionBuilder
                .withDescription("Disable startup of MapReduce services")
                .withLongOpt("disable-map-reduce")
                .create("dmr");
        options.add(disableMROption);
    }

    @Override
    public int setup(CommandLine cmd, File testHome, boolean clearData) throws Exception {
        this.testHome = new File(testHome, TemplateDir.HADOOP_DIR);
        FileUtils.forceMkdir(testHome);
        this.clearData = clearData;
        disableMapReduce = cmd.hasOption(disableMROption.getOpt());

        return 0;
    }

    @Override
    public int start(List<String> postStartupInfo) throws Exception {
        conf = HBaseConfiguration.create();

        hbaseTestUtility = HBaseTestingUtilityFactory.create(conf, testHome, clearData);
        hbaseTestUtility.startMiniCluster(1);
        if (!disableMapReduce) {
            hbaseTestUtility.startMiniMapReduceCluster(1);
        }

        postStartupInfo.add("-------------------------");
        postStartupInfo.add("HDFS is running");
        postStartupInfo.add("");
        postStartupInfo.add("HDFS web ui: http://" + conf.get("dfs.namenode.http-address"));
        postStartupInfo.add("");

        postStartupInfo.add("-------------------------");
        postStartupInfo.add("HBase is running");
        postStartupInfo.add("");
        postStartupInfo.add("HBase master web ui: http://localhost:" +
                hbaseTestUtility.getHBaseCluster().getMaster().getInfoServer().getPort());
        postStartupInfo.add("");
        postStartupInfo.add("To connect to this HBase, use the following properties:");
        postStartupInfo.add("hbase.zookeeper.quorum=localhost");
        postStartupInfo.add("hbase.zookeeper.property.clientPort=2181");
        postStartupInfo.add("");
        postStartupInfo.add("In Java code, create the HBase configuration like this:");
        postStartupInfo.add("Configuration conf = HBaseConfiguration.create();");
        postStartupInfo.add("conf.set(\"hbase.zookeeper.quorum\", \"localhost\");");
        postStartupInfo.add("conf.set(\"hbase.zookeeper.property.clientPort\", \"2181\");");
        postStartupInfo.add("");

        if (!disableMapReduce) {
            postStartupInfo.add("-------------------------");
            postStartupInfo.add("MapReduce is running");
            postStartupInfo.add("");
            postStartupInfo.add("JobTracker web ui: http://localhost:" +
                    hbaseTestUtility.getMRCluster().getJobTrackerRunner().getJobTrackerInfoPort());
            postStartupInfo.add("");
            postStartupInfo.add("Configuration conf = new Configuration();");
            postStartupInfo.add("conf.set(\"mapred.job.tracker\", \"localhost:" +
                    hbaseTestUtility.getMRCluster().getJobTrackerPort() + "\");");
            postStartupInfo.add("Job job = new Job(conf);");
            postStartupInfo.add("");
        }

        return 0;
    }

    @Override
    public void stop() {
        if (hbaseTestUtility != null) {
            try {
                hbaseTestUtility.shutdownMiniHBaseCluster();
            } catch (Throwable t) {
                log.error("Error shutting down MiniHBaseCluster", t);
            }

            try {
                hbaseTestUtility.shutdownMiniMapReduceCluster();
            } catch (Throwable t) {
                log.error("Error shutting down MiniMapReduceCluster", t);
            }

            try {
                hbaseTestUtility.shutdownMiniDFSCluster();
            } catch (Throwable t) {
                log.error("Error shutting down MiniDFSCluster", t);
            }

            try {
                hbaseTestUtility.shutdownMiniZKCluster();
            } catch (Throwable t) {
                log.error("Error shutting down MiniZKCluster", t);
            }

            hbaseTestUtility = null;
        }
    }

    public Configuration getConf() {
        return conf;
    }
}
