import java.util.Observable;
import java.util.Observer;

import javax.swing.JTextArea;

public class OutputPanel extends JTextArea implements Observer{
		
	private static OutputPanel instance;
	
	public static OutputPanel outputPanel() {
		if (instance == null) {
			instance = new OutputPanel();
		}
		return instance;
	}
	
	private OutputPanel() {
		this.setRows(10);
		this.setColumns(40);
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setEditable(false);		
	}

	//Adds new line when observable updates
	@Override
	public void update(Observable observable, Object text) {
		if (text instanceof String) {
			this.append("\n" + (String) text);
		}
	}
	
}
