package report;

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

    private Logger out;

    public NewHighlightHTMLReportGenerator(Logger l) {
        out = l;
    }

    public void parseArgs(File params) {
        if (params == null) return;
        Yaml yaml = new Yaml();
        try {
            for(Object o : yaml.loadAll(new FileInputStream(params))) {
                if (!(o instanceof Map)) {
                    log("Object " + o + " is not a Map. Skipping...");
                    continue;
                }
                handleArgs((Map) o);
            }
        } catch(FileNotFoundException fnfe) {
            System.out.println(fnfe);
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
}
