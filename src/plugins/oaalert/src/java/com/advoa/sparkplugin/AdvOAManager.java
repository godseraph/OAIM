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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.CodeSource;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.zip.ZipFile;

import org.dts.spell.SpellChecker;
import org.dts.spell.dictionary.openoffice.OpenOfficeSpellDictionary;
import org.dts.spell.dictionary.SpellDictionary;
import org.jivesoftware.spark.SparkManager;
import org.jivesoftware.spark.util.log.Log;

public class AdvOAManager {
	private static AdvOAManager instance = null;
	private AdvOAPreference preference;

	public static AdvOAManager getInstance() {
		if (instance == null) {
			instance = new AdvOAManager();
		}

		return instance;
	}

	private AdvOAManager() {

		try {
			preference = new AdvOAPreference();

			String language = SparkManager.getMainWindow().getLocale()
					.getLanguage();
			if (preference.getPreferences().getSpellLanguage() != null) {
				language = preference.getPreferences().getSpellLanguage();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public AdvOAPreference getAdvOAPreference() {
		return preference;
	}

}
