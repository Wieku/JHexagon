package net.wieku.jhexagon.resources;

import java.awt.Font;
import java.io.BufferedInputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.BitmapFont.BitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeBitmapFontData;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.utils.GdxRuntimeException;

import org.slf4j.LoggerFactory;


public enum FontManager{

	MAIN("Pixel-UniCode");
	
	protected static HashMap<FontManager, FontData> fonts = new HashMap<FontManager, FontData>();
	protected String name;
	
	
	private FontManager (String name) {
		this.name = name;
	}
	
	public static BitmapFont getFont(FontManager val, int size) {
		BitmapFontData data = fonts.get(val).data, dataCopy = new BitmapFontData();
		
		try {
			for (Field field : dataCopy.getClass().getFields()) {
				field.setAccessible(true);
				field.set(dataCopy, data.getClass().getField(field.getName()).get(data));
			}
		} catch (Exception e) {
			throw new GdxRuntimeException("Failed to create font", e);
		}
		BitmapFont font = new BitmapFont(dataCopy, fonts.get(val).regions, false);
		font.setScale((float) size / 7, (float) size / 8);
		return font;
	}
	

	
	public static void dispose() {
		fonts.clear();
	}
	
	
	
	public static void init() {
		
		LoggerFactory.getLogger(FontManager.class).info("Initializing FontManager...");
		
		for (FontManager val : values()) {
			FileHandle file = Gdx.files.internal("assets/fonts/" + val.name + ".ttf");
			String chars = "";
			try {
				Font font = Font.createFont(Font.TRUETYPE_FONT, new BufferedInputStream(file.read()));
				
				for (int c = 0; c <= Character.MAX_CODE_POINT; c++) {
					if (font.canDisplay(c)) {
						chars += (char) c;
					}
				}
				
				FreeTypeFontGenerator generator = new FreeTypeFontGenerator(file);

				FreeTypeFontParameter pam = new FreeTypeFontParameter();
				pam.size = 16;
				pam.genMipMaps = true;
				pam.shadowColor = new Color(0.5f, 0.5f, 0.5f, 1f);
				pam.shadowOffsetX = 1;
				pam.shadowOffsetY = 1;
				pam.magFilter = TextureFilter.Nearest;
				pam.minFilter = TextureFilter.MipMapNearestNearest;
				pam.characters = chars;
				
				FreeTypeBitmapFontData d = generator.generateData(pam);
				
				FontData fontData = new FontData();
				fontData.data = d;
				fontData.regions = d.getTextureRegions();
				
				fonts.put(val, fontData);
				
				generator.dispose();
				
			} catch (Exception e) {
				e.printStackTrace();
			}	
			
		}
		LoggerFactory.getLogger(FontManager.class).info("FontManager initialized!");
	}
	
}
