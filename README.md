# DSUI
A Clojure tool for displaying nested data structures as a form-based UI. DSUI stands for "Data Structure User Interface".

## Status
- Toy project

## Motivation
Avoiding this:  
![Data in Cider](img/data_cider.png)

And getting this instead:  
![Data in DSUI](img/data_DSUI.png)

## Features
Displays
- maps as forms, nested data structures a tabbed panes
- sequential data (including vectors, lists and sets) as vertical tabbed panes with (0..n) as a title
- lists containing only scalar values as JLists
- Matrices (vector of vectors) as tables
- Lists of maps, that have the same keyset and contain scalar values only, as tables
- conformed data (labeled data) returned by specs as vertical tabbed panes with the label as a title. Thus it can be also used when writing specs

## How it works
DSUI uses clojure.spec to "parse" an arbitrary data structure. The conformed data is used to generate the swing UI by calling a multimethod that polymorphically creates different types of UI elements.

A more detailed explanation can be found <a href="https://feierabendprojekte.wordpress.com/2016/09/11/generating-ui-for-arbitrarily-nested-data-structures/">here</a>

## Next Steps
- Make displaying conformed data consume less space, e.g. vertical text in tab titles
- Handle heterogeneous lists containing scalar values alongside nested collections

## Distant Future
- Attach to atom and let DSUI "watch" changes
- Flow-model-like incremental UI updates
- Make UI editable (e.g. swap value to an atom)

## License
Distributed under the Eclipse Public License, the same as Clojure.
