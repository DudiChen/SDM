package jaxb;

import exception.XMLException;
import jaxb.generated.*;

import javax.management.modelmbean.XMLParseException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileNotFoundException;

public class JaxbHandler {
    private final static String JAXB_XML_CLASSES_PACKAGE_NAME = "jaxb.generated";
    private final static String INVALID_XML_JAXB_LOAD_MESSAGE = "Invalid XML Format";
    private final static String INVALID_XML_DUPLICATED_ITEM_ID_MESSAGE = "Duplicated Unique ID for Items";
    private final static String INVALID_XML_DUPLICATED_STORE_ID_MESSAGE = "Duplicated Unique ID for Store";


    private boolean isXMLContentValid = false;

    public SuperDuperMarketDescriptor extractXMLData(File xmlFile) throws XMLException, FileNotFoundException, XMLParseException {

        SuperDuperMarketDescriptor sdMarketDescriptor = null;
        try {
            sdMarketDescriptor = deserializeFrom(xmlFile);
        } catch (JAXBException ex) {
            throw new XMLException(INVALID_XML_JAXB_LOAD_MESSAGE);
        }
        return sdMarketDescriptor;
    }



    private SuperDuperMarketDescriptor deserializeFrom(File xmlFile) throws JAXBException {
//        private SuperDuperMarketDescriptor deserializeFrom(InputStream inStream) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(JAXB_XML_CLASSES_PACKAGE_NAME);
        Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
        return (SuperDuperMarketDescriptor) unmarshaller.unmarshal(xmlFile);
    }
}

