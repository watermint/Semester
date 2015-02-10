package semester.readymade.cf.util;

import java.util.Locale;

public class SystemUtil {
    public static final String JAVA_RUNTIME_VERSION = System.getProperty("java.runtime.version");
    public static final String OS_NAME = System.getProperty("os.name");
    public static final String OS_VERSION = System.getProperty("os.version").toLowerCase(Locale.US);
    public static final String OS_ARCH = System.getProperty("os.arch");

    private static final String _OS_NAME = OS_NAME.toLowerCase(Locale.US);
    public static final boolean isWindows = _OS_NAME.startsWith("windows");
    public static final boolean isOS2 = _OS_NAME.startsWith("os/2") || _OS_NAME.startsWith("os2");
    public static final boolean isMac = _OS_NAME.startsWith("mac");
    public static final boolean isLinux = _OS_NAME.startsWith("linux");
    public static final boolean isFreeBSD = _OS_NAME.startsWith("freebsd");
    public static final boolean isSolaris = _OS_NAME.startsWith("sunos");
    public static final boolean isUnix = !isWindows && !isOS2;

    public static final boolean isFileSystemCaseSensitive = isUnix && !isMac ||
            "true".equalsIgnoreCase(System.getProperty("idea.case.sensitive.fs"));

    public static final boolean isIntelMac = isMac && "i386".equals(OS_ARCH);

    public static final boolean isMacOSTiger = isMac && isOsVersionAtLeast("10.4");
    public static final boolean isMacOSLeopard = isMac && isOsVersionAtLeast("10.5");
    public static final boolean isMacOSSnowLeopard = isMac && isOsVersionAtLeast("10.6");
    public static final boolean isMacOSLion = isMac && isOsVersionAtLeast("10.7");
    public static final boolean isMacOSMountainLion = isMac && isOsVersionAtLeast("10.8");
    public static final boolean isMacOSMavericks = isMac && isOsVersionAtLeast("10.9");
    public static final boolean isMacOSYosemite = isMac && isOsVersionAtLeast("10.10");

    public static boolean isOsVersionAtLeast(String version) {
        return StringUtil.compareVersionNumbers(OS_VERSION, version) >= 0;
    }

    public static boolean isJavaVersionAtLeast(String v) {
        return StringUtil.compareVersionNumbers(JAVA_RUNTIME_VERSION, v) >= 0;
    }
}
