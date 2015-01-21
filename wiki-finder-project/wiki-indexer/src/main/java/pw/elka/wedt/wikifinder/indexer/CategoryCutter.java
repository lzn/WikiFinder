package pw.elka.wedt.wikifinder.indexer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXException;

import pw.elka.wedt.wikifinder.xmlparser.CategoryCutterSaxHandler;

public class CategoryCutter {

	public static void main(String[] args) throws ParserConfigurationException, SAXException {
		if(args.length < 2){
			System.err.println("Arguments: input-file output-file");
		}
		String inputFileName = args[0];
		String outputFileName = args[1];
		
		
		System.out.println("Input File Name: " + inputFileName + " Output File Name: " + outputFileName );
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();

		
		CategoryCutterSaxHandler handler;
		try {
			handler = new CategoryCutterSaxHandler(outputFileName);
			parser.parse(new FileInputStream(inputFileName), handler);
		} catch (FileNotFoundException | UnsupportedEncodingException
				| XMLStreamException | FactoryConfigurationError e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
