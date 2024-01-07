package main;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

import acts.Names;
import acts.display;
import classes.CommonEntity;


public class Main {
	public static final ArrayList<ArrayList<CommonEntity>> megaList = new ArrayList<ArrayList<CommonEntity>>();
	static {
		for(var i = 0; i < Names.TableClass.length; ++i)
			megaList.add(new ArrayList<CommonEntity>());
	}
	
	/**<p>Функция main. Вызывает метод отображения окна</p>*/
	public static void main(String[] args) {
		Properties props = new Properties();
		
		try {
			props.load(new FileInputStream("log4j.properties"));
		} catch (Exception e) { e.printStackTrace(); }
		PropertyConfigurator.configure(props);
		
		
		var fr = new display(700, 500);
		fr.setVisible(true);
	}
}
