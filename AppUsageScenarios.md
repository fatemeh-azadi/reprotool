# Introduction #

Here, we provide a list of usage scenarios of our REPROTOOL application. The list is intended to describe situations from the perspective of different stakeholders of a developed software system.

# Target audience #

Every software project has its players. For our purposes, we consider just two types of players in a software development process.

  * **Business Analysts**: They are translating the problem into a requirements specification using a more explicit English. They tend to be more detail oriented. Sometimes they may understand a modeling language, but more often not.

  * **Software Engineers:** These are the people who actually write and test the code. In order to fulfil the needs of domain experts, they need an unambiguous specification. It is also important to keep tracks between the requirements and software artefacts, especially in the later project phases when the requirements will naturally change.

The reader may have noticed that we have deliberately omitted the roles of **Domain Experts** and **Software Architects**. This is because REPROTOOL is focused at the dialogue between Business Analysts and Software Engineers.


# Eclipse Projects #

REPROTOOL as a plug-in can be installed to an existing Eclipse IDE, such as the "Eclipse IDE for Java developers". It will enrich the platform with various tools for handling requirements-engineering artefacts. For this purpose, the tool provides a special Eclipse perspective composed of views and editors related to the requirements engineering tasks.

It should be noted that existing projects within a workspace can be used by our tool. The idea is that users should be able to incrementally transform their existing projects using our tool to projects specification documents are linked with code artefacts.

Usually, a larger software project is split into multiple projects (directories) within an Eclipse workspace. Specification documents (and  adjacent metadata) might be stored within existing projects or one might decide to create a separate project that gathers all documents and adjacent metadata.

  * REPROTOOL can therefore be used for aggregating specification documents from multiple projects into one unit.

# Documents and tagging #

We presume that specification documents are written by business analysts using their favourite editor (such as OpenOffice Writer) and then handed over to software engineers. Using REPROTOOL, these documents are imported to the workspace to be integrated into the development process. Not all of the paragraphs within the documents are interesting from the software engineering point of view. Some paragraphs may provide an introductory text to a chapter while others contain important artefacts, such as use-case steps. After a document is imported into REPROTOOL, it is semi-automatically tagged, i.e., parts of the text are marked with tags that define the boundaries of processed artefacts. The result is a document that is similar to the original specification, however, parts of the text are replaced by generated content.

If the original specification needs to be refined, the business analyst is asked to use the tagged version of the document rather than the original one. This way, he also verifies whether the generated version conforms to his original intentions. Even though the analyst now holds a tagged version of the document, he can edit the text without worrying about the hidden tags. Common changes within the text, such as reordering of sentences, deleting or editing, can be easily identified and traced using an XML compare tool.

# Extraction of concepts #
  * extraction of SE concepts from the documents into models (semi-automatic)

# TODO #
  * indexing the documents using the model (semi-automatic)
  * connecting fragments of the specification with code elements
  * iterative improvement of the original specification (parts of the text replaced by generated text while at the same time keeping the rest of the text unmodified)
  * to help writing unambiguous specification