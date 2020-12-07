/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.OutputNode;

import java.util.HashMap;
import java.util.Map;


/**
 * Used to configure the Simple XML Framework to 
 * deserialize attributes into a single hash map
 * for {@link com.md.nasutils.model.raidiator5.SmartInfo} attributes
 * 
 * @author michaeldoyle
 */
public class Raidiator5SmartAttributesConverter implements Converter<Map<String, String>> {

    @Override
    public Map<String, String >read(InputNode node) throws Exception {
        
        Map<String, String> attributeMap = new HashMap<>();

        InputNode inputNode = node.getNext();
        while(inputNode != null) {
            String name = inputNode.getName().replace("_", " ");
            String value = inputNode.getValue();
            attributeMap.put(name, value);
            inputNode = node.getNext();
        }
        
        return attributeMap;
    }

    @Override
    public void write(OutputNode node, Map<String, String> attributes) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
