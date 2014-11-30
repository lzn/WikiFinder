package pw.elka.wedt.wikifinder.searcher;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import pw.elka.wedt.wikifinder.config.ConfigManager;

public class LuceneIndexSearcher {
	private ConfigManager cm;

	public LuceneIndexSearcher(ConfigManager cm) {
		this.cm = cm;
	}

	public Document[] search(String text) throws IOException, ParseException {
		Analyzer analyzer = new StandardAnalyzer();
		Directory directory = FSDirectory.open(new File(cm.getIndexPath()));
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);

		String qs = text;
		QueryParser parser = new QueryParser("text", analyzer);

		Query query = parser.parse(qs);

		ScoreDoc[] hits = isearcher.search(query, null, 1000).scoreDocs;
		Document[] docs = new Document[hits.length];
		for (int i = 0; i < hits.length; i++) {
			int docId = hits[i].doc;
			Document d = isearcher.doc(docId);
			docs[i] = d;

		}
		ireader.close();
		directory.close();

		return docs;
	}
}
