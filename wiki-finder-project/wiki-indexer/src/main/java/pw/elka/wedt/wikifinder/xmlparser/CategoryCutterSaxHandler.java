package pw.elka.wedt.wikifinder.xmlparser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class CategoryCutterSaxHandler extends DefaultHandler {
	private static final Logger LOG = Logger.getLogger(CategoryCutterSaxHandler.class);
	private StringBuilder content;
	private XMLStreamWriter out;
	private boolean writeToOut = false;

	public CategoryCutterSaxHandler(String outputFileName)
			throws FileNotFoundException, UnsupportedEncodingException,
			XMLStreamException, FactoryConfigurationError {
		content = new StringBuilder();
		OutputStream outputStream = new FileOutputStream(new File(
				outputFileName));
		
		XMLOutputFactory streamWriterFactory = XMLOutputFactory.newInstance();
		streamWriterFactory.setProperty("escapeCharacters", false);
		out = streamWriterFactory.createXMLStreamWriter(new OutputStreamWriter(outputStream, "utf-8"));
		

	}

	@Override
	public void characters(char[] ch, int start, int length)
			throws SAXException {
			String tmp = new String(ch, start, length);
			this.content.append(tmp);
	}

	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		
		try {
			switch (qName) {
			case "mediawiki":
				out.writeStartElement(qName);
				break;
			case "page":
				 writeToOut=false;
				 break;
			case "title":
				
				break;
			default:
				if(writeToOut){
					LOG.debug("Writing Start Element: " + qName);
					out.writeStartElement(qName);	
				}
			
				break;
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		try {

			switch (qName) {
			case "mediawiki":
				out.writeEndElement();
				break;
			case "page":
				if(writeToOut){
					out.writeEndElement();
				}
				break;
			case "title":
				LOG.debug(content.toString().trim());
				Pattern p = Pattern.compile("Kategoria:.*");
				Matcher matcher = p.matcher(this.content.toString().trim());
				System.out.println(matcher.toString());
				
				if(matcher.matches()){
					LOG.info("endElement Title:  " + content.toString().trim());
					out.writeStartElement("page");
					out.writeStartElement("title");
					out.writeCharacters(StringEscapeUtils.escapeXml(content.toString().trim()));
					out.writeEndElement();
					writeToOut = true;
				}
				
				content = new StringBuilder();
				break;
			default:
				if(writeToOut){
					if(content.length() > 0){
						out.writeCharacters(StringEscapeUtils.escapeXml(content.toString().trim()));
					}
					
					out.writeEndElement();
					LOG.info("Writing end tag: ");
				}
				content = new StringBuilder();
				break;
			}
		} catch (XMLStreamException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void startDocument() throws SAXException {
		try {
			out.writeStartDocument();
	
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void endDocument() throws SAXException {
		try {
			out.writeEndDocument();
			out.close();
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}
