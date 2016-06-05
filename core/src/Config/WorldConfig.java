package Config;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class WorldConfig {

	public static int WINDOW_WIDTH = 640;

	public static int WINDOW_HEIGHT = 480;

	public static boolean WINDOW_FULLSCREEN = false;

	public static int WORLD_HEIGHT = 500;

	public static int WORLD_WIDTH = 500;

	public static int ANT_NUMBER = 2500;

	public static int ANT_FOOD_CARYING = 1;

	public static int MAX_SIZE_FOOD_STACK = 20;

	public static int MIN_SIZE_FOOD_STACK = 3;

	public static int PHEROMONE_INITIAL_LIFE = 25;

	public static int ANT_FIELD_OF_VIEW = 5;

	public static boolean SMOOTH_FOOD_GENERATION = true;

	public static float FOOD_COVER_PERCENT = 1f;

	public static void Load(String filename) {
		
		Field[] declaredFields = WorldConfig.class.getDeclaredFields();
		List<Field> staticFields = new ArrayList<Field>();
		for (Field field : declaredFields) {
		    if (java.lang.reflect.Modifier.isStatic(field.getModifiers())) {
		        staticFields.add(field);
		    }
		}
		
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		try {
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(new File(filename));
			final Element root = document.getDocumentElement();

			final NodeList nodeList = root.getChildNodes();

			for (int i = 0; i < nodeList.getLength(); i++) {
				final Node node = nodeList.item(i);

				if (node.getNodeType() == Node.ELEMENT_NODE) {
					final Element elem = (Element) node;
					final String name = elem.getAttribute("name");
					
					Field field = FindField(name, staticFields);
					
					if(field != null){
						try {
							if(field.getType().equals(int.class)){
								field.setInt(null, Integer.parseInt(elem.getTextContent()));
							}else if(field.getType().equals(float.class)){
								field.setFloat(null, Float.parseFloat(elem.getTextContent()));
							}else if(field.getType().equals(boolean.class)){
								field.setBoolean(null, Boolean.parseBoolean(elem.getTextContent()));
							}
							
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (DOMException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}

		catch (final ParserConfigurationException e) {
			e.printStackTrace();
		}
		catch (final SAXException e) {
			e.printStackTrace();
		}
		catch (final IOException e) {
			e.printStackTrace();
		} catch (final NumberFormatException e) {
			e.printStackTrace();
		}

	}
	
	private static Field FindField(String name, List<Field> fields){
		Field res = null;
		for(Field field : fields){
			if(field.getName().equals(name)){
				res = field;
				break;
			}
		}
		return res;
	}
}
