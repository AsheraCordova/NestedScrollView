package com.ashera.nestedscrollview;
// start - imports
import java.util.*;

import r.android.annotation.SuppressLint;
import r.android.content.Context;
import r.android.os.Build;
import r.android.view.*;
import r.android.widget.*;
import r.android.view.View.*;

import com.ashera.widget.BaseHasWidgets;

import r.android.annotation.SuppressLint;

import com.ashera.core.IFragment;
import com.ashera.widget.bus.*;
import com.ashera.converter.*;
import com.ashera.widget.bus.Event.*;
import com.ashera.widget.*;
import com.ashera.widget.IWidgetLifeCycleListener.*;
import com.ashera.layout.*;

import org.teavm.jso.dom.html.HTMLElement;

import static com.ashera.widget.IWidget.*;
//end - imports
import androidx.core.widget.NestedScrollView;
public class NestedScrollViewImpl extends BaseHasWidgets {
	//start - body
	private HTMLElement htmlElement;
	public final static String LOCAL_NAME = "androidx.core.widget.NestedScrollView"; 
	public final static String GROUP_NAME = "androidx.core.widget.NestedScrollView";
	private androidx.core.widget.NestedScrollView nestedScrollView;
	

	
	@Override
	public void loadAttributes(String localName) {
		ViewGroupImpl.register(localName);

		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("foregroundGravity").withType("gravity"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("measureAllChildren").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("nestedScrollingEnabled").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("onScrollChange").withType("string"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("fillViewport").withType("boolean"));
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("smoothScrollingEnabled").withType("boolean"));
	
		WidgetFactory.registerAttribute(localName, new WidgetAttribute.Builder().withName("layout_gravity").withType("gravity").forChild());
	}
	
	public NestedScrollViewImpl() {
		super(GROUP_NAME, LOCAL_NAME);
	}
	public  NestedScrollViewImpl(String localname) {
		super(GROUP_NAME, localname);
	}
	public  NestedScrollViewImpl(String groupName, String localname) {
		super(groupName, localname);
	}

	@Override
	public IWidget newInstance() {
		return new NestedScrollViewImpl(groupName, localName);
	}
	
	@SuppressLint("NewApi")
	@Override
	public void create(IFragment fragment, Map<String, Object> params) {
		super.create(fragment, params);
		nestedScrollView = new NestedScrollViewExt();
		
		nativeCreate(params);
		
		
		ViewGroupImpl.registerCommandConveter(this);
	}

	@Override
	public Object asWidget() {
		return nestedScrollView;
	}

	@Override
	public boolean remove(IWidget w) {
		boolean remove = super.remove(w);
		nestedScrollView.removeView((View) w.asWidget());
		 nativeRemoveView(w);            
		return remove;
	}
	
	@Override
    public boolean remove(int index) {
		IWidget widget = widgets.get(index);
        boolean remove = super.remove(index);

        if (index + 1 <= nestedScrollView.getChildCount()) {
            nestedScrollView.removeViewAt(index);
            nativeRemoveView(widget);
        }    
        return remove;
    }

	private void nativeRemoveView(IWidget widget) {
		r.android.animation.LayoutTransition layoutTransition = nestedScrollView.getLayoutTransition();
		if (layoutTransition != null && (
				layoutTransition.isTransitionTypeEnabled(r.android.animation.LayoutTransition.CHANGE_DISAPPEARING) ||
				layoutTransition.isTransitionTypeEnabled(r.android.animation.LayoutTransition.DISAPPEARING)
				)) {
			addToBufferedRunnables(() -> ViewGroupImpl.nativeRemoveView(widget));          
		} else {
			ViewGroupImpl.nativeRemoveView(widget);
		}
	}
	
	@Override
	public void add(IWidget w, int index) {
		if (index != -2) {
			View view = (View) w.asWidget();
			createLayoutParams(view);
			    if (index == -1) {
			        nestedScrollView.addView(view);
			    } else {
			        nestedScrollView.addView(view, index);
			    }
		}
		
		ViewGroupImpl.nativeAddView(asNativeWidget(), w.asNativeWidget());
		super.add(w, index);
	}
	
