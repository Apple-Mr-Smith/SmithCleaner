package smith;

import java.io.File;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.platform.win32.Advapi32Util;
import com.sun.jna.platform.win32.Shell32;
import com.sun.jna.platform.win32.ShlObj;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinReg;

public class DupeFinder {
	public static void deleteFilesWithSuffixKeepLargest() {
		
		char[] music = new char[WinDef.MAX_PATH];
		char[] documents = new char[WinDef.MAX_PATH];
		char[] pictures = new char[WinDef.MAX_PATH];
		String downloads = Advapi32Util.registryGetStringValue(WinReg.HKEY_CURRENT_USER, "Software\\Microsoft\\Windows\\CurrentVersion\\Explorer\\User Shell Folders", "{374DE290-123F-4565-9164-39C4925E467B}");

		
		Shell32.INSTANCE.SHGetFolderPath(null, ShlObj.CSIDL_MYMUSIC, null, ShlObj.SHGFP_TYPE_CURRENT, music);
		Shell32.INSTANCE.SHGetFolderPath(null, ShlObj.CSIDL_MYDOCUMENTS, null, ShlObj.SHGFP_TYPE_CURRENT, documents);
		Shell32.INSTANCE.SHGetFolderPath(null, ShlObj.CSIDL_MYDOCUMENTS, null, ShlObj.SHGFP_TYPE_CURRENT, pictures);
		
		if (downloads.contains("%")) {
			String a = downloads.substring(downloads.indexOf("%") + 1, downloads.lastIndexOf("%"));
			downloads = System.getenv(a) + downloads.substring(downloads.lastIndexOf("%") + 1, downloads.length());
		}
		
		File[] musicfiles = new File(String.valueOf(music).trim()).listFiles();
		File[] downloadsfiles = new File(downloads).listFiles();
		File[] documentsfiles = new File(String.valueOf(documents).trim()).listFiles();
		File[] picturesfiles = new File(String.valueOf(pictures).trim()).listFiles();
			
		List<File> totalfiles = new ArrayList<File>();
		
		totalfiles.addAll(Arrays.asList(musicfiles));
		totalfiles.addAll(Arrays.asList(downloadsfiles));
		totalfiles.addAll(Arrays.asList(documentsfiles));
		totalfiles.addAll(Arrays.asList(picturesfiles));

		for (File s : totalfiles) {
			if(s.isFile() && s.length() > 3) {
				String fn = s.toString().substring(0, s.toString().lastIndexOf(".")).substring(s.toString().lastIndexOf("\\") + 1);
				if (fn.substring(fn.length() - 3, fn.length()).matches("(\\(?\\d\\))")) {
					String ss = s.toString();
					File dupefile = s;
					File originalfile = new File(ss.substring(0, ss.lastIndexOf("\\") + 1) + ss.substring(ss.lastIndexOf("\\") + 1, ss.lastIndexOf(")") - 3) + ss.substring(ss.lastIndexOf(".")));
	        if (dupefile.exists() && originalfile.exists());
	        try {
					FileChannel dupeChannel = FileChannel.open(dupefile.toPath());
	        long dupeSize = dupeChannel.size();
	        FileChannel originalChannel = FileChannel.open(originalfile.toPath());
	        long originalSize = originalChannel.size();
	        
	        System.out.println(dupeSize);
	        System.out.println(originalSize);
	        
					if (dupeSize > originalSize) {
						originalfile.delete();
					}else {
						dupefile.delete();
					}
	        }catch(Exception e) {
	        	System.out.println("Error deleting file");
	        }
				}
			}
		}
	}
}
