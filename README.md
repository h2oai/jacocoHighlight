# jacocoHighlight
**_[Note: This is a work in progress. API is subject to change.]_**

This is a wrapper of [JaCoCo](http://eclemma.org/jacoco/ "JaCoCo") that expands on its functionalities in making HTML reports. Specifically, it allows for the highlighting of cells to indicate a pass or fail.

[Example](http://s3.amazonaws.com/nishant-tests-coverage/example_report/index.html "JaCoCo Example")  
In the above example, a cell passes if it has 50% or more coverage.

Note that each cell not only reflects the current item's overall coverage, but also the coverage of everything the item contains. So in the above example, a package could have >50% overall coverage, but still be marked as failing because one of its classes is failing. The same applies to classes in terms of its methods. To clarify: For a package to be marked as passing, _every_ class it contains needs to be marked as passing (as well as the package's overall coverage). For a class to be marked as passing, _every_ method it contains needs to meet its coverage criteria (as well as the class' overall coverage).

## Versions
JaCoCo version 0.7.6 is required.

## Usage
### Command Line
`java -jar path_to_jar [root_dir] [-o out_dir] [-e exec_files] [-c class_files] [-s source_roots] [-p params_file]`

| Variable          | Description                                                                                       | Default            |
| ----------------- | ------------------------------------------------------------------------------------------------- | ------------------ |
| `root_dir`        | Working directory                                                                                 | `.`                |
| `out_dir`         | Directory to output HTML report in respect to `root_dir`                                          | `report`           |
| `exec_files`      | Path to `.exec` files in respect to `root_dir` _(accepts glob patterns)_                          | `*.exec`           |
| `class_files`     | Path to class files in respect to `root_dir` _(accepts glob patterns)_                            | `**/classes`       |
| `source_roots`    | Path to the root directories of source files in respect to `root_dir` _(accepts glob patterns)_   | `**/src/main/java` |
| `params_file`     | Path to a parameter file in respect to `root_dir` _(highlighting does not occur if undefined)_    | undefined          |


## Parameter File
The parameter file specifies the passing coverage criteria for each item in the report. The file should be written according to the [YAML Version 1.1 specs](http://yaml.org/spec/1.1/ "YAML 1.1 Specs"), where the criteria for each item is in its own separate document. Each document can accept the following scalars:
| Scalar        | Description                                                   |
| ------------- | ------------------------------------------------------------- |
| `package`     | Descriptors of the package, if any                            |
| `class`       | Descriptors of the class, if any                              |
| `method`      | Descriptors of the method, if any                             |
| `values`      | Values for each coverage field                                |
| `propagate`   | Whether the criteria should be applied to subclasses/methods  |
| `default`     | The default value of each coverage field                      |

### `default`
`default` is used to specify the coverage criteria for each field that should be used if nothing was provided. For example, if the criteria for the "instruction" field in a package was undefined, then it will be set to whatever `default` has for that field. It can either be a number or a dictionary of values. If the former, then that value is applied to every field. By default each field is given a value of `0`.

Example:
    default: 50 # Sets each field to require 50% or more coverage by default
    ---
    default: {instruction: 30, complexity: 40, line: 10, class: 50, method: 50, branch: 0}

### `package`

## To Do
- Tests!
- Clean up!