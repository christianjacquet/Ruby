package com.digitalemu.xml;
 
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
 
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
 
public class WriteXML {
 
	public static void main(String argv[]) {
 
	  try {
 
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
 
		// root elements
		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("MyCraft");
		doc.appendChild(rootElement);
 
		// staff elements
		Element material = doc.createElement("material");
		rootElement.appendChild(material);
 
		// set attribute to staff element
		material.setAttribute("name", "sand");
		
		Element properties = doc.createElement("properties");
		properties.setAttribute("transparent", "no");
		properties.setAttribute("solid", "yes");
		properties.setAttribute("burnable", "no");
		properties.setAttribute("conductive", "no");
		properties.setAttribute("emitLight", "0");
		properties.setAttribute("mass", "5");
		properties.setAttribute("hardness", "2");
		material.appendChild(properties);
		
		Element resistance = doc.createElement("resistance");
		resistance.setAttribute("velocity", "14");
		resistance.setAttribute("mass", "14");
		resistance.setAttribute("digger", "4");
		resistance.setAttribute("sharpness", "14");
		resistance.setAttribute("attackarea", "14");
		resistance.setAttribute("heat", "12");
		resistance.setAttribute("water", "16");
		resistance.setAttribute("fire", "16");
		resistance.setAttribute("electricity", "16");
		material.appendChild(resistance);
		
		Element spawn = doc.createElement("spawn");
		spawn.setAttribute("fire", "sand");
		spawn.setAttribute("heat", "glass");
		spawn.setAttribute("water", "sand");
		spawn.setAttribute("destruction", "sand");
		material.appendChild(spawn);
		
		Element render = doc.createElement("render");
		render.setAttribute("front", "sand");
		render.setAttribute("back", "sand");
		render.setAttribute("left", "sand");
		render.setAttribute("right", "sand");
		render.setAttribute("top", "sand");
		render.setAttribute("bottom", "sand");
		material.appendChild(render);

 

 
		// write the content into xml file
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	    transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount","2");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new File("res/confw.xml"));
 
		// Output to console for testing
		// StreamResult result = new StreamResult(System.out);
 
		transformer.transform(source, result);
 
		System.out.println("File saved!");
 
	  } catch (ParserConfigurationException pce) {
		pce.printStackTrace();
	  } catch (TransformerException tfe) {
		tfe.printStackTrace();
	  }
	}
}