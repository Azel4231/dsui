# DSUI
A Clojure tool for displaying arbitrary nested data structures using swing. DSUI stands for "Data Structure User Interface".

Currently DSUI is more of a proof-of-concept. There's a lot to improve. Especially when displaying deeply nested and/or heterogenous data structures.

# How it works
DSUI uses clojure.spec to "parse" an arbitrary data structure. The conformed data is used to generate the swing UI by calling a multimethod that polymorphically creates different types of UI elements.

# NextSteps
- Improve Layout (e.g. GridBagLayout)
- Read nested Vectors as a table
- Spec the function dsui
- Handle primitive/scalar values
- Make a version of dsui that returns a JPanel instead of displaying a JFrame, so generated UIs can be used as part of hand-written UIs.
- Improve UI for deeply nested and/or heterogenous data structures

# License
Distributed under the Eclipse Public License, the same as Clojure.
