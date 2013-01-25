/* Copyright 2011 Jacques Berger

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

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        CatalogDocument catalog = new CatalogDocument("xml/catalog.xml");
        
        // Lecture
        
        System.out.println("Catalogue complet:");
        List<CD> list = catalog.getCDList();
        for (CD cd : list) {
            System.out.println(" - " + cd.getTitle() + " de " + cd.getArtist());
        }
        System.out.println("Il y'a " + list.size() + " CD(s) dans la liste.");
        System.out.println();
        
        System.out.println("Albums parus depuis 1990:");
        list = catalog.getCDListSince(1990);
        for (CD cd : list) {
            System.out.println(" - " + cd.getTitle());
        }
        System.out.println("Il y'a " + list.size() + " CD(s) dans la liste.");
        System.out.println();
        
        System.out.println("Prix des albums encore en stocks");
        list = catalog.getCDListInStock();
        for (CD cd : list) {
            System.out.println(" - " + cd.getTitle() + ": " + cd.getPrice() + "$");
        }
        System.out.println("Il y'a " + list.size() + " CD(s) dans la liste.");
        
        // Ecriture
        
        // Création d'un nouveau cd
        CD cd = new CD();
        cd.setId(999);
        cd.setTitle("Californication");
        cd.setArtist("Red Hot Chili Peppers");
        cd.setInStock(true);
        cd.setPrice(14.99);
        cd.setYear(1999);
        // Ecriture dans le XML
        catalog.addCD(cd);
        catalog.saveToFile("xml/new_catalog.xml");
    }

}