package oripa.gui.bind.binder;

import java.awt.event.KeyListener;

import javax.swing.AbstractButton;

/**
 * This interface provides a general format of linking some object to a GUI
 * button with ID.
 *
 * @author Koji
 *
 * @param <ToBeBound>
 */
public interface BinderInterface<ToBeBound> {

	/**
	 *
	 * @param buttonClass
	 *            Class object of a button to be created
	 * @param target
	 *            An object to be bound to button.doClick()
	 * @param id
	 *            {@code StringID} member for label
	 * @param keyListener
	 *            For catching key actions which requires immediate
	 *            drawing(e.g., for catching Ctrl pressed) It's weird to
	 *            handling by button object but needed because the focus stays
	 *            on the selected button.
	 *
	 * @return
	 */
	public AbstractButton createButton(
			Class<? extends AbstractButton> buttonClass,
			ToBeBound target, String id,
			KeyListener keyListener);

//	public void setOptionalActionListeners(ActionListener[] others);

}