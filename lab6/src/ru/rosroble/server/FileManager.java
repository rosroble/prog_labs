package ru.rosroble.server;

import ru.rosroble.common.data.*;
import ru.rosroble.common.exceptions.AccessDeniedException;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;


/**
 * A class used for working with files. Provides XML read and write operations. The file is specified with environment variable.
 */
public class FileManager {
    private ArrayList<Ticket> collection;
    private String envVar;
    private boolean isRead = false;

    public FileManager(ArrayList<Ticket> collection, String envVarName) {
        this.collection = collection;
        this.envVar = System.getenv(envVarName);
        if (envVar == null) {
            System.out.println("Предупреждение: не найдена переменная окружения, содержащая путь к xml файлу.");
        }
    }

    /**
     * @param collection - a collection to set for working with file manager.
     */
    public void setCollection(ArrayList<Ticket> collection) {
        this.collection = collection;
    }

    /**
     * Saves the collection into XML-file specified in envVar.
     */
    public void saveCollectionToFile() {
        if (envVar != null) {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = null;
            try {
                db = dbf.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }
            Document document = db.newDocument();
            Element root = document.createElement("collection");
            document.appendChild(root);

            // append the document with tickets
            for (Ticket t: collection) {
                createXMLTicketStructure(t, document);
            }

            // create the xml file
            //transform the DOM Object to an XML File
            transformIntoFile(document);


        } else {
            System.out.println("Ошибка записи. Переменная окружения с путём к файлу не найдена.");
            // System.out.println(System.getenv());
        }

    }

    /**
     * Creates an XML-structure via DOM-parser for the specified ticket.
     * @param t - a ticket to create a structure for
     * @param document - a document the structure is created in.
     */
    public void createXMLTicketStructure(Ticket t, Document document) {
        // root
        Element ticketRoot = document.createElement("ticket");
        document.getFirstChild().appendChild(ticketRoot);

        // id
        Attr attrId = document.createAttribute("id");
        attrId.setValue(String.valueOf(t.getId()));
        ticketRoot.setAttributeNode(attrId);

        // name
        Element name = document.createElement("name");
        name.appendChild(document.createTextNode(t.getName()));
        ticketRoot.appendChild(name);

        // coordinates
        Element coordinates = document.createElement("coordinates");
        Coordinates c = t.getCoordinates();
        Element x = document.createElement("x");
        Element y = document.createElement("y");
        x.appendChild(document.createTextNode(String.valueOf(c.getX())));
        y.appendChild(document.createTextNode(String.valueOf(c.getY())));
        coordinates.appendChild(x);
        coordinates.appendChild(y);
        ticketRoot.appendChild(coordinates);

        // creationDate
        Element date = document.createElement("creationDate");
        //DateTimeFormatter f = DateTimeFormatter.ofPattern("dd-mm-yyyy");
        date.appendChild(document.createTextNode(t.getCreationDate().toString()));
        ticketRoot.appendChild(date);

        // price
        Element price = document.createElement("price");
        price.appendChild(document.createTextNode(String.valueOf(t.getPrice())));
        ticketRoot.appendChild(price);

        // refundable
        Element ref = document.createElement("refundable");
        ref.appendChild(document.createTextNode(String.valueOf((t.getRefundable()))));
        ticketRoot.appendChild(ref);

        // TicketType
        Element ticketType = document.createElement("type");
        ticketType.appendChild(document.createTextNode(t.getType().name()));
        ticketRoot.appendChild(ticketType);

        // venue
        Element venue = document.createElement("venue");
        Venue v = t.getVenue();
        Element vId = document.createElement("id");
        Element vName = document.createElement("name");
        Element vCapacity = document.createElement("capacity");
        Element vType = document.createElement("type");
        vId.appendChild(document.createTextNode(String.valueOf(v.getId())));
        vName.appendChild(document.createTextNode(v.getName()));
        vCapacity.appendChild(document.createTextNode(String.valueOf(v.getCapacity())));
        vType.appendChild(document.createTextNode(v.getType().name()));
        venue.appendChild(vId);
        venue.appendChild(vName);
        venue.appendChild(vCapacity);
        venue.appendChild(vType);
        ticketRoot.appendChild(venue);
        // vAddress
        Element vAddress = document.createElement("address");
        Address a = v.getAddress();
        Element aStreet = document.createElement("street");
        aStreet.appendChild(document.createTextNode(a.getStreet()));
        vAddress.appendChild(aStreet);
        venue.appendChild(vAddress);
        // aLocation
        Element aLocation = document.createElement("town");
        Location l = a.getTown();
        Element lX = document.createElement("x");
        Element lY = document.createElement("y");
        Element lZ = document.createElement("z");
        Element lName = document.createElement("name");
        lX.appendChild(document.createTextNode(String.valueOf(l.getX())));
        lY.appendChild(document.createTextNode(String.valueOf(l.getY())));
        lZ.appendChild(document.createTextNode(String.valueOf(l.getZ())));
        lName.appendChild(document.createTextNode(l.getName()));
        aLocation.appendChild(lX);
        aLocation.appendChild(lY);
        aLocation.appendChild(lZ);
        aLocation.appendChild(lName);
        vAddress.appendChild(aLocation);
    }

