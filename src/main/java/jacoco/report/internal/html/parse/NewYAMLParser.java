package jacoco.report.internal.html.parse;
import jacoco.report.internal.html.parse.util.NameList;
import jacoco.report.internal.html.parse.util.NameString;
import org.jacoco.core.analysis.ICoverageNode.CounterEntity;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.util.*;

/**
 * Created by nkalonia1 on 3/23/16.
 */
public class NewYAMLParser {
    private Set<CounterEntity> _default_headers;

    private boolean _log_out;
    private boolean _log_err;
    private OutputStreamWriter _out;
    private OutputStreamWriter _err;

    private boolean _propagate;
    private static final NameString _wild_name = new NameString("*");
    private static final NameList _wild_list = new NameList(new NameString[] {_wild_name}, true);
    private Map<CounterEntity, Double> _default_values;
    private Map<CounterEntity, Double> _current_values;

    private List<NewParseItem> _items;

    public NewYAMLParser(OutputStream out, OutputStream err) {
        if (out != null) {
            _out = new OutputStreamWriter(out);
            _log_out = true;
        } else _log_out = false;
        if (err != null) {
            _err = new OutputStreamWriter(err);
            _log_err = true;
        } else _log_err = false;
        _default_values = new HashMap<CounterEntity, Double>();
        _current_values = new HashMap<CounterEntity, Double>();
        _items = new ArrayList<NewParseItem>();
        _default_headers = new HashSet<CounterEntity>();
        for (CounterEntity ce : CounterEntity.values()) {
            _default_headers.add(ce);
        }
        resetValues();
    }

    public NewYAMLParser(OutputStream out) { this(out, out); }

    public NewYAMLParser() { this(null, null); }

    public List<NewParseItem> parse(File params) {
        _items.clear();
        if (params == null) return _items;
        Yaml yaml = new Yaml();
        try {
            for(Object o : yaml.loadAll(new FileInputStream(params))) {
                log("Starting Document...");
                if (o instanceof Map) {
                    log("Starting block...");
                    resetValues();
                    RootParser rp = new RootParser(this);
                    NewParseItem pi = rp.parse((Map) o);
                    if(pi.isValid()) _items.add(pi);
                    log("Ending block...");
                } else if (o instanceof List) {
                    log("Starting List...");
                    for (Object i : (List) o) {
                        if (!(i instanceof Map)) {
                            err("Object " + i + " in list " + o + " is not a map. Skipping...");
                            continue;
                        }
                        log("Starting block...");
                        RootParser rp = new RootParser(this);
                        NewParseItem pi = rp.parse((Map) i);
                        if(pi.isValid()) _items.add(pi);
                        log("Ending block...");
                    }
                    log("Ending List...");
                } else {
                    err("Object " + o + " is not a map. Skipping...");
                    continue;
                }
                log("Ending Document...");
            }
        } catch (FileNotFoundException fnfe) {
            err("Couldn't find file: " + params);
        }
        return _items;
    }

    private void resetValues() {
        _items = new ArrayList<NewParseItem>();
        for (CounterEntity ce : _default_headers) {
            _default_values.put(ce, 0.0);
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

    public Collection<CounterEntity> getHeaders() { return _default_headers; }
}
