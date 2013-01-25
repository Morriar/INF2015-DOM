/* Copyright 2013 Alexandre Terrasa

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
package org.moz.domcds;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

public class CatalogDocument {
    
    private Document document;
    
    public CatalogDocument(String documentFilePath) 
            throws ParserConfigurationException, SAXException, IOException {
        parseXmlDocument(documentFilePath);
    }

    private void parseXmlDocument(String documentFilePath) 
            throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory documentFactory = initializeDocumentFactory();
        DocumentBuilder parser = documentFactory.newDocumentBuilder();
        document = parser.parse(documentFilePath);
    }

    private DocumentBuilderFactory initializeDocumentFactory() {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentFactory.setIgnoringComments(true);
        documentFactory.setCoalescing(true);
        documentFactory.setNamespaceAware(true);
        return documentFactory;
    }
    
    public void saveToFile(String filePath) throws Exception {
        Source domSource = new DOMSource(document);
        File xmlFile = new File(filePath);
        Result serializationResult = new StreamResult(xmlFile);
        Transformer xmlTransformer = TransformerFactory.newInstance().newTransformer();
        xmlTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
        xmlTransformer.transform(domSource, serializationResult);
    }
    
    /* Catalog methods */
    
    /*
     * Get a Node and return a CD object
     */
    public CD node2CD(Node node) {
        CD cd = new CD();
        
        // get cd id from attributes
        int id = Integer.parseInt(node.getAttributes().getNamedItem("id").getNodeValue());
        cd.setId(id);
        
        // get node children
        NodeList fields = node.getChildNodes();
        
        for (int i = 0; i < fields.getLength(); i++) {
            Node field = fields.item(i);
            switch (field.getNodeName()) {
                case "title":
                    cd.setTitle(field.getTextContent());
                    break;
                case "artist":
                    cd.setArtist(field.getTextContent());
                    break;
                case "instock":
                    if(field.getTextContent().equals("true")) {
                        cd.setInStock(true);
                    } else {
                        cd.setInStock(false);
                    }
                    break;
                case "price":
                    cd.setPrice(Double.parseDouble(field.getTextContent()));
                    break;
                case "year":
                    cd.setYear(Integer.parseInt(field.getTextContent()));
                    break;
            }
        }
        return cd;
    }
    
    public List<CD> getCDList() {
        List<CD> list = new ArrayList();
        NodeList cds = document.getElementsByTagName("cd");
        for (int i = 0; i < cds.getLength(); i++){
            CD cd = node2CD(cds.item(i));
            list.add(cd);
        }
        return list;
    }
    
    public List<CD> getCDListSince(int year) {
        List<CD> list = new ArrayList();
        for (CD cd : getCDList()){
            if(cd.getYear() >= year) {
               list.add(cd);
            }
        }
        return list;
    }
    
    public List<CD> getCDListInStock() {
        List<CD> list = new ArrayList();
        for (CD cd : getCDList()){
            if(cd.isInStock()) {
               list.add(cd);
            }
        }
        return list;
    }
      
    public void addCD(CD cd) {
        Element newCD = document.createElement("cd");
        newCD.setAttribute("id", cd.getId().toString());
        
        Element title = document.createElement("title");
        title.setTextContent(cd.getTitle());
        newCD.appendChild(title);
        
        Element artist = document.createElement("artist");
        artist.setTextContent(cd.getArtist());
        newCD.appendChild(artist);
        
        Element instock = document.createElement("instock");
        if(cd.isInStock()) {
            instock.setTextContent("true");
        } else {
            instock.setTextContent("false");
        }
        newCD.appendChild(instock);
        
        Element price = document.createElement("price");
        price.setTextContent(cd.getPrice().toString());
        newCD.appendChild(price);
        
        Element year = document.createElement("year");
        year.setTextContent(cd.getYear().toString());
        newCD.appendChild(year);
        
        document.getDocumentElement().appendChild(newCD);
    }
}
