# Introduction #

This page describes GUI in the developed reprotool project and shows mapping from the procasor project. Reader should have at least basic user experience with procasor project.

The goal of the GUI mapping is tight integration with eclipse platform and extension of the existing views and components. For example, standard console view will be used to show the output of the linguistic tools.

Table of contents:


# Use case step editor #

_Use case editor_ will be composed of the two main parts.  In the upper section of the editor will be tree table showing steps of the use case. Lower section will be occupied with area for editing of the step.

![http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/UseCaseEditor/UseCaseEditorWithManualSentenceAnalysisView.png](http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/UseCaseEditor/UseCaseEditorWithManualSentenceAnalysisView.png)

# Sentence analysis view #

_Sentence analysis view_ will enable user to select type of the selected use case step (action type). After the action type is selected the view will be populated with additional boxes. For example _Subject_, _Verb_, _Direct object_ and _Indirect object_ boxes will be added when user selects _Send_ action type.

![http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/ManualLinguisticAnalysis/ManualSentenceAnalysisView.png](http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/ManualLinguisticAnalysis/ManualSentenceAnalysisView.png)

Text in the box will correspond to the text marked in the _Use case step editor_.

![http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/ManualLinguisticAnalysis/BoxDetail.png](http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/ManualLinguisticAnalysis/BoxDetail.png)

## Future improvements ##

  * Allow multiple actions in one sentence. For example "Customer pays and System handles payment".
  * Allow zero or more representative objects. For example _"System sends information to the order system and customer care system"_. No representative object sentence example: _"User logs in"_.
  * Allow conditions, for example _"If information includes payment information, system validates credit card"_.

All these improvements are based on the paper [Improved Processing of Textual Use Cases: Deriving Behavior Specifications](http://d3s.mff.cuni.cz/publications/DrazanMencl-ImprovedUC-SOFSEM2007.pdf)

# Procasor models #

In procasor are use cases bound to (procase) use case model. But this
functionality seems to be important only for behavioral protocol generation.

Therefore it should be moved into _use cases models_, which will be included in _procase plugin_. Filtering of the use cases according to model may be added to the _view menu_ in _project overview_.

![http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/ProjectOverview/UseCasesModels.png](http://svn.codespot.com/a/eclipselabs.org/reprotool/trunk/documentation/screens/ProjectOverview/UseCasesModels.png)