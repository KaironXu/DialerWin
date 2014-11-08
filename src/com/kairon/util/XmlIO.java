package com.kairon.util;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class XmlIO {

	/**
	 * ��ȡXML�ļ��е�����
	 * @param fileName �ļ���
	 * @return XML�ļ��еļ�ֵ��
	 */
	public static Map<String, Map<String, String>> read(String fileName) {
		Map<String, Map<String, String>> info = new HashMap<String, Map<String, String>>();

		String filePath = System.getProperty("user.dir") + File.separator
				+ "res" + File.separator + fileName; // ��ȡXML�ļ�Ŀ¼
		DocumentBuilderFactory builderFactory = DocumentBuilderFactory
				.newInstance(); // ����������
		Document document = null;

		DocumentBuilder builder;
		try {
			builder = builderFactory.newDocumentBuilder(); // ������
			document = builder.parse(new File(filePath)); // �ĵ���ģ��

			Element rootElement = document.getDocumentElement();
			Map<String, String> userMap = new HashMap<String, String>();
			NodeList userNodeList = rootElement.getElementsByTagName("user");
			if (userNodeList != null) {
				Element element = (Element) userNodeList.item(0);
				String userID = element.getElementsByTagName("ID").item(0)
						.getTextContent();
				userMap.put("ID", userID);
				String password = element.getElementsByTagName("password")
						.item(0).getTextContent();
				userMap.put("password", password);
			}

			Map<String, String> routerMap = new HashMap<String, String>();
			NodeList routerNodeList = rootElement
					.getElementsByTagName("router");
			if (routerNodeList != null) {
				Element element = (Element) routerNodeList.item(0);
				String IP = element.getElementsByTagName("IP").item(0)
						.getTextContent();
				routerMap.put("IP", IP);
				String username = element.getElementsByTagName("username")
						.item(0).getTextContent();
				routerMap.put("username", username);
				String password = element.getElementsByTagName("password")
						.item(0).getTextContent();
				routerMap.put("password", password);
			}

			if (routerMap != null && userMap != null) {
				info.put("user", userMap);
				info.put("router", routerMap);
			}
		} catch (Exception e) {
		}
		return info;
	}

	public static void write(String userID,String password,String IProuter,
			String usernameRouter,String passwordRouter) {
		//ʵ����������
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			//����Document����
			Document doc = builder.newDocument();
			//����XML�ļ�����ĸ��ֶ������л�
			Element info = doc.createElement("info");
			
			Element user = doc.createElement("user");
			Element eUserID = doc.createElement("ID");
			Element ePassword = doc.createElement("password");
			Text txUserID = doc.createTextNode(userID);
			Text txPassword = doc.createTextNode(password);
			eUserID.appendChild(txUserID);
			ePassword.appendChild(txPassword);
			user.appendChild(eUserID);
			user.appendChild(ePassword);
			
			Element router = doc.createElement("router");
			Element eIPRouter = doc.createElement("IP");
			Element eUsernameRouter = doc.createElement("username");
			Element ePasswordRouter = doc.createElement("password");
			Text txIPRouter = doc.createTextNode(IProuter);
			Text txUsernameRouter = doc.createTextNode(usernameRouter);
			Text txPasswordRouter = doc.createTextNode(passwordRouter);
			eIPRouter.appendChild(txIPRouter);
			eUsernameRouter.appendChild(txUsernameRouter);
			ePasswordRouter.appendChild(txPasswordRouter);
			router.appendChild(eIPRouter);
			router.appendChild(eUsernameRouter);
			router.appendChild(ePasswordRouter);
			
			info.appendChild(user);
			info.appendChild(router);
			
			doc.appendChild(info);
			
			doc2XmlFile(doc,"UserInfo.xml");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
	}
	
	public static boolean doc2XmlFile(Document document, String fileName) {
	   boolean flag = true;
	   try {
	    TransformerFactory tFactory = TransformerFactory.newInstance();
	    Transformer transformer = tFactory.newTransformer();
	    /** ���� */
	    // transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	    DOMSource source = new DOMSource(document);
	    String filePath = System.getProperty("user.dir") + File.separator
				+ "res" + File.separator + fileName; // ��ȡXML�ļ�Ŀ¼
	    StreamResult result = new StreamResult(new File(filePath));
	    transformer.transform(source, result);
	   } catch (Exception ex) {
	    flag = false;
	    ex.printStackTrace();
	   }
	   return flag;
	}
	
}
