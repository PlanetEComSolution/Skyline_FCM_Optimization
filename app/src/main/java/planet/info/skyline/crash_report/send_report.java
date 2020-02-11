package planet.info.skyline.crash_report;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import java.util.Hashtable;

public class send_report implements KvmSerializable {

	private String SETTINGS_GLOBAL;
	private String ANDROID_VERSION;
	private String PACKAGE_NAME;
	private String USER_CRASH_DATE;
	private String BUILD;
	private String STACK_TRACE;
	private String APP_VERSION_NAME;
	private String BRAND;
	
	public send_report(String SETTINGS_GLOBAL, String ANDROID_VERSION, String PACKAGE_NAME, String USER_CRASH_DATE, String BUILD, String STACK_TRACE, String APP_VERSION_NAME, String BRAND) {

		this.SETTINGS_GLOBAL = SETTINGS_GLOBAL;
		this.ANDROID_VERSION = ANDROID_VERSION;
		this.PACKAGE_NAME = PACKAGE_NAME;
		this.USER_CRASH_DATE = USER_CRASH_DATE;
		this.BUILD = BUILD;
		this.STACK_TRACE = STACK_TRACE;
		this.APP_VERSION_NAME = APP_VERSION_NAME;
		this.BRAND = BRAND;
			}

	
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "SETTINGS_GLOBAL";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ANDROID_VERSION";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "PACKAGE_NAME";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "USER_CRASH_DATE";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "BUILD";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "STACK_TRACE";
			break;
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "APP_VERSION_NAME";
			break;
		case 7:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "BRAND";
			break;
				default:
			break;
		}
	}	

	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			SETTINGS_GLOBAL = (value.toString());
			break;
		case 1:
			ANDROID_VERSION = value.toString();
			break;
		case 2:
			PACKAGE_NAME = value.toString();
			break;
		case 3:
			USER_CRASH_DATE = value.toString();
			break;
		case 4:
			BUILD = value.toString();
			break;
		case 5:
			STACK_TRACE = value.toString();
			break;
		case 6:
			APP_VERSION_NAME = value.toString();
			break;
		case 7:
			BRAND = value.toString();
			break;
				default:
			break;
		}
	}

	@Override
	public Object getProperty(int arg0) {

		switch (arg0) {
		case 0:
			return SETTINGS_GLOBAL;
		case 1:
			return ANDROID_VERSION;
		case 2:
			return PACKAGE_NAME;

		case 3:
			return USER_CRASH_DATE;

		case 4:
			return BUILD;

		case 5:
			return STACK_TRACE;

		case 6:
			return APP_VERSION_NAME;

		case 7:
			return BRAND;
			default:
			break;
		}

		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 8;
	}

	
	

}