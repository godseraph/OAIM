/**
 * $RCSfile: ,v $
 * $Revision: $
 * $Date: $
 * 
 * Copyright (C) 2004-2011 Jive Software. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.advoa.sparkplugin;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.jivesoftware.resource.Res;
import org.jivesoftware.spark.component.VerticalFlowLayout;
import org.jivesoftware.spark.util.ResourceUtils;

public class AdvOAPreferenceDialog extends JPanel implements
	ActionListener {
    private static final long serialVersionUID = -1836601903928057855L;

    private JCheckBox alertEnabled;
    
    private JTextField oauserField = new JTextField();
    private JLabel passwordLabel = new JLabel();
    private JPasswordField passwordField = new JPasswordField();

    private JCheckBox ignoreCase;
    private JCheckBox showLanguages; 
    private JPanel spellPanel;


    ArrayList<String> languages;

    public AdvOAPreferenceDialog() {

	spellPanel = new JPanel();
	alertEnabled = new JCheckBox();
	showLanguages = new JCheckBox();
	spellPanel.setLayout(new GridBagLayout());
	
	ignoreCase = new JCheckBox();
	ignoreCase.addActionListener(new ActionListener() {
	    
	    @Override
	    public void actionPerformed(ActionEvent e) {
		setIgnoreUppercase(ignoreCase.isSelected());	
	    }
	});
	
	JLabel lLanguage = new JLabel();
	// spellcheckingEnabled.setText(SpellcheckerResource.getString("preference.spellcheckingEnabled"));

	alertEnabled.addActionListener(this);

	spellPanel.add(alertEnabled, new GridBagConstraints(0, 0, 2, 1,	1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	spellPanel.add(passwordLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	spellPanel.add(passwordField, new GridBagConstraints(1, 1, 1, 1, 1.0, 0.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 150, 0));

	spellPanel.add(lLanguage, new GridBagConstraints(0, 2, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	spellPanel.add(showLanguages, new GridBagConstraints(0, 3, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));
	spellPanel.add(ignoreCase, new GridBagConstraints(0, 4, 2, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(5, 5, 5, 5), 0, 0));

	// Setup MNEMORICS
	ResourceUtils.resButton(alertEnabled, "aaa");
    ResourceUtils.resLabel(passwordLabel, passwordField, "OAÓÃ»§Ãû:");

	
	ResourceUtils.resButton(ignoreCase, "ccc");
	ResourceUtils.resButton(showLanguages, "ddd");
	
	setLayout(new VerticalFlowLayout());
	spellPanel.setBorder(BorderFactory
		.createTitledBorder("eeee"));
	add(spellPanel);
    }

    public void updateUI(boolean enable) {
	if (enable) {
		passwordField.setEnabled(true);
	    ignoreCase.setEnabled(true);
	    showLanguages.setEnabled(true);
	} else {
		passwordField.setEnabled(false);
	    ignoreCase.setEnabled(false);
	    showLanguages.setEnabled(false);
	}
    }

    public void setSpellCheckingEnabled(boolean enable) {
	alertEnabled.setSelected(enable);
	updateUI(enable);
    }

    
    public boolean getEnableLanuageSelection()
    {
        return showLanguages.isSelected();
    }
    
    public void setEnableLanuageSelection(boolean show)
    {
        showLanguages.setSelected(show);
    }


    public boolean isSpellCheckingEnabled() {
	return alertEnabled.isSelected();
    }

    public void actionPerformed(ActionEvent event) {
	updateUI(alertEnabled.isSelected());
    }
    
    public boolean getIgnoreUppercase() {
	return ignoreCase.isSelected();
    }
    
    public void setIgnoreUppercase(boolean ignore) {
	ignoreCase.setSelected(ignore);
    }
}
