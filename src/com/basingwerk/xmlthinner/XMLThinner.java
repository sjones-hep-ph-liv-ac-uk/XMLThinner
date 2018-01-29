package com.basingwerk.xmlthinner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

public class XMLThinner {
    public static void main(String[] args) throws Exception {
        ArrayList<String> tags;
        Boolean incl = null;
        if (args.length != 3) {
            System.err.println("Usage: cmd <incl | excl> tagfilename xmlfilename");
            System.exit(1);
        }
        if (args[0].equalsIgnoreCase("incl")) 
            incl = true;
        else if (args[0].equalsIgnoreCase("excl")) 
                incl = false;
         else {
                System.err.println("Usage: cmd <incl | excl> tagfilename xmlfilename");
                System.exit(1);
        }
        tags = readTags(args[1]);
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser sp = spf.newSAXParser();
            XMLReader xreader = sp.getXMLReader();
            xreader.setContentHandler(new MyContentHandler(xreader, incl, tags));
            xreader.parse(args[2]);
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
}
