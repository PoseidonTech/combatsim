package com.rpg.combatsim.utility;

import com.rpg.combatsim.CombatSim;
import com.rpg.combatsim.domain.GameConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.SchemaFactory;
import java.io.File;

public final class FileImportManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(FileImportManager.class);

    private FileImportManager(){ // private default constructor
    }

    public static GameConfig gameConfigFileImport() {
        SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        GameConfig gameConfig = null;

        try  {
            JAXBContext jaxbContext = JAXBContext.newInstance(GameConfig.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            jaxbUnmarshaller.setSchema(factory.newSchema(new File(CombatSim.class.getResource("/gameConfig.xsd").getPath())));
            gameConfig = (GameConfig) jaxbUnmarshaller.unmarshal(new File(System.getProperty("gameConfig")));
        } catch (JAXBException | SAXException ex) {
            LOGGER.error("Error Loading gameConfig.xml", ex);
        }

        return gameConfig;
    }

}
