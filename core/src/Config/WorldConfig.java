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
	public static int ANT_FOOD_CARYING = 60;

	/** Maximum size of a food stack. */
	public static int MAX_SIZE_FOOD_STACK = 1200;

	/** Minimum size of a food stack. */
	public static int MIN_SIZE_FOOD_STACK = 180;

	/** Maximum size of a pheromone used only to clamp it's color gradient. */
	public static float MAX_PHEROMONE_LIFE = 200;

	/** The pheromone initial life. */
	public static int PHEROMONE_INITIAL_LIFE = 10;

	/** Ant life bar */
	public static int ANT_INITIAL_LIFE = 100;

	/** The ant field of view. */
	public static int ANT_FIELD_OF_VIEW = 3;

	/** smoothing the food generation. */
	public static boolean SMOOTH_FOOD_GENERATION = true;

	/** The food cover intensity. */
	public static float FOOD_COVER_INTENSITY = 1.0f;

	/** The default food in an ant base. */
	public static int DEFAULT_FOOD_IN_BASE = 10000;

	/** The number of ant bases. */
	public static int BASE_NUMBER = 2;

	/** The radius of an ant base. */
	public static int BASE_RADIUS = 30;

	/** The ant's life expectancy. */
	public static int LIFE_EXPECTANCY = 2000;

	/** Soldier Attack's */
	public static int SOLDIER_DAMAGE = 10;

	/** The ant's hunger bar. */
	public static int HUNGER_BAR = 100;

	/** Maximum number of ants that can be spawned at the time. */
	public static int ANT_POP_NUMBER = 100;

	/** Amount of food a dead ant's body becomes. */
	public static int DEAD_ANT_FOOD_VALUE = 10;
	
	/** The seed used by the random number generator, -1 is a random seed */
	public static long RANDOM_SEED = -1;

	/**
	 * Load an XML configuration file.
	 *
	 * @param filename
	 *            the filename of the configuration file
	 */
	public static void load(String filename) {
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

					Field field = findField(name, staticFields);

					if (field != null) {
						try {
							// Simple parsing, since we have every field with
							// reflexion, it's easy
							// to put each data were it belong.
							if (field.getType().equals(int.class)) {
								field.setInt(null, Integer.parseInt(elem.getTextContent()));
							} else if (field.getType().equals(float.class)) {
								field.setFloat(null, Float.parseFloat(elem.getTextContent()));
							} else if (field.getType().equals(boolean.class)) {
								field.setBoolean(null, Boolean.parseBoolean(elem.getTextContent()));
							} else if (field.getType().equals(long.class)) {
								field.setLong(null, Long.parseLong(elem.getTextContent()));
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
		} catch (final SAXException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final NumberFormatException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Find a field in a list of static field by name.
	 *
	 * @param name
	 *            the name of the field we are seeking
	 * @param fields
	 *            the list of fields we are searching in
	 * @return the field if we found it, null otherwise
	 */
	private static Field findField(String name, List<Field> fields) {
		Field res = null;
		for (Field field : fields) {
			if (field.getName().equals(name)) {
				res = field;
				break;
			}
		}
		return res;
	}
}
