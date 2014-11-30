package pw.elka.wedt.wikifinder.searcher;

import java.io.IOException;

import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.ParseException;

import pw.elka.wedt.wikifinder.config.ConfigManager;

public class WikiSearcher {
	public static void main(String[] args) throws IOException, ParseException {

		ConfigManager cf = new ConfigManager();
		cf.load();
		LuceneIndexSearcher lis = new LuceneIndexSearcher(cf);
		if (args.length > 0) {
			Document[] doc = lis.search(args[0]);
			for (int i = 0; i < doc.length; ++i) {

				System.out.println(i + "\t" + doc[i].get("title") + "\t"
						+ doc[i].get("id"));
			}
		} else {
			System.out.println("Podaj co chcesz wyszukać");
		}

	}
}
