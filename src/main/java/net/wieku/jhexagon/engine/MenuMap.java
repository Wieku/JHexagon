package net.wieku.jhexagon.engine;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Scaling;
import net.wieku.jhexagon.maps.Map;
import net.wieku.jhexagon.utils.GUIHelper;

/**
 * @author Sebastian Krajewski on 04.04.15.
 */
public class MenuMap extends Table {

	public Map map;

	private Image borderTop;
	private Image borderBottom;
	private Image borderLeft;
	private Image borderRight;

	public MenuMap(Map map){
		super();
		//setBackground(GUIHelper.getTxRegion(new Color(0,0,0,0.5f)));
		this.map = map;

		borderTop = new Image(GUIHelper.getTxHRegion(Color.WHITE, 2), Scaling.stretchX);
		borderBottom = new Image(GUIHelper.getTxHRegion(Color.WHITE, 2), Scaling.stretchX);
		borderLeft = new Image(GUIHelper.getTxWRegion(Color.WHITE, 2), Scaling.stretchY);
		borderRight = new Image(GUIHelper.getTxWRegion(Color.WHITE, 2), Scaling.stretchY);

		borderTop.setVisible(false);
		borderBottom.setVisible(false);
		borderLeft.setVisible(false);
		borderRight.setVisible(false);

		add(borderTop).fill().colspan(4).row();


		Table info = new Table();
		info.add(new Label(map.info.name, GUIHelper.getLabelStyle(Color.WHITE, 16))).top().left().expandX().fillX().row();
		info.add(new Label(map.info.description, GUIHelper.getLabelStyle(Color.WHITE, 14))).top().left().expandX().fillX().row();
		info.add(new Label("Author: "+map.info.author, GUIHelper.getLabelStyle(Color.WHITE, 12))).top().left().expandX().fillX().row();
		info.add(new Label("Song: " + map.info.songName + " by " + map.info.songAuthor, GUIHelper.getLabelStyle(Color.WHITE, 12))).top().left().expandX().fillX().row();


		add(borderLeft).fillY();
		add(info).expand().padLeft(5).padTop(5).padBottom(5).left();

		add(borderRight).fill().row();
		add(borderBottom).colspan(4).fill();


	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		setWidth(Menu.pane.getWidth());
		setX(0);
		layout();
		super.draw(batch, parentAlpha);
	}

	public void check(boolean visible) {
		borderTop.setVisible(visible);
		borderBottom.setVisible(visible);
		borderLeft.setVisible(visible);
		borderRight.setVisible(visible);
	}

}
