package springVisualizer.XML;

import springCommon.Parameters;

public class XMLComment extends XMLObj {
	public XMLComment(String comment) {
		super(comment);
	}
	@Override
	protected void buildString(StringBuffer sb, int nbTab) {
		if(Parameters.platformFilePrettyXml){
			appendWithTabs(sb, nbTab, "<!-- ");
		} else {
			sb.append("<!-- ");
		}
		sb.append(this.name)
			.append(" -->");
	}
}
