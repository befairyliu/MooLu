package com.moolu.framework.util;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nanan on 2/3/2015.
 */
public class XML2Feature extends DefaultHandler{

    //TODO for DefaultHandler class
    //This variable is used to store the results after conversion
    private List<AppSupportFeature> features;

    private AppSupportFeature feature;

    private StringBuffer buffer = new StringBuffer();

    public List<AppSupportFeature> getFeatures(){
        return features;
    }

    @Override
    public void startDocument() throws SAXException {
        features = new ArrayList<AppSupportFeature>();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        if(localName.equals("feature")){
            feature = new AppSupportFeature();
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if(localName.equals("feature")){
            features.add(feature);
        } else if(localName.equals("name")){
            feature.setName(buffer.toString().trim());
            buffer.setLength(0);
        } else if(localName.equals("isSupported")){
            feature.setSupported(Boolean.parseBoolean(buffer.toString().trim()));
            buffer.setLength(0);
        }
        super.endElement(uri, localName, qName);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        buffer.append(ch,start,length);
        super.characters(ch, start, length);
    }
}
