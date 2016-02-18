package com.list.lod.util;

import java.util.Locale;

public class ResourceBundle {
	public static java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("com.list.lod.setting.setting", Locale.KOREA);
	public static java.util.ResourceBundle fileBundle = java.util.ResourceBundle.getBundle("com.list.lod.setting.system", Locale.KOREA);
}
