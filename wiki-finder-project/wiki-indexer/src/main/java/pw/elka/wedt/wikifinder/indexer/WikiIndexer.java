package pw.elka.wedt.wikifinder.indexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import pw.elka.wedt.wikifinder.config.ConfigManager;
import pw.elka.wedt.wikifinder.xmlparser.CategorySaxHandler;
import pw.elka.wedt.wikifinder.xmlparser.SAXHandler;
import pw.elka.wedt.wikifinder.xmlparser.StopParsingException;

public class WikiIndexer {
	private static final Logger LOG = Logger.getLogger(SAXHandler.class);
	static ConfigManager cf = new ConfigManager();
	
	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		System.out.println("Recording logs to log/wedt.log. \nProcessing...");
		cf.load();
		LuceneIndexer li = new LuceneIndexer(cf.getIndexPath());
		li.init();
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();

		
		CategorySaxHandler csh = new CategorySaxHandler();

		Date start = new Date();
		try {
			parser.parse(new FileInputStream(cf.getCategorySourceFile()), csh);
			
			csh.assignLevel();
			SAXHandler handler = new SAXHandler(li, csh.getArticles());
			
			parser.parse(new FileInputStream(cf.getSourceFile()), handler);
			
			
		} catch (StopParsingException e) {
			LOG.info("Parsing stopped");
		}catch (Exception e) {
			e.printStackTrace();
		}
		li.endIndexer();
		Date end = new Date();
		LOG.info("Indexing time: "
				+ (end.getTime() - start.getTime()) / 1000L + " [s]");
		System.out.println("Indexing time: "
				+ (end.getTime() - start.getTime()) / 1000L + " [s]");
	}
}