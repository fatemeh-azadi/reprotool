# 2012 (todo) #
  * reprotool.model.ecore/model/swproj - the following classes should be in a different package/model: CounterExample, Step, UseCaseTransition
  * Cleaning up / extending / refactoring model of annotations

# 2011-08-05 #
**Participants:** Krajicek, Tomori, Fiala, Vinarek

**Topics discussed:**
  * Discussed EMF format of the use case step.
  * J. Krajicek will update editor to use updated EMF use case step model ([issue 34](https://code.google.com/p/reprotool/issues/detail?id=34)) and add content assist ([issue 35](https://code.google.com/p/reprotool/issues/detail?id=35), [issue 36](https://code.google.com/p/reprotool/issues/detail?id=36)).
  * J. Vinarek will simplify saving of the model ([issue 37](https://code.google.com/p/reprotool/issues/detail?id=37)) and refactoring of the model.
  * O. Fiala promised intensive work on linguistic use case step analysis ([issue 32](https://code.google.com/p/reprotool/issues/detail?id=32), [issue 33](https://code.google.com/p/reprotool/issues/detail?id=33)).
  * R. Tomori promised to work on project with start of the September. Has pending tasks with LTS.

# 2011-07-29 #
**Participants:** Simko, Krajicek, Vinarek

**Topics discussed:**
  * Need of EMF model for exchange between editor and linguistic tools.
  * Implemented use case step editor with ## and %% tag notation.

# 2011-04-08 #
**Participants:** Simko, Fiala, Krajicek, Vinarek

# 2011-04-01 #
**Participants:** Simko, Tomori, Krajicek, Vinarek

# 2011-03-25 #
**Participants:** Pop, Simko, Tomori, Krajicek, Vinarek

**Topics discussed:**
  * some derived properties in the EMF model should be generated automatically (useCases reference vs requirements reference)
  * At the moment, there is no need to keep hierarchy of actors
  * We have designed an editor used by business analysts for editing tagged texts linked to the model. The editor should use nested tags. Every tag can be marked as deletable, editable, movable within container.
  * Notification among UCStep view, parse tree view and UC Editor.

# 2011-03-18 #
**Participants:** Pop, Tomori, Vinarek

**Sentence negations**
  * Until next week report about existing tools (Wordnet, NetEx tool)
  * Prepare results on example sentences from previous meeting

**Models:**
  * Specification model - divide to more files (dir for Actors, for Use-case...,   related by URI links)

**GUI**
  * ree view finished

# 2011-03-11 #
**Participants:** Pop, Simko, Krajicek, Vinarek, Tomori

# 2011-03-04 #
**Participants:** Pop, Simko, Krajicek, Vinarek
  * editor should not completely hide the tags (use different colors)
  * model elements represent some concepts in the SE domain. They have multiple tags and will receive notifications if corresponding text elements changes, is deleted or is moved. The class representing the concept should implement such an interface.
  * classification techniques for identification of SE artefacts
  * we should remember the feedback from user about the classification

# 2011-02-25 #
**Participants:** Simko, Vinarek, Tomori, Krajicek
  * roles assigned for Tomori and Krajicek
  * everybody should be able to run the prototype
  * tasks will be assigned using the issue tracker at the end of every meeting
  * plugins discussed:
    * parsed sentence tree view
    * token view plugin
    * token wizard
    * linguistic tools (single plugin as a prototype)
    * use-case steps editor plugin
    * use-case navigator plugin
    * editor: split text to sections/paragraphs/requirements/use-cases/uc-steps
    * editor: text with hidden tags
    * merge/compare tagged texts

# 2011-02-18 #
**Participants:** Pop, Vinarek, Tomori, Fiala

# 2011-02-11 #
**Canceled**

# 2011-01-28 #
**Participants:** Simko, Pop, Krajicek, Vinarek

**Topics discussed:**
  * gui discussion
  * linguistic tools as a cloud service
  * linguistic tools as an optional component
  * how to link textual documents with generated model