package springVisualizer.XML;

import java.util.ArrayList;
import java.util.List;

import springCommon.Parameters;

public class XMLObj{
	protected String name;
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
	
	public XMLObj addAttr(String name){
		this.attrs.add(new XMLAttr(name, null));
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
		this.buildString(sb, 0);
		return sb.toString();
	}
	
	protected void buildString(StringBuffer sb, int nbTab) {
		if(Parameters.platformFilePrettyXml){
			appendWithTabs(sb, nbTab, "<");
		} else {
			sb.append("<");
		}
		
		sb.append(this.name);
		
		for(XMLAttr a : this.attrs){
			sb.append(" ");
			sb.append(a.toString());
		}
		
		if(childs.size() != 0){
			sb.append(">");
			
			if(Parameters.platformFilePrettyXml){
				sb.append("\n");
			}
			
			for(XMLObj c : this.childs){
				c.buildString(sb, nbTab + 1);
				
				if(Parameters.platformFilePrettyXml){
					sb.append("\n");
				}
			}
			if(Parameters.platformFilePrettyXml){
				appendWithTabs(sb, nbTab, "</");
			} else {
				sb.append("</");
			}
			sb.append(this.name);
			sb.append(">");
		} else {
			sb.append("/>");
		}
	}
	protected static void appendWithTabs(StringBuffer sb, int nbTab, String content){
		for(int i = 0; i < nbTab; i++){
			sb.append("\t");
		}
		sb.append(content);
	}
}