	private void createLayoutParams(View view) {
		androidx.core.widget.NestedScrollView.LayoutParams layoutParams = (androidx.core.widget.NestedScrollView.LayoutParams) view.getLayoutParams();
		
		layoutParams = (androidx.core.widget.NestedScrollView.LayoutParams) view.getLayoutParams();
		if (layoutParams == null) {
			layoutParams = new androidx.core.widget.NestedScrollView.LayoutParams(-2, -2);
			view.setLayoutParams(layoutParams);
		}  else {
			layoutParams.height = -2;
			layoutParams.width = -2;
		}
	}
	
	private androidx.core.widget.NestedScrollView.LayoutParams getLayoutParams(View view) {
		return (androidx.core.widget.NestedScrollView.LayoutParams) view.getLayoutParams();		
	}
	
	@SuppressLint("NewApi")
	@Override
	public void setChildAttribute(IWidget w, WidgetAttribute key, String strValue, Object objValue) {
		View view = (View) w.asWidget();
		androidx.core.widget.NestedScrollView.LayoutParams layoutParams = getLayoutParams(view);
		ViewGroupImpl.setChildAttribute(w, key, objValue, layoutParams);		
		
		switch (key.getAttributeName()) {
		case "layout_width":
			layoutParams.width = (int) objValue;
			break;	
		case "layout_height":
			layoutParams.height = (int) objValue;
			break;
			case "layout_gravity": {
				
							layoutParams.gravity = ((int)objValue);
				
			}
			break;
		default:
			break;
		}
		
		
		view.setLayoutParams(layoutParams);		
	}
	
	@SuppressLint("NewApi")
	@Override
	public Object getChildAttribute(IWidget w, WidgetAttribute key) {
		Object attributeValue = ViewGroupImpl.getChildAttribute(w, key);		
		if (attributeValue != null) {
			return attributeValue;
		}
		View view = (View) w.asWidget();
		androidx.core.widget.NestedScrollView.LayoutParams layoutParams = getLayoutParams(view);

		switch (key.getAttributeName()) {
		case "layout_width":
			return layoutParams.width;
		case "layout_height":
			return layoutParams.height;
			case "layout_gravity": {
return layoutParams.gravity;			}

		}
		
		return null;

	}
	
		
	public class NestedScrollViewExt extends androidx.core.widget.NestedScrollView implements ILifeCycleDecorator, com.ashera.widget.IMaxDimension{
		private MeasureEvent measureFinished = new MeasureEvent();
		private OnLayoutEvent onLayoutEvent = new OnLayoutEvent();
		private List<IWidget> overlays;
		public IWidget getWidget() {
			return NestedScrollViewImpl.this;
		}
		private int mMaxWidth = -1;
		private int mMaxHeight = -1;
		@Override
		public void setMaxWidth(int width) {
			mMaxWidth = width;
		}
		@Override
		public void setMaxHeight(int height) {
			mMaxHeight = height;
		}
		@Override
		public int getMaxWidth() {
			return mMaxWidth;
		}
		@Override
		public int getMaxHeight() {
			return mMaxHeight;
		}

		public NestedScrollViewExt() {
			super();
			
		}
		
		@Override
		public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

