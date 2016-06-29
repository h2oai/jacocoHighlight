package report;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

/**
 * Created by nkalonia1 on 6/15/16.
 */
public class ArgHandler {
    ArgParams _params;
    private Logger _l;

    public ArgHandler(Logger l) {
        this(l, new ArgParams());
    }

    public ArgHandler(Logger l, ArgParams params) {
        this._l = l;
        _params = params.shallowCopy();
    }

    public ArgItem handleArgs(Map m) {
        Object val;
        ArgItem ai = new InvalidArgItem();
        if ((val = m.get("root")) != null) {
            log("Found 'root'");
            if (val instanceof String) {
                _params.root = Paths.get((String) val).toAbsolutePath().normalize();
            } else {
                err("Root path \"" + val + "\" is not a String. Using working directory instead...");
                _params.root = Paths.get(System.getProperty("user.dir"));
            }
        }
        if ((val = m.get("exec")) != null) {
            log("Found 'exec'");
            _params.execs = new ExecutionData(addFiles(val, new ArrayList<File>(), _params.root));
        }
        if ((val = m.get("src")) != null) {
            log("Found 'src'");
            _params.sources = addFiles(val, new ArrayList<File>(), _params.root);
        }
        if ((val = m.get("title")) != null) {
            log("Found 'title'");
            if (val instanceof String) {
                _params.title = (String) val;
                log("Setting title to \"" + _params.title + "\"");
            } else {
                err("Title is not a String");
            }
        }
        if (_params.title == null) {
            _params.title = _params.root.getFileName().toString();
            log("Defaulting title to \"" + _params.title + "\"");
        }
        if ((val = m.get("class")) != null) {
            log("found 'class'");
            if (val instanceof String || val instanceof Map) {
                List<Object> new_val = (new ArrayList<Object>(1));
                new_val.add(val);
                val = new_val;
            }
            if (val instanceof List) {
                List l = (List) val;
                if (l.size() > 0) {
                    Object item = l.get(0);
                    if (item instanceof Map) {
                        ai = createGroupArgItem(l);
                    } else if (item instanceof String) {
                        ai = createBundleArgItem(l);
                    } else {
                        err("Invalid type found in List for 'class'");
                    }
                } else {
                    ai = createBundleArgItem(new ArrayList<String>(0));
                }
            } else {
                err("Invalid type for 'class': " + val);
            }
        }
        return ai;
    }

    private ArgItem createGroupArgItem(List l) {
        log("Creating Group...");
        GroupArgItem gai = new GroupArgItem(_params);
        Collection<ArgItem> argItems = new ArrayList<ArgItem>();
        for (Object item : l) {
            if (item instanceof Map) {
                ArgHandler ah = new ArgHandler(_l, _params);
                argItems.add(ah.handleArgs((Map) item));
            } else {
                err(item + " is not a Map. Skipping...");
            }
        }
        gai.addClass(argItems);
        return gai;
    }

    private ArgItem createBundleArgItem(List l) {
        log("Creating Bundle...");
        BundleArgItem bai = new BundleArgItem(_params);
        bai.addClass(addFiles(l, new ArrayList<File>(), _params.root));
        return bai;
    }

    private Collection<File> addFiles(Object o, Collection<File> l, Path root) {
        if (o instanceof String) {
            log("Found String");
            PathMatcher pm = FileSystems.getDefault().getPathMatcher("glob:" + root.resolve((String) o));
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
        return l;
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

    private void log(String s) {
        _l.log(s);
    }

    private void err(String s) {
        _l.err(s);
    }
}
