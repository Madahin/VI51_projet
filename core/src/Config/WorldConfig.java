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

// TODO: Auto-generated Javadoc
/**
 * The application configuration.
 */
public class WorldConfig {

	/** The window width. */
	public static int WINDOW_WIDTH = 640;

	/** The window height. */
	public static int WINDOW_HEIGHT = 480;

	/** Is the window fullscreen. */
	public static boolean WINDOW_FULLSCREEN = false;

	/** The world height. */
	public static int WORLD_HEIGHT = 500;

	/** The world width. */
	public static int WORLD_WIDTH = 500;

	/** The ant number. */
	public static int ANT_NUMBER = 2500;

	/** How much food an ant can carry. */
	public static int ANT_FOOD_CARYING = 1;

	/** Maximum size of a food stack. */
	public static int MAX_SIZE_FOOD_STACK = 20;

	/** Minimum size of a food stack. */
	public static int MIN_SIZE_FOOD_STACK = 3;

	/** The pheromone initial life. */
	public static int PHEROMONE_INITIAL_LIFE = 200;

	/** The ant field of view. */
	public static int ANT_FIELD_OF_VIEW = 5;

	/** smoothing the food generation. */
	public static boolean SMOOTH_FOOD_GENERATION = true;

	/** The food cover intensity. */
	public static float FOOD_COVER_INTENSITY = 0.75f;
	
	public static float FOOD_COVER_PERCENT = 0.75f; // PLEASE CORRECT THIS SHIT
	
	/** The default food in an ant base. */
	public static int DEFAULT_FOOD_IN_BASE = 1000;
	
	/** The number of ant bases. */
	public static int BASE_NUMBER = 2;
	
	/** The radius of an ant base. */
	public static int BASE_RADIUS = 30;

	/**
	 * Load an XML configuration file.
	 *
	 * @param filename the filename of the configuration file
	 */
	public static void Load(String filename) {
		// We use reflection to get every static fields
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
							// Simple parsing, since we have every field with reflexion, it's easy
							// to put each data were it belong.
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
	
	/**
	 * Find a field in a list of static field by name.
	 *
	 * @param name the name of the field we are seeking
	 * @param fields the list of fields we are searching in
	 * @return the field if we found it, null otherwise
	 */
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
