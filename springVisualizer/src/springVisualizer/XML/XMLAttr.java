package springVisualizer.XML;

public class XMLAttr{
	private String name;
	private String value;

	public XMLAttr(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}
	
	@Override
	public String toString() {
		if(value != null && !value.equals("")){
			StringBuffer sb = new StringBuffer();
			sb.append(name);
			sb.append("=\"");
			sb.append(value);
			sb.append("\"");
			return sb.toString();
		} else {
			return name;
		}
	}
}