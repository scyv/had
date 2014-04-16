package de.ksitec.had.server;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * Copyright 2010 Cinovo AG<br>
 * <br>
 * Util class for providing template functionality
 * 
 * @author yschubert
 * 
 */
public class TemplateUtil {
	
	private static final Charset UTF8 = Charset.forName("UTF-8");
	
	private static final Map<String, String> templateCache = new ConcurrentHashMap<String, String>();
	
	private static final boolean templateCacheEnabled = true;
	
	
	/**
	 * Get the content of template, where the placeholders are replaced by the data given in replacements. <br>
	 * Hint: The templates are cached in memory with the first call to reduce IO load of the server. Key of a entry is the given filePath.<br>
	 * 
	 * Limiters of the placeholder are { and } <br>
	 * <br>
	 * This utility supports recursive inclusion of the templates. For accomplishing this, use e.g. {include:&lt;path&gt;}. Path is a path
	 * expression of the file to include relative to <code>filePath</code>
	 * 
	 * @param filePath Path of the template
	 * @param replacements Map of &lt;placeholder, content&gt; pairs. The placeholder's format MUST comply to the regular expression:
	 *            [A-Z0-9_-]+ e.g.: USER_NAME or OPTION1 or TE-ST
	 * @return the template content
	 */
	public static synchronized String getTemplate(final String filePath, final Map<String, String> replacements) {
		return TemplateUtil.getTemplate(filePath, replacements, "\\{", "\\}", true);
	}
	
	/**
	 * Get the content of template, where the placeholders are replaced by the data given in replacements. <br>
	 * Hint: The templates are cached in memory with the first call to reduce IO load of the server. Key of a entry is the given filePath.<br>
	 * 
	 * Limiters of the placeholder are { and } <br>
	 * <br>
	 * This utility supports recursive inclusion of the templates. For accomplishing this, use e.g. {include:&lt;path&gt;}. Path is a path
	 * expression of the file to include relative to <code>filePath</code>
	 * 
	 * @param filePath Path of the template
	 * @param replacements Map of &lt;placeholder, content&gt; pairs. The placeholder's format MUST comply to the regular expression:
	 *            [A-Z0-9_-]+ e.g.: USER_NAME or OPTION1 or TE-ST
	 * @param doReplacements set this to false if you do not want to make replacements (used in nested templates)
	 * @return the template content
	 */
	public static synchronized String getTemplate(final String filePath, final Map<String, String> replacements, final boolean doReplacements) {
		return TemplateUtil.getTemplate(filePath, replacements, "\\{", "\\}", doReplacements);
	}
	
	/**
	 * Get the content of template, where the placeholders are replaced by the data given in replacements. <br>
	 * Hint: The templates are cached in memory with the first call to reduce IO load of the server. Key of a entry is the given filePath.<br>
	 * <br>
	 * This utility supports recursive inclusion of the templates. For accomplishing this, use e.g. {include:&lt;path&gt;}. Path is a path
	 * expression of the file to include relative to <code>filePath</code>
	 * 
	 * @param filePath Path of the template
	 * @param replacements Map of &lt;placeholder, content&gt; pairs. The placeholder's format MUST comply to the regular expression:
	 *            [A-Z0-9_-]+ e.g.: USER_NAME or OPTION1 or TE-ST
	 * @param limiterLeft left side of the placeholder's limiter
	 * @param limiterRight right side of the placeholder's limiter
	 * @param doReplacements set this to false if you do not want to make replacements (used in nested templates)
	 * @return the template content, or an empty string if template was not found (or template is empty itself)
	 */
	public static synchronized String getTemplate(final String filePath, final Map<String, String> replacements, final String limiterLeft, final String limiterRight, final boolean doReplacements) {
		String content = "";
		try {
			
			if (TemplateUtil.templateCacheEnabled && TemplateUtil.templateCache.containsKey(filePath)) {
				content = TemplateUtil.templateCache.get(filePath);
				// logger.debug("Template cache match: " + filePath);
			} else {
				final StringBuilder fileContent = new StringBuilder();
				try (final BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), TemplateUtil.UTF8))) {
					String line = "";
					while ((line = br.readLine()) != null) {
						fileContent.append(line + "\n");
					}
				}
				content = fileContent.toString();
				
