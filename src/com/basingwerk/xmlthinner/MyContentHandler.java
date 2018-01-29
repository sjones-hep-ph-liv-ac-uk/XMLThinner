package com.basingwerk.xmlthinner;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class MyContentHandler extends DefaultHandler {
    private Writer out;
    private StringBuffer textBuffer;
    private ArrayList<String> tags;
    private Boolean incl;
    private XMLReader xmlReader;

    public MyContentHandler(XMLReader xmlReader, Boolean incl, ArrayList<String> tags) {
        this.tags = tags;
        this.incl = incl;
        this.xmlReader = xmlReader;
        try {
            this.out = new OutputStreamWriter(System.out, "UTF8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public void startElement(String uri, String sName, String qName, Attributes attrs) throws SAXException {
        echoText();
        String eName = sName;
        if ("".equals(eName)) {
            eName = qName;
        }
        Boolean listed = isInTagList(attrs, this.tags);
        if (listed && !incl) {
            xmlReader.setContentHandler(new IgnoringContentHandler(xmlReader, this));
        } else if (!listed && incl) {
            xmlReader.setContentHandler(new IgnoringContentHandler(xmlReader, this));
        } else {
            emit("<" + eName);
            if (attrs != null) {
                for (int i = 0; i < attrs.getLength(); i++) {
                    String aName = attrs.getLocalName(i);
                    if ("".equals(aName))
                        aName = attrs.getQName(i);
                    emit(" ");
                    emit(aName + "=\"" + attrs.getValue(i) + "\"");
                }
            }
            emit(">");
        }
    }

    private Boolean isInTagList(Attributes attrs, ArrayList<String> tags) {
        int attributeLength = attrs.getLength();
        for (int i = 0; i < attributeLength; i++) {
            String attrName = attrs.getQName(i);
            if ("name".equalsIgnoreCase(attrName)) {
                String name = attrs.getValue(i);
                for (String s : tags) {
                    if (name.equals(s)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void endElement(String uri, String sName, String qName) throws SAXException {
        echoText();
        String eName = sName;
        if ("".equals(eName))
            eName = qName;
        emit("</" + eName + ">");
    }

    public void characters(char[] buf, int offset, int len) throws SAXException {
        String s = new String(buf, offset, len);
        if (textBuffer == null) {
            textBuffer = new StringBuffer(s);
        } else {
            textBuffer.append(s);
        }
    }

    private void echoText() throws SAXException {
        if (textBuffer == null)
            return;
        String s = "" + textBuffer;
        emit(s);
        textBuffer = null;
    }

    private void emit(String s) throws SAXException {
        try {
            out.write(s);
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }
}
