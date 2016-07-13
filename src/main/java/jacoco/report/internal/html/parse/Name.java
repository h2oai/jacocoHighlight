package jacoco.report.internal.html.parse;

import jacoco.report.internal.html.parse.util.NameList;
import jacoco.report.internal.html.parse.util.NameString;
import org.jacoco.core.analysis.IClassCoverage;
import org.jacoco.core.analysis.ICoverageNode;
import org.jacoco.core.analysis.IMethodCoverage;

import java.util.HashMap;

public abstract class Name<S,L> {
    enum String_Field {
        NAME, SIGNATURE, SUPERCLASS, DESCRIPTION
    }

    enum List_Field {
        INTERFACES
    }

    protected HashMap<String_Field, S> strings;
    protected HashMap<List_Field, L> lists;

    public Name() {
        strings = new HashMap<String_Field, S>(3, 1);
        lists = new HashMap<List_Field, L>(1, 1);
    }

    void put(S s, String_Field f) {
        if (strings.containsKey(f)) throw new IllegalStateException("String_Field already exists.");
        if (s == null) throw new IllegalArgumentException("'null' cannot be an argument");
        strings.put(f, s);
    }

    void put(L l, List_Field f) {
        if (lists.containsKey(f)) throw new IllegalStateException("String_Field already exists.");
        if (l == null) throw new IllegalArgumentException("'null' cannot be an argument");
        lists.put(f, l);
    }

    S get(String_Field s) {
        return strings.get(s);
    }

    L get(List_Field l) {
        return lists.get(l);
    }

    public abstract boolean matches(StringName sn);
}

class AntName extends Name<NameString, NameList> {
    @Override
    public boolean matches(StringName sn) {
        for (String_Field f : strings.keySet()) {
            if (!sn.strings.containsKey(f)) return false;
            if (!strings.get(f).matches(sn.strings.get(f))) return false;
        }
        for (List_Field f : lists.keySet()) {
            if (!sn.lists.containsKey(f)) return false;
            if (!lists.get(f).matches(sn.lists.get(f))) return false;
        }
        return true;
    }
}

class StringName extends Name<String, String[]> {
    @Override
    public boolean matches(StringName sn) {
        for (String_Field f : strings.keySet()) {
            if (!sn.strings.containsKey(f)) return false;
            if (!strings.get(f).equals(sn.strings.get(f))) return false;
        }
        for (List_Field f : lists.keySet()) {
            if (!sn.lists.containsKey(f)) return false;
            String[] arr_1, arr_2;
            if ((arr_1 = lists.get(f)).length != (arr_2 = sn.lists.get(f)).length) return false;
            for (int i = 0; i < arr_1.length; ++i) {
                if (!arr_1[i].equals(arr_2[i])) return false;
            }
        }
        return true;
    }

}

class InvalidAntName extends AntName {
    @Override
    public boolean matches(StringName sn) {
        return false;
    }
}

class NameCreator {
    static StringName create(ICoverageNode n) {
        StringName name = new StringName();
        name.put(n.getName(), Name.String_Field.NAME);
        if (n instanceof IClassCoverage) {
            String signature = ((IClassCoverage) n).getSignature() == null ? "" : ((IClassCoverage) n).getSignature();
            String superclass = ((IClassCoverage) n).getSuperName() == null ? "" : ((IClassCoverage) n).getSuperName();
            String[] interfaces = ((IClassCoverage) n).getInterfaceNames() == null ? new String[0] : ((IClassCoverage) n).getInterfaceNames();
            name.put(signature, Name.String_Field.SIGNATURE);
            name.put(superclass, Name.String_Field.SUPERCLASS);
            name.put(interfaces, Name.List_Field.INTERFACES);
        } else if (n instanceof IMethodCoverage) {
            String signature = ((IMethodCoverage) n).getSignature() == null ? "" : ((IMethodCoverage) n).getSignature();
            String description = ((IMethodCoverage) n).getDesc() == null ? "" : ((IMethodCoverage) n).getDesc();
            name.put(signature, Name.String_Field.SIGNATURE);
            name.put(description, Name.String_Field.SUPERCLASS);
        }
        return name;
    }
}