			if(mMaxWidth > 0) {
	        	widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.AT_MOST);
	        }
	        if(mMaxHeight > 0) {
	            heightMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxHeight, MeasureSpec.AT_MOST);

	        }

	        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
			IWidgetLifeCycleListener listener = (IWidgetLifeCycleListener) getListener();
			if (listener != null) {
			    measureFinished.setWidth(getMeasuredWidth());
			    measureFinished.setHeight(getMeasuredHeight());
				listener.eventOccurred(EventId.measureFinished, measureFinished);
			}
			postOnMeasure(widthMeasureSpec, heightMeasureSpec);
		}
		
		@Override
		protected void onLayout(boolean changed, int l, int t, int r, int b) {
			super.onLayout(changed, l, t, r, b);
			ViewImpl.setDrawableBounds(NestedScrollViewImpl.this, l, t, r, b);
			if (!isOverlay()) {
			ViewImpl.nativeMakeFrame(asNativeWidget(), l, t, r, b, (int) (computeVerticalScrollRange()));
			}
			replayBufferedEvents();
	        ViewImpl.redrawDrawables(NestedScrollViewImpl.this);
	        overlays = ViewImpl.drawOverlay(NestedScrollViewImpl.this, overlays);
			
			IWidgetLifeCycleListener listener = (IWidgetLifeCycleListener) getListener();
			if (listener != null) {
				onLayoutEvent.setB(b);
				onLayoutEvent.setL(l);
				onLayoutEvent.setR(r);
				onLayoutEvent.setT(t);
				onLayoutEvent.setChanged(changed);
				listener.eventOccurred(EventId.onLayout, onLayoutEvent);
			}
			
			if (isInvalidateOnFrameChange() && isInitialised()) {
				NestedScrollViewImpl.this.invalidate();
			}
		}	
		
		@Override
		public void execute(String method, Object... canvas) {
			
		}

		public void updateMeasuredDimension(int width, int height) {
			setMeasuredDimension(width, height);
		}


		@Override
		public ILifeCycleDecorator newInstance(IWidget widget) {
			throw new UnsupportedOperationException();
		}

		@Override
		public void setAttribute(WidgetAttribute widgetAttribute,
				String strValue, Object objValue) {
			throw new UnsupportedOperationException();
		}		
		

		@Override
		public List<String> getMethods() {
			throw new UnsupportedOperationException();
		}
		
		@Override
		public void initialized() {
			throw new UnsupportedOperationException();
		}
		
        @Override
        public Object getAttribute(WidgetAttribute widgetAttribute) {
            throw new UnsupportedOperationException();
        }
        @Override
        public void drawableStateChanged() {
        	super.drawableStateChanged();
        	if (!isWidgetDisposed()) {
        		ViewImpl.drawableStateChanged(NestedScrollViewImpl.this);
        	}
        }
        private Map<String, IWidget> templates;
    	@Override
    	public r.android.view.View inflateView(java.lang.String layout) {
    		if (templates == null) {
    			templates = new java.util.HashMap<String, IWidget>();
    		}
    		IWidget template = templates.get(layout);
    		if (template == null) {
    			template = (IWidget) quickConvert(layout, "template");
    			templates.put(layout, template);
    		}
    		
    		IWidget widget = template.loadLazyWidgets(NestedScrollViewImpl.this);
			return (View) widget.asWidget();
    	}   
        
    	@Override
		public void remeasure() {
    		if (getFragment() != null) {
    			getFragment().remeasure();
    		}
		}
    	
        @Override
		public void removeFromParent() {
        	NestedScrollViewImpl.this.getParent().remove(NestedScrollViewImpl.this);
		}
        @Override
        public void getLocationOnScreen(int[] appScreenLocation) {
        	appScreenLocation[0] = htmlElement.getBoundingClientRect().getLeft();
        	appScreenLocation[1] = htmlElement.getBoundingClientRect().getTop();
        }
        @Override
        public void getWindowVisibleDisplayFrame(r.android.graphics.Rect displayFrame){
        	
        	org.teavm.jso.dom.html.TextRectangle boundingClientRect = htmlElement.getBoundingClientRect();
			displayFrame.top = boundingClientRect.getTop();
        	displayFrame.left = boundingClientRect.getLeft();
        	displayFrame.bottom = boundingClientRect.getBottom();
        	displayFrame.right = boundingClientRect.getRight();
        }
        @Override
		public void offsetTopAndBottom(int offset) {
			super.offsetTopAndBottom(offset);
			ViewImpl.nativeMakeFrame(asNativeWidget(), getLeft(), getTop(), getRight(), getBottom());
		}
		@Override
		public void offsetLeftAndRight(int offset) {
			super.offsetLeftAndRight(offset);
			ViewImpl.nativeMakeFrame(asNativeWidget(), getLeft(), getTop(), getRight(), getBottom());
		}
		@Override
		public void setMyAttribute(String name, Object value) {
			if (name.equals("state0")) {
				setState0(value);
				return;
			}
			if (name.equals("state1")) {
				setState1(value);
				return;
			}
			if (name.equals("state2")) {
				setState2(value);
				return;
			}
			if (name.equals("state3")) {
				setState3(value);
				return;
			}
			if (name.equals("state4")) {
				setState4(value);
				return;
			}
			NestedScrollViewImpl.this.setAttribute(name, value, !(value instanceof String));
		}
        @Override
        public void setVisibility(int visibility) {
            super.setVisibility(visibility);
            ((HTMLElement)asNativeWidget()).getStyle().setProperty("display", visibility != View.VISIBLE ? "none" : "block");
            
        }
        
    	public void setState0(Object value) {
    		ViewImpl.setState(NestedScrollViewImpl.this, 0, value);
    	}
    	public void setState1(Object value) {
    		ViewImpl.setState(NestedScrollViewImpl.this, 1, value);
    	}
    	public void setState2(Object value) {
    		ViewImpl.setState(NestedScrollViewImpl.this, 2, value);
    	}
    	public void setState3(Object value) {
    		ViewImpl.setState(NestedScrollViewImpl.this, 3, value);
    	}
    	public void setState4(Object value) {
    		ViewImpl.setState(NestedScrollViewImpl.this, 4, value);
    	}
        	public void state0() {
        		ViewImpl.state(NestedScrollViewImpl.this, 0);
        	}
        	public void state1() {
        		ViewImpl.state(NestedScrollViewImpl.this, 1);
        	}
        	public void state2() {
        		ViewImpl.state(NestedScrollViewImpl.this, 2);
        	}
        	public void state3() {
        		ViewImpl.state(NestedScrollViewImpl.this, 3);
        	}
        	public void state4() {
        		ViewImpl.state(NestedScrollViewImpl.this, 4);
        	}
                        
        public void stateYes() {
        	ViewImpl.stateYes(NestedScrollViewImpl.this);
        	
        }
        
        public void stateNo() {
        	ViewImpl.stateNo(NestedScrollViewImpl.this);
        }
     
		@Override
		public void endViewTransition(r.android.view.View view) {
			super.endViewTransition(view);
			runBufferedRunnables();
		}
	
	}
	@Override
	public Class getViewClass() {
		return NestedScrollViewExt.class;
	}
	
	@SuppressLint("NewApi")
	@Override
	public void setAttribute(WidgetAttribute key, String strValue, Object objValue, ILifeCycleDecorator decorator) {
				ViewGroupImpl.setAttribute(this, key, strValue, objValue, decorator);
		Object nativeWidget = asNativeWidget();
		switch (key.getAttributeName()) {
			case "foregroundGravity": {


	nestedScrollView.setForegroundGravity((int)objValue);



			}
			break;
			case "measureAllChildren": {


	nestedScrollView.setMeasureAllChildren((boolean)objValue);



			}
			break;
			case "nestedScrollingEnabled": {


	nestedScrollView.setNestedScrollingEnabled((boolean)objValue);



			}
			break;
			case "onScrollChange": {


		setOnScrollChangeListener(objValue);



			}
			break;
			case "fillViewport": {


	nestedScrollView.setFillViewport((boolean)objValue);



			}
			break;
			case "smoothScrollingEnabled": {


	nestedScrollView.setSmoothScrollingEnabled((boolean)objValue);



			}
			break;
		default:
			break;
		}
		postSetAttribute(key, strValue, objValue, decorator);
	}
	
	@Override
	@SuppressLint("NewApi")
	public Object getAttribute(WidgetAttribute key, ILifeCycleDecorator decorator) {
		Object attributeValue = ViewGroupImpl.getAttribute(this, key, decorator);
		if (attributeValue != null) {
			return attributeValue;
		}
		Object nativeWidget = asNativeWidget();
		switch (key.getAttributeName()) {
			case "measureAllChildren": {
if (Build.VERSION.SDK_INT >= 14) {
return nestedScrollView.getMeasureAllChildren();
}
break;			}
		}
		return null;
	}


    
    @Override
    public void requestLayout() {
    	if (isInitialised()) {
    		ViewImpl.requestLayout(this, asNativeWidget());
    	}
    }
    
    @Override
    public void invalidate() {
    	if (isInitialised()) {
    		ViewImpl.invalidate(this, asNativeWidget());
    	}
    }
    
	

	@Override
	public Object asNativeWidget() {
		return htmlElement;
	}

    
    @org.teavm.jso.JSBody(params = {}, script = "return window.getScrollbarWidth();")
    private static native int getScrollbarWidth();
    @Override
    public void initialized() {
    	super.initialized();
    	thumbWidth = getScrollbarWidth();
    }
	private void nativeCreate(Map<String, Object> params) {
		htmlElement = org.teavm.jso.dom.html.HTMLDocument.current().createElement("div");
		htmlElement.getStyle().setProperty("overflow-y", "auto");
		htmlElement.getStyle().setProperty("overflow-x", "hidden");
		htmlElement.getStyle().setProperty("box-sizing", "border-box");
		
		ViewImpl.setOnListener(this, htmlElement, new org.teavm.jso.dom.events.EventListener<org.teavm.jso.dom.events.Event>() {
            @Override
            public void handleEvent(org.teavm.jso.dom.events.Event event) {
                int selection = htmlElement.getScrollTop();
                handleScroll(selection, 1);
            }
    	    
    	}, "scroll", "scroll");
	}
	


	private void setOnScrollChangeListener(Object objValue) {
		if (objValue instanceof String) {
			nestedScrollView.setOnScrollChangeListener(new OnScrollChangeListener(this, (String) objValue, "onScrollChange"));
		} else {
			nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) objValue);
		}
	}
	


	private int oldScrollY = 0;
	private int nestedScrollStopDelay = 700;
	private long lastScrollEvent;
	private r.android.os.Handler nestedScrollHandler = new r.android.os.Handler();
	private final int[] mScrollOffset = new int[2];
	private final int[] mScrollConsumed = new int[2];

	private void handleScroll(int selection, int eventDetail) {
		nestedScrollHandler.removeCallbacksAndMessages(null);
		long currentTime = System.currentTimeMillis();
		
		if (currentTime - lastScrollEvent >= nestedScrollStopDelay) {
			nestedScrollView.startNestedScroll(androidx.core.view.ViewCompat.SCROLL_AXIS_VERTICAL, androidx.core.view.ViewCompat.TYPE_TOUCH);	
		}
		nestedScrollHandler.postDelayed(() -> {
			nestedScrollView.stopNestedScroll();
		}, nestedScrollStopDelay);
		lastScrollEvent = System.currentTimeMillis();
	    final int scrolledDeltaY = selection - oldScrollY;
	    final int unconsumedY = 0;
	
	    if (nestedScrollView.dispatchNestedPreScroll(0, scrolledDeltaY, mScrollConsumed, mScrollOffset,
	    		androidx.core.view.ViewCompat.TYPE_TOUCH)) {
	    	// TODO - in ios
	    }
	    
	    mScrollConsumed[1] = 0;
	
	    nestedScrollView.dispatchNestedScroll(0, scrolledDeltaY, 0, unconsumedY, mScrollOffset,
	    		androidx.core.view.ViewCompat.TYPE_TOUCH, mScrollConsumed);
	    if(eventDetail == 0 && (selection == 0 || hasReachedEnd(selection))) {
	    	setEnabled(false);
	    	new r.android.os.Handler().postDelayed(() -> {
	    		setEnabled(true);
	    	}, 100);
	    }
	    androidx.core.widget.NestedScrollView.OnScrollChangeListener onScrollChangeListener = nestedScrollView.getOnScrollChangeListener();
		if (onScrollChangeListener != null) {
	    	onScrollChangeListener.onScrollChange(nestedScrollView, 0, selection, 0, oldScrollY);
	    }
	    nestedScrollView.getViewTreeObserver().dispatchOnScrollChanged();
	    oldScrollY = selection;
	}

	private void postSetAttribute(WidgetAttribute key, String strValue, Object objValue,
			ILifeCycleDecorator decorator) {
		switch (key.getAttributeName()) {
		case "nestedScrollingEnabled":
			setEnabled((boolean) objValue);
			break;

		default:
			break;
		}
	}

	


	private int thumbWidth = 0; 
	private void postOnMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		nestedScrollView.adjustPaddingIfScrollBarPresent(widthMeasureSpec, heightMeasureSpec, thumbWidth);
	}
	

	@SuppressLint("NewApi")
