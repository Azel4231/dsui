# DSUI
A Clojure tool for displaying arbitrary, nested data structures as a read-only, form-based UI. DSUI stands for "Data Structure User Interface".

## Motivation

1. Being tired of writing UI code.

1. Avoiding this:  
![Data in Cider](img/data_cider.png)

1. And getting this instead:  
![Data in DSUI](img/data_DSUI.png)

## Usage

```clojure
(dsui.swing/dsui any-data)
```

displays the data as a form-based UI.

```clojure
(dsui.swing/conform-ui ::any-spec any-data)
```

shows how the data conformed to the spec. Useful for specs with choices (via s/or) or labeled values (vis s/cat). If the data does not conform, displays the explanation (explain-data) as a ui.

## Basic Features
Displays:
- maps as forms, nested data structures a tabbed panes
- sequential data (including vectors, lists and sets) as vertical tabbed panes with (0..n) as a title
- lists containing only scalar values as JLists
- Matrices (vector of vectors) as tables
- Lists of maps, that have the same keyset and contain scalar values only, as tables
- conformed data (labeled data) returned by specs as vertical tabbed panes with the label as a title. Thus DSUI can be also used to see how things conformed when writing specs

Does not support:
- a single scalar value

Not suited for:
- graph-like data

## How it works
DSUI uses clojure.spec to "parse" an arbitrary data structure. The conformed data is used to generate the swing UI by calling a multimethod that polymorphically creates different types of UI elements.

More detailed explanations can be found <a href="https://feierabendprojekte.wordpress.com/2016/09/11/generating-ui-for-arbitrarily-nested-data-structures/">here</a> and <a href="https://feierabendprojekte.wordpress.com/2017/06/12/conforming-conformed-data/">here</a>

## Next Steps
- Decomplect processing pipeline: move parts that are agnostic of the UI framework into a separate step so they can be reused for other UI frameworks.
- Migrate to <a href="https://github.com/halgari/fn-fx">fn-fx</a>

## Distant Future
- Attach to atom and let DSUI "watch" changes
- Make UI editable (e.g. swap value to an atom)

## License
Distributed under the Eclipse Public License, the same as Clojure.
