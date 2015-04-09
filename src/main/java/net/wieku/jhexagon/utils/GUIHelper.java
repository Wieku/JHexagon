package net.wieku.jhexagon.utils;

import net.wieku.jhexagon.resources.FontManager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton.ImageButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List.ListStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.SelectBox.SelectBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Slider.SliderStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextField.TextFieldStyle;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import java.util.HashMap;

public class GUIHelper{

	public static HashMap<Integer, LabelStyle> styles = new HashMap<>();


	public static Table getTable(Color background){
		Table table = new Table();
		table.setBackground(getTxRegion(background));
		return table;
	}
	
	public static LabelStyle getLabelStyle(Color color, int size){
		LabelStyle stl = new LabelStyle();
		stl.font =  FontManager.getFont(FontManager.MAIN, size);
		stl.font.setMarkupEnabled(true);
		stl.fontColor = color.cpy();
		styles.put(size, stl);
		return stl;
	}
	
	public static LabelStyle getLabelStyle(Color background, Color color, int size){
		LabelStyle stl = new LabelStyle();
		stl.background = getTxRegion(background);
		stl.font = FontManager.getFont(FontManager.MAIN, size);
		stl.fontColor = color.cpy();
		styles.put(size, stl);
		return stl;
	}
	
	public static ProgressBarStyle getProgressBarStyle(Color bg,Color bar, int weight){
		ProgressBarStyle stl = new ProgressBarStyle();
		stl.background = getTxHRegion(bg.cpy(),weight);
		stl.knobBefore = getTxHRegion(bar.cpy(),weight);
		return stl;
	}
	
	public static SliderStyle getProgressSliderStyle(Color bg,Color bar, int weight){
		SliderStyle stl = new SliderStyle();
		stl.background = getTxHRegion(bg.cpy(),weight);
		stl.knobBefore = getTxHRegion(bar.cpy(),weight);
		return stl;
	}
	
	public static ScrollPaneStyle getScrollPaneStyle(Color bg, Color knob){
		ScrollPaneStyle style = new ScrollPaneStyle();
		
		style.hScroll = getTxHRegion(bg, 10);
		style.vScroll = getTxWRegion(bg, 10);
		style.hScrollKnob = getTxHRegion(knob, 10);
		style.vScrollKnob = getTxWRegion(knob, 10);
		
		return style;
	}
	
	public static TextFieldStyle getTextFieldStyle(Color bg, Color textColor){
		TextFieldStyle stl = new TextFieldStyle();
		stl.background = getTxRegion(bg);
		stl.background.setLeftWidth(5);
		stl.background.setRightWidth(5);
		stl.background.setTopHeight(5);
		stl.background.setBottomHeight(5);
		stl.font = FontManager.getFont(FontManager.MAIN, 14);
		stl.selection = GUIHelper.getTxRegion(new Color(0.8f, 0.8f, 0.8f, 0.5f));
		stl.cursor = GUIHelper.getTxRegion(Color.LIGHT_GRAY);
		stl.fontColor = textColor.cpy();
		return stl;
	}
	
	public static TextButtonStyle getTextButtonStyle(Color color, int size){
		TextButtonStyle stl = new TextButtonStyle();
		stl.font =  FontManager.getFont(FontManager.MAIN, size);
		stl.fontColor = color;
		return stl;
	}
	
	public static ImageButtonStyle getImageButtonStyle(Texture texture){
		TextureRegionDrawable d = getTxRegion(texture);
		ImageButtonStyle stl = new ImageButtonStyle(d,d,d,d,d,d);
		return stl;
	}
	
	public static TextButtonStyle getTextButtonStyle(Color background, Color color, int size){
		TextButtonStyle stl = new TextButtonStyle();
		stl.font =  FontManager.getFont(FontManager.MAIN, size);
		stl.up = getTxRegion(background);
		stl.over = getTxRegion(background.lerp(Color.WHITE, 0.05f));
		stl.down = getTxRegion(background.lerp(Color.LIGHT_GRAY, 0.05f));
		stl.fontColor = color;
		return stl;
	}
	
	public static SliderStyle getSliderStyle(){
		SliderStyle stl = new SliderStyle(getTxRegion(Color.WHITE,1,3),getTxRegion(Color.LIGHT_GRAY,10,10));
		return stl;
	}
	
	public static SliderStyle getSliderStyle(Pixmap knob, Pixmap bg){
		SliderStyle stl = new SliderStyle(getTxRegion(bg),getTxRegion(knob));
		return stl;
	}
	
	public static ListStyle getListStyle(int size, Color fontNor, Color fontSel, Color selection, Color background){
		ListStyle style = new ListStyle(FontManager.getFont(FontManager.MAIN, size), fontSel, fontNor, getTxRegion(selection));
		style.background = getTxRegion(background);
		return style;
	}
	
	public static SelectBoxStyle getSelectBoxStyle(Color bg, Color scrollBg, Color listBg, int size){
		return new SelectBoxStyle(FontManager.getFont(FontManager.MAIN, size), Color.WHITE, getTxRegion(bg), getScrollPaneStyle(scrollBg, Color.WHITE), getListStyle(size, Color.WHITE, Color.WHITE, Color.LIGHT_GRAY, listBg));
	}
	
	public static TextureRegionDrawable getTxRegion(Color color){
		return getTxRegion(color,1,1);
	}

	public static TextureRegionDrawable getTxWRegion(Color color, int width){
		return getTxRegion(color,width,1);
	}
	
	public static TextureRegionDrawable getTxHRegion(Color color, int height){
		return getTxRegion(color, 1, height);
	}
	
	private static TextureRegionDrawable getTxRegion(Color color, int width, int height) {
		
		Pixmap pixMap = new Pixmap(width, height, Format.RGBA8888);
		pixMap.setColor(color);
		pixMap.fillRectangle(0, 0, width, height);
		return getTxRegion(pixMap);
	}
	
	public static TextureRegionDrawable getTxRegion(Texture texture){
		TextureRegionDrawable rg = new TextureRegionDrawable(new TextureRegion(texture));
		return rg;
	}
	
	public static TextureRegionDrawable getTxRegion(Pixmap pixMap){
		TextureRegionDrawable rg = new TextureRegionDrawable(new TextureRegion(new Texture(pixMap)));
		return rg;
	}
	
	public static Label text(String text, Color color, int size){
		return new Label(text,getLabelStyle(color, size));
	}

	public static Label text(String text, Color bg, Color color, int size) {
		return new Label(text,getLabelStyle(bg, color, size));
	}

	public static Actor text(String string, Color bg, Color color, int i,int j) {
		LabelStyle style = getLabelStyle(bg, color, i);
		style.background.setLeftWidth(j);
		return new Label(string, style);
	}
	
	public static TextButtonStyle getTextButtonStyle(Color background, Color color, int size, int padLeft){
		TextButtonStyle stl = new TextButtonStyle();
		stl.font =  FontManager.getFont(FontManager.MAIN, size);
		stl.up = getTxRegion(background);
		stl.up.setLeftWidth(padLeft);
		stl.over = getTxRegion(background.lerp(Color.WHITE, 0.05f));
		stl.over.setLeftWidth(padLeft);
		stl.down = getTxRegion(background.lerp(Color.LIGHT_GRAY, 0.05f));
		stl.down.setLeftWidth(padLeft);
		stl.fontColor = color;
		return stl;
	}
	
}