private static class OnScrollChangeListener implements NestedScrollView.OnScrollChangeListener, com.ashera.widget.IListener{
private IWidget w; private View view; private String strValue; private String action;
public String getAction() {return action;}
public OnScrollChangeListener(IWidget w, String strValue)  {
this.w = w; this.strValue = strValue;
}
public OnScrollChangeListener(IWidget w, String strValue, String action)  {
this.w = w; this.strValue = strValue;this.action=action;
}
public void onScrollChange(NestedScrollView v,int scrollX,int scrollY,int oldScrollX,int oldScrollY){
    
	if (action == null || action.equals("onScrollChange")) {
		// populate the data from ui to pojo
		w.syncModelFromUiToPojo("onScrollChange");
	    java.util.Map<String, Object> obj = getOnScrollChangeEventObj(v,scrollX,scrollY,oldScrollX,oldScrollY);
	    String commandName =  (String) obj.get(EventExpressionParser.KEY_COMMAND_NAME);
	    
	    // execute command based on command type
	    String commandType = (String)obj.get(EventExpressionParser.KEY_COMMAND_TYPE);
		switch (commandType) {
		case "+":
		    if (EventCommandFactory.hasCommand(commandName)) {
		    	 EventCommandFactory.getCommand(commandName).executeCommand(w, obj, v,scrollX,scrollY,oldScrollX,oldScrollY);
		    }

			break;
		default:
			break;
		}
		
		if (obj.containsKey("refreshUiFromModel")) {
			Object widgets = obj.remove("refreshUiFromModel");
			com.ashera.layout.ViewImpl.refreshUiFromModel(w, widgets, true);
		}
		if (w.getModelUiToPojoEventIds() != null) {
			com.ashera.layout.ViewImpl.refreshUiFromModel(w, w.getModelUiToPojoEventIds(), true);
		}
		if (strValue != null && !strValue.isEmpty() && !strValue.trim().startsWith("+")) {
		    com.ashera.core.IActivity activity = (com.ashera.core.IActivity)w.getFragment().getRootActivity();
		    if (activity != null) {
		    	activity.sendEventMessage(obj);
		    }
		}
	}
    return;
}//#####

public java.util.Map<String, Object> getOnScrollChangeEventObj(NestedScrollView v,int scrollX,int scrollY,int oldScrollX,int oldScrollY) {
	java.util.Map<String, Object> obj = com.ashera.widget.PluginInvoker.getJSONCompatMap();
    obj.put("action", "action");
    obj.put("eventType", "scrollchange");
    obj.put("fragmentId", w.getFragment().getFragmentId());
    obj.put("actionUrl", w.getFragment().getActionUrl());
    
    if (w.getComponentId() != null) {
    	obj.put("componentId", w.getComponentId());
    }
    
    PluginInvoker.putJSONSafeObjectIntoMap(obj, "id", w.getId());
     
        PluginInvoker.putJSONSafeObjectIntoMap(obj, "scrollX", scrollX);
        PluginInvoker.putJSONSafeObjectIntoMap(obj, "scrollY", scrollY);
        PluginInvoker.putJSONSafeObjectIntoMap(obj, "oldScrollX", oldScrollX);
        PluginInvoker.putJSONSafeObjectIntoMap(obj, "oldScrollY", oldScrollY);
    
    // parse event info into the map
    EventExpressionParser.parseEventExpression(strValue, obj);
    
    // update model data into map
    w.updateModelToEventMap(obj, "onScrollChange", (String)obj.get(EventExpressionParser.KEY_EVENT_ARGS));
    return obj;
}
}


	@Override
	public void setId(String id){
		if (id != null && !id.equals("")){
			super.setId(id);
			nestedScrollView.setId((int) quickConvert(id, "id"));
		}
	}
	
    
    @Override
    public void setVisible(boolean b) {
        ((View)asWidget()).setVisibility(b ? View.VISIBLE : View.GONE);
    }

	
