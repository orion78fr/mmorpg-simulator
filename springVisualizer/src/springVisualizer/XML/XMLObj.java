package springVisualizer.XML;

import java.util.ArrayList;
import java.util.List;

public class XMLObj{
	private String name;
	private List<XMLAttr> attrs = new ArrayList<XMLAttr>();
	private List<XMLObj> childs = new ArrayList<XMLObj>();
	
	public XMLObj(String name){
		super();
		this.name = name;
	}

	public XMLObj addAttr(XMLAttr attr){
		this.attrs.add(attr);
		return this;
	}
	
	public XMLObj addAttr(String name, String value){
		this.attrs.add(new XMLAttr(name, value));
		return this;
	}
	
	public XMLObj addChild(XMLObj child){
		this.childs.add(child);
		return this;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		this.toString(sb, 0);
		return sb.toString();
	}
	private void toString(StringBuffer sb, int nbTab) {
		appendWithTabs(sb, nbTab, "<");
		sb.append(this.name);
		for(XMLAttr a : this.attrs){
			sb.append(" ");
			sb.append(a.toString());
		}
		
		if(childs.size() != 0){
			sb.append(">\n");
			
			for(XMLObj c : this.childs){
				c.toString(sb, nbTab + 1);
				sb.append("\n");
			}
			
			appendWithTabs(sb, nbTab, "</");
			sb.append(this.name);
			sb.append(">");
		} else {
			sb.append(" />");
		}
	}
	private void appendWithTabs(StringBuffer sb, int nbTab, String content){
		for(int i = 0; i < nbTab; i++){
			sb.append("\t");
		}
		sb.append(content);
	}
}