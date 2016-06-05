package Config;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

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

					if (name.equals("WINDOW_WIDTH")) {
						WINDOW_WIDTH = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("WINDOW_HEIGHT")) {
						WINDOW_HEIGHT = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("WINDOW_FULLSCREEN")) {
						WINDOW_FULLSCREEN = Boolean.parseBoolean(elem.getTextContent());
					}

					if (name.equals("WORLD_HEIGHT")) {
						WORLD_HEIGHT = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("WORLD_WIDTH")) {
						WORLD_WIDTH = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("ANT_NUMBER")) {
						ANT_NUMBER = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("ANT_FOOD_CARYING")) {
						ANT_FOOD_CARYING = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("MAX_SIZE_FOOD_STACK")) {
						MAX_SIZE_FOOD_STACK = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("MIN_SIZE_FOOD_STACK")) {
						MIN_SIZE_FOOD_STACK = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("PHEROMONE_INITIAL_LIFE")) {
						PHEROMONE_INITIAL_LIFE = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("ANT_FIELD_OF_VIEW")) {
						ANT_FIELD_OF_VIEW = Integer.parseInt(elem.getTextContent());
					}

					if (name.equals("SMOOTH_FOOD_GENERATION")) {
						SMOOTH_FOOD_GENERATION = Boolean.parseBoolean(elem.getTextContent());
					}

					if (name.equals("FOOD_COVER_PERCENT")) {
						FOOD_COVER_PERCENT = Float.parseFloat(elem.getTextContent());
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
}