private NestedScrollViewCommandBuilder builder;
private NestedScrollViewBean bean;
public Object getPlugin(String plugin) {
	return WidgetFactory.getAttributable(plugin).newInstance(this);
}
public NestedScrollViewBean getBean() {
	if (bean == null) {
		bean = new NestedScrollViewBean();
	}
	return bean;
}
public NestedScrollViewCommandBuilder getBuilder() {
	if (builder == null) {
		builder = new NestedScrollViewCommandBuilder();
	}
	return builder;
}


public  class NestedScrollViewCommandBuilder extends com.ashera.layout.ViewGroupImpl.ViewGroupCommandBuilder <NestedScrollViewCommandBuilder> {
    public NestedScrollViewCommandBuilder() {
	}
	
	public NestedScrollViewCommandBuilder execute(boolean setter) {
		if (setter) {
			executeCommand(command, null, IWidget.COMMAND_EXEC_SETTER_METHOD);
			getFragment().remeasure();
		}
		executeCommand(command, null, IWidget.COMMAND_EXEC_GETTER_METHOD);
return this;	}

public NestedScrollViewCommandBuilder setForegroundGravity(String value) {
	Map<String, Object> attrs = initCommand("foregroundGravity");
	attrs.put("type", "attribute");
	attrs.put("setter", true);
	attrs.put("orderSet", ++orderSet);

	attrs.put("value", value);
return this;}
public NestedScrollViewCommandBuilder tryGetMeasureAllChildren() {
	Map<String, Object> attrs = initCommand("measureAllChildren");
	attrs.put("type", "attribute");
	attrs.put("getter", true);
	attrs.put("orderGet", ++orderGet);
return this;}

public Object isMeasureAllChildren() {
	Map<String, Object> attrs = initCommand("measureAllChildren");
	return attrs.get("commandReturnValue");
}
public NestedScrollViewCommandBuilder setMeasureAllChildren(boolean value) {
	Map<String, Object> attrs = initCommand("measureAllChildren");
	attrs.put("type", "attribute");
	attrs.put("setter", true);
	attrs.put("orderSet", ++orderSet);

	attrs.put("value", value);
return this;}
public NestedScrollViewCommandBuilder setNestedScrollingEnabled(boolean value) {
	Map<String, Object> attrs = initCommand("nestedScrollingEnabled");
	attrs.put("type", "attribute");
	attrs.put("setter", true);
	attrs.put("orderSet", ++orderSet);

	attrs.put("value", value);
return this;}
public NestedScrollViewCommandBuilder setOnScrollChange(String value) {
	Map<String, Object> attrs = initCommand("onScrollChange");
	attrs.put("type", "attribute");
	attrs.put("setter", true);
	attrs.put("orderSet", ++orderSet);

	attrs.put("value", value);
return this;}
public NestedScrollViewCommandBuilder setFillViewport(boolean value) {
	Map<String, Object> attrs = initCommand("fillViewport");
	attrs.put("type", "attribute");
	attrs.put("setter", true);
	attrs.put("orderSet", ++orderSet);

	attrs.put("value", value);
return this;}
public NestedScrollViewCommandBuilder setSmoothScrollingEnabled(boolean value) {
	Map<String, Object> attrs = initCommand("smoothScrollingEnabled");
	attrs.put("type", "attribute");
	attrs.put("setter", true);
	attrs.put("orderSet", ++orderSet);

	attrs.put("value", value);
return this;}
}
public class NestedScrollViewBean extends com.ashera.layout.ViewGroupImpl.ViewGroupBean{
		public NestedScrollViewBean() {
			super(NestedScrollViewImpl.this);
		}
public void setForegroundGravity(String value) {
	getBuilder().reset().setForegroundGravity(value).execute(true);
}

public Object isMeasureAllChildren() {
	return getBuilder().reset().tryGetMeasureAllChildren().execute(false).isMeasureAllChildren(); 
}
public void setMeasureAllChildren(boolean value) {
	getBuilder().reset().setMeasureAllChildren(value).execute(true);
}

public void setNestedScrollingEnabled(boolean value) {
	getBuilder().reset().setNestedScrollingEnabled(value).execute(true);
}

public void setOnScrollChange(String value) {
	getBuilder().reset().setOnScrollChange(value).execute(true);
}

public void setFillViewport(boolean value) {
	getBuilder().reset().setFillViewport(value).execute(true);
}

public void setSmoothScrollingEnabled(boolean value) {
	getBuilder().reset().setSmoothScrollingEnabled(value).execute(true);
}

}


