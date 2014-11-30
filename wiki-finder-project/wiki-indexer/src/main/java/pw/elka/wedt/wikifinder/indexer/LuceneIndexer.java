package pw.elka.wedt.wikifinder.indexer;

import java.io.File;
import java.io.IOException;
import org.apache.lucene.analysis.Analyzer;
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
		Analyzer analyzer = new StandardAnalyzer();

		this.directory = FSDirectory.open(new File(this.indexPath));
		IndexWriterConfig config = new IndexWriterConfig(Version.LATEST,
				analyzer);
		this.iwriter = new IndexWriter(this.directory, config);
	}

	public void endIndexer() throws IOException {
		this.iwriter.close();
		this.directory.close();
	}

	public void addDocument(Document doc) throws IOException {
		this.iwriter.addDocument(doc);
	}
}
