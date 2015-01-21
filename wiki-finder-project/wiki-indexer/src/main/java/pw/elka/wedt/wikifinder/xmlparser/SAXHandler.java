package pw.elka.wedt.wikifinder.xmlparser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;

import pw.elka.wedt.wikifinder.indexer.LuceneIndexer;
import pw.elka.wedt.wikifinder.wiki.Article;

public class SAXHandler extends WikipediaHandler {
	private static final Logger LOG = Logger.getLogger(SAXHandler.class);
	LuceneIndexer indexer;

	public SAXHandler(LuceneIndexer li) {
		super();
		this.indexer = li;

	}

	public SAXHandler(LuceneIndexer li, HashMap<String, Article> articles) {
		super();
		this.indexer = li;
		setArticles(articles);
	}

	private void addArticleToIndex() throws IOException {
		Document doc = new Document();

		doc.add(new Field("title", title, TextField.TYPE_STORED));
		doc.add(new Field("text", text, TextField.TYPE_STORED));
		doc.add(new Field("id", id, StringField.TYPE_STORED));

		ArrayList<String> extractCategories = extractCategories();

		Article a = new Article();
		a.title = title;
		a.id = id;
		if (!getArticles().containsKey(title))
			getArticles().put(title, a);

		if (extractCategories.size() > 0) {
			for (String catName : extractCategories) {
				Article category = getArticles().get(catName);

				if (category != null) {
					LOG.info("Page title: " + title
							+ " adding category: " + category.title);
					a.addCategory(category);
					category.addChild(a);
				} else {
					LOG.warn("No Categories found... Page title: "
									+ title + " Category Name: " + catName);
				}
			}

			a.calculateLevel();
			writeCategoryMap(a, doc);

		} else {
			LOG.warn("Page title: " + title
					+ "doesn't have any category");
		}

		indexer.addDocument(doc);

		LOG.info("Indexed article: " + title + "\t number: "
				+ this.count);
	}

	private void writeCategoryMap(Article a, Document doc) {

		LinkedList<Article> queue = new LinkedList<Article>();
		HashSet<String> categories = new HashSet<String>();
		if (a.getCategories() != null) {
			queue.addAll(a.getCategories().keySet());

			while (!queue.isEmpty()) {
				Article c = queue.poll();
				if (!categories.contains(c.title)) {
					doc.add(new Field("cat-" + c.level, c.title,
							StringField.TYPE_STORED));
					categories.add(c.title);
					if (c.getCategories() != null) {
						for (Article cat : c.getCategories().keySet()) {
							if (!categories.contains(cat.title)) {
								queue.add(cat);
							}
						}
					}
				}
			}
		}
	}

	@Override
	protected void processPage() {
		try {
			addArticleToIndex();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
}