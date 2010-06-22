Introduction
===

This tiny project is a plug-in for the ReCourse Learning Design Editor found by [http://tencompetence-project.bolton.ac.uk/ldauthor/index.html](http://tencompetence-project.bolton.ac.uk/ldauthor/index.html)

Learning Design is a formal descriptor of any teaching-learning flow. ReCourse is a particular editor for this descriptor. This plug-in uses the [Website Parse Template](http://en.wikipedia.org/wiki/Website_Parse_Template) technique to extract HTML contents from a set of web pages to create the textual elements of the Learning Design descriptor **in bulk**.

It is common that institute provides a CMS or any kind of HTML template for teachers to publish course materials onto the Internet. To convert such materials into Learning Design format via ReCourse editor, this plug-in facilitates the conversion by consuming a common parse template. Given that there is a common parse template available to the teachers, they can use (and re-use) that parse template to extract textual elements from those course materials in HTML. As a result, teachers do not have to copy-n-paste those written descriptions of course materials into the ReCourse editor.

Build
===

First, you need a source copy of ReCourse editor. Do,
	cvs -d:pserver:anonymous@tencompetence.cvs.sourceforge.net:/cvsroot/tencompetence login 
	cvs -z3 -d:pserver:anonymous@tencompetence.cvs.sourceforge.net:/cvsroot/tencompetence export org.tencompetence.jdom wp6/org.eclipse.epf.richtext wp6/org.tencompetence.imsldmodel wp6/org.tencompetence.ldauthor wp6/org.tencompetence.ldauthor.fedora wp6/org.tencompetence.ldauthor.opendock wp6/org.tencompetence.ldpublisher wp6/org.tencompetence.ldvisualiser wp6/org.tencompetence.qtieditor wp6/org.tencompetence.widgetadvert

Second, you need Eclipse 3.5.x and its plug-in: GEF 3.5.x, Zest 1.2.x

Third, replace "wp6/org.tencompetence.ldauthor/ldauthor.product" file of the cvs export in first step by the "wp6/org.tencompetence.ldauthor/ldauthor.product" file included in this git repo.

Forth, import this project and the projects exported from cvs in first step to your Eclipse workspace.

Fifth, follow this [blog post](http://aniefer.blogspot.com/2009/06/using-deltapack-in-eclipse-35.html) to include the Eclipse delta pack for building cross-platform packages.

Last, follow step #5 of the notes found by "wp6/org.tencompetence.ldauthor/build-notes.txt" of cvs export to build your binary packages.

