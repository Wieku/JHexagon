package net.wieku.jhexagon.engine.menu.options;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import net.wieku.jhexagon.engine.menu.Menu;
import net.wieku.jhexagon.engine.menu.buttons.Element;

import java.util.ArrayList;

/**
 * @author Sebastian Krajewski on 08.04.15.
 */
public class Section extends Table {

	ArrayList<Element> elements = new ArrayList<>();
	int selectedIndex = 0;

	public Section(){
		super();
		addListener(new InputListener() {
			@Override
			public boolean keyDown(InputEvent event, int keycode) {

				if (keycode == Keys.DOWN) {

					elements.get(selectedIndex).select(false);

					if (++selectedIndex == elements.size()) selectedIndex = 0;

					elements.get(selectedIndex).select(true);

					Menu.playBeep();

				} else if (keycode == Keys.UP) {
					elements.get(selectedIndex).select(false);

					if (--selectedIndex < 0) selectedIndex = elements.size() - 1;

					elements.get(selectedIndex).select(true);

					Menu.playBeep();

				} else {
					event.setKeyCode(keycode);
					elements.get(selectedIndex).onEvent(event);
				}

				return super.keyDown(event, keycode);
			}

			@Override
			public boolean keyUp(InputEvent event, int keycode) {
				if (keycode == Keys.ESCAPE) {
					Options.getInstance().setMenu(OptionMenu.instance);
					Menu.playBeep();
				}
				return false;
			}
		});
	}

	public void addButton(Element<?> element){
		elements.add(element);
	}

}
