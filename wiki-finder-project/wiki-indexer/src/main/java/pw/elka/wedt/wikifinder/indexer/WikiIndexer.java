package pw.elka.wedt.wikifinder.indexer;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.SAXException;
import pw.elka.wedt.wikifinder.config.ConfigManager;
import pw.elka.wedt.wikifinder.wiki.Article;
import pw.elka.wedt.wikifinder.xmlparser.SAXHandler;
import pw.elka.wedt.wikifinder.xmlparser.StopParsingException;

public class WikiIndexer {
	static ConfigManager cf = new ConfigManager();

	public static void main(String[] args) throws ParserConfigurationException,
			SAXException, IOException {
		cf.load();
		LuceneIndexer li = new LuceneIndexer(cf.getIndexPath());
		li.init();
		SAXParserFactory parserFactory = SAXParserFactory.newInstance();
		SAXParser parser = parserFactory.newSAXParser();

		SAXHandler handler = new SAXHandler(li);

		Date start = new Date();
		try {
			parser.parse(new FileInputStream(cf.getSourceFile()), handler);
		} catch (StopParsingException e) {
			System.out.println("Parsing stopped");
		}
		li.endIndexer();

		for (Article a : handler.articles) {
			System.out.println(a.toString());
		}

		Date end = new Date();
		System.out.println("Indexing time: "
				+ (end.getTime() - start.getTime()) / 1000L + " [s]");
	}
}