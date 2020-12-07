/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import com.md.nasutils.model.raidiator5.Options;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import java.util.HashMap;
import java.util.Map;


/**
 * Used to configure the Simple XML Framework to 
 * deserialize attributes into a single hash map
 * for {@link com.md.nasutils.model.raidiator5.Log} arguments
 * 
 * @author michaeldoyle
 */
public class Raidiator5OptionsConverter implements Converter<Options> {

    @Override
    public Options read(InputNode node) throws Exception {
        NodeMap<InputNode> nodeMap = node.getAttributes();
        
        Map<String, String> attributeMap = new HashMap<>();

        for (String inputNode : nodeMap) {
            attributeMap.put(
                    nodeMap.get(inputNode).getName(),
                    nodeMap.get(inputNode).getValue());
        }

        Options opts = new Options();
        opts.setOptions(attributeMap);
        
        return opts;
    }

    @Override
    public void write(OutputNode node, Options opts) {
        NodeMap<OutputNode> nodeMap = node.getAttributes();
        for (Map.Entry<String, String> o : opts.getOptions().entrySet()) {
            nodeMap.put(o.getKey(), o.getValue());
        }
    }
}
