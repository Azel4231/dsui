# DSUI
A Clojure tool for displaying arbitrary nested data structures using swing. DSUI stands for "Data Structure User Interface".

## Status
Toy project

## How it works
DSUI uses clojure.spec to "parse" an arbitrary data structure. The conformed data is used to generate the swing UI by calling a multimethod that polymorphically creates different types of UI elements.

A more detailed explanation can be found <a href="https://feierabendprojekte.wordpress.com/2016/09/11/generating-ui-for-arbitrarily-nested-data-structures/">here</a>.

## NextSteps
- Support sets
- Read nested Vectors as a table
- Handle primitive/scalar values
- Make UI editable (e.g. swap value to an atom)

## License
Distributed under the Eclipse Public License, the same as Clojure.
