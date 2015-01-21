package pw.elka.wedt.wikifinder.indexer;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.morfologik.MorfologikAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class LuceneIndexer {
	private IndexWriter iwriter = null;
	private String indexPath;
	private Directory directory;

	public LuceneIndexer(String indexPath) {
		this.indexPath = indexPath;
	}

	public void init() throws IOException {
		//Analyzer analyzer = new StandardAnalyzer();
		Analyzer analyzer = new MorfologikAnalyzer();
		directory = FSDirectory.open(new File(indexPath));
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,
				analyzer);
		iwriter = new IndexWriter(directory, config);
	}

	public void endIndexer() throws IOException {
	
		iwriter.close();
		directory.close();
	}

	public void addDocument(Document doc) throws IOException {
		iwriter.addDocument(doc);
	}
}
