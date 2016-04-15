# jacocoHighlight
**_[Note: This is a work in progress. API is subject to change.]_**

This is a wrapper of [JaCoCo](http://eclemma.org/jacoco/ "JaCoCo") that expands on its functionalities in making HTML reports. Specifically, it allows for the highlighting of cells to indicate a pass or fail.

[Example](http://htmlpreview.github.io/?https://github.com/nkalonia1/jacocoHighlight/master/example_report/index.html "JaCoCo Example")  
In the above example, a cell passes if it has 50% or more coverage.

Note that each cell not only reflects the current item's overall coverage, but also the coverage of everything the item contains. So in the above example, a package could have >50% overall coverage, but still be marked as failing because one of its classes is failing. The same applies to classes in terms of its methods. To clarify: For a package to be marked as passing, _every_ class it contains needs to be marked as passing (as well as the package's overall coverage). For a class to be marked as passing, _every_ method it contains needs to meet its coverage criteria (as well as the class' overall coverage).

## Versions
JaCoCo version 0.7.6 is required.

## Usage
### Command Line
`java -jar path_to_jar [root_dir] [-o out_dir] [-e exec_files] [-c class_files] [-s source_roots] [-p params_file]`

| Variable          | Usage                                                                                             | Default            |
| ----------------- | ------------------------------------------------------------------------------------------------- | ------------------ |
| `root_dir`        | Working directory                                                                                 | `.`                |
| `out_dir`         | Directory to output HTML report in respect to `root_dir`                                          | `report`           |
| `exec_files`      | Path to `.exec` files in respect to `root_dir` _(accepts glob patterns)_                          | `*.exec`           |
| `class_files`     | Path to class files in respect to `root_dir` _(accepts glob patterns)_                            | `**/classes`       |
| `source_roots`    | Path to the root directories of source files in respect to `root_dir` _(accepts glob patterns)_   | `**/src/main/java` |
| `params_file`     | Path to a parameter file in respect to `root_dir`                                                 | `null`             |

## To Do
- Tests!
- Clean up!