private NestedScrollViewCommandParamsBuilder paramsBuilder;
private NestedScrollViewParamsBean paramsBean;

public NestedScrollViewParamsBean getParamsBean() {
	if (paramsBean == null) {
		paramsBean = new NestedScrollViewParamsBean();
	}
	return paramsBean;
}
public NestedScrollViewCommandParamsBuilder getParamsBuilder() {
	if (paramsBuilder == null) {
		paramsBuilder = new NestedScrollViewCommandParamsBuilder();
	}
	return paramsBuilder;
}



public class NestedScrollViewParamsBean extends com.ashera.layout.ViewGroupImpl.ViewGroupParamsBean{
public Object getLayoutGravity(IWidget w) {
	java.util.Map<String, Object> layoutParams = new java.util.HashMap<String, Object>();
	java.util.Map<String, Object> command = getParamsBuilder().reset().tryGetLayoutGravity().getCommand();
	
	layoutParams.put("layoutParams", command);
	w.executeCommand(layoutParams, null, COMMAND_EXEC_GETTER_METHOD); 
	return getParamsBuilder().getLayoutGravity();
}
public void setLayoutGravity(IWidget w, String value) {
	java.util.Map<String, Object> layoutParams = new java.util.HashMap<String, Object>();
	layoutParams.put("layoutParams", getParamsBuilder().reset().setLayoutGravity(value).getCommand());
	w.executeCommand(layoutParams, null, COMMAND_EXEC_SETTER_METHOD);
	w.getFragment().remeasure();
}

}





