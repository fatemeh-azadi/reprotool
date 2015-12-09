# Introduction #

This page describes an ongoing discussion about the meta-model that describes all the aspects of the ReProTool project.

# Persistence and querying for EMF #
  * [CDO](http://www.eclipse.org/cdo/) + [Hibernate](http://www.hibernate.org/)
  * Teneo
  * ModelQuery
  * EMF Triple
  * [EMF Index](http://www.eclipse.org/emfindex/) ([EmfWiki](http://wiki.eclipse.org/EmfIndex))

# Other useful technologies #
  * http://www.eclipse.org/ajdt/xref/

# Parts of the Meta-Model #

The whole ReProTool meta-model consists of several sub-meta-models divided into separate packages. Each package should encapsulate a certain aspect of the intended application e.g. Input, Static Structure, Behaviour...

The following picture provides an overview of the main parts of the meta-model. In the first phase, we would like to limit the number of model elements. For example, we only consider plain text files and java source files as an input for the first prototype. Future releases of the application should, however, support more formats.

![http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/reprotool-model-overview.png](http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/reprotool-model-overview.png)

## The Core ##
The core model describes the concept of traceability links which is used throughout the whole transformation pipeline. Outcomes of transformation tools will be linked to the original elements using traceability links.

## Files and Documents ##
Represents input files. In the initial phase, only plan text files and java source files will be supported.

## Structural Artefacts ##
### Generic Document Structure ###
Textual documents will be split into sections and further to paragraphs. Some paragraphs might be later identified as requirements or even as a special type of requirements such as use-cases. The identification may be automatic or manual.

### Abstract Syntax of Source Code ###
In the initial phase, only Java source files will be parsed and the information about classes and methods will be extracted. These artefacts will be later connected with requirements.

## Software Engineering Artefacts ##
Contains sub-models describing different aspects of the software engineering domain:
  * Requirements Model
  * Domain Model
  * Behaviour Model
  * Component Model

### Requirements model ###
Requirements can be either a generic text paragraphs or can be further processed, such as Use-cases

TODO...