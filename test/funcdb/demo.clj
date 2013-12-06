(ns funcdb.demo
  "This is my scratch-pad for experimenting with the core functions
  without cluttering the core file" 
  (:use funcdb.core))

; generally useful values
(def attribs {:name "bob"
              1 "the loneliest number"})


; try identify-sequential

(def idseq (identify-sequential 0))
(def idseq-copy idseq)
(idseq attribs)
(idseq-copy attribs)
(idseq attribs)
(idseq-copy attribs)
(idseq attribs)
(println "[identify-sequential] Should see 5: " (idseq-copy attribs)) 

