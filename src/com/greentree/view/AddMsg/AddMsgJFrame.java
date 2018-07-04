package com.greentree.view.AddMsg;

import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import com.greentree.model.business.manager.GreenTreeManager;

/**
 * provides the interface for adding a message to the active {@link Token}
 * 
 * @author david.dietrich
 *
 */
public class AddMsgJFrame extends JFrame {
	/**
	 * Eclipse-generated serial number for extending <code>{@link java.io.Serializable}</code>
	 */
	private static final long serialVersionUID = 5145168338973715605L;
	
	/** 
	 * the selected key will be used to identify which <code>Token</code> has access to the message
	 */
	private JLabel keyLbl = new JLabel("To:");
	private JButton keyBtn = new JButton("Open key file...");
	
	/** 
	 * this will be saved by the <code>submitBtn</code>
	 */
	private JLabel msgLbl = new JLabel("Message:");
	private JTextField msgFld = new JTextField(20);
	
	/** 
	 * used for submitting the message to the current {@link com.greentree.model.domain.Token} 
	 * {@link java.util.ArrayList}<{@link com.greentree.model.domain.Block}>
	 */
	private JButton submitBtn = new JButton("Submit");

	/** {@link Container}, {@link GridBagLayout}, and {@link GridBagConstraints} members */
	Container cPane = getContentPane();
	GridBagLayout gBagL = new GridBagLayout();
	GridBagConstraints gBagC = new GridBagConstraints();

	/** {@link JLabel} and {@link JTextField} for the hours during which the msg is accessible */
	private JLabel hoursLbl = new JLabel("Valid for (hours):");
	private JTextField hoursFld = new JTextField(9);
	
	/**
	 * Creates a new UI which prompts the user to create or submit a <code>{@link RSAPublicKey}
	 * </code> and passphrase. These will be used to manage a <code>{@link Token}</code> object for
	 * the user.
	 * 
	 * @param manager {@link GreenTreeManager} to authenticate and store the <code>Token</code>
	 */
	public AddMsgJFrame() {
		super("Add Message");
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		cPane.setLayout(gBagL);
		gBagC.weightx = 10.0;
		gBagC.fill = GridBagConstraints.HORIZONTAL;
		
		// add key label
		gBagC.gridx = 0;
		gBagC.gridy = 0;
		gBagL.setConstraints(keyLbl, gBagC);
		cPane.add(keyLbl);
		
		// add key button
		gBagC.gridx = 1;
		gBagC.gridy = 0;
		gBagL.setConstraints(keyBtn, gBagC);
		cPane.add(keyBtn, gBagC);
		
		// add message field label
		gBagC.gridx = 0;
		gBagC.gridy = 1;
		cPane.add(msgLbl, gBagC);
		
		// add message text field
		gBagC.gridx = 1;
		gBagC.gridy = 1;
		cPane.add(msgFld, gBagC);
		
		// add hours label
		gBagC.gridx = 0;
		gBagC.gridy = 2;
		cPane.add(hoursLbl, gBagC);
		
		// add hours field
		gBagC.gridx = 1;
		gBagC.gridy = 2;
		cPane.add(hoursFld, gBagC);
		
		// add submit button
		gBagC.gridx = 1;
		gBagC.gridy = 3;
		cPane.add(submitBtn, gBagC);
		
		pack();
		setVisible(true);
	}
	
	/** @return "Open key file..." <code>{@link JButton}</code> */
	public JButton getKeyBtn() {
		return this.keyBtn;
	}

	/** @return "Submit" <code>{@link JButton}</code> */
	public JButton getSubmitBtn() {
		return this.submitBtn;
	}

	/**
	 * @return {@link String} contents of the message field
	 */
	public String getMsgFld() {
		return this.msgFld.getText();
	}

	/** @return long milliseconds equivalent of hours input by the user */
	public int getHours() {
		int hours = 0;
		try {
			hours = Integer.valueOf(this.hoursFld.getText());
		} catch (Exception e) {
			hours = 0;
		}
		return hours;
	}
}