    /**
     * Transforms a document object to a real XML-file on the machine.
     * @param document - a document object to transform into file
     */
    public void transformIntoFile(Document document) {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        try {
            BufferedOutputStream buffOutStr = getBuffOutStr();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(buffOutStr);
            transformer.transform(domSource, streamResult);
            System.out.println("Запись успешна.");
        } catch (FileNotFoundException e) {
            System.out.println("Ошибка записи. Файл по указанному в переменной окружения пути не найден.");
        } catch (TransformerException e) {
            System.out.println("Непредвиденная ошибка конфигурации.");
        } catch (AccessDeniedException e) {
            e.printMessage();
        }
    }

    /**
     * Reads all the tickets from the XML-file.
     * @return the ArrayList with Tickets parsed.
     */
    public ArrayList<Ticket> parseCollectionFromFile() {
        ArrayList<Ticket> tickets = new ArrayList<>();
        Ticket t;

        try {
            DocumentBuilderFactory f = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = f.newDocumentBuilder();
            Document document = db.parse(getBuffInStr());
            document.getDocumentElement().normalize();
            NodeList nodeList = document.getElementsByTagName("ticket");

            for (int index = 0; index < nodeList.getLength(); index++) {
                Node node = nodeList.item(index);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;
                    // building an object
                    t = new Ticket();
                    t.setId(Long.parseLong(e.getAttribute("id")));
                    t.setPrice(Double.parseDouble(e.getElementsByTagName("price").item(0).getTextContent()));
                    t.setName(e.getElementsByTagName("name").item(0).getTextContent());
                    t.setRefundable(Boolean.parseBoolean(e.getElementsByTagName("refundable").item(0).getTextContent()));
                    t.setType(TicketType.valueOf(e.getElementsByTagName("type").item(0).getTextContent()));
                    t.setCreationDate(LocalDate.parse(e.getElementsByTagName("creationDate").item(0).getTextContent()));
                    // coords
                    Element coordinates = (Element) (e.getElementsByTagName("coordinates").item(0));
                    t.setCoordinates(new Coordinates(Float.parseFloat(coordinates.getElementsByTagName("x").item(0).getTextContent()),
                            Long.parseLong(coordinates.getElementsByTagName("y").item(0).getTextContent())));
                    // venue
                    Element vE = (Element) (e.getElementsByTagName("venue").item(0));
                    Venue v = new Venue();
                    v.setId(Long.parseLong(vE.getElementsByTagName("id").item(0).getTextContent()));
                    v.setName(vE.getElementsByTagName("name").item(0).getTextContent());
                    v.setCapacity(Long.parseLong(vE.getElementsByTagName("capacity").item(0).getTextContent()));
                    v.setType(VenueType.valueOf(vE.getElementsByTagName("type").item(0).getTextContent()));
                    // address
                    Element aE = (Element) (vE.getElementsByTagName("address").item(0));
                    Element lE = (Element) (aE.getElementsByTagName("town").item(0));
                    Address a = new Address(
                            aE.getElementsByTagName("street").item(0).getTextContent(),
                            new Location(
                                    Long.parseLong(lE.getElementsByTagName("x").item(0).getTextContent()),
                                    Integer.parseInt(lE.getElementsByTagName("y").item(0).getTextContent()),
                                    Long.parseLong(lE.getElementsByTagName("z").item(0).getTextContent()),
                                    lE.getElementsByTagName("name").item(0).getTextContent()));
                    v.setAddress(a);
                    t.setVenue(v);
                    tickets.add(t);
                }
            }
            isRead = true;
        } catch (IOException e) {
            System.out.println("Ошибка чтения.");
        } catch (AccessDeniedException e) {
            e.printMessage();
        } catch (ParserConfigurationException e) {
            System.out.println("Ошибка конфигурации парсера.");
        } catch (SAXException e) {
            System.out.println("Ошибка парсинга. Проверьте структуру XML-файла.");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка парсинга. Проверьте правильность введенных данных.");
        } catch (NullPointerException e) {
            System.out.println("Ошибка парсинга. Проверьте, что файл существует и все необходимые поля заполнены.");
        }

        System.out.println("Объектов загружено: " + tickets.size());
        return tickets;

    }

    /**
     * Tries to access the file and creates an buffered output stream connected to this file.
     * @return BufferedOutputStream connected to the file specified in environment variable
     * @throws FileNotFoundException - no file found in the given path
     * @throws AccessDeniedException - the file is found, but the user has no rights to write into it.
     */
    public BufferedOutputStream getBuffOutStr() throws FileNotFoundException, AccessDeniedException {
        File file = new File(envVar);
        if (file.exists() && !file.canWrite()) throw new AccessDeniedException("Ошибка доступа. Нет прав на запись в файл.");
        return new BufferedOutputStream(new FileOutputStream(file));
    }

    /**
     * Tries to access the file and creates a buffered input stream connected to this file.
     * @return BufferedInputStream connected to the file specified in environment variable.
     * @throws FileNotFoundException - no file found in the given path
     * @throws AccessDeniedException - the file is found, but the user has no rights to read from it.
     */
    public BufferedInputStream getBuffInStr() throws FileNotFoundException, AccessDeniedException {
        File file = new File(envVar);
        if (file.exists() && !file.canRead()) throw new AccessDeniedException("Ошибка доступа. Нет прав на чтение файла.");
        return new BufferedInputStream(new FileInputStream(file));
    }

    public boolean isRead() {
        return isRead;
    }
}




