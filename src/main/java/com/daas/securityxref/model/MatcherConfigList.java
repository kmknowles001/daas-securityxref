package com.daas.securityxref.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class MatcherConfigList{

    private List<MatcherConfigItem> matchItemList;
    @Getter private List<MatcherConfigItem> matcherIdentifierList;
    @Getter private List<MatcherConfigItem> matcherQualifierList;

    public MatcherConfigList(){

        matchItemList = new ArrayList();
        matcherIdentifierList = new ArrayList<>();
        matcherQualifierList = new ArrayList<>();
    }

    // get by key
    public MatcherConfigItem get(String key){
        for(MatcherConfigItem configItem : matchItemList) {
            if(configItem.getCode().equals(key.toUpperCase())){
                return configItem;
            }
        }
        return null;
    }

    public int size() {
        return matchItemList.size();
    }

    public void add(MatcherConfigItem item){
        matchItemList.add(item);
        if (item.getCategory().equals("IDENTIFIER"))
            matcherIdentifierList.add(item);
        if (item.getCategory().equals("QUALIFIER"))
            matcherQualifierList.add(item);
    }
}
