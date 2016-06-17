package report;

import java.io.*;

/**
 * Created by nkalonia1 on 6/15/16.
 */
public class Logger {

    private OutputStreamWriter _out;
    private OutputStreamWriter _err;
    private boolean _log_out;
    private boolean _log_err;


    public Logger(OutputStream out, OutputStream err) {
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

    public Logger(OutputStream out) {
        this(out, out);
    }

    public Logger() {
        this(null, null);
    }

    public void log(String s) {
        if (_log_out) {
            try {
                _out.write(s = "LOG: " + s + "\n", 0, s.length());
                _out.flush();
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
    }

    public void err(String s) {
        if (_log_err) {
            try {
                _err.write(s = "ERROR: " + s + "\n", 0, s.length());
                _err.flush();
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
    }

    public void close() throws IOException {
        if (_out != null) {
            _out.close();
        }
        if (_err != null) {
            _err.close();
        }
        _log_out = _log_err = false;
    }
}
