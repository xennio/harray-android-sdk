package io.xenn.android.event;

import java.util.ArrayList;
import java.util.List;

public class ChainProcessorHandler {
    private List<AfterPageViewEventHandler> handlers = new ArrayList<>();

    public void addHandler(AfterPageViewEventHandler afterPageViewEventHandler){
        this.handlers.add(afterPageViewEventHandler);
    }

    public void callAll(String pageType){
        for (AfterPageViewEventHandler eachHandler : handlers) {
            eachHandler.callAfter(pageType);
        }
    }
}