				if (!limiterLeft.isEmpty() && !limiterRight.isEmpty()) {
					Map<String, String> includeMap = new HashMap<String, String>();
					// ** process include instruction: {include:filename}
					{
						Pattern pattern = Pattern.compile(limiterLeft + "include:([^" + limiterRight + "]+)" + limiterRight);
						Matcher matcher = pattern.matcher(content);
						File currentFile = new File(filePath);
						
						while (matcher.find()) {
							String fileToInclude = matcher.group(1);
							includeMap.put(limiterLeft + "include:" + fileToInclude + limiterRight, TemplateUtil.getTemplate(currentFile.getParentFile().getAbsolutePath() + File.separator + fileToInclude, replacements, false));
						}
					}
					// **************
					
					for (Entry<String, String> entry : includeMap.entrySet()) {
						String replacement = entry.getValue().replaceAll("\\$", "UP_DOLLAR_UP");
						content = content.replaceAll(entry.getKey(), replacement);
					}
				}
				// **************
				
				if (TemplateUtil.templateCacheEnabled) {
					TemplateUtil.templateCache.put(filePath, content);
					
					// TemplateUtil.templateCache.put(filePath, content);
					// logger.debug("Template cache push: " + filePath +
					// ", Now " + templateCache.size() + " entries.");
				}
				
			}
			
			if ((replacements != null) && doReplacements) {
				String replacement = "";
				for (Entry<String, String> entry : replacements.entrySet()) {
					// $ has to be replaced, as it will be recognized by the
					// regular expression engine as reference, what is not
					// wanted here
					// Pattern.quote(s) does not work in this case!
					replacement = entry.getValue();
					if (replacement != null) {
						replacement = replacement.replaceAll("\\$", "UP_DOLLAR_UP");
					} else {
						replacement = "";
					}
					
					content = content.replaceAll(limiterLeft + entry.getKey().toUpperCase() + limiterRight, replacement);
				}
				// undo the replacement of the $
				content = content.replaceAll("UP_DOLLAR_UP", "\\$");
			}
		} catch (Exception e) {
			return e.getMessage();
		}
		// clear content from not replaced placeholders
		if (doReplacements) {
			Pattern pattern = Pattern.compile(limiterLeft + "[A-Z0-9_-]+" + limiterRight);
			Matcher matcher = pattern.matcher(content);
			content = matcher.replaceAll("");
		}
		
		return content;
	}
	
	/**
	 * Makes a direct replacement in a string. Should not be used as there is no caching.
	 * 
	 * @param replacements -
	 * @param _content -
	 * @param limiterLeft -
	 * @param limiterRight -
	 * @return replaced string -
	 */
	public static synchronized String directReplacement(final String _content, final Map<String, String> replacements, final String limiterLeft, final String limiterRight) {
		String content = _content;
		if (replacements != null) {
			String replacement = "";
			for (Entry<String, String> entry : replacements.entrySet()) {
				// $ has to be replaced, as it will be recognized by the regular
				// expression engine as reference, what is not wanted here
				// Pattern.quote(s) does not work in this case!
				replacement = entry.getValue();
				if (replacement != null) {
					replacement = replacement.replaceAll("\\$", "UP_DOLLAR_UP");
				} else {
					replacement = "";
				}
				content = content.replaceAll(limiterLeft + entry.getKey().toUpperCase() + limiterRight, replacement);
			}
			// undo the replacement of the $
			content = content.replaceAll("UP_DOLLAR_UP", "\\$");
		}
		
		return content;
	}
}
