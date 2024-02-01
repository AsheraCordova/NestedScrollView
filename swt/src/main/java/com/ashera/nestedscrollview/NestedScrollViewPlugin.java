package com.ashera.nestedscrollview;

import com.ashera.widget.WidgetFactory;

public class NestedScrollViewPlugin  {
    public static void initPlugin() {
    	//start - widgets
        WidgetFactory.register(new com.ashera.nestedscrollview.NestedScrollViewImpl());
        //end - widgets
    }
}
