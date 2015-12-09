Table of contents:


# General resources #
  * Stanford links http://nlp.stanford.edu/links/statnlp.html
  * Software resources http://www-a2k.is.tokushima-u.ac.jp/member/kita/NLP/nlp_tools.html

# Linguistics tools #

Suggested and used tools for linguistics analysis. We need to obtain a sentence structure and base form of all words. This process is composed of four parts, each is described below.

## Tokenizer ##

A basic tool for string splitting. Tokenizer separates numbers from letters, separates words and deals with a punctuation.

| Input | Natural language sentence; negation detection |
|:------|:----------------------------------------------|
| Output | Array of tokens (eg. words, numbers)          |

### Tools ###
  * _The EGYPT toolkit - perl tokenizer_

Part of  a statistical machine translation toolkit.
http://www.clsp.jhu.edu/ws99/projects/mt/

  * LingPipe - tokenizer (added in version 4.0.1)

Good looking Java toolkit for processing text using computational linguistics.
http://alias-i.com/lingpipe/

  * Simple Java tokenizer for regular expressions

http://introcs.cs.princeton.edu/72regular/Tokenizer.java.html

## Tagger ##
Usually tool just for part-of-speech tagging. Identifies basic linguistic category for each word.

| Input | Array of tokens |
|:------|:----------------|
| Output | Array of POS tagged tokens (eg. adjectives, verbs) |

### Tools ###
  * _A Maximum Entropy Model for Part-Of-Speech Tagging_

Java implementation of this tagger - http://www.inf.ed.ac.uk/resources/nlp/local_doc/MXPOST.html

Another wrapper - http://godel.stanford.edu/public/doc-versions/util/doc/api/csli/util/nlp/postag/MXPOST.html as a part of basic tools (/util) at the Center for the Study of Language and Information at Stanford University http://godel.stanford.edu/twiki/bin/view/Calo/CaloSoftwareMainPage

Penn Treebank Tags - explanation of all tags http://bulba.sdsu.edu/jeanette/thesis/PennTags.html#RB

  * _Stanford Log-linear Part-Of-Speech Tagger_

Java implementation of the log-linear part-of-speech taggers
http://nlp.stanford.edu/software/tagger.shtml

## Parser ##

Mainly a statistical parser. This tool is used to discover sentence structure, usually written as a syntactic tree. Part of a syntactic analysis.

| Input | Array of POS tagged tokens |
|:------|:---------------------------|
| Output | Parse trees of each sentence |

### Tools ###
  * M. Collins parsing model
http://people.csail.mit.edu/mcollins/code.html

  * Multilingual Statistical Parsing Engine
in Java - based on Michal Collins's parser http://www.cis.upenn.edu/~dbikel/software.html#stat-parser

  * MaltParser
modern data-driven dependency parser in Java http://maltparser.org/

  * RASP: Robust Accurate Statistical Parsing toolkit
bigger parser - http://www.informatics.sussex.ac.uk/research/groups/nlp/projects/rasp.php

## Morphological analyser ##

Analysis and description of the structure of morphemes. For our purpose is sufficient obtaining lemmas (base forms). Eg. _have_ for _had_. Lemmatizers are finding only lemmas, stemmers are for finding stems (eg. _bug_ for _debugging_).

| Input | Parse trees of each sentence |
|:------|:-----------------------------|
| Output | Lemma for each word          |

### Tools ###
  * _Morphological tool-set_
Developed by G. Minnen at the University of Sussex http://www.informatics.susx.ac.uk/research/groups/nlp/carroll/morph.html

  * _mate-tools_ (anna)
Tools set including lemmatizer with models http://code.google.com/p/mate-tools/

  * _Lttoolbox-java_
Part of Apertium project (translator) - java implementation http://wiki.apertium.org/wiki/Lttoolbox-java

## General tools ##
  * _Core NLP_
Stanford suite of Core NLP Tools (allmost all operations)
http://nlp.stanford.edu/software/corenlp.shtml

  * _XTAG_
Tool for operations with Tree Adjoining Grammars http://www.cis.upenn.edu/~xtag/

# Sentence negation #
| Keywords: | negative-positive conversion |
|:----------|:-----------------------------|

  1. Identification of a negation in sentence
  1. Dealing with negation

- antonyms links in a corpora

  * WordNet http://wordnet.princeton.edu/
Testing WordNet online - http://wordnetweb.princeton.edu/perl/webwn

  * MIT Java Wordnet Interface (JWI) http://projects.csail.mit.edu/jwi/

## Tools & Algorithms ##

  * Sentiment Analysis with Python NLTK Text Classification - online tool -http://text-processing.com/demo/sentiment/

  * Negation Detection Processes - basic algorithm - http://blog.typeslashcode.com/voxpop/2010/02/negation-detection-processes/

# Reference #

## Tools ##

  * Ying He, Mehmet Kayaalp: _A Comparison of 13 Tokenizers on MEDLINE_ http://www.lhncbc.nlm.nih.gov/lhc/docs/reports/2006/tr2006003.pdf

  * Adwait Ratnaparkhi: _A Maximum Entropy Model for Part-Of-Speech Tagging_ http://www.ldc.upenn.edu/acl/W/W96/W96-0213.pdf

## Sentence negation ##

  * Sanda Harabagiu, Andrew Hickl and Finley Lacatusu: _Negation, Contrast and Contradiction in Text Processing_ http://www.aaai.org/Papers/AAAI/2006/AAAI06-120.pdf

  * SSergey Goryachev, Margarita Sordo, Qing T. Zeng, Long Ngo: _Implementation and Evaluation of Four Different Methods of Negation Detection_ https://www.i2b2.org/software/projects/hitex/negation.pdf

  * Katsura, Y., Matsumoto, K., Ren, F.:_Flexible English writing support based on negative-positive conversion method_ http://ieeexplore.ieee.org/xpl/freeabs_all.jsp?arnumber=5587778

  * Pradeep G. Mutalik, MD, Aniruddha Deshpande, MD, and Prakash M. Nadkarni, MD: _Use of General-purpose Negation Detection to Augment Concept Indexing of Medical Documents_ http://www.ncbi.nlm.nih.gov/pmc/articles/PMC130070/


