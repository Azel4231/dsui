# DSUI
A Clojure tool for displaying nested data structures as a form-based UI. DSUI stands for "Data Structure User Interface".

## Status
- Toy project

## Motivation
- Using it to avoid this:  
![Data in Cider](img/data_cider.png)
- And to get this instead:  
![Data in DSUI](img/data_DSUI.png)

## How it works
DSUI uses clojure.spec to "parse" an arbitrary data structure. The conformed data is used to generate the swing UI by calling a multimethod that polymorphically creates different types of UI elements.

A more detailed explanation can be found <a href="https://feierabendprojekte.wordpress.com/2016/09/11/generating-ui-for-arbitrarily-nested-data-structures/">here</a>.

## NextSteps
- Handle map entries. This would allow DSUI to display data conformed by specs, including DSUI's own specs.
- Handle heterogeneous lists containing scalar values alongside nested collections.

## Distant Future
- Attach to atom and let DSUI "watch" changes
- Flow-model like incremental UI updates
- Make UI editable (e.g. swap value to an atom)

## License
Distributed under the Eclipse Public License, the same as Clojure.
