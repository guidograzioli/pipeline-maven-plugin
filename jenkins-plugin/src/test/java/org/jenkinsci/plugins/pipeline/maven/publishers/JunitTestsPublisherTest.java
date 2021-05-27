package org.jenkinsci.plugins.pipeline.maven.publishers;

import static java.lang.System.getProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.jenkinsci.plugins.workflow.graph.FlowNode;
import org.jenkinsci.plugins.workflow.steps.StepContext;
import org.junit.Test;
import org.w3c.dom.Document;

import java.io.File;
import java.io.InputStream;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilderFactory;

import hudson.EnvVars;
import hudson.FilePath;
import hudson.model.Run;
import hudson.tasks.junit.TestResultAction;

public class JunitTestsPublisherTest {

    @Test
    public void test_surefire_plugin() throws Exception {
        final InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(
                        "org/jenkinsci/plugins/pipeline/maven/maven-spy-maven-surefire-plugin.xml");
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new File(
                "src/test/resources/org/jenkinsci/plugins/pipeline/maven/surefire-reports/TEST-some.groupid.AnArtifactTest.xml")
                        .lastModified());
        final StepContext context = mock(StepContext.class);
        final Run run = mock(Run.class);
        final FlowNode node = mock(FlowNode.class);
        final EnvVars envvars = mock(EnvVars.class);
        when(context.get(FilePath.class)).thenReturn(new FilePath(new File("")));
        when(context.get(Run.class)).thenReturn(run);
        when(context.get(FlowNode.class)).thenReturn(node);
        when(node.getId()).thenReturn("nodeId");
        when(run.getEnvironment(any())).thenReturn(envvars);
        when(run.getTimestamp()).thenReturn(calendar);
        when(run.getRootDir()).thenReturn(new File(getProperty("java.io.tmpdir")));
        when(envvars.expand(any()))
                .thenReturn("src/test/resources/org/jenkinsci/plugins/pipeline/maven/surefire-reports/*.xml");

        new JunitTestsPublisher().process(context, doc.getDocumentElement());

        verify(run).addAction(any(TestResultAction.class));
    }

    @Test
    public void test_failsafe_plugin() throws Exception {
        final InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(
                        "org/jenkinsci/plugins/pipeline/maven/maven-spy-maven-failsafe-plugin.xml");
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new File(
                "src/test/resources/org/jenkinsci/plugins/pipeline/maven/failsafe-reports/TEST-some.groupid.AnArtifactTest.xml")
                        .lastModified());
        final StepContext context = mock(StepContext.class);
        final Run run = mock(Run.class);
        final FlowNode node = mock(FlowNode.class);
        final EnvVars envvars = mock(EnvVars.class);
        when(context.get(FilePath.class)).thenReturn(new FilePath(new File("")));
        when(context.get(Run.class)).thenReturn(run);
        when(context.get(FlowNode.class)).thenReturn(node);
        when(node.getId()).thenReturn("nodeId");
        when(run.getEnvironment(any())).thenReturn(envvars);
        when(run.getTimestamp()).thenReturn(calendar);
        when(run.getRootDir()).thenReturn(new File(getProperty("java.io.tmpdir")));
        when(envvars.expand(any()))
                .thenReturn("src/test/resources/org/jenkinsci/plugins/pipeline/maven/failsafe-reports/*.xml");

        new JunitTestsPublisher().process(context, doc.getDocumentElement());

        verify(run).addAction(any(TestResultAction.class));
    }

    @Test
    public void test_karma_plugin() throws Exception {
        final InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream("org/jenkinsci/plugins/pipeline/maven/maven-spy-maven-karma-plugin.xml");
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new File(
                "src/test/resources/org/jenkinsci/plugins/pipeline/maven/karma-reports/TEST-karma.xml")
                        .lastModified());
        final StepContext context = mock(StepContext.class);
        final Run run = mock(Run.class);
        final FlowNode node = mock(FlowNode.class);
        final EnvVars envvars = mock(EnvVars.class);
        when(context.get(FilePath.class)).thenReturn(new FilePath(new File("")));
        when(context.get(Run.class)).thenReturn(run);
        when(context.get(FlowNode.class)).thenReturn(node);
        when(node.getId()).thenReturn("nodeId");
        when(run.getEnvironment(any())).thenReturn(envvars);
        when(run.getTimestamp()).thenReturn(calendar);
        when(run.getRootDir()).thenReturn(new File(getProperty("java.io.tmpdir")));
        when(envvars.expand(any()))
                .thenReturn("src/test/resources/org/jenkinsci/plugins/pipeline/maven/karma-reports/*.xml");

        new JunitTestsPublisher().process(context, doc.getDocumentElement());

        verify(run).addAction(any(TestResultAction.class));
    }

    @Test
    public void test_frontend_plugin() throws Exception {
        final InputStream in = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(
                        "org/jenkinsci/plugins/pipeline/maven/maven-spy-maven-frontend-plugin.xml");
        final Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(new File(
                "src/test/resources/org/jenkinsci/plugins/pipeline/maven/karma-reports/TEST-karma.xml")
                        .lastModified());
        final StepContext context = mock(StepContext.class);
        final Run run = mock(Run.class);
        final FlowNode node = mock(FlowNode.class);
        final EnvVars envvars = mock(EnvVars.class);
        when(context.get(FilePath.class)).thenReturn(new FilePath(new File("")));
        when(context.get(Run.class)).thenReturn(run);
        when(context.get(FlowNode.class)).thenReturn(node);
        when(node.getId()).thenReturn("nodeId");
        when(run.getEnvironment(any())).thenReturn(envvars);
        when(run.getTimestamp()).thenReturn(calendar);
        when(run.getRootDir()).thenReturn(new File(getProperty("java.io.tmpdir")));
        when(envvars.expand(any()))
                .thenReturn("src/test/resources/org/jenkinsci/plugins/pipeline/maven/karma-reports/*.xml");

        new JunitTestsPublisher().process(context, doc.getDocumentElement());

        verify(run).addAction(any(TestResultAction.class));
    }
}
