package com.basingwerk.xmlthinner;
import java.io.*;
import java.util.ArrayList;
import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
@SuppressWarnings("unused")

public class IgnoringContentHandler extends DefaultHandler {

    private int depth = 1;
    private XMLReader xmlReader;
    private ContentHandler contentHandler;

    public IgnoringContentHandler(XMLReader xmlReader, ContentHandler contentHandler) {
        this.contentHandler = contentHandler;
        this.xmlReader = xmlReader;
    }

    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        depth++;
    }

    public void endElement(String uri, String localName, String qName) throws SAXException {
        depth--;
        if (0 == depth) {
            xmlReader.setContentHandler(contentHandler);
        }
    }
}
