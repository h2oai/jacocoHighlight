# jacocoHighlight
**_[Note: This is a work in progress. API is subject to change.]_**

[Download](http://s3.amazonaws.com/nishant-tests-coverage/jacocoHighlight_app.jar "Download Jar")

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
The parameter file specifies the passing coverage criteria for each item in the report. The file should be written according to the [YAML Version 1.1 specs](http://yaml.org/spec/1.1/ "YAML 1.1 Specs"), where the criteria for each item is in its own separate document. The values defined for some scalars in a document are carried over to the next one (the exact list is specified below).

Each document can accept the following scalars:

| Scalar                    | Description                                                       | Carried Over? |
| ------------------------- | ----------------------------------------------------------------- | :-----------: |
| [`package`](#package)     | Descriptors of the package, if any                                | Yes           |
| [`class`](#class)         | Descriptors of the class, if any                                  | Yes           |
| [`method`](#method)       | Descriptors of the method, if any                                 | Yes           |
| [`values`](#values)       | Minimum criteria of the hit ratio for each coverage counter       | No            |
| [`propagate`](#propagate) | Whether the criteria should also be applied to subclasses/methods | Yes           |
| [`default`](#default)     | Default criteria of the hit ratio for each coverage counter       | Yes           |

### `package`
`package` defines the package(s) to which the coverage criteria should be applied. The scalar expects to be mapped to a sequence of mappings that define various aspects of the package. If `package` is defined, the values for `class` and `method` will **not** be carried over from the previous document.

List of allowed mappings:

| Scalar    | Type      | Description                                               | Default   |
| --------- | :-------: | --------------------------------------------------------- | :-------: |
| `name`    | `String`  | Package name _(supports wildcard characters `*` and `?`)_ | undefined |

If a `package` is instead assigned a String, then it will be interpreted as a package with `name` being that String and all of its other scalars being their default values.

### `class`
`class` defines the class(es) to which the coverage criteria should be applied. The scalar expects to be mapped to a sequence of mappings that define various aspects of the class. If `class` is defined, the value for `method` will **not** be carried over from the previous document.

List of expected mappings:

| Scalar    | Type      | Description                                               | Default   |
| --------- | :-------: | --------------------------------------------------------- | :-------: |
| `name`    | `String`  | Class name _(supports wildcard characters `*` and `?`)_   | undefined |

If a `class` is instead assigned a String, then it will be interpreted as a package with `name` being that String and all of its other scalars being their default values.

### `method`
`method` defines the method(s) to which the coverage criteria should be applied. The scalar expects to be mapped to a sequence of mappings that define various aspects of the method.

List of expected mappings:

| Scalar    | Type      | Description                                               | Default   |
| --------- | :-------: | --------------------------------------------------------- | :-------: |
| `name`    | `String`  | Method name _(supports wildcard characters `*` and `?`)_  | undefined |

If a `method` is instead assigned a String, then it will be interpreted as a package with `name` being that String and all of its other scalars being their default values. 

### `values`

| Scalar        | Type          | Description                           |
| ------------- | :-----------: | --------------------------------------|
| `instruction` | `int | float` | Minimum instruction hit percentage    |
| `branch`      | `int | float` | Minimum branch hit percentage         |
| `complexity`  | `int | float` | Minimum complexity hit percentage     |
| `line`        | `int | float` | Minimum line hit percentage           |
| `method`      | `int | float` | Minimum method hit percentage         |
| `class`       | `int | float` | Minimum class hit percentage          |

### `propagate`
`propagate` is a boolean value. If `true`, then whatever criteria is applied to the item(s) defined in the document will also be recursively applied to every class/method it contains. By default `propagate` is set to `false`.

### `default`
`default` is used to specify the coverage criteria for each field that should be used if nothing was provided. For example, if the criteria for the "instruction" field in a package was undefined, then it will be set to whatever `default` has for that field. It can either be a number or a dictionary of values. If the former, then that value is applied to every field. By default each field is given a value of `0`.

### Examples

```YAML
# Set every item in the bundle to have minimum coverage of 50%
package: {name: "*"}
values: {instruction: 50, branch: 50, line: 50, complexity: 50, method: 50, class: 50}
propagate: true
---
# Alternitavely, it can be written like this
package: "*"
values: 50
propagate: true
---
# Set class "bar" in package "foo" to have a minimum instruction coverage of 45%
package: "foo"
class: "bar"
values: {instruction: 45}
propagate: false
```


## To Do
- Tests!
- Clean up!