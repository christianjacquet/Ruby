package com.digitalemu.xml;
 
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
 
public class ReadXML {
 
  public static void main(String argv[]) {
 
    try {
 
	File fXmlFile = new File("res/confw.xml");
	DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	Document doc = dBuilder.parse(fXmlFile);
 
	//optional, but recommended
	//read this - http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
	doc.getDocumentElement().normalize();
 
	System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
	
	NodeList nList = doc.getElementsByTagName("material");
	for (int temp = 0; temp < nList.getLength(); temp++) {		 
		Node nNode = nList.item(temp);
		Element eElement = (Element) nNode;
		System.out.println(nNode.getNodeName()+ "-"+eElement.getAttribute("name") );
		NodeList prop = eElement.getElementsByTagName("properties");
		Node propnode = prop.item(0);
		Element propelement = (Element) propnode;
		System.out.println(
				propnode.getNodeName()+ 
				"- burnable:"+propelement.getAttribute("burnable")+
				"- conductive:"+propelement.getAttribute("conductive")+
				"- emitLight:"+propelement.getAttribute("emitLight")+
				"- hardness:"+propelement.getAttribute("hardness")+
				"- mass:"+propelement.getAttribute("mass")+
				"- solid:"+propelement.getAttribute("solid")+
				"- transparent:"+propelement.getAttribute("transparent"));
		NodeList rest = eElement.getElementsByTagName("resistance");
		Node restnode = rest.item(0);
		Element restelement = (Element) restnode;
		System.out.println(
				restnode.getNodeName()+ 
				"- attackarea:"+restelement.getAttribute("attackarea")+
				"- digger:"+restelement.getAttribute("digger")+
				"- electricity:"+restelement.getAttribute("electricity")+
				"- fire:"+restelement.getAttribute("fire")+
				"- mass:"+restelement.getAttribute("mass")+
				"- sharpness:"+restelement.getAttribute("sharpness")+
				"- water:"+restelement.getAttribute("water")+
				"- velocity:"+restelement.getAttribute("velocity"));
		NodeList spawn = eElement.getElementsByTagName("spawn");
		Node spawnnode = spawn.item(0);
		Element spawnelement = (Element) spawnnode;
		System.out.println(
				spawnnode.getNodeName()+ 
				"- destruction:"+spawnelement.getAttribute("destruction")+
				"- fire:"+spawnelement.getAttribute("fire")+
				"- heat:"+spawnelement.getAttribute("heat")+
				"- water:"+spawnelement.getAttribute("water"));
		NodeList render = eElement.getElementsByTagName("render");
		Node rendernode = render.item(0);
		Element renderelement = (Element) rendernode;
		System.out.println(
				rendernode.getNodeName()+ 
				"- back:"+renderelement.getAttribute("back")+
				"- bottom:"+renderelement.getAttribute("bottom")+
				"- front:"+renderelement.getAttribute("front")+
				"- left:"+renderelement.getAttribute("left")+
				"- right:"+renderelement.getAttribute("right")+
				"- top:"+renderelement.getAttribute("top"));

	}
	System.out.println("Done myxml");
	
	
//	
//	
//	
//	NodeList nList = doc.getElementsByTagName("material");
//	 
//	System.out.println("----------------------------");
// 
//	for (int temp = 0; temp < nList.getLength(); temp++) {
// 
//		Node nNode = nList.item(temp);
// 
//		System.out.println("\nCurrent Element :" + nNode.getNodeName());
// 
//		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
// 
//			Element eElement = (Element) nNode;
// 
//			System.out.print("Material id : " + eElement.getAttribute("id"));
//			System.out.print(" Name : " + eElement.getElementsByTagName("name").item(0).getTextContent());
//			System.out.print("  transparant : " + eElement.getElementsByTagName("transparant").item(0).getTextContent());
//			System.out.print("  solid : " + eElement.getElementsByTagName("solid").item(0).getTextContent());
//			System.out.println("  strength : " + eElement.getElementsByTagName("strength").item(0).getTextContent());
// 
//		}
//	}
// 
//	nList = doc.getElementsByTagName("staff");
// 
//	System.out.println("----------------------------");
// 
//	for (int temp = 0; temp < nList.getLength(); temp++) {
// 
//		Node nNode = nList.item(temp);
// 
//		System.out.println("\nCurrent Element :" + nNode.getNodeName());
// 
//		if (nNode.getNodeType() == Node.ELEMENT_NODE) {
// 
//			Element eElement = (Element) nNode;
// 
//			System.out.println("Staff id : " + eElement.getAttribute("id"));
//			System.out.println("First Name : " + eElement.getElementsByTagName("firstname").item(0).getTextContent());
//			System.out.println("Last Name : " + eElement.getElementsByTagName("lastname").item(0).getTextContent());
//			System.out.println("Nick Name : " + eElement.getElementsByTagName("nickname").item(0).getTextContent());
//			System.out.println("Salary : " + eElement.getElementsByTagName("salary").item(0).getTextContent());
// 
//		}
//	}

    } catch (Exception e) {
	e.printStackTrace();
    }
  }
 
}