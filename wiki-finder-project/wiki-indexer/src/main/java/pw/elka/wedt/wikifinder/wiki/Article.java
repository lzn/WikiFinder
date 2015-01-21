package pw.elka.wedt.wikifinder.wiki;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.apache.log4j.Logger;

import pw.elka.wedt.wikifinder.indexer.ArticleListWrapper;
import pw.elka.wedt.wikifinder.xmlparser.SAXHandler;

public class Article {
	private static final Logger LOG = Logger.getLogger(SAXHandler.class);
	public String title;
	public String ns;
	public String id;
	public String text;
	public String revisionId;
	private HashMap<Article, Integer> categories; //TODO: change to ArrayList
	private ArrayList<Article> children;
	public Integer level;

	public String toString() {
		return "title: " + this.title + " ; ns: " + this.ns + " ; id: "
				+ this.id;
	}

	public HashMap<Article, Integer> getCategories() {
		return categories;
	}

	public ArrayList<Article> getChildren() {
		return children;
	}

	public void addCategory(Article category) {
		if (categories == null) {
			// categories = new ArrayList<Article>(10);
			categories = new HashMap<Article, Integer>(3);
		}
		categories.put(category, null);
		
	}

	public void addChild(Article child) {
		if (children == null) {
			children = new ArrayList<Article>(3);
		}
		children.add(child);
	}

	public ArticleListWrapper setLevel(Integer level) {
		if(this.level == null){
			LOG.debug("Page: " + title + " setting level: " + level);
			this.level=level;
			return new ArticleListWrapper(children, level+1);
		}else if(this.level > level){
			LOG.debug("Page: " + title + " setting min level: " + level);
			this.level = level;
			return new ArticleListWrapper(children, level+1);
		}
		return null;
	}

	public void calculateLevel() {
		if(categories == null){
			return;
		}
		
		Collection<Article> values = categories.keySet();
		Integer maxLevel = 0;
		for (Article a : values) {
			if(maxLevel != null && a.level != null)
			maxLevel = Integer.max(maxLevel, a.level );
		}
		level = maxLevel;
	}
}