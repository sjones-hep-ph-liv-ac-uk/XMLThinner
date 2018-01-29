package com.basingwerk.xmlthinner;

import java.io.*;
import java.util.ArrayList;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

@SuppressWarnings("unused")

public class Demo1 extends DefaultHandler {
    static private Writer out;
    StringBuffer textBuffer;
    static ArrayList<String> tags;
    static Boolean incl;
    
    public static void main(String[] args) {
        if (args.length != 3) {
            System.err.println("Usage: cmd <incl | excl> tagfilename xmlfilename");
            System.exit(1);
        }
        if (args[0].equalsIgnoreCase("incl")) {
            incl = true;
        }
        else {
            if (args[0].equalsIgnoreCase("excl")) {
                incl = false;
            }
            else {
                System.err.println("Usage: cmd <incl | excl> tagfilename xmlfilename");
                System.exit(1);
            }
        }
        tags = readTags (args[1]);
        
        try {
            out = new OutputStreamWriter(System.out, "UTF8");
            DefaultHandler handler = new Demo1();
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            saxParser.parse(new File(args[2]), handler);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }
    

    private static ArrayList<String> readTags(String tagFile) {
        ArrayList<String> tags = new ArrayList<String>();
        try {
            File file = new File(tagFile);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                tags.add(line.trim());
            }
            fileReader.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        return tags;
    }

    public void startElement(String namespaceURI, String sName,  String qName,  Attributes attrs) throws SAXException {
        echoText();
        String eName = sName; 
        if ("".equals(eName)) {
            eName = qName; 
        }
        emit("<" + eName);
        if (attrs != null) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String aName = attrs.getLocalName(i); // Attr name
                if ("".equals(aName))
                    aName = attrs.getQName(i);
                emit(" ");
                emit(aName + "=\"" + attrs.getValue(i) + "\"");
            }
        }
        emit(">");
    }

    public void endElement(String namespaceURI, String sName, String qName   ) throws SAXException {
        echoText();
        String eName = sName; 
        if ("".equals(eName))
            eName = qName; 
        emit("<" + eName + ">");
    }

    public void characters(char buf[], int offset, int len) throws SAXException {
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

    private void nl() throws SAXException {
        String lineEnd = System.getProperty("line.separator");
        try {
            out.write(lineEnd);
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

    public void startDocument() throws SAXException {
        emit("<?xml version='1.0' encoding='UTF-8'?>");
        nl();
    }

    public void endDocument() throws SAXException {
        try {
            nl();
            out.flush();
        } catch (IOException e) {
            throw new SAXException("I/O error", e);
        }
    }

}