public class NestedScrollViewCommandParamsBuilder extends com.ashera.layout.ViewGroupImpl.ViewGroupCommandParamsBuilder<NestedScrollViewCommandParamsBuilder>{
public NestedScrollViewCommandParamsBuilder tryGetLayoutGravity() {
	Map<String, Object> attrs = initCommand("layout_gravity");
	attrs.put("type", "attribute");
	attrs.put("getter", true);
	attrs.put("orderGet", ++orderGet);
return this;}

public Object getLayoutGravity() {
	Map<String, Object> attrs = initCommand("layout_gravity");
	return attrs.get("commandReturnValue");
}
public NestedScrollViewCommandParamsBuilder setLayoutGravity(String value) {
	Map<String, Object> attrs = initCommand("layout_gravity");
	attrs.put("type", "attribute");
	attrs.put("setter", true);
	attrs.put("orderSet", ++orderSet);

	attrs.put("value", value);
return this;}
}

	//end - body

	private boolean hasReachedEnd(int selection) {
		return (htmlElement.getScrollTop() + htmlElement.getOffsetHeight()) >= (htmlElement.getScrollHeight());
	}
	
	private void setEnabled(boolean b) {
		if (b) {
			htmlElement.getStyle().setProperty("overflow-y", "auto");
		} else {
			htmlElement.getStyle().setProperty("overflow-y", "hidden");
		}
		
	}

}
