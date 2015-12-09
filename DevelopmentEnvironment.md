Note:The following steps were tested on _Ubuntu 10.04 LTS Lucid Lynx, kernel 2.6.32-27-generic i686 with 4GiB RAM_.

Table of contents:


# Summary #
  * Install Eclipse
  * Install XText (contains MWE2)
  * Install Window Builder Pro plugin
  * Install GEF Zest Visualisation Toolkit plugin
  * (Optional) Install Subclipse SVN plugin
  * Checkout the reprotool SVN repository
  * Run REPROTOOL eclipse application


# Installation Details #

## Eclipse ##
Download and install Eclipse Indigo 3.7 from http://eclipse.org/downloads/, choose
_Eclipse Modeling Tools (includes Incubating components)_.
(download zip/gz, unpack to dir e.g. "eclipse-reprotool")

## XText 2.1+ (contains MWE2) ##
In Eclipse Menu: `Help -> Install Modeling Components`, search for XText and click install. The MWE2 plugins are needed for code generation separating hand-written code from genrated code.
This approach is inspired by Moritz Eysholdt's presentation at ECLIPSECON'11: http://www.slideshare.net/meysholdt/codegeneration-goodies.

We also use the Xtend language for transformation of models.

## Window Builder Pro (optional) ##
This step is optional, Window Builder Pro is used for visual development of GUI parts (dialogs, perspectives,...).
Install the plugin from update site (`Help -> Install new software`), enter location http://dl.google.com/eclipse/inst/d2wbpro/latest/3.7 ([also described here](http://code.google.com/intl/cs-CZ/javadevtools/download-wbpro.html))

Select `SWT Designer Core`, `SWT Designer SWT_AWT Support` and `WindowBuilder GroupLayout Support`.

If you plan to develop GUI, select also `SWT Designer` (these packages are optional). Other modules shouldn't be needed.

## GEF Zest Visualization Toolkit ##
This plugin is required for rendering graphs.
Open `Help->Update` select `http://download.eclipse.org/tools/gef/updates/milestones/` update site and search for "zest". install package `Graphical Editing Framework Zest Visualization Toolkit` version 1.3.0 or newer.

## EMF plugin update ##
Eclipse after installation of required plugins updates also EMF plugins (tested 12. 11. 2011). Unfortunately this update contains bug (see [issue 40](https://code.google.com/p/reprotool/issues/detail?id=40)).
Steps to remove the bug:
  1. [Download maintanance build M201110311121](http://www.eclipse.org/modeling/download.php?file=/modeling/emf/emf/downloads/drops/2.7.x/M201110311121/emf-xsd-Update-M201110311121.zip) from [EMF downloads section](http://www.eclipse.org/modeling/emf/downloads/?project=emf).
  1. In Eclipse select `Help -> Install New Software ... -> Add... -> Archive...` and in opened dialog select downloaded zip archieve.
  1. Check `EMF - Eclipse Modeling Framework Core Runtime`, finish the update and restart Eclipse. http://svn.codespot.com/a/eclipselabs.org/reprotool/wiki/img/emf-bugfix/emf-bugfix-install.PNG

## Subclipse SVN plugin (optional) ##
This step is optional, you could easily work with command-line-based SVN client.
~~To install Subclipse go to `Help -> Eclipse Marketplace` and search for "Subclipse".~~
Install Subclipse from update site http://subclipse.tigris.org/update_1.6.x (`help -> install new software`).

Subclipse requires native JavaHL library. This can be installed on Ubuntu using: `sudo apt-get install libsvn-java`

**Note**: If imported projects are not recognized as connected to SVN, try checkout with older version of the svn client. Problem occured with subclipse 1.6.x version and [tortoise svn](http://tortoisesvn.net/) version 1.7.x. Switch to older 1.6.x tortoise svn client solved the problem.

## WireframeSketcher plugin (optional) ##
This step is optional, [WireframeSketcher](http://wireframesketcher.com) is tool for creation of GUI wireframes (see images in [ProcasorToEclipseGuiMapping](ProcasorToEclipseGuiMapping.md)).
To install WireframeSketcher go to `Help -> Install new software` and add http://wireframesketcher.com/updates update site.

Import eclipse project with sketches into workspace from `documentation/ReproToolSketchProject`. Directory `documentation/screens` is intended for batch export of sketches to images.

**Note**: WireframeSketcher is paid commercial software freeware, trial version was used to create GUI wireframes. Look at http://wireframesketcher.com/install.html for limitations and legal notices.

## Maven Integration for Eclipse (optional) ##
Maven with [Tycho](http://www.eclipse.org/tycho/) plugin is used for build of the eclipse plugins on the build server. You don't need this plugin for development but it's useful to ensure that your plugin is  correctly set for headless build.

To install Maven plugin go to `Help -> Eclipse Marketplace` and search for "maven integration for eclipse" and install the found plugin.

To build all reprotool plugins right-click on `pom.xml` in `reprotool.maven.aggregator` and select `Run as -> Maven install`.
Don't be scared that initial build takes long time and downloads many dependencies.

More about Maven + Tycho:

Tycho tutorial: http://www.eclipse.org/tycho/documentation.php

Sources of project Spray (uses extensively EMF, Xtext, Xtend, reprotool Maven configuration is inspired by this project):
http://code.google.com/a/eclipselabs.org/p/spray/

# Project Setup Details #
The whole SVN structure can be checked out manually from SVN to an external directory outside of the Eclipse workspace. Once checked out, the reprotool sub-projects can be separately imported to your Eclipse workspace using `File -> Import -> General -> Existing Projects into Workspace`. This approach might seem more complicated but it gives you additional flexibility and can also be easily combined with the Subclipse SVN plugin.

Checkout project sources as described on
[Checkout](http://code.google.com/a/eclipselabs.org/p/reprotool/source/checkout) page.

Pick a sub-projects to import, i.e. `modules/ide` and `modules/ReprotoolModel`. Import projects to your workspace as described above: `File -> Import -> General -> Existing Projects into Workspace`. Navigate to the sub-projects directory (e.g. `modules/ide`). Also make sure the _Copy projects into workspace_ checkbox **is not checked** because we **don't want to copy** the project's content to the workspace, we want to modify the original files in the checked-out directory.

In `reprotool.fm.nusmv` project right click on `nusmvtools-dependencies.psf` and select `Import Project Set...`. This step will checkout svn projects from external NuSVN repository.

# Generating source files from model #
In `reprotool.model` project right click on `src/reprotool.model/reprotool.model.mwe2` file, select `Run As -> MWE2 Workflow`. Model source files will be generated.

Double click on `model/reprotool.model.genmodel` and in opened editor right click on `Reprotool` root node. Select `Generate Edit Code` and `Generate Editor Code`.

# Running the application #
Try launching the plugin: Double click on `plugin.xml` and push play button in upper right corner. New instance of eclipse should start, inside this started IDE select `Window -> Open perspective -> Other -> reprotool`.

## OutOfMemory error ##
If you encounter OutOfMemoryError, go to `Run -> Run Configurations -> Eclipse Application` and select configuration which you used for launch. Select `Arguments` tab and add `-XX:MaxPermSize=512m` into `VM arguments` area.

# Product export #
Product is currently based on plugins (feature based product caused problems). To export the product for multiple platforms:
  1. Follow instructions from http://ugosan.org/eclipse-rcp-delta-pack/ and install delta pack from http://download.eclipse.org/eclipse/downloads/drops/R-3.7.1-201109091335/index.php#DeltaPack .
  1. Open `org.eclipse.reprotool.product/ReprotoolIDE-plugin-based.product` and run export wizard