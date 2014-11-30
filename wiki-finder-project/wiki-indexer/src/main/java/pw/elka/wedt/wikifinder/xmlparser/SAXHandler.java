package pw.elka.wedt.wikifinder.xmlparser;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import pw.elka.wedt.wikifinder.indexer.LuceneIndexer;
import pw.elka.wedt.wikifinder.wiki.Article;

public class SAXHandler extends DefaultHandler {
	public List<Article> articles = new ArrayList();
	Article a = null;
	String content = null;
	Boolean revision = Boolean.valueOf(false);
	Integer count = Integer.valueOf(0);
	LuceneIndexer indexer;

	public SAXHandler(LuceneIndexer indexer) {
		this.indexer = indexer;
	}

	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		switch (qName) {
		case "page":
			if (this.count.intValue() < 5000000) {
				this.a = new Article();
			} else
				throw new StopParsingException();

			break;
		case "revision":
			this.revision = Boolean.valueOf(true);
		}
	}

	public void endElement(String uri, String localName, String qName) {
		switch (qName) {
		case "page":
			try {
				addArticleToIndex(this.a);
				count = count + 1; // TODO: chyba :)?
			} catch (IOException e) {
				e.printStackTrace();
			}

			// TODO e = this.count; Integer localInteger = this.count =
			// Integer.valueOf(this.count.intValue() + 1);
			break;
		case "title":
			this.a.title = this.content;
			break;
		case "ns":
			this.a.ns = this.content;
			break;
		case "id":
			if (!this.revision.booleanValue())
				this.a.id = this.content;
			else {
				this.a.revisionId = this.content;
			}
			break;
		case "text":
			this.a.text = this.content;
			break;
		case "revision":
			this.revision = Boolean.valueOf(false);
		}
	}

	public void characters(char[] ch, int start, int length) {
		this.content = String.copyValueOf(ch, start, length).trim();
	}

	private void addArticleToIndex(Article article) throws IOException {
		Document doc = new Document();

		doc.add(new Field("title", this.a.title, TextField.TYPE_STORED));
		doc.add(new Field("text", this.a.text, TextField.TYPE_STORED));
		doc.add(new Field("id", this.a.id, StringField.TYPE_STORED));

		this.indexer.addDocument(doc);

		System.out.println("Indexed article: " + article.title + "\t number: "
				+ this.count);
	}
}