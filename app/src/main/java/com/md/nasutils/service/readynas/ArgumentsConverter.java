/* 
 * Copyright 2013-2020 Michael J Doyle
 */
package com.md.nasutils.service.readynas;

import com.md.nasutils.model.os6.Arguments;

import org.simpleframework.xml.convert.Converter;
import org.simpleframework.xml.stream.InputNode;
import org.simpleframework.xml.stream.NodeMap;
import org.simpleframework.xml.stream.OutputNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Used to configure the Simple XML Framework to 
 * deserialize attributes into a single hash map
 * for {@link com.md.nasutils.model.os6.Log} arguments
 * 
 * @author michaeldoyle
 */
public class ArgumentsConverter implements Converter<Arguments> {

    @Override
    public Arguments read(InputNode node) throws Exception {
        NodeMap<InputNode> nodeMap = node.getAttributes();
        
        Map<String, String> attributeMap = new HashMap<>();

        for (String inputNode : nodeMap) {
            attributeMap.put(
                    nodeMap.get(inputNode).getName(),
                    nodeMap.get(inputNode).getValue());
        }

        Arguments args = new Arguments();
        args.setArgs(attributeMap);
        
        return args;
    }

    @Override
    public void write(OutputNode node, Arguments args) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
