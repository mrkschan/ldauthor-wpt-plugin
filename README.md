Introduction
===

This tiny project is a plug-in for the ReCourse Learning Design Editor found by [http://tencompetence-project.bolton.ac.uk/ldauthor/index.html](http://tencompetence-project.bolton.ac.uk/ldauthor/index.html)

Learning Design is a formal descriptor of any teaching-learning flow. ReCourse is a particular editor for this descriptor. This plug-in uses the [Website Parse Template](http://en.wikipedia.org/wiki/Website_Parse_Template) technique to extract HTML contents from a set of web pages to create the textual elements of the Learning Design descriptor **in bulk**.

It is common that institute provides a CMS or any kind of HTML template for teachers to publish course materials onto the Internet. To convert such materials into Learning Design format via ReCourse editor, this plug-in facilitates the conversion by consuming a common parse template. Given that there is a common parse template available to the teachers, they can use (and re-use) that parse template to extract textual elements from those course materials in HTML. As a result, teachers do not have to copy-n-paste those written descriptions of course materials into the ReCourse editor.

All in all, this plug-in includes a simple Website Parse Template editor and a HTML extraction module.

Build
===

First, you need a source copy of ReCourse editor. Do,
	cvs -d:pserver:anonymous@tencompetence.cvs.sourceforge.net:/cvsroot/tencompetence login 
	cvs -z3 -d:pserver:anonymous@tencompetence.cvs.sourceforge.net:/cvsroot/tencompetence export -DNOW org.tencompetence.jdom wp6/org.eclipse.epf.richtext wp6/org.tencompetence.imsldmodel wp6/org.tencompetence.ldauthor wp6/org.tencompetence.ldauthor.fedora wp6/org.tencompetence.ldauthor.opendock wp6/org.tencompetence.ldpublisher wp6/org.tencompetence.ldvisualiser wp6/org.tencompetence.qtieditor wp6/org.tencompetence.widgetadvert

Second, you need Eclipse 3.5.x and its plug-in: GEF 3.5.x, Zest 1.2.x

Third, replace "wp6/org.tencompetence.ldauthor/ldauthor.product" file of the cvs export in first step by the "wp6/org.tencompetence.ldauthor/ldauthor.product" file included in this git repo.

Forth, import this project and the projects exported from cvs in first step to your Eclipse workspace.

Fifth, follow this [blog post](http://aniefer.blogspot.com/2009/06/using-deltapack-in-eclipse-35.html) to include the Eclipse delta pack for building cross-platform packages.

Last, follow step #5 of the notes found by "wp6/org.tencompetence.ldauthor/build-notes.txt" of cvs export to build your binary packages.

Run-time environment
===

- JRE 1.6+
- XULRunner 1.9.2+ see [https://developer.mozilla.org/en/XULRunner_1.9.2_Release_Notes](https://developer.mozilla.org/en/XULRunner_1.9.2_Release_Notes) for installation details (used by Website Parse Template editor only)

Credit
===

An ontology owl file found by "ldauthor.wpt/src/ldauthor/wpt/ontology/eume_imsld.owl" is a modified clone of this [file](http://ont-space.googlecode.com/svn/trunk/%20ont-space_v.1.0%20--username%20raquel.rebollo.fernandez/ont/translators/sld/EUME_Onto_Educational_Ontology.owl).

[NekoHTML parser](http://nekohtml.sourceforge.net/index.html) is used for HTML parsing under the [Apache 2.0 license](http://apache.org/licenses/LICENSE-2.0.txt).

Jena API is used for ontology processing under the license:

	© Copyright 2000, 2001, 2002, 2003, 2004, 2005, 2006, 2007, 2008, 2009 Hewlett-Packard Development Company, LP
	
	Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
	
	   1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
	   2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other materials provided with the distribution.
	   3. The name of the author may not be used to endorse or promote products derived from this software without specific prior written permission.
	
	THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 

