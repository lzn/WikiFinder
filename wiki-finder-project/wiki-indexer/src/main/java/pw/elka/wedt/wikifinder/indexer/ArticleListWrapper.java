package pw.elka.wedt.wikifinder.indexer;

import java.util.ArrayList;

import pw.elka.wedt.wikifinder.wiki.Article;

public class ArticleListWrapper {

	public ArrayList<Article> children;
	public Integer level;

	public ArticleListWrapper(ArrayList<Article> children, Integer level) {
		this.children = children;
		this.level = level;
	}
}
