package pw.elka.wedt.wikifinder.xmlparser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import pw.elka.wedt.wikifinder.wiki.Article;

public abstract class WikipediaHandler extends DefaultHandler {
	private static final Logger LOG = Logger.getLogger(WikipediaHandler.class);
	private static final String CATEGORY_TEXT_PATTERN = "(\\[\\[)(Kategoria:.+?)([|!]*?)(\\s*?)((\\|.*?)?)(\\]\\])";
	protected StringBuilder content;
	protected Integer count;
	protected String title;
	protected String text;
	protected String id;
	protected String revisionId;
	protected Boolean revision;
	
	private HashMap<String, Article> articles;
	
	public WikipediaHandler() {
		content = new StringBuilder();
		count = new Integer(0);
		revision = new Boolean(false);
	}
	
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		switch (qName) {
		case "page":
			//if (count.intValue() >= 50000000)
			//	throw new StopParsingException();
			break;
		case "title":
			this.content = new StringBuilder();
			break;
		case "text":
			this.content = new StringBuilder();
			break;
		case "id":
			this.content = new StringBuilder();
			break;
		case "revision":
			this.revision = Boolean.valueOf(true);
			break;
		default:
			break;
		}
	
	}
	public void endElement(String uri, String localName, String qName) {
		switch (qName) {
		case "page":
				processPage();
				count++;
			break;
		case "title":
			title = content.toString();
			break;
		case "id":
			if (!this.revision.booleanValue())
				id = content.toString();
			else {
				revisionId = content.toString();;
			}
			break;
		case "text":
			text = content.toString();
			
			content = new StringBuilder();
			break;
		case "revision":
			revision = Boolean.valueOf(false);
			break;
		default:
			break;
		}
		
	}
	protected ArrayList<String> extractCategories(){
		ArrayList<String> a = new ArrayList<String>();
		Pattern p = Pattern.compile(CATEGORY_TEXT_PATTERN);
		Matcher matcher = p.matcher(text.toString());
		while(matcher.find()){
			LOG.debug("Match: " + matcher.toString());
			LOG.info("extractCategories Article: " + title +" category: " + matcher.group(2));
			a.add(matcher.group(2));
			
		}
		return a;
	}
	
	@SuppressWarnings("rawtypes")
	public HashMap<Integer, HashSet<String>> getCategoryMap(Article article) {
		Article a = article;
		LOG.info(a);
		LinkedList<Article> queue = new LinkedList<Article>();
		HashMap<Integer, HashSet<String> > categories = new HashMap<Integer, HashSet<String>>();
		if (a.getCategories() != null) {
			queue.addAll(a.getCategories().keySet());
		}
		while (true) {
			if (queue.isEmpty()) {
				break;
			}
			a = queue.poll();
			if (a.getCategories() != null) {
				HashMap<Article, Integer> parents = a.getCategories();
				Iterator i = parents.entrySet().iterator();
				while (i.hasNext()) {
					@SuppressWarnings("unchecked")
					Map.Entry<Article, Integer> e = (Map.Entry<Article, Integer>) i.next();
					Article aaa = e.getKey();
					if(categories.containsKey(aaa.level)){
						categories.get(aaa.level).add(aaa.title);
					}else{
						HashSet<String> h = new HashSet<String>();
						h.add(aaa.title);
						categories.put(aaa.level, h);
					}
				}
				queue.addAll(a.getCategories().keySet());
			}

		}
		return categories;
	}
	protected abstract void processPage();
	
	public void characters(char[] ch, int start, int length) {
		String tmp = new String(ch, start, length);	
		this.content.append(tmp);
	}

	public HashMap<String, Article> getArticles() {
		return articles;
	}

	public void setArticles(HashMap<String, Article> articles) {
		this.articles = articles;
	}
	
}
