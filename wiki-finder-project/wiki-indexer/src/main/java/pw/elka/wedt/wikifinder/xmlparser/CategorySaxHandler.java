package pw.elka.wedt.wikifinder.xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import org.apache.log4j.Logger;

import pw.elka.wedt.wikifinder.indexer.ArticleListWrapper;
import pw.elka.wedt.wikifinder.wiki.Article;


public class CategorySaxHandler extends WikipediaHandler {
	private static final Logger LOG = Logger.getLogger(CategorySaxHandler.class);
	private static final int INIT_ARRAY_SIZE = 1500000;
	Article begin;

	public CategorySaxHandler() {
		super();
		setArticles(new HashMap<String, Article>(INIT_ARRAY_SIZE));
	}

	@Override
	protected void processPage() {
		LOG.info("Processing Page: \"" + title + "\" id: " + id);

		Article a;
		a = getArticles().get(title);

		if (a == null) {
			LOG.info("No article - creating");
			a = new Article();
			getArticles().put(title, a);
		}
		if (title.matches("Kategoria:Kategorie")) {
			begin = a;
		}
		a.title = title;
		a.id = id;
		a.revisionId = revisionId;
		
		ArrayList<String> categories = extractCategories();
		for (String category : categories) {
			Article c = getArticles().get(category);
			if (c == null) {
				LOG.info("Creating article, category: "+ category + "\"");
				c = new Article();
				c.title = category;
				getArticles().put(category, c);

			}
			c.addChild(a);
			a.addCategory(c);
		}
	}

	public void printCategoryTree(String article) {
		Article a = getArticles().get(article);
		
		LOG.info("\n" + a);
		LinkedList<Article> queue = new LinkedList<Article>();
		if (a.getCategories() != null) {
			queue.addAll(a.getCategories().keySet());
		}
		while (true) {
			if (queue.isEmpty()) {
				LOG.info("printCategoryTree no more articles");
				break;
			}
			a = queue.poll();
			LOG.info("printCategoryTree Page: " + a.title + " Level: " + a.level);
			if (a.getCategories() != null) {
				HashMap<Article, Integer> parents = a.getCategories();
				Iterator i = parents.entrySet().iterator();
				while (i.hasNext()) {
					Map.Entry<Article, Integer> e = (Map.Entry<Article, Integer>) i.next();
					LOG.info("printCategoryTree Category: " + e.getKey().title + " Level: " + e.getKey().level);
				}

				queue.addAll(a.getCategories().keySet());
			}

		}

	}
	public void printCategoryTree2(String article) {
		Article a = getArticles().get(article);
		HashMap<Integer, HashSet<String> > categories = getCategoryMap(a);
		
		Iterator ihe = categories.entrySet().iterator();
		while(ihe.hasNext()){
			Map.Entry<Integer, HashSet<String>> he = (Map.Entry<Integer, HashSet<String>>) ihe.next();
			LOG.info("Level: " +  he.getKey());
			for (String titlSe : he.getValue()) {
				LOG.info("Page: " + titlSe);
			}
			
			
		}

	}

	public void assignLevel() {
		LinkedList<ArticleListWrapper> queue = new LinkedList<ArticleListWrapper>();
		queue.add(begin.setLevel(0));
		while (!queue.isEmpty()) {
			ArticleListWrapper aw = queue.poll();
			Integer level = aw.level;
			if (aw.children != null) {
				for (Article a : aw.children) {				
					ArticleListWrapper child = a.setLevel(level);
					if(child!=null)
						queue.add(child);
				}
			}
		}

	}

}
