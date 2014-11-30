package pw.elka.wedt.wikifinder.wiki;

public class Article {
	public String title;
	public String ns;
	public String id;
	public String text;
	public String revisionId;

	public String toString() {
		return "title: " + this.title + " ; ns: " + this.ns + " ; id: "
				+ this.id;
	}
}