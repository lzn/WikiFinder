package pw.elka.wedt.wikifinder.searcher;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
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
	private static final int RESULT_NUM = 10;
	private static final Logger LOG = Logger.getLogger(LuceneIndexSearcher.class);
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

		ScoreDoc[] hits = isearcher.search(query, null, RESULT_NUM).scoreDocs;
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

	public Document[] searchForKeywords(List<String> keywords) throws IOException, ParseException {
		StringBuilder query = new StringBuilder();
		int count = 0;
		for (String string : keywords) {
			if(count>0){
				query.append(" OR ");
			}
			
			query.append("title:"+string+" OR text:"+string);
			count++;
		}
		LOG.debug("Prepared query: " + query.toString());
		return search(query.toString());
	}
}
