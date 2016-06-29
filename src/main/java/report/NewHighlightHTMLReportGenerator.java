package report;

import jacoco.report.html.HighlightHTMLFormatter;
import jacoco.report.internal.html.parse.NewYAMLParser;
import org.jacoco.core.tools.ExecFileLoader;
import org.jacoco.report.FileMultiReportOutput;
import org.jacoco.report.IReportVisitor;
import org.jacoco.report.html.HTMLFormatter;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by nkalonia1 on 5/27/16.
 */
public class NewHighlightHTMLReportGenerator {
    private static File DEFAULT_CONFIG = new File("config.yml");
    private static File DEFAULT_OUT = new File("out");

    private Logger out;

    public NewHighlightHTMLReportGenerator(Logger l) {
        out = l;
    }

    public void parseArgs(File params) throws IOException {
        if (params == null) return;
        Yaml yaml = new Yaml();
        for(Object o : yaml.loadAll(new FileInputStream(params))) {
            if (!(o instanceof Map)) {
                log("Object " + o + " is not a Map. Skipping...");
                continue;
            }
            Map m = (Map) o;
            File parseParamDirectory = DEFAULT_CONFIG;
            File reportDirectory = DEFAULT_OUT;
            Object val;
            if ((val = m.get("config")) != null) {
                log ("Found 'config'");
                if (val instanceof String) {
                    parseParamDirectory = new File((String) val);
                } else {
                    err("Invalid type for 'config'. Expected String. Defaulting to \"" + DEFAULT_CONFIG + "\"...");
                }
            }
            if ((val = m.get("out")) != null) {
                log ("Found 'out'");
                if (val instanceof String) {
                    reportDirectory = new File((String) val);
                } else {
                    err("Invalid type for 'out'. Expected String. Defaulting to \"" + DEFAULT_OUT + "\"...");
                }
            }
            log("Parsing args...");
            ArgHandler ah = new ArgHandler(out);
            ArgItem ai = ah.handleArgs((Map) o);
            log("Parsing config...");
            NewYAMLParser param_parser = new NewYAMLParser(System.out, System.err);
            final HTMLFormatter htmlFormatter = new HighlightHTMLFormatter(param_parser.parse(parseParamDirectory));
            log("Creating visitor...");
            final IReportVisitor visitor = htmlFormatter
                    .createVisitor(new FileMultiReportOutput(reportDirectory));
            log("Creating sessions...");
            ExecFileLoader sessionsEFL = ai.getAllExecs().createLoader();
            visitor.visitInfo(sessionsEFL.getSessionInfoStore().getInfos(),
                    sessionsEFL.getExecutionDataStore().getContents());
            log("Analyzing...");
            ai.analyze(visitor);
            visitor.visitEnd();
        }
    }

    void log(String s) {
        out.log(s);
    }

    void err(String s) {
        out.err(s);
    }

    public void closeStreams() throws IOException {
        out.close();
    }

    public static void main(String[] args) throws IOException {
        NewHighlightHTMLReportGenerator r =new NewHighlightHTMLReportGenerator(new Logger(System.out, System.err));
        System.out.println(args[0]);
        r.parseArgs(new File(args[0]));
    }
}
