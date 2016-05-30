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

    private OutputStreamWriter _out;
    private OutputStreamWriter _err;
    private boolean _log_out;
    private boolean _log_err;


    public NewHighlightHTMLReportGenerator(OutputStream out, OutputStream err) {
        if (out != null) {
            _out = new OutputStreamWriter(out);
            _log_out = true;
        } else _log_out = false;
        if (err != null) {
            _err = new OutputStreamWriter(err);
            _log_err = true;
        } else _log_err = false;
        _log_out = _out != null;
        _log_err = _err != null;
    }

    public NewHighlightHTMLReportGenerator(OutputStream out) {
        this(out, out);
    }

    public NewHighlightHTMLReportGenerator() {
        this(null, null);
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

    private void handleArgs(Map m, String title, Path root, Collection<File> exec, Collection<File> src, String key) {
        Object val;
        if ((val = m.get("root")) != null) {
            log("Found 'root'");
            if (val instanceof String) {
                root = Paths.get((String) val).toAbsolutePath().normalize();
            } else {
                err("Root path \"" + val + "\" is not a String. Using working directory instead...");
            }
        }
        if ((val = m.get("exec")) != null) {
            log("Found 'exec'");
            addFiles(val, exec, root);
        }
        if ((val = m.get("src")) != null) {
            log("Found 'src'");
            addFiles(val, src, root);
        }
        if ((val = m.get("title")) != null) {
            log("Found 'title'");
            if (val instanceof String) {
                title = (String) val;
                log("title = \"" + title + "\"");
            } else {
                err("Title is not a String");
            }
        }
        if ((val = m.get("class")) != null) {
            /*log("found 'class'");
            if ("bundle".equals(key)) {
                handleBundle(val, root);
            } else if ("group".equals(key)) {
                handleGroup(val, title, root, exec, src);
            } else {
                handleUnknown(val, title, root, exec, src);
            }*/
        }
        if (title == null) {
            title = root.getFileName().toString();
            log("Defaulting title to \"" + title +"\"");
        }
    }

    private void handleArgs(Map m) {
        handleArgs(m, null, Paths.get(System.getProperty("user.dir")), new ArrayList<File>(), new ArrayList<File>(), null);
    }

    private void addFiles(Object o, Collection<File> l, Path root) {
        if (o instanceof String) {
            log("Found String");
            PathMatcher pm = FileSystems.getDefault().getPathMatcher("glob:" + o);
            findFiles(root, pm, l);
        } else if (o instanceof List) {
            log("Beginning List...");
            for (Object child : (List) o) {
                addFiles(child, l, root);
            }
            log("Ending List");
        } else {
            err(o + " is not a String or List. Skipping...");
        }
    }

    private void findFiles(Path root, final PathMatcher pm, final Collection<File> files) {
        SimpleFileVisitor<Path> v = new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) {
                if (file != null && pm.matches(file)) {
                    files.add(file.toFile());
                }
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult preVisitDirectory(Path dir,
                                                     BasicFileAttributes attrs) {
                if (dir != null && pm.matches(dir)) {
                    files.add(dir.toFile());
                }
                return FileVisitResult.CONTINUE;
            }
        };
        try {
            Files.walkFileTree(root, v);
        } catch (IOException ioe) {
            err(ioe.toString());
        }
    }

    void log(String s) {
        if (_log_out) {
            try {
                _out.write(s = "LOG: " + s + "\n", 0, s.length());
                _out.flush();
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
    }

    void err(String s) {
        if (_log_err) {
            try {
                _err.write(s = "ERROR: " + s + "\n", 0, s.length());
                _err.flush();
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
    }

    public void closeStreams() throws IOException {
        if (_out != null) {
            _out.close();
        }
        if (_err != null) {
            _err.close();
        }
        _log_out = _log_err = false;
    }
}
