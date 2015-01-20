package wedt.finder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.queryparser.classic.ParseException;

import pw.elka.wedt.wikifinder.config.ConfigManager;
import pw.elka.wedt.wikifinder.searcher.LuceneIndexSearcher;


/**
 * @author kjrz
 */
public class ActualWikiFinder implements WikiFinder {
	private static final Logger LOG = Logger.getLogger(ActualFinding.class);
    
	public class ActualFinding implements WikiFinder.Finding {
        // TODO: representation of a finding by fields + getters
    	
    	private final Integer level;
    	private final ArrayList<String> tags;
    	public ActualFinding(Integer level, ArrayList<String> tags) {
			 this.level = level;
			 this.tags = tags;
		}
		public Integer getLevel() {
			return level;
		}
		public ArrayList<String> getTags() {
			return tags;
		}
    }

    @Override
    public List<Finding> match(List<String> keywords) {
        //throw new UnsupportedOperationException();
        ConfigManager cm = new ConfigManager();
        
        LuceneIndexSearcher lis = new LuceneIndexSearcher(cm);
        ArrayList<WikiFinder.Finding> finding = new ArrayList<WikiFinder.Finding>();
        
      
        
        try {
			Document[] doc = lis.searchForKeywords(keywords);
			HashMap<Integer, HashMap<String,Integer>> results = new HashMap<Integer, HashMap<String, Integer>>();
			for (Document document : doc) {
				List<IndexableField> fields = document.getFields();
				ArrayList<String> cat = new ArrayList<String>();
				for (IndexableField indexableField : fields) {
					String name = indexableField.name();
					String value = indexableField.stringValue();
					Pattern p = Pattern.compile("(cat-)(\\d+)");
					Matcher matcher = p.matcher(name);
					LOG.debug(matcher.toString());
					
					 
					if(matcher.matches()){
						LOG.info("Name: " + name + " Value: " + value + " Group(0)" + matcher.group(0) + " Group(1)" + matcher.group(1) + " Group(2)" + matcher.group(2));
						Integer level = Integer.valueOf(matcher.group(2));
						HashMap<String,Integer> m = results.get(level);
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
			
			Iterator<Entry<Integer, HashMap<String, Integer>>> iterator = results.entrySet().iterator();
			while(iterator.hasNext()){
				Entry<Integer, HashMap<String, Integer>> next = iterator.next();
				Integer level = next.getKey();
				
				//TODO: dorobic sortowanie
				ArrayList<String> tags;// = new ArrayList<String>();
				//HashMap<String, Integer> value = next.getValue();
		//		Iterator<String> valueIt = value.keySet().iterator();
				tags = new ArrayList<String>(next.getValue().keySet());
								
				finding.add(new ActualFinding(level, tags));				
			}
			
		} catch (IOException | ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    
        return finding;
    }
    
    
}
