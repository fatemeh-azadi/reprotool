# Introduction #

This page is intended to collect basic guidelines used across whole reprotool project.



# Error handling in eclipse (displaying + logging) #

Eclipse has own mechanism to display and log exceptions.

Main parts are:
  * `IStatus` - Wrapped information (message, exception) with severity.
  * `StatusManager` - Class accepting statuses and hints how to handle them. In the example below the status is passed with flag `StatusManager.BLOCK | StatusManager.LOG` thus it will be logged to the file (and showed in the _Problems view_) and modal dialog will be shown to the user.

Example (from [ProjectWizard.java]):

```
IStatus status = new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Error during project initialization", exception);
StatusManager.getManager().handle(status, StatusManager.BLOCK | StatusManager.LOG);
```

Log file is located in `<workspace-dir>\.metadata\.log`

More about eclipse error handling can be found at
http://wiki.eclipse.org/Status_Handling_Best_Practices

# How to access resources bundled in plugins #
See http://www.vogella.de/blog/2010/07/06/reading-resources-from-plugin/

# Javadoc #
Minimal requirement is to have top level comment for each class with `@author` tag filled.
Desired situation is to have every public class/method/field
documented when work is finished.

| Hint: Comments can be inserted with shortcut `alt + shift + j` |
|:---------------------------------------------------------------|

# Code formatting #
Project `reprotool.ide` has associated code-formatting settings to unify formatting style (i.e. indenting with tabs vs spaces, line length, ...). Style defined by this settings should be "the right one", so that the whole project can be reformatted with it.

To prevent eclipse from formatting your code, surround the code with `@formatter` tags like this (this should be rather rare):

```
//@formatter:off
MyClass.method()
  .anotherMethod()
  .yetAnotherMethod();
//@formatter:on
```

Path to view/edit settings:
`Project -> Properties -> Java Code Style -> Formatter -> Edit`

| Hint: Code can be formatted with shortcut `ctrl + shift + f` |
|:-------------------------------------------------------------|

# Internationalization #

Externalized strings are placed by EMF code generator into into plugin.properties file. This how-to explains string externalization in non-generated EMF plugin.

## To externalize strings from java source files: ##

  1. Open `META-INF/MANIFEST.MF` file and add `"Bundle-Localization: plugin"` line. http://svn.codespot.com/a/eclipselabs.org/reprotool/wiki/img/string-externalization/add-line-to-manifest.PNG
  1. Right click on project, select _Source_ and _Externalize Strings..._
  1. In _Externalize Strings_ dialog change _"Key"_ of each externalized string to something meaningful.
  1. Click on _"Configure"_, change _Class name_ to class containing plugin activator. http://svn.codespot.com/a/eclipselabs.org/reprotool/wiki/img/string-externalization/configure-accessor-class.PNG
  1. Change _Property file name_ to `plugin.properties`.
  1. Click on _"Configure"_ again and change _"Substitution pattern"_ to `INSTANCE.getString(${key})`.
  1. In _Externalize Strings_ dialog click on _Next_ and _Finish_.
  1. Move generated `plugin.properties` to plugin root folder.
  1. Open `plugin.xml`, go to _"Build"_ tab and check `"plugin.properties"` in _Binary Build_. http://svn.codespot.com/a/eclipselabs.org/reprotool/wiki/img/string-externalization/check-plugin-properties.PNG
  1. Try to run plugin(s) with new configuration.

## To externalize strings from bundle files: ##

Open `plugin.xml` and in _"Overview"_ tab click to _"Externalize Strings Wizard"_, select strings and finish dialog. http://svn.codespot.com/a/eclipselabs.org/reprotool/wiki/img/string-externalization/externalize-bundle-strings.PNG

# Eclipse/EMF databinding #
Data binding is mechanism for data synchronization between model and view. Using this framework is preferred over using property change listeners which may cause infinite loops or memory leaks when used incorrectly.

Note that WindowBuilder Pro v1.0.0 does not have support for EMFEditProperties which interact with domain model trough EMF commands. Therefore GUI designer is not used
to generate a binding and it is created manually.

## Useful links ##

http://www.eclipsepluginsite.com - tutorial about eclipse plugin development.

http://code.google.com/javadevtools/wbpro/features/swt/data_binding/index.html - eclipse databinding in WindowBuilder Pro

http://www.vogella.de/articles/EclipseDataBinding/article.html - eclipse databinding tutorial.

http://tomsondev.bestsolution.at/2009/06/06/galileo-improved-emf-databinding-support/ - series of blog posts about new EMF binding using EMF commands to interact with domain model.

http://code.google.com/javadevtools/wbpro/wizards/databinding/automatic_0.html - Automatic creation of dialogs (panels, ...) with databinding.

## How to display a launch configuration in "favorites" menu ##
  * Open menu _Run -> Run Configurations..._
  * Select an existing Launch Configuration or create a new one
  * Select the _Common_ tab
  * Select _Shared file_ and insert path to the directory where the configuration will be stored, such as `/myproject/.launch`
  * Adjust the checkboxes in _Display in favorites menu_ (_Run_, _Debug_ or both)

## Accessing eclipse plug-in extensions from code ##
```
  IExtensionPoint[] extPoints =
    Platform.getExtensionRegistry().getExtensionPoints("my.plugin.id");

  for (IExtensionPoint extPoint : extPoints) {
    IExtension[] extensions = extPoint.getExtensions();
    // do simething with extensions
  }
```