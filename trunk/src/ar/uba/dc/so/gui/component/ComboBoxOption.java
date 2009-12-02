package ar.uba.dc.so.gui.component;

public class ComboBoxOption<K> {
	private K value;
	private String text;
	
	public ComboBoxOption(String text, K value) {
		this.text = text;
		this.value = value;
	}
	
	public K getValue() {
		return value;
	}
	
	public String getText() {
		return text;
	}
	
	public String toString() {
		return text;
	}
}
