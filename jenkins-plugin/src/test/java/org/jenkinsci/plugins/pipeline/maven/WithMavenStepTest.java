/*
 * The MIT License
 *
 * Copyright 2016 CloudBees, Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.pipeline.maven;

import com.cloudbees.plugins.credentials.CredentialsScope;
import com.cloudbees.plugins.credentials.SystemCredentialsProvider;
import com.cloudbees.plugins.credentials.domains.Domain;
import com.cloudbees.plugins.credentials.impl.UsernamePasswordCredentialsImpl;
import hudson.model.Fingerprint;
import hudson.model.FingerprintMap;
import hudson.model.Result;
import hudson.plugins.sshslaves.SSHLauncher;
import hudson.slaves.DumbSlave;
import hudson.slaves.RetentionStrategy;
import org.apache.commons.io.FileUtils;
import org.hamcrest.Matchers;
import org.jenkinsci.plugins.pipeline.maven.docker.JavaGitContainer;
import org.jenkinsci.plugins.workflow.cps.CpsFlowDefinition;
import org.jenkinsci.plugins.workflow.job.WorkflowJob;
import org.jenkinsci.plugins.workflow.job.WorkflowRun;
import org.jenkinsci.test.acceptance.docker.DockerRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.jvnet.hudson.test.Issue;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;

public class WithMavenStepTest extends AbstractIntegrationTest {

    @Rule
    public DockerRule<JavaGitContainer> slaveRule = new DockerRule<>(JavaGitContainer.class);

    @Before
    public void setup() throws Exception {
        super.setup();

        JavaGitContainer slaveContainer = slaveRule.get();

        SystemCredentialsProvider.getInstance().getDomainCredentialsMap()
                .put(Domain.global(), Collections.singletonList(new UsernamePasswordCredentialsImpl(
                        CredentialsScope.GLOBAL, "test", null, "test", "test")));
        DumbSlave agent = new DumbSlave("remote","/home/test/slave",new SSHLauncher(slaveContainer.ipBound(22), slaveContainer.port(22), "test"));
        agent.setNumExecutors(1);
        agent.setRetentionStrategy(RetentionStrategy.INSTANCE);
        jenkinsRule.jenkins.addNode(agent);
    }

    @Issue("SECURITY-441")
    @Test
    public void maven_build_on_remote_agent_with_settings_file_on_master_fails() throws Exception {
        File onMaster = new File(jenkinsRule.jenkins.getRootDir(), "onmaster");
        String secret = "secret content on master";
        FileUtils.writeStringToFile(onMaster, secret, StandardCharsets.UTF_8);
        String pipelineScript = "node('remote') {withMaven(mavenSettingsFilePath: '" + onMaster + "') {echo readFile(MVN_SETTINGS)}}";

        WorkflowJob p = jenkinsRule.createProject(WorkflowJob.class, "p");
        p.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        WorkflowRun b = jenkinsRule.assertBuildStatus(Result.FAILURE, p.scheduleBuild2(0));
        jenkinsRule.assertLogNotContains(secret, b);
    }

    @Test
    public void disable_all_publishers() throws Exception {

        loadMonoDependencyMavenProjectInGitRepo( this.gitRepoRule );

        String pipelineScript = "node('master') {\n" + "    git($/" + gitRepoRule.toString() + "/$)\n"
                + "    withMaven(publisherStrategy: 'EXPLICIT') {\n"
                + "        sh 'mvn package'\n"
                + "    }\n"
                + "}";

        String commonsLang3version35Md5 = "780b5a8b72eebe6d0dbff1c11b5658fa";
        WorkflowJob firstPipeline = jenkinsRule.createProject(WorkflowJob.class, "disable-all-publishers");
        firstPipeline.setDefinition(new CpsFlowDefinition(pipelineScript, true));
        jenkinsRule.assertBuildStatus(Result.SUCCESS, firstPipeline.scheduleBuild2(0));
        FingerprintMap fingerprintMap = jenkinsRule.jenkins.getFingerprintMap();
        Fingerprint fingerprint = fingerprintMap.get(commonsLang3version35Md5);
        assertThat( fingerprint, Matchers.nullValue() );
    }
}
