package wedt.finder;

import java.io.IOException;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;

import pw.elka.wedt.wikifinder.config.ConfigManager;
import pw.elka.wedt.wikifinder.searcher.LuceneIndexSearcher;



public class ActualWikiFinder implements WikiFinder {
	private static final int TAGS_NUM = 5;
	private static final String CAT_NAME_PATTERN = "(cat-)(\\d+)";
	private static final Logger LOG = Logger.getLogger(ActualFinding.class);
    
	public class ActualFinding implements WikiFinder.Finding {
        private final Integer level;
    	private final List<String> tags;
    	public ActualFinding(Integer level, List<String> tags) {
			 this.level = level;
			 this.tags = tags;
		}
		public Integer getLevel() {
			return level;
		}
		public List<String> getTags() {
			return tags;
		}
    }

    @Override
    public List<Finding> match(List<String> keywords) {
        ConfigManager cm = new ConfigManager();
        
        LuceneIndexSearcher lis = new LuceneIndexSearcher(cm);
        ArrayList<WikiFinder.Finding> finding = new ArrayList<WikiFinder.Finding>();
 
        try {
			Document[] doc = lis.searchForKeywords(keywords);
			HashMap<Integer, HashMap<String,Integer>> results = new HashMap<Integer, HashMap<String, Integer>>();
			for (Document document : doc) {
				String articleTitle = document.getField("title").stringValue();
				LOG.info("Processing article title: " + articleTitle ); 
				
				List<IndexableField> fields = document.getFields();
				for (IndexableField indexableField : fields) {
					String name = indexableField.name();
					String value = indexableField.stringValue();
					Pattern p = Pattern.compile(CAT_NAME_PATTERN);
					Matcher matcher = p.matcher(name);
					LOG.debug(matcher.toString());
					 
					if(matcher.matches()){
						LOG.info("Article title: " + articleTitle  + " Name: " + name + " Value: " + value);
						Integer level = Integer.valueOf(matcher.group(2));
						HashMap<String,Integer> m = results.get(level);
						if(level>0){
							if(m != null){
								m = results.get(level);
								if(m.containsKey(value)){
									m.replace(value, m.get(value) +1 );
								}else{
									m.put(value,1);
								}
							
							}else{
								m = new HashMap<String, Integer>();
								m.put(value, 1);
								results.put(level, m);
							}
						
						}
					}
				}
				
			}

			for (Entry<Integer, HashMap<String, Integer>> entry : results.entrySet()) {
				Integer level = entry.getKey();
				List<String> tags = pick(entry.getValue(), TAGS_NUM);
				finding.add(new ActualFinding(level, tags));
			}
			
			ArrayList<String> articles = new ArrayList<String>();
			for(int i=0 ; i<TAGS_NUM ; ++i){
				
				articles.add(doc[i].getField("title").stringValue());
			}
			finding.add(new ActualFinding(finding.size()+1, articles));
		} catch (IOException | ParseException e) {
			LOG.error(e);
			// TODO: ?
		}
    
        return finding;
    }

	/**
	 * http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
	 */
	private List<String> pick(HashMap<String, Integer> map, int n) {
		ValueComparator bvc =  new ValueComparator(map);
		TreeMap<String,Integer> sorted = new TreeMap<>(bvc);
		sorted.putAll(map);
		ArrayList<String> keys = new ArrayList<>();
		keys.addAll(sorted.keySet());
		
		return keys.size() > n ? keys.subList(0, n) : keys;
	}

	/**
	 * http://stackoverflow.com/questions/109383/how-to-sort-a-mapkey-value-on-the-values-in-java
	 */
	private class ValueComparator implements Comparator<String> {
		private final Map<String, Integer> base;

		public ValueComparator(Map<String, Integer> base) {
			this.base = base;
		}

		// Note: this comparator imposes orderings that are inconsistent with equals.
		public int compare(String a, String b) {
			if (base.get(a) >= base.get(b)) {
				return -1;
			} else {
				return 1;
			} // returning 0 would merge keys
		}
	}
}
