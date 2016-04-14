# jacocoHighlight
This is a wrapper of [JaCoCo](http://eclemma.org/jacoco/ "JaCoCo") that expands on its functionalities in making HTML reports. Specifically, it allows for the highlighting of cells to indicate a pass or fail.

[Example](http://htmlpreview.github.io/?https://github.com/nkalonia1/jacocoHighlight/tree/master/example_report/index.html "JaCoCo Example")
In the above example, a cell passes if it has 50% or more coverage.

Note that each cell not only reflects the current item's overall coverage, but also the coverage of everything the item contains. So in the above example, a package could have >50% overall coverage, but still be marked as failing because one of its classes is failing. The same applies to classes in terms of its methods.
To clarify: For a package to be marked as passing, *every* class it contains needs to be marked as passing (as well as the package's overall coverage). For a class to be marked as passing, *every* method it contains needs to meet its coverage criteria (as well as the class' overall coverage).

## Versions
JaCoCo version 0.7.6 is required.

## Usage


